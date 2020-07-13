package cs.hku.wallpaper_sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import cs.hku.wallpaper_sdk.model.UserResp;
import cs.hku.wallpaper_sdk.util.Util;

public class Login extends AppCompatActivity {
    EditText txt_UserName, txt_UserPW;
    Button btn_Login;
    TextView txt_signup;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.login);
        Util.InitNetWork(this);
        InitView();
        SetOnClick();
//        if (Util.getUid(this) != -1) {
//            Intent intent = new Intent(Login.this, MainActivity.class);
//            startActivity(intent);
//        }
    }

    public void SetOnClick(){
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_UserName.getText().toString();
                String password = txt_UserPW.getText().toString();

                ViseHttp.POST("/user/login")
                        .addParam("name", username)
                        .addParam("password", password)
                        .request(new ACallback<UserResp>() {
                            @Override
                            public void onSuccess(UserResp data) {
                                int status = 0;
                                int uid = -1;
                                String message = "";
                                status = data.getStatus();
                                uid = data.getUid();
                                message = data.getMessage();
                                if (status == 0) {
                                    Toast.makeText(getApplicationContext(), "Login failed, " + message, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                SharedPreferences sharedPref = getSharedPreferences("wallpaper_user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("wallpaper_uid", uid);
                                editor.apply();
                                editor.commit();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {
                                Toast.makeText(getApplicationContext(), "Request failed, " + errMsg + "; code = " + errCode, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void InitView(){
        txt_UserName = findViewById(R.id.txt_username);
        txt_UserPW = findViewById(R.id.txt_pw);
        txt_signup = findViewById(R.id.txt_signup);
        btn_Login = findViewById(R.id.btn_login);
    }
}
