package com.example.android.shopping2sep.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.CartFunctionality;
import com.example.android.shopping2sep.Models.CartProductModel;
import com.example.android.shopping2sep.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartCustomViewHolder> {
    public static final String TAG = Col1ListAdapter.class.getSimpleName();
    private Activity activity;

    public ArrayList<CartProductModel> cartList;
    private CartProductModel product;

    TextView subTot_tv;

    public CartListAdapter(){}

    public CartListAdapter(Activity activity, ArrayList<CartProductModel> cartList){
        Log.d(TAG, "Col1ListAdapter() constructor parameters productList: "+ cartList.toString());
        this.activity = activity;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() parameters parent: "+ parent.toString() +" viewType: "+viewType);
        subTot_tv = (TextView) parent.findViewById(R.id.subtot_ca_tv);
        View view = LayoutInflater.from(activity).inflate( R.layout.cart_row, null);
        CartCustomViewHolder viewHolder = new CartCustomViewHolder(view,activity );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartCustomViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder() parameters holder: "+ holder.toString() +" position: "+position);

        product = cartList.get(position);
        holder.product_id.setText(product.getProduct_id());
        holder.name_tv.setText(product.getProduct_name());
//        holder.mrp_tv.setText(product.getMrp());
//        holder.selprice_tv.setText(product.getSel_price());
//        holder.weight_tv.setText(product.getWeight());
        holder.qtn_tv.setText(product.getQtn());

//        Typeface typefaceB = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-SemiBold.ttf");
//        holder.title_tv.setTypeface(typefaceB);
//        Typeface typefaceR = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-Regular.ttf");
//        holder.des_tv.setTypeface(typefaceR);

        if (product.getProduct_img() != null && !  product.getProduct_img().isEmpty()) {

            if (product.getProduct_img().length() > 0) {
                Log.v("EventListAdapter", "item image=" + product.get_full_img_path() );
                Picasso.get()
                        .load(product.get_full_img_path())
                        .placeholder(R.mipmap.ic_launcher)       // optional
                        .error(R.drawable.no_image)             // optional
                        .into(holder.product_iv);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != cartList ? cartList.size() : 0);
    }


    public  class CartCustomViewHolder extends RecyclerView.ViewHolder{
        public final String TAG = CartCustomViewHolder.class.getSimpleName();
        public TextView product_id;
        public LinearLayout row_ll;

        public ImageView product_iv;
        public TextView name_tv, mrp_tv, selprice_tv , weight_tv, qtn_tv;
        public ImageButton add_btn, remove_btn;

        private CartFunctionality cartFunctionality;

        public CartCustomViewHolder(@NonNull View itemView){
            super(itemView);
        }

        public CartCustomViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            Log.d(TAG, "CartCustomViewHolder() parameters itemView: "+ itemView.toString());

            product_id = (TextView) itemView.findViewById(R.id.cart_prod_id);
            product_iv = (ImageView) itemView.findViewById(R.id.img_cr_iv);
            name_tv = (TextView)itemView.findViewById(R.id.name_cr_tv);
            qtn_tv = (TextView) itemView.findViewById(R.id.qtn_cr_tv);

            mrp_tv = (TextView) itemView.findViewById(R.id.mrp_cr_tv);
            selprice_tv = (TextView) itemView.findViewById(R.id.selprice_cr_tv);
            weight_tv =(TextView) itemView.findViewById(R.id.wgt_cr_tv);

            cartFunctionality = new CartFunctionality(activity);
            add_btn = (ImageButton)itemView.findViewById(R.id.add_cr_btn);
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "add_btn onClick()");
                    int prev_qtn = Integer.parseInt(qtn_tv.getText().toString());
                    int cur_qtn = prev_qtn+1;
                    qtn_tv.setText(String.format("%d", cur_qtn));
                    float prev_subTot = Float.parseFloat(subTot_tv.getText().toString());
                    float product_mrp = Float.parseFloat(mrp_tv.getText().toString());
                    float cur_subTot = prev_subTot + product_mrp;
                    subTot_tv.setText(String.format("%d", cur_subTot));
                    cartFunctionality.updateQnty(product_id.getText().toString(),qtn_tv.getText().toString());
                }
            });

            remove_btn = (ImageButton) itemView.findViewById(R.id.remove_cr_btn);
            remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "remove_btn onClick()");
                    int prev_qtn = Integer.parseInt(qtn_tv.getText().toString());
                    int cur_qtn = prev_qtn-1;

                    float prev_subTot = Float.parseFloat(subTot_tv.getText().toString());
                    float product_mrp = Float.parseFloat(mrp_tv.getText().toString());
                    float cur_subTot = prev_subTot - product_mrp;
                    subTot_tv.setText(String.format("%d", cur_subTot));

                    if(cur_qtn<=0){
                        cartFunctionality.removeFromCart(product_id.getText().toString());

                        remove_from_view(getAdapterPosition());
                    }
                    else {
                        qtn_tv.setText(String.format("%d", cur_qtn));
                        cartFunctionality.updateQnty(product_id.getText().toString(),qtn_tv.getText().toString());
                    }

                }
            });

            row_ll = (LinearLayout) itemView.findViewById(R.id.cart_row_ll);
            row_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "row_ll onClick()");
