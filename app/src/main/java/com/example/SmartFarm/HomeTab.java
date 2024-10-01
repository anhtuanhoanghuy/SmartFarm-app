package com.example.SmartFarm;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.SmartFarm.adapter.DeviceAdapter2;
import com.example.SmartFarm.adapter.MqttViewModel;
import com.example.SmartFarm.api.ApiService;
import com.example.SmartFarm.model.Device;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeTab extends Fragment implements OnMapReadyCallback {
    private MqttViewModel viewModel;
    private GoogleMap mGoogleMap;
    private Fragment mapFragment;
    private double lat, lon;
    TextView weather_place, weather_temp, weather_humidity, weather_wind, weather_description;
    ImageView weather_icon;
    public HomeTab() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weather_place = view.findViewById(R.id.weather_place);
        weather_temp = view.findViewById(R.id.weather_temp);
        weather_humidity = view.findViewById(R.id.weather_humidity);
        weather_wind = view.findViewById(R.id.weather_wind);
        weather_description = view.findViewById(R.id.weather_description);
        weather_icon = view.findViewById(R.id.weather_icon);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String token = ((MainActivity) getActivity()).getToken();
        AutoCompleteTextView auto_complete;
        auto_complete = view.findViewById(R.id.auto_complete);
        getDevice(token,auto_complete);
        SwipeRefreshLayout swipeRefreshLayout1;
        swipeRefreshLayout1 = view.findViewById(R.id.swipe_layout_1);
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDevice(token,auto_complete);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout1.setRefreshing(false);
                    }
                },2000);
            }
        });

        auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedDevice = adapterView.getItemAtPosition(i).toString();
                String regex = "e-id: (\\w+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(selectedDevice);

                if (matcher.find()) {
                    String eID = matcher.group(1);
                    MqttConnectOptions options = new MqttConnectOptions();
                    options.setUserName("hoanghuyanhtuan");
                    options.setPassword("Tuan2002".toCharArray());
                    String clientId = MqttClient.generateClientId();
                    viewModel = new ViewModelProvider(requireActivity()).get(MqttViewModel.class);
                    MqttViewModel mqttViewModel = new ViewModelProvider(requireActivity()).get(MqttViewModel.class);
                    MqttAndroidClient client =
                            new MqttAndroidClient(getContext(), "ssl://35a196d8b54146f08f917c8c382e1c0a.s1.eu.hivemq.cloud:8883",
                                    clientId);
                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            Log.d("message",message.toString());
                            mqttViewModel.setMessage(message.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(String.valueOf(message));
                                String dataString = jsonObject.getString("data");

                                // Tách các giá trị và lưu vào mảng
                                String[] dataArray = dataString.split(",");
                                // In ra mảng
                                    lat = Double.valueOf(dataArray[1]);
                                    lon = Double.valueOf(dataArray[2]);
                                if (mGoogleMap != null) {
                                    LatLng location = new LatLng(lat, lon);
                                    // Xóa các marker cũ nếu có
                                    mGoogleMap.clear();
                                    // Thêm marker mới
                                    mGoogleMap.addMarker(new MarkerOptions().position(location).title(eID));
                                    // Di chuyển camera đến vị trí mới
                                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                                }
                                getWeather(lat,lon);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });

                    try {
                        IMqttToken token = client.connect(options);
                        viewModel.setE_Id(eID);
                        viewModel.setMqttClient(client);
                        token.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // We are connected
                                Toast.makeText(getContext(),"Kết nối thành công đến thiết bị " + eID,Toast.LENGTH_SHORT).show();
                                String topic = eID+"/client";
                                int qos = 2;
                                try {
                                    IMqttToken subToken = client.subscribe(topic, qos);
                                    subToken.setActionCallback(new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {
                                            // The message was published
                                            Log.d("subscribe","subscribed to " + topic);
                                        }

                                        @Override
                                        public void onFailure(IMqttToken asyncActionToken,
                                                              Throwable exception) {
                                            // The subscription could not be performed, maybe the user was not
                                            // authorized to subscribe on the specified topic e.g. using wildcards

                                        }
                                    });
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                // Something went wrong e.g. connection timeout or firewall problems
                                Toast.makeText(getContext(),"onFailure",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public static HomeTab newInstance() {
        HomeTab fragment = new HomeTab();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_tab, container, false);
    }

    public void getDevice(String token, AutoCompleteTextView auto_complete) {
        ApiService.apiService.sendPOST_getDevice(token,"getDevice").enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                List<Device> deviceList = response.body();
                if (deviceList.isEmpty()) {
                    deviceList.add(new Device("","Không có thiết bị"));
                }
                DeviceAdapter2 adapter = new DeviceAdapter2(getContext(), deviceList);
                auto_complete.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                Toast.makeText(getContext(),"Có lỗi xảy ra, vui lòng thử lại.",Toast.LENGTH_SHORT).show();
            }
        });
    }
//21.0380439,105.783475
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    private void getWeather(Double lat, Double lon) {
        ApiService.weatherApiService.sendGET_weather(String.valueOf(lat),String.valueOf(lon)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    weather_place.setText(jsonObject.getString("name"));
                    weather_temp.setText(jsonObject.getJSONObject("main").getDouble("temp") + "°C");
                    weather_humidity.setText(jsonObject.getJSONObject("main").getInt("humidity") + "%");
                    weather_wind.setText(jsonObject.getJSONObject("wind").getDouble("speed") + " km/h");
                    weather_description.setText(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                    String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                    Glide.with(HomeTab.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(weather_icon);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("api",t.getMessage());
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}