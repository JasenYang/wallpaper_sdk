package cs.hku.wallpaper_sdk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import cs.hku.wallpaper_sdk.model.UserResp;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterActivity extends Activity {

    EditText txt_set_username;
    EditText txt_set_pw;
    Button btn_sign;
    TextView txt_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        InitView();
        SetOnClick();
    }

    public void InitView(){
        txt_set_username = findViewById(R.id.txt_set_username);
        txt_set_pw = findViewById(R.id.txt_set_pw);
        btn_sign = findViewById(R.id.btn_sign);
        txt_signup = findViewById(R.id.txt_signup);
    }

    public void SetOnClick(){
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_set_username.getText().toString();
                String password = txt_set_pw.getText().toString();
                ViseHttp.POST("/user/register")
                        .addParam("name", username)
                        .addParam("password", password)
                        .request(new ACallback<UserResp>() {
                            @Override
                            public void onSuccess(UserResp data) {
                                Log.d(TAG, "onSuccess: " + data);
                                int status = 0;
                                int uid = -1;
                                String message = "";

                                status = data.getStatus();
                                uid = data.getUid();
                                message = data.getMessage();


                                if (status == 0) {
                                    Toast.makeText(getApplicationContext(), "Register failed, " + message , Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                SharedPreferences sharedPref = getSharedPreferences("wallpaper_user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("wallpaper_uid", uid);
                                editor.apply();
                                editor.commit();
                                Intent intent = new Intent(RegisterActivity.this, HomepageActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {
                                Toast.makeText(getApplicationContext(), "Register failed, " + errMsg + "; code = " + errCode, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
