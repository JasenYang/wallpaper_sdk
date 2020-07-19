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
        setContentView(R.layout.homepage);
//        img_list = get_data();
//        GridView gridview = (GridView) findViewById(R.id.grid);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, img_list, R.layout.grid_item,
//                new String[]{"img", "name"}, new int[]{R.id.img, R.id.img_name});
//        gridview.setAdapter(simpleAdapter);
//
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), " " + img_list.get(position).get("name"), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(getBaseContext(), Detail.class);
//                intent.putExtra("index", position);
//                startActivity(intent);
//            }
//
//        });
//
//    }
//    //现在只是用测试数据，连上数据库的话可能要进一步处理，没连过数据库，我也不知道咋处理……
//    private ArrayList<Map<String, Object>> get_data(){
//        ArrayList<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
//        int[] imgsrc = {R.drawable.wall01, R.drawable.wall02, R.drawable.wall03, R.drawable.wall04, R.drawable.wall05,
//                R.drawable.wall06, R.drawable.wall07, R.drawable.wall08, R.drawable.wall09, R.drawable.wall10};
//        for(int i = 0; i < 10; i++){
//            Map<String, Object> data_map = new HashMap<String, Object>();
//            data_map.put("name", "Pic" + i);
//            data_map.put("img", imgsrc[i]);
//            data_list.add(data_map);
//        }
//        return data_list;
//    }
        InitView();
        InitData();

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
}
