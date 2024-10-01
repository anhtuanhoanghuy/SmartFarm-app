package com.example.SmartFarm.api;

import com.example.SmartFarm.model.Automation;
import com.example.SmartFarm.model.Device;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

//Link API:http://localhost:3000/SmartFarm/backend/Login.php
public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.95.1/SmartFarm/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    ApiService weatherApiService = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

//    https://api.openweathermap.org/data/2.5/weather?lat=20.3524474&lon=106.5552532&appid=2a57a02df82bc7d87088d657a944cb5a&units=metric&lang=vi
    @FormUrlEncoded
    @POST("backend/Login.php")
    Call<String> sendPOST_login(@Field("username") String username,
                                 @Field("password") String password);

    @FormUrlEncoded
    @POST("backend/Register.php")
    Call<String> sendPOST_register(@Field("accountname") String accountname,
                                    @Field("username") String username,
                                    @Field("password") String password);

    @GET("data/2.5/weather?appid=2a57a02df82bc7d87088d657a944cb5a&units=metric&lang=vi")
    Call<ResponseBody> sendGET_weather(@Query("lat") String lat,
                                       @Query("lon") String lon);

    @FormUrlEncoded
    @POST("backend/Device.php")
    Call<List<Device>> sendPOST_getDevice(@Field("token") String token,
                                         @Field("function") String function);
    @FormUrlEncoded
    @POST("backend/Device.php")
    Call<String> sendPOST_updateDevice(@Field("token") String token,
                                       @Field("function") String function,
                                       @Field("device_name") String device_name,
                                       @Field("device_eid") String device_eid,
                                       @Field("update") String update);

    @FormUrlEncoded
    @POST("backend/Device.php")
    Call<String> sendPOST_deleteDevice(@Field("token") String token,
                                       @Field("function") String function,
                                       @Field("device_eid") String device_eid);

    @FormUrlEncoded
    @POST("backend/ChangeAccount.php")
    Call<String> sendPOST_changeAccount(@Field("token") String token,
                                       @Field("accountname") String accountname,
                                       @Field("oldpassword") String oldpassword,
                                        @Field("newpassword") String newpassword);

    @FormUrlEncoded
    @POST("backend/Automation.php")
    Call<List<Automation>> sendPOST_getTimer(@Field("token") String token,
                                        @Field("function") String function,
                                        @Field("e_Id") String e_Id);

    @FormUrlEncoded
    @POST("backend/Automation.php")
    Call<String> sendPOST_updateTimer(@Field("token") String token,
                                          @Field("function") String function,
                                          @Field("e_Id") String e_Id,
                                          @Field("machine") String machine,
                                          @Field("timer_status") String timer_status,
                                          @Field("timer_data") String device_timer_send);

}
