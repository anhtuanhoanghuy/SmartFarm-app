package com.example.SmartFarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.SmartFarm.model.Device;
import com.example.myapplication.R;

import java.util.List;

public class DeviceAdapter2  extends ArrayAdapter<Device> {
    private Context mContext;
    private List<Device> deviceList;


    public DeviceAdapter2(@NonNull Context context, @NonNull List<Device> deviceList) {
        super(context, 0, deviceList);
        this.mContext = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_device,parent,false);
        }
        Device device = deviceList.get(position);

        TextView device_name = convertView.findViewById(R.id.device_name);
        TextView e_Id = convertView.findViewById(R.id.e_Id);
        TextView e_Id_lable = convertView.findViewById(R.id.e_Id_label);
        device_name.setText(device.getDevicename());
        if (device.getE_Id().equals("")) {
            e_Id_lable.setVisibility(View.INVISIBLE);
        } else {
            e_Id_lable.setVisibility(View.VISIBLE);
        }
        e_Id.setText(device.getE_Id());

        return convertView;
    }
}
