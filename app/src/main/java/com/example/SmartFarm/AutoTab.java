package com.example.SmartFarm;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.SmartFarm.adapter.MqttViewModel;
import com.example.SmartFarm.api.ApiService;
import com.example.SmartFarm.model.Automation;
import com.example.myapplication.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoTab extends Fragment {
    MqttViewModel viewModel;
    String token;
    Button fan_timer_save_bttn ,
            cool_timer_save_bttn ,
            frost_timer_save_bttn,
            water_timer_save_bttn;
    Button[] save_bttn;
    Switch fan_bttn , fan_timer1_bttn , fan_timer2_bttn , fan_timer3_bttn ,
            cool_bttn , cool_timer1_bttn, cool_timer2_bttn, cool_timer3_bttn,
            frost_bttn ,frost_timer1_bttn, frost_timer2_bttn , frost_timer3_bttn ,
            water_bttn , water_timer1_bttn , water_timer2_bttn , water_timer3_bttn ;
    Switch[] fan_timer_bttn, cool_timer_bttn, frost_timer_bttn, water_timer_bttn, machine_bttn, timer_bttn ;
    EditText[] fan_timer, cool_timer, frost_timer, water_timer;
    EditText fan_onValue, fan_offValue , fan_timer1_on , fan_timer1_off, fan_timer2_on , fan_timer2_off, fan_timer3_on , fan_timer3_off ,
            cool_onValue, cool_offValue , cool_timer1_on , cool_timer1_off , cool_timer2_on, cool_timer2_off , cool_timer3_on , cool_timer3_off ,
            frost_onValue , frost_offValue , frost_timer1_on, frost_timer1_off , frost_timer2_on , frost_timer2_off , frost_timer3_on , frost_timer3_off ,
            water_onValue , water_offValue , water_timer1_on , water_timer1_off, water_timer2_on , water_timer2_off , water_timer3_on , water_timer3_off ;
    public AutoTab() {
    }


    public static AutoTab newInstance() {
        AutoTab fragment = new AutoTab();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MqttViewModel.class);
        token = ((MainActivity) getActivity()).getToken();
        SwipeRefreshLayout swipeRefreshLayout3;
        swipeRefreshLayout3 = view.findViewById(R.id.swipe_layout_3);
        swipeRefreshLayout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimer(token);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout3.setRefreshing(false);
                    }
                },2000);
            }
        });
        super.onViewCreated(view, savedInstanceState);
        FlexboxLayout fan_dropdown_bttn, cool_dropdown_bttn, frost_dropdown_bttn, water_dropdown_bttn;
        CardView fan_dropdown, cool_dropdown, frost_dropdown, water_dropdown;
        fan_timer_save_bttn = view.findViewById(R.id.fan_timer_save_bttn);
                cool_timer_save_bttn = view.findViewById(R.id.cool_timer_save_bttn);
                frost_timer_save_bttn = view.findViewById(R.id.frost_timer_save_bttn);
                water_timer_save_bttn = view.findViewById(R.id.water_timer_save_bttn);

                fan_bttn = view.findViewById(R.id.fan_bttn);
                fan_timer1_bttn = view.findViewById(R.id.fan_timer1_bttn);
                fan_timer2_bttn = view.findViewById(R.id.fan_timer2_bttn);
                fan_timer3_bttn = view.findViewById(R.id.fan_timer3_bttn);
                cool_bttn = view.findViewById(R.id.cool_bttn);
                cool_timer1_bttn = view.findViewById(R.id.cool_timer1_bttn);
                cool_timer2_bttn = view.findViewById(R.id.cool_timer2_bttn);
                cool_timer3_bttn = view.findViewById(R.id.cool_timer3_bttn);
                frost_bttn = view.findViewById(R.id.frost_bttn);
                frost_timer1_bttn = view.findViewById(R.id.frost_timer1_bttn);
                frost_timer2_bttn = view.findViewById(R.id.frost_timer2_bttn);
                frost_timer3_bttn = view.findViewById(R.id.frost_timer3_bttn);
                water_bttn = view.findViewById(R.id.water_bttn);
                water_timer1_bttn = view.findViewById(R.id.water_timer1_bttn);
                water_timer2_bttn = view.findViewById(R.id.water_timer2_bttn);
                water_timer3_bttn = view.findViewById(R.id.water_timer3_bttn);
                fan_onValue = view.findViewById(R.id.fan_onValue);
                fan_offValue = view.findViewById(R.id.fan_offValue);
                fan_timer1_on = view.findViewById(R.id.fan_timer1_on);
                fan_timer1_off = view.findViewById(R.id.fan_timer1_off);
                fan_timer2_on = view.findViewById(R.id.fan_timer2_on);
                fan_timer2_off = view.findViewById(R.id.fan_timer2_off);
                fan_timer3_on = view.findViewById(R.id.fan_timer3_on);
                fan_timer3_off = view.findViewById(R.id.fan_timer3_off);
                cool_onValue = view.findViewById(R.id.cool_onValue);
                cool_offValue = view.findViewById(R.id.cool_offValue);
                cool_timer1_on = view.findViewById(R.id.cool_timer1_on);
                cool_timer1_off = view.findViewById(R.id.cool_timer1_off);
                cool_timer2_on = view.findViewById(R.id.cool_timer2_on);
                cool_timer2_off = view.findViewById(R.id.cool_timer2_off);
                cool_timer3_on = view.findViewById(R.id.cool_timer3_on);
                cool_timer3_off = view.findViewById(R.id.cool_timer3_off);
                frost_onValue = view.findViewById(R.id.frost_onValue);
                frost_offValue = view.findViewById(R.id.frost_offValue);
                frost_timer1_on = view.findViewById(R.id.frost_timer1_on);
                frost_timer1_off = view.findViewById(R.id.frost_timer1_off);
                frost_timer2_on = view.findViewById(R.id.frost_timer2_on);
                frost_timer2_off = view.findViewById(R.id.frost_timer2_off);
                frost_timer3_on = view.findViewById(R.id.frost_timer3_on);
                frost_timer3_off = view.findViewById(R.id.frost_timer3_off);
                water_onValue = view.findViewById(R.id.water_onValue);
                water_offValue = view.findViewById(R.id.water_offValue);
                water_timer1_on = view.findViewById(R.id.water_timer1_on);
                water_timer1_off = view.findViewById(R.id.water_timer1_off);
                water_timer2_on = view.findViewById(R.id.water_timer2_on);
                water_timer2_off = view.findViewById(R.id.water_timer2_off);
                water_timer3_on = view.findViewById(R.id.water_timer3_on);
                water_timer3_off = view.findViewById(R.id.water_timer3_off);
        machine_bttn = new Switch[] {fan_bttn, cool_bttn, frost_bttn, water_bttn};
        timer_bttn = new Switch[]{fan_timer1_bttn, fan_timer2_bttn, fan_timer3_bttn, cool_timer1_bttn, cool_timer2_bttn, cool_timer3_bttn, frost_timer1_bttn, frost_timer2_bttn, frost_timer3_bttn, water_timer1_bttn, water_timer2_bttn, water_timer3_bttn};
        save_bttn = new Button[]{fan_timer_save_bttn, cool_timer_save_bttn, frost_timer_save_bttn, water_timer_save_bttn};
        fan_dropdown_bttn = view.findViewById(R.id.fan_dropdown_bttn);
        cool_dropdown_bttn = view.findViewById(R.id.cool_dropdown_bttn);
        frost_dropdown_bttn = view.findViewById(R.id.frost_dropdown_bttn);
        water_dropdown_bttn = view.findViewById(R.id.water_dropdown_bttn);
        fan_dropdown = view.findViewById(R.id.fan_dropdown);
        cool_dropdown = view.findViewById(R.id.cool_dropdown);
        frost_dropdown = view.findViewById(R.id.frost_dropdown);
        water_dropdown = view.findViewById(R.id.water_dropdown);
        fan_dropdown_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    if (fan_dropdown.getVisibility() == View.GONE) {
                        fan_dropdown.setVisibility(View.VISIBLE);
                    } else if (fan_dropdown.getVisibility() == View.VISIBLE) {
                        fan_dropdown.setVisibility(View.GONE);
                    }
                }
            }
        });
        cool_dropdown_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    if (cool_dropdown.getVisibility() == View.GONE) {
                        cool_dropdown.setVisibility(View.VISIBLE);
                    } else if (cool_dropdown.getVisibility() == View.VISIBLE) {
                        cool_dropdown.setVisibility(View.GONE);
                    }
                }
            }
        });
        frost_dropdown_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    if (frost_dropdown.getVisibility() == View.GONE) {
                        frost_dropdown.setVisibility(View.VISIBLE);
                    } else if (frost_dropdown.getVisibility() == View.VISIBLE) {
                        frost_dropdown.setVisibility(View.GONE);
                    }
                }
            }
        });
        water_dropdown_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    if (water_dropdown.getVisibility() == View.GONE) {
                        water_dropdown.setVisibility(View.VISIBLE);
                    } else if (water_dropdown.getVisibility() == View.VISIBLE) {
                        water_dropdown.setVisibility(View.GONE);
                    }
                }
            }
        });
        if (viewModel.getE_Id() != null) {
            getTimer(token);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBttn_status(fan_timer_bttn, cool_timer_bttn, frost_timer_bttn, water_timer_bttn, fan_timer, cool_timer, frost_timer, water_timer);
            }
        };


        for (Switch machine_button : machine_bttn) {
            machine_button.setOnClickListener(listener);
        }

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTimerBttn(fan_timer_bttn, cool_timer_bttn, frost_timer_bttn, water_timer_bttn, fan_timer, cool_timer, frost_timer, water_timer);
            }
        };

        for (Switch timer_button : timer_bttn) {
           timer_button.setOnClickListener(listener1);
        }

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.fan_timer_save_bttn) {
                    updateTimer("fan_timer", fan_bttn, fan_onValue, fan_offValue, fan_timer_bttn, fan_timer);
                } else if (id == R.id.cool_timer_save_bttn) {
                    updateTimer("cooling_timer",cool_bttn, cool_onValue, cool_offValue, cool_timer_bttn, cool_timer);
                } else if (id == R.id.frost_timer_save_bttn) {
                    updateTimer("frost_timer",frost_bttn, frost_onValue, frost_offValue, frost_timer_bttn, frost_timer);
                } else if (id == R.id.water_timer_save_bttn) {
                    updateTimer("water_timer",water_bttn, water_onValue, water_offValue, water_timer_bttn, water_timer);
                }
            }
        };

        for (Button button : save_bttn) {
            button.setOnClickListener(listener2);
        }
    }
    public void getTimer(String token) {
        ApiService.apiService.sendPOST_getTimer(token,"getTimer",viewModel.getE_Id()).enqueue(new Callback<List<Automation>>() {
            @Override
            public void onResponse(Call<List<Automation>> call, Response<List<Automation>> response) {
                Gson gson = new Gson();
                List<Automation> automationList = response.body();
                // Kiểm tra kết quả
                for (Automation automation : automationList) {
                    String[] fan_timer_arr = automation.getFan_timer().split("[,]");
                    String[] cooling_timer_arr = automation.getCooling_timer().split("[,]");
                    String[] frost_timer_arr = automation.getFrost_timer().split("[,]");
                    String[] water_timer_arr = automation.getWater_timer().split("[,]");
                    String[] fan_timer_status_arr = automation.getFan_timer_status().split("[,]");
                    String[] cool_timer_status_arr = automation.getCooling_timer_status().split("[,]");
                    String[] frost_timer_status_arr = automation.getFrost_timer_status().split("[,]");
                    String[] water_timer_status_arr = automation.getWater_timer_status().split("[,]");
                    // In các phần tử của mảng
                    fan_timer_bttn = new Switch[]{fan_timer1_bttn, fan_timer2_bttn, fan_timer3_bttn};
                    cool_timer_bttn = new Switch[]{cool_timer1_bttn, cool_timer2_bttn, cool_timer3_bttn};
                    frost_timer_bttn = new Switch[]{frost_timer1_bttn, frost_timer2_bttn, frost_timer3_bttn};
                    water_timer_bttn = new Switch[]{water_timer1_bttn, water_timer2_bttn, water_timer3_bttn};
                    fan_timer = new EditText[]{fan_timer1_on, fan_timer1_off, fan_timer2_on, fan_timer2_off, fan_timer3_on, fan_timer3_off};
                    cool_timer = new EditText[]{cool_timer1_on, cool_timer1_off, cool_timer2_on, cool_timer2_off, cool_timer3_on, cool_timer3_off};
                    frost_timer = new EditText[]{frost_timer1_on, frost_timer1_off, frost_timer2_on, frost_timer2_off, frost_timer3_on, frost_timer3_off};
                    water_timer = new EditText[]{water_timer1_on, water_timer1_off, water_timer2_on, water_timer2_off, water_timer3_on, water_timer3_off};

                    fan_bttn.setChecked(fan_timer_status_arr[0].equals("0") ? false:true);
                    cool_bttn.setChecked(cool_timer_status_arr[0].equals("0") ? false:true);
                    frost_bttn.setChecked(frost_timer_status_arr[0].equals("0") ? false:true);
                    water_bttn.setChecked(water_timer_status_arr[0].equals("0") ? false:true);

                    setTimers(fan_timer_bttn, fan_timer, fan_timer_status_arr, fan_timer_arr);
                    setTimers(cool_timer_bttn, cool_timer, cool_timer_status_arr, cooling_timer_arr);
                    setTimers(frost_timer_bttn, frost_timer, frost_timer_status_arr, frost_timer_arr);
                    setTimers(water_timer_bttn, water_timer, water_timer_status_arr, water_timer_arr);

                    fan_onValue.setText(fan_timer_arr[0]);
                    fan_offValue.setText(fan_timer_arr[1]);
                    cool_onValue.setText(cooling_timer_arr[0]);
                    cool_offValue.setText(cooling_timer_arr[1]);
                    frost_onValue.setText(frost_timer_arr[0]);
                    frost_offValue.setText(frost_timer_arr[1]);
                    water_onValue.setText(water_timer_arr[0]);
                    water_offValue.setText(water_timer_arr[1]);
                    checkBttn_status(fan_timer_bttn, cool_timer_bttn, frost_timer_bttn, water_timer_bttn, fan_timer, cool_timer, frost_timer, water_timer);
                }
            }

            @Override
            public void onFailure(Call<List<Automation>> call, Throwable t) {
                Log.e("err",t.getMessage());
            }
        });
    }

    public void checkBttn_status(Switch[] fan_timer_bttn, Switch[] cool_timer_bttn, Switch[] frost_timer_bttn, Switch[] water_timer_bttn,EditText[] fan_timer,EditText[] cool_timer,EditText[] frost_timer,EditText[] water_timer){
        handleSwitchBttn(fan_bttn, fan_onValue, fan_offValue, fan_timer_bttn, fan_timer);
        handleSwitchBttn(cool_bttn, cool_onValue, cool_offValue, cool_timer_bttn, cool_timer);
        handleSwitchBttn(frost_bttn, frost_onValue, frost_offValue, frost_timer_bttn, frost_timer);
        handleSwitchBttn(water_bttn, water_onValue, water_offValue, water_timer_bttn, water_timer);
    }
    public void checkTimerBttn(Switch[] fan_timer_bttn, Switch[] cool_timer_bttn, Switch[] frost_timer_bttn, Switch[] water_timer_bttn,EditText[] fan_timer,EditText[] cool_timer,EditText[] frost_timer,EditText[] water_timer){
        handleTimerSwitch(fan_bttn,fan_timer_bttn, fan_timer);
        handleTimerSwitch(cool_bttn,cool_timer_bttn, cool_timer);
        handleTimerSwitch(frost_bttn,frost_timer_bttn, frost_timer);
        handleTimerSwitch(water_bttn,water_timer_bttn, water_timer);
    };

    public void checkSaveBttn(int id) {
        if(id == R.id.fan_timer_save_bttn) {
            updateTimer("fan_timer", fan_bttn, fan_onValue, fan_offValue, fan_timer_bttn, fan_timer);
        } else if (id == R.id.cool_timer_save_bttn) {
            updateTimer("cooling_timer",cool_bttn, cool_onValue, cool_offValue, cool_timer_bttn, cool_timer);
        } else if (id == R.id.frost_timer_save_bttn) {
            updateTimer("frost_timer",frost_bttn, frost_onValue, frost_offValue, frost_timer_bttn, frost_timer);
        } else if (id == R.id.water_timer_save_bttn) {
            updateTimer("water_timer",water_bttn, water_onValue, water_offValue, water_timer_bttn, water_timer);
        }
    }
    private void handleSwitchBttn(@NonNull Switch mainSwitch, EditText onValue, EditText offValue, Switch[] timerSwitches, EditText[] timers) {
        if (mainSwitch.isChecked()) {
            onValue.setEnabled(true);
            offValue.setEnabled(true);
            for (Switch timerSwitch : timerSwitches) {
                timerSwitch.setChecked(false);
            }
            for (EditText timer : timers) {
                timer.setEnabled(false);
            }
        } else {
            onValue.setEnabled(false);
            offValue.setEnabled(false);
            for (int i = 0; i < timerSwitches.length; i++) {
                if (timerSwitches[i].isChecked()) {
                    mainSwitch.setChecked(false);
                    timers[i * 2].setEnabled(true);
                    timers[i * 2 + 1].setEnabled(true);
                } else {
                    timers[i * 2].setEnabled(false);
                    timers[i * 2 + 1].setEnabled(false);
                }
            }
        }
    }

    public void handleTimerSwitch(Switch mainSwitch, @NonNull Switch[] timerSwitch, EditText[] timer){
        for (int i = 0;i<timerSwitch.length;i++) {
            if (timerSwitch[i].isChecked()) {
                mainSwitch.setChecked(false);
                timer[i*2].setEnabled(true);
                timer[i*2+1].setEnabled(true);
            } else {
                timer[i*2].setEnabled(false);
                timer[i*2+1].setEnabled(false);
            }
        }
    };
    private void setTimers(Switch[] timerSwitches, EditText[] timers, @NonNull String[] statusArr, String[] timerArr) {
        for (int i = 1; i < statusArr.length; i++) {
            timerSwitches[i - 1].setChecked(statusArr[i].equals("0") ? false : true);
            timers[i * 2 - 2].setText(timerArr[i * 2]);
            timers[i * 2 - 1].setText(timerArr[i * 2 + 1]);
        }
    }
    public void updateTimer(String device, @NonNull Switch mainSwitch, @NonNull EditText onVal, @NonNull EditText offVal, Switch[] timer_bttn, EditText[] timer) {
        String device_timer_send = onVal.getText().toString() + "," + offVal.getText().toString();
        String timer_status = "";
        if (mainSwitch.isChecked()) {
            timer_status+="1";
        } else {
            timer_status+="0";
        }
        Boolean check = true;
        for (int i = 0;i<timer_bttn.length;i++) {
            if (timer_bttn[i].isChecked() && !timer[i*2].getText().toString().equals("") && !timer[i*2+1].getText().toString().equals("")) {
                timer_status+=",1";
            } else if (!timer_bttn[i].isChecked() && !timer[i*2].getText().toString().equals("") && !timer[i*2+1].getText().toString().equals("")) {
                timer_status+=",0";
            }
            if (timer_bttn[i].isChecked()) {
                if (timer[i*2].getText().toString().equals("") || timer[i*2+1].getText().toString().equals("")) {
                    check = false;
                }
            } else {
                if (timer[i*2].getText().toString().equals("") && timer[i*2+1].getText().toString().equals("")) {
                    check = true;
                } else if (!timer[i*2].getText().toString().equals("") && !timer[i*2+1].getText().toString().equals("")) {
                    check = true;
                } else {
                    check = false;
                }
            }

            if(check==true){
                device_timer_send+=","+timer[i*2].getText().toString() + "," + timer[i*2+1].getText().toString();
            }
        }

        if(check==false) {
            Toast.makeText(getContext(),"Vui lòng điền đầy đủ thời gian",Toast.LENGTH_SHORT).show();
        } else {
            ApiService.apiService.sendPOST_updateTimer(token,"updateTimer","23uyd324",device,timer_status,device_timer_send).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")) {
                        Toast.makeText(getContext(),"Cập nhật thành công.",Toast.LENGTH_SHORT).show();
                    } else if(response.body().equals("0")) {
                        Toast.makeText(getContext(),"Cập nhật thất bại.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(),"Có lỗi xảy ra, vui lòng thử lại.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
