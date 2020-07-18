package cs.hku.wallpaper_sdk.stl_opengl.home;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

import cs.hku.wallpaper_sdk.R;
import cs.hku.wallpaper_sdk.stl_opengl.base.BaseFragment;
import cs.hku.wallpaper_sdk.stl_opengl.stl.StlRenderFragment;
import cs.hku.wallpaper_sdk.stl_opengl.utils.FragmentUtils;


public class HomeFragment extends BaseFragment {

    public static final int REQUEST_STL_FILE = 0X01;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        File file = new File(Environment.getExternalStorageDirectory(), "Pikachu.stl");
        FragmentUtils.addFragment(getFragmentManager(), StlRenderFragment.getInstance(file), R.id.fl_container, true);
    }
}
