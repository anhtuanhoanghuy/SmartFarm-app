package com.example.SmartFarm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.SmartFarm.adapter.DeviceAdapter;
import com.example.SmartFarm.api.ApiService;
import com.example.SmartFarm.model.Device;
import com.example.myapplication.R;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonTab extends Fragment {


    public PersonTab() {
    }


    public static PersonTab newInstance() {
        PersonTab fragment = new PersonTab();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_tab, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayout logout_dialog_layout = view.findViewById(R.id.logout_dialog_layout);
        ImageButton logoutBttn = view.findViewById(R.id.logoutBttn);
        logoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.logout_dialog, logout_dialog_layout);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(view);
                TextView cancel_button = view.findViewById(R.id.cancel_button);
                TextView accept = view.findViewById(R.id.accept_button);
                final AlertDialog alertDialog = builder.create();
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();

                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = getActivity().getSharedPreferences("remember_me", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("account_saved","");
                        editor.apply();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                    }
                });

            }
        });
        String mikExp = "[\\$\\\\#\\^&\\*\\(\\)\\[\\]\\+_\\{\\}`~=\\\\!\\|/\\?\\.,:;\"'@]";
        Pattern pattern = Pattern.compile(mikExp);
        Button accountsave;
        String username = ((MainActivity) getActivity()).getUsername();
        String token = ((MainActivity) getActivity()).getToken();
        DeviceAdapter deviceAdapter;
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.rcv_device);
        deviceAdapter = new DeviceAdapter(getContext(),token);
        getDevice(token, deviceAdapter);
        SwipeRefreshLayout swipeRefreshLayout4;
        swipeRefreshLayout4 = view.findViewById(R.id.swipe_layout_4);
        swipeRefreshLayout4.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDevice(token, deviceAdapter);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout4.setRefreshing(false);
                    }
                },2000);
            }
        });
        ImageButton add_device;
        EditText accountname, oldpassword, newpassword, renewpassword;
        accountname = view.findViewById(R.id.accountname);
        oldpassword = view.findViewById(R.id.oldpassword);
        newpassword = view.findViewById(R.id.newpassword);
        renewpassword = view.findViewById(R.id.renewpassword);
        accountsave = view.findViewById(R.id.account_save);
        accountsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;
                if (!newpassword.getText().toString().equals("")){
                    if(renewpassword.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Nhập lại mật khẩu mới",Toast.LENGTH_SHORT).show();
                        check = false;
                    } else {
                        if (newpassword.getText().toString().length() < 8) {
                            Toast.makeText(getContext(),"Mật khẩu phải có tối thiểu 8 kí tự",Toast.LENGTH_SHORT).show();
                            check = false;
                        } else if (!newpassword.getText().toString().equals(renewpassword.getText().toString())) {
                            Toast.makeText(getContext(),"Nhập lại mật khẩu mới không trùng khớp",Toast.LENGTH_SHORT).show();
                            check = false;
                        }
                    }
                }

                if (accountname.getText().toString().equals("")) {
                    Toast.makeText(getContext(),"Nhập tên chủ tài khoản",Toast.LENGTH_SHORT).show();
                    check = false;
                }

                if (!renewpassword.getText().toString().equals("")) {
                    if (newpassword.getText().toString().equals("")) {
                        Toast.makeText(getContext(),"Nhập mật khẩu mới",Toast.LENGTH_SHORT).show();
                        check = false;
                    }
                }
                if (!accountname.getText().toString().equals("") || !newpassword.getText().toString().equals("") || !renewpassword.getText().toString().equals("")) {
                    if (oldpassword.getText().toString().equals("")) {
                        Toast.makeText(getContext(),"Nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
                        check = false;
                    } else  {
                        boolean status = true;
                        if (!accountname.getText().toString().equals("") && pattern.matcher(accountname.getText().toString()).find()){
                            status = false;
                        }
                        if (!newpassword.getText().toString().equals("") && pattern.matcher(newpassword.getText().toString()).find()){
                            status = false;
                        }
                        if (status == true && check == true){
                            ApiService.apiService.sendPOST_changeAccount(token,accountname.getText().toString(),oldpassword.getText().toString(),newpassword.getText().toString()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("0")) {
                                        Toast.makeText(getContext(),"Mật khẩu không đúng.",Toast.LENGTH_SHORT).show();
                                    } else if (response.body().equals("1")) {
                                        Toast.makeText(getContext(),"Thay đổi thông tin thành công.",Toast.LENGTH_SHORT).show();
                                        oldpassword.setText("");
                                        newpassword.setText("");
                                        renewpassword.setText("");
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (status == false) {
                            Toast.makeText(getContext(),"Vui lòng nhập đúng định dạng.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        add_device = view.findViewById(R.id.add_device);
        add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = new Device("","Thiết bị mới");
                deviceAdapter.addDevice(device);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(deviceAdapter);
        super.onViewCreated(view, savedInstanceState);
        accountname.setText(username);
    }

    private void getDevice(String token, DeviceAdapter deviceAdapter) {
        ApiService.apiService.sendPOST_getDevice(token,"getDevice").enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                List<Device> list_device = response.body();
                deviceAdapter.setData(list_device);
            }
            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
            }
        });
    }
}

