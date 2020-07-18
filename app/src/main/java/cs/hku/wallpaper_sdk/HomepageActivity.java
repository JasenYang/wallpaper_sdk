package cs.hku.wallpaper_sdk;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs.hku.wallpaper_sdk.constant.Var;
import cs.hku.wallpaper_sdk.image.ImageAdapter;
import cs.hku.wallpaper_sdk.model.ImgResp;
import cs.hku.wallpaper_sdk.model.ModelAdapter;
import cs.hku.wallpaper_sdk.service.WallPaperOrientationService;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLObject;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLRenderer;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetchCallback;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetcher;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomepageActivity extends AppCompatActivity {

    private List<STLObject> objects = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private String resource = Var.host + "public/";
    private GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        InitView();
        //InitData();

        //展示【模型】


        File file = new File(Environment.getExternalStorageDirectory(), "pikachu.stl");
        names.add("pikachu");

        loadModel(file);
        Log.i("test","--------");

        ModelAdapter modelAdapter = new ModelAdapter(this, objects, names);
        gridview.setAdapter(modelAdapter);

    }


    public void InitView(){
        gridview = findViewById(R.id.grid);
    }
    private void InitData(){
        ViseHttp.POST("/image/fetch")
//                .addParam("uid", String.valueOf(Util.getUid(this)))
                .addParam("uid", "0")
                .request(new ACallback<ImgResp>() {
                    @Override
                    public void onSuccess(ImgResp resp) {
                        int status = resp.getStatus();
                        List<String> filenames = resp.getFilename();
                        String message = resp.getMessage();
                        if (status == 0) {
                            Toast.makeText(getApplicationContext(), "fetch image failed, " + message, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (String file : filenames) {
                            String[] arr = file.split("@");
                            names.add(arr[0]);
                            imageUrls.add(resource + arr[1]);
                        }
//                        handler.sendEmptyMessage(0);
                        Toast.makeText(getApplicationContext(), "fetch image success, " + names +"@" + imageUrls, Toast.LENGTH_SHORT).show();
                        InitAdapter();
                        SetOnClick();
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Toast.makeText(getApplicationContext(), "Fetch image failed, " + errMsg + "; code = " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void InitAdapter(){
        ImageAdapter imageAdapter = new ImageAdapter(this, imageUrls, names);
        gridview.setAdapter(imageAdapter);
    }

    public void SetOnClick(){
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), " " + img_list.get(position).get("name"), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), Detail.class);
                intent.putExtra("index", position);
                startActivity(intent);
            }

        });
    }

    public void loadModel(final File stlFile) {
        if (stlFile !=null && stlFile.exists()) {
            StlFetcher.fetchStlFile(stlFile, new StlFetchCallback() {
                @Override
                public void onBefore() {
//                    view.showFetchProgressDialog(0);
                }

                @Override
                public void onProgress(int progress) {
                    Toast.makeText(HomepageActivity.this, "正在加载: " + progress, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish(STLObject stlObject) {
//                    view.hideFetchProgressDialog();
//                    StlRenderFragment fragment = (StlRenderFragment) view;
//                    view.showModel(stlObject);
                    objects.add(stlObject);
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
    }
}
