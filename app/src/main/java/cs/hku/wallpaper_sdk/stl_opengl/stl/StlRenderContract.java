package cs.hku.wallpaper_sdk.stl_opengl.stl;

import java.io.File;

import cs.hku.wallpaper_sdk.stl_opengl.base.BasePresenter;
import cs.hku.wallpaper_sdk.stl_opengl.base.BaseView;


public interface StlRenderContract {

    interface View extends BaseView {
        void showToastMsg(String msg);
        void showModel(STLObject stlObject);
        void showFetchProgressDialog(int progress);
        void hideFetchProgressDialog();
    }

    interface Presenter extends BasePresenter {
        void loadModel(File file);
    }

}
