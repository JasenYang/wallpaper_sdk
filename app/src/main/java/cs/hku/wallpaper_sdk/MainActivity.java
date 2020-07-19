package cs.hku.wallpaper_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import cs.hku.wallpaper_sdk.constant.Var;
import cs.hku.wallpaper_sdk.service.WallPaperOrientationService;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLObject;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetchCallback;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetcher;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlRenderFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        Button btn = findViewById(R.id.back_to_home);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(MainActivity.this, TLSModelActivity.class);
//                startActivity(intent);


                File file = new File(Environment.getExternalStorageDirectory(), "pikachu.stl");
//                File file = new File(Environment.getExternalStorageDirectory(), "castle.stl");
//                File file = new File(Environment.getExternalStorageDirectory(), "bird.stl");
                Log.i("test","directory_pictures="+file);
                loadModel(file);

//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
            }
        });
    }

    public void loadModel(final File stlFile) {
        /*
        if (stlFile !=null && stlFile.exists()) {
            Log.i("test","???");
            StlFetcher.fetchStlFile(stlFile, new StlFetchCallback() {
                @Override
                public void onBefore() {
//                    view.showFetchProgressDialog(0);
                }

                @Override
                public void onProgress(int progress) {
//                    view.showFetchProgressDialog(progress);
                    Toast.makeText(MainActivity.this, "正在加载: " + progress, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish(STLObject stlObject) {
//                    view.hideFetchProgressDialog();
//                    StlRenderFragment fragment = (StlRenderFragment) view;
//                    view.showModel(stlObject);
                    Toast.makeText(MainActivity.this, "模型加载完成", Toast.LENGTH_SHORT).show();


                    Var.stlObject = stlObject;
                    Intent intent = new Intent(
                            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(MainActivity.this, GLWallpaperService.class));
                    WallPaperOrientationService.StartOrientationListener(MainActivity.this);
                    startActivityForResult(intent, Var.FromSetWallPaper);
                }

                @Override
                public void onError() {
//                    view.hideFetchProgressDialog();
//                    view.showToastMsg("文件解析失败");
                }
            });
        }else{
//            view.showToastMsg("未找到模型文件");
        }

         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("GGD", "onActivityResult: "+requestCode);
        if (requestCode == Var.FromSetWallPaper) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
}
