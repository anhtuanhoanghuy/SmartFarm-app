package com.example.SmartFarm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SmartFarm.api.ApiService;
import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private TextView register,usernameRegisterLabel,forget,login, register_status;
    private EditText usernameRegisterInput, usernameInput, passwordInput;
    private LinearLayout Appname;
    private Button loginBttn, registerBttn;
    private CheckBox remember_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("remember_me",MODE_PRIVATE);
        String account_saved = preferences.getString("account_saved","");
        if (!account_saved.equals("")) {
            String[] parts = account_saved.split(":");
            String username = parts[0];
            String token = parts[1];
            Bundle bundle = new Bundle();
            Intent intent = new Intent(Login.this, MainActivity.class);
            bundle.putString("username",username);
            bundle.putString("token",token);
            intent.putExtra("account_info",bundle);
            startActivity(intent);
            finish();
        }

        String mikExp = "[\\$\\\\#\\^&\\*\\(\\)\\[\\]\\+_\\{\\}`~=\\\\!\\|/\\?\\.,:;\"'@]";
        Pattern pattern = Pattern.compile(mikExp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        remember_me = findViewById(R.id.remember_me);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loginBttn = findViewById(R.id.loginBttn);
        registerBttn = findViewById(R.id.registerBttn);
        usernameRegisterLabel = findViewById(R.id.usernameRegisterLabel);
        usernameRegisterInput = findViewById(R.id.usernameRegisterInput);
        register_status = findViewById(R.id.register_status);
        Appname = findViewById(R.id.Appname);
        forget = findViewById(R.id.forget);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameRegisterLabel.setVisibility(View.VISIBLE);
                usernameRegisterInput.setVisibility(View.VISIBLE);
                usernameInput.setText("");
                passwordInput.setText("");
                usernameRegisterInput.setText("");
                usernameRegisterInput.requestFocus();
                remember_me.setVisibility(View.GONE);
                forget.setVisibility(View.GONE);
                register.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                loginBttn.setVisibility(View.GONE);
                registerBttn.setVisibility(View.VISIBLE);
                register_status.setText("");
                register_status.setVisibility(View.GONE);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameRegisterLabel.setVisibility(View.GONE);
                usernameRegisterInput.setVisibility(View.GONE);
                usernameInput.setText("");
                passwordInput.setText("");
                usernameInput.requestFocus();
                forget.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
                registerBttn.setVisibility(View.GONE);
                loginBttn.setVisibility(View.VISIBLE);
                register_status.setText("");
                register_status.setVisibility(View.GONE);
                remember_me.setVisibility(View.VISIBLE);
            }
        });

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                login(username,password);

            }
        });

        registerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("") || usernameRegisterInput.getText().toString().equals("")) {
                    register_status.setText("Vui lòng nhập đầy đủ thông tin.");
                    register_status.setVisibility(View.VISIBLE);
                } else {
                    if(pattern.matcher(usernameInput.getText().toString()).find() || pattern.matcher(passwordInput.getText().toString()).find() || pattern.matcher(usernameRegisterInput.getText().toString()).find()) {
                        register_status.setText("Vui lòng nhập đúng định dạng.");
                        register_status.setVisibility(View.VISIBLE);
                    } else if (passwordInput.getText().toString().length() < 8) {
                        register_status.setText("Mật khẩu phải có tối thiểu 8 kí tự.");
                        register_status.setVisibility(View.VISIBLE);
                    } else {
                        ApiService.apiService.sendPOST_register(usernameRegisterInput.getText().toString(), usernameInput.getText().toString(),passwordInput.getText().toString()).enqueue(new Callback<String>() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equals("register_failed")) {
                                    register_status.setText("Tên đăng nhập đã được sử dụng.");
                                    register_status.setVisibility(View.VISIBLE);
                                } else if(response.body().equals("register_success")) {
                                    register_status.setText("Đăng ký tài khoản thành công.");
                                    register_status.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(Login.this,"Có lỗi xảy ra, vui lòng thử lại.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    };

    private void login(String username, String password) {
        ApiService.apiService.sendPOST_login(username,password).enqueue(new Callback<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.body().equals("account_success")) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    List<String> Cookielist = response.headers().values("Set-Cookie");
                    String username = (Cookielist .get(1).split(";"))[0].split("=")[1];
                    try {
                        username = URLDecoder.decode(username, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    String token = (Cookielist .get(0).split(";"))[0].split("=")[1];
                    bundle.putString("username", username);
                    bundle.putString("token",token);
                    if (remember_me.isChecked()) {
                        SharedPreferences preferences = getSharedPreferences("remember_me",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("account_saved",username + ":" + token);
                        editor.apply();
                    }
                    intent.putExtra("account_info",bundle);
                    startActivity(intent);
                    finish();
                } else if(response.body().equals("account_failed")) {
                    register_status.setText("Sai thông tin đăng nhập.");
                    register_status.setVisibility(View.VISIBLE);
                } else if (response.body().equals("error")) {
                    Toast.makeText(Login.this,"Có lỗi xảy ra, vui lòng thử lại.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Login.this,"Có lỗi xảy ra, vui lòng thử lại.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
