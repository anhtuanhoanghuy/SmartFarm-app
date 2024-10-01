package com.example.SmartFarm.adapter;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MqttViewModel extends ViewModel {
    private String e_Id;
    private MqttAndroidClient mqttClient;
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void setMessage(String message) {
        messageLiveData.setValue(message);
    }


    public void setMqttClient(MqttAndroidClient client) {
        this.mqttClient = client;
    }

    public MqttAndroidClient getMqttClient() {
        return mqttClient;
    }

    public String getE_Id() {
        return e_Id;
    }

    public void setE_Id(String e_Id) {
        this.e_Id = e_Id;
    }

    public void publish(String content) {
        String topic = e_Id + "/control";;
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = content.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            mqttClient.publish(topic, message);
            Log.d("publish:",topic +  ": " + message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
}