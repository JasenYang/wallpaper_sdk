package cs.hku.wallpaper_sdk;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.victor.loading.rotate.RotateLoading;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.callback.UCallback;
import com.vise.xsnow.http.mode.DownProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cs.hku.wallpaper_sdk.constant.Var;
import cs.hku.wallpaper_sdk.model.ModelResp;
import cs.hku.wallpaper_sdk.model.ModelAdapter;
import cs.hku.wallpaper_sdk.service.WallPaperOrientationService;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;
import cs.hku.wallpaper_sdk.stl_opengl.STL_View;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLObject;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetchCallback;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlFetcher;
import cs.hku.wallpaper_sdk.util.FileUtils;
import cs.hku.wallpaper_sdk.util.Util;

public class HomepageActivity extends AppCompatActivity {

    private static final int loadFinish = 28;
    private static final int UPLOAD = 29;

    private List<STLObject> objects = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private String resource = Var.host + "/static/model/";
    private Button btn_add;
    private RotateLoading rotateloading;
    private GridView gridview;
    private STL_View stlview;
    private ModelAdapter modelAdapter;
    private MessageHandler mHandler = new MessageHandler();
    private int uid;
    private ArrayList<FileAndUrl> fileNotExists = new ArrayList<>();
    private ArrayList<File> modelFiles = new ArrayList<>();
    class MessageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case loadFinish:
                    gridview.setAdapter(modelAdapter);
                    rotateloading.stop();
                    break;
            }
        }
    }

    class FileAndUrl {
        File file;
        String url;

        public FileAndUrl(File file, String url) {
            this.file = file;
            this.url = url;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //stlview = (STL_View) findViewById(R.id.stlItem);
        InitView();
//        InitData();
        rotateloading.start();

        InitData();

        //展示【模型】

//        File file = new File(Environment.getExternalStorageDirectory(), "Pikachu.stl");
//        names.add("Pikapika");
//        loadModel(file);
//
//        file = new File(Environment.getExternalStorageDirectory(), "Eevee.stl");
//        names.add("Eevee");
//        loadModel(file);


        SetOnClick();
    }

    // 打开app 之后，首刷初始化结果
    public class mThread extends Thread{
        List<ModelResp.model> models;
        public mThread(List<ModelResp.model> models) {
            this.models = models;
        }
        @Override
        public void run() {
            for (ModelResp.model model : models) {
                names.add(model.getName());
                imageUrls.add(resource + model.getName());
                File file = new File(Environment.getExternalStorageDirectory(), model.getName());
                if (!file.exists()) {
                    fileNotExists.add(new FileAndUrl(file, model.getModel_path()));
                } else {
                    modelFiles.add(file);
                }
            }
            DownloadFiles();
            InitModelAdapter();
        }
    }

    public void DownloadFiles() {
        final int total = fileNotExists.size();
        final int current = 1;
        for (FileAndUrl fu: fileNotExists) {
            final File file = fu.getFile();
            String url = fu.getUrl();
            ViseHttp.DOWNLOAD(url)
//                    .setRootName(Environment.getExternalStorageDirectory().getName())
                    .setDirName("/")
                    .setFileName(file.getName())
                    .request(new ACallback<DownProgress>() {
                        @Override
                        public void onSuccess(DownProgress downProgress) {
                            //下载进度回调
                            Toast.makeText(HomepageActivity.this, "正在下载缺失的模型: " + current + "/" + total + " --> " + downProgress.getPercent() + "%", Toast.LENGTH_SHORT).show();
                            if (downProgress.isDownComplete()) {
                                modelFiles.add(file);
                            }
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            //下载失败
                            Toast.makeText(HomepageActivity.this, "下载失败 : " + errMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void InitModelAdapter () {
        int total = modelFiles.size();
        int current = 1;
        for (File file : modelFiles) {
            loadModel(current, total, file);
            current ++;
        }
        modelAdapter = new ModelAdapter(this, objects, names);
        mHandler.sendEmptyMessage(loadFinish);
    }

    class loadAfterUploadThread extends Thread {
        private File file;
        public loadAfterUploadThread(File file, String name) {
            this.file = file;
            names.add(name);
        }
        @Override
        public void run() {
            loadModel(1, 1, file);
            modelAdapter = new ModelAdapter(getApplication(), objects, names);
            mHandler.sendEmptyMessage(loadFinish);
        }
    }

    public void InitView(){
        gridview = findViewById(R.id.grid);
        rotateloading = findViewById(R.id.rotateloading);
        btn_add = findViewById(R.id.btn_add);
    }

    private void InitData(){
        uid = Util.getUid(this);
        ViseHttp.POST("/model/fetch")
                .addParam("uid", String.valueOf(Util.getUid(this)))
//                .addParam("uid", "0")
                .request(new ACallback<ModelResp>() {
                    @Override
                    public void onSuccess(ModelResp resp) {
                        int status = resp.getStatus();
                        List<ModelResp.model> models = resp.getBody();
                        String message = resp.getMessage();
                        if (status == 0) {
                            Toast.makeText(getApplicationContext(), "fetch modle failed, " + message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new mThread(models).start();
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Toast.makeText(getApplicationContext(), "Fetch model failed, " + errMsg + "; code = " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void SetOnClick(){
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), " " + img_list.get(position).get("name"), Toast.LENGTH_SHORT).show();

                Var.stlObject = objects.get(position);
                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(HomepageActivity.this, GLWallpaperService.class));
                WallPaperOrientationService.StartOrientationListener(HomepageActivity.this);
                startActivityForResult(intent, Var.FromSetWallPaper);
            }

        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFile(v);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Var.FromSetWallPaper:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            case UPLOAD:
                super.onActivityResult(requestCode, resultCode, data);
                if (data == null) {
                    // 用户未选择任何文件，直接返回
                    return;
                }
                Uri uri = data.getData(); // 获取用户选择文件的URI
                if (uri == null) {
                    Toast.makeText(HomepageActivity.this, "wrong uri", Toast.LENGTH_LONG).show();
                    return;
                }
                String path = FileUtils.getFilePathByUri(this, uri);
                if (path == null) {
                    Toast.makeText(HomepageActivity.this, "wrong file path", Toast.LENGTH_LONG).show();
                    return;
                }
                final File file = new File(path);
                final String fileName = file.getName();
                String fileType=fileName.substring(fileName.lastIndexOf("."),fileName.length());
                if (!fileType.equals(".stl")) {
                    Toast.makeText(HomepageActivity.this, "wrong file type", Toast.LENGTH_LONG).show();
                    return;
                }
                rotateloading.start();
                ViseHttp.UPLOAD("/model/upload", new UCallback() {
                    @Override
                    public void onProgress(long currentLength, long totalLength, float percent) {
                        Toast.makeText(HomepageActivity.this, "正在上传: " + percent + "%", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        rotateloading.stop();
                    }
                }).addFile("file", file)
                        .addParam("name", fileName)
                        .addParam("classify", "private")
                        .addParam("uid", String.valueOf(uid))
                        .tag("请求Tag，为防止内存泄露，必须设置，在onDestroy中调用cancelTag时使用")
                        .request(new ACallback<Object>() {
                            @Override
                            public void onSuccess(Object data) {
                                //请求成功
                                rotateloading.stop();
                                new loadAfterUploadThread(file, fileName).start();
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {
                                //请求失败，errCode为错误码，errMsg为错误描述
                                Toast.makeText(HomepageActivity.this, "upload failed: " + errMsg, Toast.LENGTH_LONG).show();
                                rotateloading.stop();
                            }
                        });
                break;
        }
    }

    public void loadModel(final int current , final int total, final File stlFile) {
        if (stlFile !=null && stlFile.exists()) {
            StlFetcher.fetchStlFile(stlFile, new StlFetchCallback() {
                @Override
                public void onBefore() {
//                    view.showFetchProgressDialog(0);
                }

                @Override
                public void onProgress(int progress) {
                    Toast.makeText(HomepageActivity.this, "正在加载: " + current + "/" + total + " --> " + progress + "%", Toast.LENGTH_SHORT).show();
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
                    Log.i("test","fail");
                }
            });

        }else{
//            view.showToastMsg("未找到模型文件");
        }
    }

    public void pickFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        this.startActivityForResult(intent, UPLOAD);
    }

}
