package cs.hku.wallpaper_sdk.stl_opengl.home;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cs.hku.wallpaper_sdk.R;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;
import cs.hku.wallpaper_sdk.stl_opengl.utils.FragmentUtils;

public class TLSModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentUtils.addFragment(getSupportFragmentManager(), new HomeFragment(), R.id.fl_container, false);
    }
}
