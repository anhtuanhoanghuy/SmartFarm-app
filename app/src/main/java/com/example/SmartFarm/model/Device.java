package com.example.SmartFarm.model;

import androidx.annotation.NonNull;

public class Device {
    private String e_Id;
    private String devicename;
    private boolean isNew;

    public Device(String e_Id, String devicename) {
        this.e_Id = e_Id;
        this.devicename = devicename;
    }

    public Device(String e_Id, String devicename, boolean isNew) {
        this.e_Id = e_Id;
        this.devicename = devicename;
        this.isNew = isNew;
    }

    public String getE_Id() {
        return e_Id;
    }

    public void setE_id(String e_Id) {
        this.e_Id = e_Id;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @NonNull
    @Override
    public String toString() {
        if (devicename.equals("Không có thiết bị")) {
            return "";
        } else {
            return devicename + "    (e-id: " + e_Id + ")";
        }

    }
}
