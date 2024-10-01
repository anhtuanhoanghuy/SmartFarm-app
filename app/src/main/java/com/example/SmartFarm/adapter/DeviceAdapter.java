package com.example.SmartFarm.adapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SmartFarm.api.ApiService;
import com.example.SmartFarm.model.Device;
import com.example.myapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private Context mContext;
    private List<Device> mListDevice;
    private String token;

    public DeviceAdapter(List<Device> mListDevice) {
        this.mListDevice = mListDevice;
    }

    public DeviceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public DeviceAdapter(String token) {
        this.token = token;
    }

    public DeviceAdapter(Context mContext, String token) {
        this.mContext = mContext;
        this.token = token;
    }

    public void setData(List<Device> list) {
        this.mListDevice = list;
        notifyDataSetChanged();
    }
    public void setFilterList(List<Device> filterList) {
        this.mListDevice = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device,parent,false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Device device = mListDevice.get(position);
        if (device == null) {
            return;
        }
        holder.e_Id.setText(device.getE_Id());
        if (device.isNew()) {
            holder.e_Id.setEnabled(true);
        } else {
            holder.e_Id.setEnabled(false);
        }

        holder.devicename.setText(device.getDevicename());
        holder.more_bttn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                MenuBuilder menuBuilder;
                menuBuilder = new MenuBuilder(mContext);
                MenuInflater inflater = new MenuInflater(mContext);
                inflater.inflate(R.menu.more_option,menuBuilder);
                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(mContext,menuBuilder,view);
                menuPopupHelper.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {

                        if (item.getItemId() == R.id.connect_device) {
                            String update = "0";
                            if (!holder.e_Id.isEnabled()) {
                                update = "1";
                            }
                            ApiService.apiService.sendPOST_updateDevice(token,"addDevice",holder.devicename.getText().toString(), holder.e_Id.getText().toString(),update).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.body().equals("0")) {
                                        Toast.makeText(mContext,"Đã tồn tại thiết bị.",Toast.LENGTH_SHORT).show();
                                    } else if(response.body().equals("1")) {
                                        Toast.makeText(mContext,"Không tồn tại thiết bị.",Toast.LENGTH_SHORT).show();
                                    } else if(response.body().equals("2")) {
                                        Toast.makeText(mContext,"Thêm thiết bị thành công.",Toast.LENGTH_SHORT).show();
                                    } else if(response.body().equals("3")) {
                                        Toast.makeText(mContext,"Cập nhật thiết bị thành công.",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            return true;
                        } else if (item.getItemId() == R.id.delete_device) {
                            LinearLayout delete_device_dialog_layout = view.findViewById(R.id.delete_device_dialog_layout);
                            View view = LayoutInflater.from(mContext).inflate(R.layout.delete_device_dialog, delete_device_dialog_layout);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                                    ApiService.apiService.sendPOST_deleteDevice(token,"deleteDevice", device.getE_Id()).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if(response.body().equals("1")) {
                                                mListDevice.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, mListDevice.size());
                                                alertDialog.dismiss();
                                            } else if(response.body().equals("0")) {
                                                Toast.makeText(mContext,"Có lỗi xảy ra, vui lòng thử lại.",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                menuPopupHelper.show();
            }
        });
    }

    public void release() {
        mContext = null;
    }

    @Override
    public int getItemCount() {
        if(mListDevice != null) {
            return mListDevice.size();
        }
        return 0;
    }


    public static class DeviceViewHolder extends RecyclerView.ViewHolder{
        private EditText e_Id, devicename;
        ImageButton more_bttn;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            e_Id = itemView.findViewById(R.id.e_Id);
            devicename = itemView.findViewById(R.id.devicename);
            more_bttn = itemView.findViewById(R.id.more_bttn);
        }
    }



    public void addDevice(Device newDevice) {
        newDevice.setNew(true);
        mListDevice.add(newDevice);
        notifyItemInserted(mListDevice.size() - 1);
    }
}
