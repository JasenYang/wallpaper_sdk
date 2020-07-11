package cs.hku.wallpaper_sdk.stl_opengl.stl;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;


import java.io.File;

import butterknife.InjectView;
import cs.hku.wallpaper_sdk.R;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;
import cs.hku.wallpaper_sdk.stl_opengl.base.BaseFragment;
import cs.hku.wallpaper_sdk.stl_opengl.utils.ToastUtils;

public class StlRenderFragment extends BaseFragment implements StlRenderContract.View{
    @InjectView(R.id.stlView)
    GLWallpaperService.GLEngine.STLView stlView;
    File stlFile;
    StlRenderContract.Presenter presenter;

    public static Fragment getInstance(File file){
        StlRenderFragment fragment = new StlRenderFragment();
        fragment.stlFile = file;
        fragment.presenter = new StlRenderPresenter(fragment);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stl_render;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        presenter.loadModel(stlFile);
    }

    @Override
    public void showToastMsg(String msg) {
        ToastUtils.showShort(mContext, msg);
    }

    @Override
    public void showModel(STLObject stlObject) {
        stlView.getStlRenderer().requestRedraw(stlObject);
    }

    ProgressDialog stlProgressDialog;
    @Override
    public void showFetchProgressDialog(int progress) {
        if(stlProgressDialog == null){
            stlProgressDialog = new ProgressDialog(mContext);
        }
        stlProgressDialog.setTitle("文件加载中");
        stlProgressDialog.setIndeterminate(false);
        stlProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        stlProgressDialog.setMax(100);
        stlProgressDialog.setCancelable(false);
        stlProgressDialog.setProgress(progress);
        stlProgressDialog.show();
    }

    @Override
    public void hideFetchProgressDialog() {
        if(stlProgressDialog != null){
            stlProgressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        stlView.onResume();
        stlView.getStlRenderer().requestRedraw();
    }
}
