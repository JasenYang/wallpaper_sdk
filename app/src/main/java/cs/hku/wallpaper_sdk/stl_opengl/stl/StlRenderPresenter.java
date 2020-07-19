package cs.hku.wallpaper_sdk.stl_opengl.stl;


import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cs.hku.wallpaper_sdk.HomepageActivity;
import cs.hku.wallpaper_sdk.MainActivity;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;


public class StlRenderPresenter implements StlRenderContract.Presenter {
    StlRenderContract.View view;

    public StlRenderPresenter(StlRenderContract.View view){
        this.view = view;
    }

    @Override
    public void loadModel(final File stlFile) {
        if (stlFile !=null && stlFile.exists()) {
            StlFetcher.fetchStlFile(stlFile, new StlFetchCallback() {
                @Override
                public void onBefore() {
//                    view.showFetchProgressDialog(0);
                }

                @Override
                public void onProgress(int progress) {
                    //Toast.makeText(this, "正在加载: " + progress, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish(STLObject stlObject) {
//                    view.hideFetchProgressDialog();
//                    StlRenderFragment fragment = (StlRenderFragment) view;
//                    view.showModel(stlObject);

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

}
