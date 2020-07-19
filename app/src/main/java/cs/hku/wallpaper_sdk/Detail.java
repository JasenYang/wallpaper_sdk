package cs.hku.wallpaper_sdk;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Detail extends AppCompatActivity {

    private TextView tv_imgTitle;
    private ImageView iv_img;
    private Button btn_back,set_wallpaper;
    private int res;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        //现在没有连接到数据库，是通过图片序号获得图片
        int index = intent.getIntExtra("index", 0);

        setContentView(R.layout.detailpage);
        tv_imgTitle = (TextView)findViewById(R.id.txt_imgtitle);
        iv_img = (ImageView)findViewById(R.id.img);
        btn_back = (Button)findViewById(R.id.btn_back2home);
        set_wallpaper = (Button)findViewById(R.id.set_wallpaper);

        //通过索引获得图片名称和来源，连上数据库后更改setText和setImageResource的参数就好
        tv_imgTitle.setText("Pic" + index);
        res = R.drawable.wall07;
        switch(index){
            case 0: res = R.drawable.wall01; break;
            case 1: res = R.drawable.wall02; break;
            case 2: res = R.drawable.wall03; break;
            case 3: res = R.drawable.wall04; break;
            case 4: res = R.drawable.wall05; break;
            case 5: res = R.drawable.wall06; break;
        }
        iv_img.setImageResource(res);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(homeIntent);
            }
        });

        set_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    manager.setResource(res);
                    Toast.makeText(getApplicationContext(), "set wall paper successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
