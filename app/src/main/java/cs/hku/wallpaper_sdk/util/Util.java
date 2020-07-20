package cs.hku.wallpaper_sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.vise.xsnow.http.ViseHttp;

import java.net.URISyntaxException;

import cs.hku.wallpaper_sdk.constant.Var;

//

public class Util {
    public static int getUid(Activity activity){
        if (activity == null) {
            return -1;
        }
        SharedPreferences sharedPref = activity.getSharedPreferences("wallpaper_user", Context.MODE_PRIVATE);
        if (null == sharedPref) {
            return -1;
        }
        return sharedPref.getInt("wallpaper_uid", -1);
    }

    public static void InitNetWork(Context context){
        ViseHttp.init(context);
        ViseHttp.CONFIG()
                //配置请求主机地址
                .baseUrl(Var.host);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
