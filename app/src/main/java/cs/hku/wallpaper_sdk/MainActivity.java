package cs.hku.wallpaper_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs.hku.wallpaper_sdk.image.ImageAdapter;
import cs.hku.wallpaper_sdk.model.ImgResp;
import cs.hku.wallpaper_sdk.service.WallPaperOrientationService;
import cs.hku.wallpaper_sdk.util.Util;
import cs.hku.wallpaper_sdk.constant.Var;

public class MainActivity extends AppCompatActivity {
    ArrayList<Map<String, Object>> img_list= new ArrayList<Map<String, Object>>();
    ArrayList<String> imageUrls = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    String resource = Var.host + "public/";
    GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.homepage);
        /*
        Button btn = findViewById(R.id.back_to_home);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
        WallPaperOrientationService.StartOrientationListener(this);

         */
        //GridView是通过map格式处理数据的
        Util.InitNetWork(this);

//        img_list = get_data();
        gridview = findViewById(R.id.grid);
        InitData();
//        ImageAdapter imageAdapter = new ImageAdapter(this, imageUrls, names);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, img_list, R.layout.grid_item,
//                new String[]{"img", "name"}, new int[]{R.id.img, R.id.img_name});
//        gridview.setAdapter(imageAdapter);

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
}
