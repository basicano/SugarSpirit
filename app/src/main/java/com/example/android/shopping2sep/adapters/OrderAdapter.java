package com.example.android.shopping2sep.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.Models.OrderModel;
import com.example.android.shopping2sep.R;

import java.util.ArrayList;

public class OrderAdapter extends  RecyclerView.Adapter<OrderAdapter.CustomViewHolder>{
    public final String TAG = OrderAdapter.class.getSimpleName();

    OrderModel orderModel;
    ArrayList<OrderModel> order_list;
    private Activity parent_activity;

    public OrderAdapter(Activity parent_activity, ArrayList<OrderModel> order_list){
        this.order_list = order_list;
        this.parent_activity = parent_activity;
    }

    @NonNull
    @Override
    public OrderAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder() parameters parent: "+ parent.toString() +" viewType: "+viewType);
        View view = LayoutInflater.from(parent_activity).inflate(R.layout.order_row, null);
        OrderAdapter.CustomViewHolder viewHolder = new OrderAdapter.CustomViewHolder(view, parent_activity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.CustomViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() parameters holder: "+ holder.toString() +" position: "+position);

        orderModel = order_list.get(position);
        holder.nof_items.setText(orderModel.getNo_of_items());
        holder.pay_amt.setText(orderModel.getBilled_charge());
        holder.order_id.setText(orderModel.getOrder_id());
        holder.dely_on.setText(orderModel.getDely_date());
        holder.order_stats.setText(orderModel.getOrder_status());
    }

    @Override
    public int getItemCount() {
        return (null != order_list ? order_list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView nof_items, pay_amt, order_id, dely_on, order_stats;
        public AppCompatButton view_order;

        public CustomViewHolder(View view, Activity parent_activity){
            super(view);

            nof_items = (TextView) view.findViewById(R.id.OH_nof_items);
            pay_amt = (TextView) view.findViewById(R.id.OH_pay_amt);
            order_id = (TextView) view.findViewById(R.id.OH_order_id);
            dely_on = (TextView) view.findViewById(R.id.OH_dely_on);
            order_stats = (TextView) view.findViewById(R.id.OH_order_stat);

            view_order = (AppCompatButton) view.findViewById(R.id.OH_view_ordr);
            view_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    View Order details page
                }
            });
        }
    }
}
