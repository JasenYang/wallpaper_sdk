package cs.hku.wallpaper_sdk.stl_opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;

import cs.hku.wallpaper_sdk.constant.Var;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLObject;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLRenderer;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetchCallback;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetcher;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlRenderFragment;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlRenderPresenter;


public class GLWallpaperService extends WallpaperService {
    public static GLEngine.STLView glSurfaceView;
    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    public class GLEngine extends Engine {
         private StlRenderPresenter presenter;
         private boolean renderSet;
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            glSurfaceView = new STLView(GLWallpaperService.this, null);

//            File file = new File(Environment.getExternalStorageDirectory(), "Pikachu.stl");
//            loadModel(file);
            //检查是否支持 opengl es 2.0
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

            final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                    || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                    && (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")));
            if (supportsEs2) {
                Toast.makeText(GLWallpaperService.this,
                        "I am running, yeah!!",
                        Toast.LENGTH_LONG).show();

                renderSet = true;
            } else {
                Toast.makeText(GLWallpaperService.this,
                        "This device does not support OpenGL ES 2.0",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (Var.stlObject == null ) {
                Toast.makeText(GLWallpaperService.this,
                        "模型加载失败",
                        Toast.LENGTH_LONG).show();
                return;
            }
            glSurfaceView.getStlRenderer().requestRedraw(Var.stlObject);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (renderSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    glSurfaceView.onPause();
                }
            }
        }

         @Override
        public void onDestroy() {
            super.onDestroy();
        }

         public class STLView extends GLSurfaceView {

             private STLRenderer stlRenderer;
             //这里将偏移数值降低
             private final float TOUCH_SCALE_FACTOR = 180.0f / 320/2;
             private float previousX;
             private float previousY;
             private void changeDistance(float scale) {
                 stlRenderer.scale = scale;
             }
             // 缩放比例
             private float pinchScale = 1.0f;
             private PointF pinchStartPoint = new PointF();
             private float pinchStartZ = 0.0f;
             private float pinchStartDistance = 0.0f;
             private float pinchMoveX = 0.0f;
             private float pinchMoveY = 0.0f;
             private static final int TOUCH_NONE = 0;
             private static final int TOUCH_DRAG = 1;
             private static final int TOUCH_ZOOM = 2;
             private int touchMode = TOUCH_NONE;

             public STLView(Context context, AttributeSet attrs) {
                 super(context, attrs);
                 stlRenderer = new STLRenderer();
                 setRenderer(stlRenderer);
             }

             public STLRenderer getStlRenderer(){
                 return stlRenderer;
             }

             @Override
             public SurfaceHolder getHolder() {
                 return getSurfaceHolder();
             }

             public void renderModel (float rotateX, float rotateY, float rotateZ) {
                 // change view point
                 stlRenderer.angleX = rotateX;
                 stlRenderer.angleY = rotateY;
                 stlRenderer.angleZ = rotateZ;
                 stlRenderer.requestRedraw();
                 requestRender();
             }
             @Override
             public boolean onTouchEvent(MotionEvent event) {
                 switch (event.getAction() & MotionEvent.ACTION_MASK) {
                     // starts pinch
                     case MotionEvent.ACTION_POINTER_DOWN:
                         if (event.getPointerCount() >= 2) {
                             pinchStartDistance = getPinchDistance(event);
                             //pinchStartZ = pinchStartDistance;
                             if (pinchStartDistance > 50f) {
                                 getPinchCenterPoint(event, pinchStartPoint);
                                 previousX = pinchStartPoint.x;
                                 previousY = pinchStartPoint.y;
                                 touchMode = TOUCH_ZOOM;
                             }
                         }
                         break;

                     case MotionEvent.ACTION_MOVE:
                         if (touchMode == TOUCH_ZOOM && pinchStartDistance > 0) {
                             // on pinch
                             PointF pt = new PointF();

                             getPinchCenterPoint(event, pt);
                             pinchMoveX = pt.x - previousX;
                             pinchMoveY = pt.y - previousY;
                             float dx = pinchMoveX;
                             float dy = pinchMoveY;
                             previousX = pt.x;
                             previousY = pt.y;

                             stlRenderer.angleX +=  dy * TOUCH_SCALE_FACTOR;
//					stlRenderer.angleY +=  dx * TOUCH_SCALE_FACTOR;
                             stlRenderer.angleZ +=  dx * TOUCH_SCALE_FACTOR;

                             pinchScale = getPinchDistance(event) / pinchStartDistance;
                             changeDistance(pinchScale);
                             stlRenderer.requestRedraw();
                             invalidate();
                         }
                         break;

                     // end pinch
                     case MotionEvent.ACTION_UP:
                     case MotionEvent.ACTION_POINTER_UP:
                         pinchScale=0;
                         pinchStartZ=0;
                         if (touchMode == TOUCH_ZOOM) {
                             touchMode = TOUCH_NONE;

                             pinchMoveX = 0.0f;
                             pinchMoveY = 0.0f;
                             pinchScale = 1.0f;
                             pinchStartPoint.x = 0.0f;
                             pinchStartPoint.y = 0.0f;
                             invalidate();
                         }
                         break;
                 }

                 switch (event.getAction() & MotionEvent.ACTION_MASK) {
                     // start drag
                     case MotionEvent.ACTION_DOWN:
                         if (touchMode == TOUCH_NONE && event.getPointerCount() == 1) {
                             touchMode = TOUCH_DRAG;
                             previousX = event.getX();
                             previousY = event.getY();
                         }
                         break;

                     case MotionEvent.ACTION_MOVE:
                         if (touchMode == TOUCH_DRAG) {
                             float x = event.getX();
                             float y = event.getY();

                             float dx = x - previousX;
                             float dy = y - previousY;
                             previousX = x;
                             previousY = y;

                             // change view point
                             stlRenderer.angleX +=  dy * TOUCH_SCALE_FACTOR;
//					stlRenderer.angleY +=  dx * TOUCH_SCALE_FACTOR;
                             stlRenderer.angleZ +=  dx * TOUCH_SCALE_FACTOR;

                             stlRenderer.requestRedraw();
                             requestRender();
                         }
                         break;

                     // end drag
                     case MotionEvent.ACTION_UP:
                         if (touchMode == TOUCH_DRAG) {
                             touchMode = TOUCH_NONE;
                             break;
                         }
                         stlRenderer.setsclae();
                 }

                 return true;
             }

             /**
              *
              * @param event
              * @return pinched distance
              */
             private float getPinchDistance(MotionEvent event) {
                 float x=0;
                 float y=0;
                 try {
                     x = event.getX(0) - event.getX(1);
                     y = event.getY(0) - event.getY(1);
                 } catch (IllegalArgumentException e) {
                     e.printStackTrace();
                 }
                 return (float) Math.sqrt(x * x + y * y);
             }

             /**
              *
              * @param event
              * @param pt pinched point
              */
             private void getPinchCenterPoint(MotionEvent event, PointF pt) {
                 pt.x = (event.getX(0) + event.getX(1)) * 0.5f;
                 pt.y = (event.getY(0) + event.getY(1)) * 0.5f;
             }
         }

     }
}