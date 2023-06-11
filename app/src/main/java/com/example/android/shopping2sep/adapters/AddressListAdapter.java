package com.example.android.shopping2sep.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.ManageAddressActivity;
import com.example.android.shopping2sep.Models.AddressModel;
import com.example.android.shopping2sep.R;

import java.util.ArrayList;

public class AddressListAdapter extends  RecyclerView.Adapter<AddressListAdapter.CustomViewHolder> {
    public final String TAG = AddressListAdapter.class.getSimpleName();

    private ArrayList<AddressModel> addr_list;
    AddressModel address;
    private Activity activity;

    private int lastSelectedPosition = -1;

    public AddressListAdapter(Activity activity, ArrayList<AddressModel> addr_list) {
        this.addr_list = addr_list;
        this.activity = activity;
        ManageAddressActivity.addr_selected = false;
    }

    @NonNull
    @Override
    public AddressListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder() parameters parent: "+ parent.toString() +" viewType: "+viewType);
        View view = LayoutInflater.from(activity).inflate( R.layout.addr_row, null);
        AddressListAdapter.CustomViewHolder viewHolder = new AddressListAdapter.CustomViewHolder(view, activity);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(AddressListAdapter.CustomViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder() parameters holder: "+ holder.toString() +" position: "+position);
        address = addr_list.get(position);
        holder.addr_des.setText(address.getFullAddress());
        holder.select_addr.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return (null != addr_list ? addr_list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView addr_des;
        public RadioButton select_addr;
        public ImageView edit, delete;

        public CustomViewHolder(View view, Activity activity) {
            super(view);
//             Initializing views
            addr_des = (TextView) view.findViewById(R.id.addr_des_tv);
            select_addr = (RadioButton) view.findViewById(R.id.addr_select_rb);

            edit = (ImageView) view.findViewById(R.id.edit_addr);
            delete  = (ImageView) view.findViewById(R.id.delete_addr);

//            Address Radio button onClickListener
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    ManageAddressActivity.del_address = addr_list.get(getAdapterPosition());
                    ManageAddressActivity.addr_selected = true;
                    notifyDataSetChanged();
//                    Log.d(TAG, "Current selected delivery address : " + ManageAddressActivity.del_address + " Receiver name : " + ManageAddressActivity.del_address.getRcvr_name());
                }
            };
            itemView.setOnClickListener(clickListener);
            select_addr.setOnClickListener(clickListener);
        }
    }

}
