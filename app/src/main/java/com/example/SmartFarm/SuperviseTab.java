package com.example.SmartFarm;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.SmartFarm.adapter.MqttViewModel;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SuperviseTab extends Fragment {
    private MqttViewModel viewModel;
    boolean time_out = true;
    Handler handler = new Handler();
    public SuperviseTab() {
        // Required empty public constructor
    }


    public static SuperviseTab newInstance() {
        SuperviseTab fragment = new SuperviseTab();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supervise_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MqttViewModel.class);
        super.onViewCreated(view, savedInstanceState);
        TextView rssi, gps_lat, gps_lon, temp, humidity, light, moisture, o2, co2, pH;
        Switch fan1_bttn, fan2_bttn, fan3_bttn, pump_bttn, cool_bttn, frost_bttn, water_bttn, light_bttn, isult_bttn;
        ImageButton status_bttn;
        status_bttn = view.findViewById(R.id.status_bttn);
        rssi = view.findViewById(R.id.rssi);
        gps_lat = view.findViewById(R.id.gps_lat);
        gps_lon = view.findViewById(R.id.gps_lon);
        temp = view.findViewById(R.id.temp);
        humidity = view.findViewById(R.id.humidity);
        light = view.findViewById(R.id.light);
        moisture = view.findViewById(R.id.moisture);
        o2 = view.findViewById(R.id.o2);
        co2 = view.findViewById(R.id.co2);
        pH = view.findViewById(R.id.pH);
        fan1_bttn = view.findViewById(R.id.fan1_bttn);
        fan2_bttn = view.findViewById(R.id.fan2_bttn);
        fan3_bttn = view.findViewById(R.id.fan3_bttn);
        pump_bttn = view.findViewById(R.id.pump_bttn);
        cool_bttn = view.findViewById(R.id.cool_bttn);
        frost_bttn = view.findViewById(R.id.frost_bttn);
        water_bttn = view.findViewById(R.id.water_bttn);
        light_bttn = view.findViewById(R.id.light_bttn);
        isult_bttn = view.findViewById(R.id.isult_bttn);
        viewModel = new ViewModelProvider(requireActivity()).get(MqttViewModel.class);
            viewModel.getMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onChanged(String message) {
//                {"data":"64,21.0380439,105.783475,30,77,87,0,0,0,0,0,0,0,0,1"}

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(message);
                        String dataString = jsonObject.getString("data");

                        // Tách các giá trị và lưu vào mảng
                        String[] dataArray = dataString.split(",");


                        // In ra mảng

                            rssi.setText(dataArray[0] + "%");
                            gps_lat.setText(convertToDMS(Double.valueOf(dataArray[1]),true));
                            gps_lon.setText(convertToDMS(Double.valueOf(dataArray[2]),false));
                            temp.setText(dataArray[3] + "°C");
                            humidity.setText(dataArray[4] + "%");
                            light.setText(dataArray[5] + "lux");
                            moisture.setText(dataArray[6] + "%");
                            if (time_out == true) {
                                fan1_bttn.setChecked(dataArray[7].equals("0") ? false : true);
                                fan2_bttn.setChecked(dataArray[8].equals("0") ? false : true);
                                fan3_bttn.setChecked(dataArray[9].equals("0") ? false : true);
                                pump_bttn.setChecked(dataArray[10].equals("0") ? false : true);
                                cool_bttn.setChecked(dataArray[11].equals("0") ? false : true);
                                frost_bttn.setChecked(dataArray[12].equals("0") ? false : true);
                                water_bttn.setChecked(dataArray[13].equals("0") ? false : true);
                                status_bttn.setBackgroundTintList(ColorStateList.valueOf(dataArray[14].equals("0") ? R.color.sea_blue : R.color.gray));
                                if (dataArray[14].equals("0")) {
                                    fan1_bttn.setClickable(false);
                                    fan2_bttn.setClickable(false);
                                    fan3_bttn.setClickable(false);
                                    pump_bttn.setClickable(false);
                                    cool_bttn.setClickable(false);
                                    frost_bttn.setClickable(false);
                                    water_bttn.setClickable(false);
                                    light_bttn.setClickable(false);
                                    isult_bttn.setClickable(false);
                                } else if (dataArray[14].equals("1")) {
                                    fan1_bttn.setClickable(true);
                                    fan2_bttn.setClickable(true);
                                    fan3_bttn.setClickable(true);
                                    pump_bttn.setClickable(true);
                                    cool_bttn.setClickable(true);
                                    frost_bttn.setClickable(true);
                                    water_bttn.setClickable(true);
                                    light_bttn.setClickable(true);
                                    isult_bttn.setClickable(true);
                                }
                            }


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        fan1_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(fan1_bttn.isChecked() ? "11" : "10");
                    setTime_out();
                }
            }
        });

        fan2_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(fan2_bttn.isChecked() ? "21" : "20");
                    setTime_out();
                }
            }
        });

        fan3_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(fan3_bttn.isChecked() ? "31" : "30");
                    setTime_out();
                }
            }
        });

        pump_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(pump_bttn.isChecked() ? "41" : "40");
                    setTime_out();
                }
            }
        });

        cool_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(cool_bttn.isChecked() ? "51" : "50");
                    setTime_out();
                }
            }
        });

        frost_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(frost_bttn.isChecked() ? "61" : "60");
                    setTime_out();
                }
            }
        });

        water_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    publishMessage(water_bttn.isChecked() ? "71" : "70");
                    setTime_out();
                }
            }
        });

        status_bttn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (viewModel.getE_Id() == null) {
                    Toast.makeText(getContext(),"Vui lòng chọn thiết bị.",Toast.LENGTH_SHORT).show();
                } else {
                    if (status_bttn.getBackgroundTintList().equals(ColorStateList.valueOf(R.color.sea_blue))) {
                        publishMessage("@");
                        status_bttn.setBackgroundTintList(ColorStateList.valueOf(R.color.gray));
                        fan1_bttn.setClickable(true);
                        fan2_bttn.setClickable(true);
                        fan3_bttn.setClickable(true);
                        pump_bttn.setClickable(true);
                        cool_bttn.setClickable(true);
                        frost_bttn.setClickable(true);
                        water_bttn.setClickable(true);
                        light_bttn.setClickable(true);
                        isult_bttn.setClickable(true);
                    } else if (status_bttn.getBackgroundTintList().equals(ColorStateList.valueOf(R.color.gray))) {
                        publishMessage("!");
                        status_bttn.setBackgroundTintList(ColorStateList.valueOf(R.color.sea_blue));
                        fan1_bttn.setClickable(false);
                        fan2_bttn.setClickable(false);
                        fan3_bttn.setClickable(false);
                        pump_bttn.setClickable(false);
                        cool_bttn.setClickable(false);
                        frost_bttn.setClickable(false);
                        water_bttn.setClickable(false);
                        light_bttn.setClickable(false);
                        isult_bttn.setClickable(false);
                    }
                    setTime_out();
                }
            }
        });
    }
    private void publishMessage(String content) {
        viewModel.publish(content);
    }
    private void setTime_out() {
        time_out = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time_out = true;
            }
        },4000);
    }

    // Phương thức chuyển từ tọa độ thập phân sang DMS (độ, phút, giây)
    private String convertToDMS(double decimalCoord, boolean isLatitude) {
        // Lấy phần nguyên là độ
        int degrees = (int) decimalCoord;

        // Lấy phần thập phân còn lại và tính phút
        double fractionalPart = Math.abs(decimalCoord - degrees);
        int minutes = (int) (fractionalPart * 60);

        // Lấy phần thập phân của phút và tính giây
        double seconds = (fractionalPart * 60 - minutes) * 60;

        // Định dạng kết quả thành DMS
        String direction;
        if (isLatitude) {
            direction = (degrees >= 0) ? "N" : "S";
        } else {
            direction = (degrees >= 0) ? "E" : "W";
        }

        return Math.abs(degrees) + "°" + minutes + "'" + String.format("%.2f", seconds) + "\" " + direction;
    }
}