//                TODO View Product Activity
                }
            });
        }

    }

    public void remove_from_view(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartList.size());
    }
}


//class CartCustomViewHolder extends RecyclerView.ViewHolder{
//    public final String TAG = CartCustomViewHolder.class.getSimpleName();
//    private final TextView product_id;
//    public LinearLayout row_ll;
//
//    public ImageView product_iv;
//    public TextView name_tv, mrp_tv, selprice_tv , weight_tv, qtn_tv;
//    public ImageButton add_btn, remove_btn;
//
//    private CartFunctionality cartFunctionality;
//
//    public CartCustomViewHolder(@NonNull View itemView, Activity activity) {
//        super(itemView);
//        Log.d(TAG, "CartCustomViewHolder() parameters itemView: "+ itemView.toString());
//
//        product_id = (TextView) itemView.findViewById(R.id.cart_prod_id);
//        product_iv = (ImageView) itemView.findViewById(R.id.img_cr_iv);
//        name_tv = (TextView)itemView.findViewById(R.id.name_cr_tv);
//        qtn_tv = (TextView) itemView.findViewById(R.id.qtn_cr_tv);
//
//        mrp_tv = (TextView) itemView.findViewById(R.id.mrp_cr_tv);
//        selprice_tv = (TextView) itemView.findViewById(R.id.selprice_cr_tv);
//        weight_tv =(TextView) itemView.findViewById(R.id.wgt_cr_tv);
//
//        cartFunctionality = new CartFunctionality(activity);
//        add_btn = (ImageButton)itemView.findViewById(R.id.add_cr_btn);
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "add_btn onClick()");
//                int qtn = Integer.parseInt(qtn_tv.getText().toString());
//                ++qtn;
//                qtn_tv.setText(qtn);
//                cartFunctionality.updateQnty(product_id.getText().toString(),qtn_tv.getText().toString());
//            }
//        });
//
//        remove_btn = (ImageButton) itemView.findViewById(R.id.remove_cr_btn);
//        remove_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "remove_btn onClick()");
//                    int qtn = Integer.parseInt(qtn_tv.getText().toString());
//                    --qtn;
//                    if(qtn<=0){
//                        cartFunctionality.removeFromCart(product_id.getText().toString());
//                    }
//                    else {
//                        qtn_tv.setText(qtn);
//                        cartFunctionality.updateQnty(product_id.getText().toString(),qtn_tv.getText().toString());
//                    }
//
//            }
//        });
//
//        row_ll = (LinearLayout) itemView.findViewById(R.id.cart_row_ll);
//        row_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "row_ll onClick()");
//            }
//        });
//    }
//
//}