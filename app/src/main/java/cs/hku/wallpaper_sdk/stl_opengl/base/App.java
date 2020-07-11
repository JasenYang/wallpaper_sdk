package cs.hku.wallpaper_sdk.stl_opengl.base;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App app = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if(app == null){
            app = this;
        }
    }

    public static Context getContext(){
        return app.getApplicationContext();
    }
}
