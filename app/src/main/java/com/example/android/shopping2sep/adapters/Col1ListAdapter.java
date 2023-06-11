package com.example.android.shopping2sep.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.CartFunctionality;
import com.example.android.shopping2sep.Models.ProductDetailModel;
import com.example.android.shopping2sep.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Col1ListAdapter extends RecyclerView.Adapter <Col1CustomViewHolder>{
    public static final String TAG = Col1ListAdapter.class.getSimpleName();

    private ArrayList<ProductDetailModel> productList;
    private ProductDetailModel product;
    HashMap<String, String> cart_summary;
    private Activity activity;

//    public Col1ListAdapter(Context context, ArrayList<ProductDetailModel> productList ){
//        Log.d(TAG, "Col1ListAdapter() constructor parameters productList: "+ productList.toString());
//        this.context = context;
//        this.productList = productList;
//    }

    public Col1ListAdapter(Activity activity, ArrayList<ProductDetailModel> productList, HashMap<String, String> cart_summary){
        Log.d(TAG, "Col1ListAdapter() constructor parameters productList: "+ productList.toString());
        this.activity = activity;
        this.productList = productList;
        this.cart_summary = cart_summary;
    }

    @NonNull
    @Override
    public Col1CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() parameters parent: "+ parent.toString() +" viewType: "+viewType);
        View view = LayoutInflater.from(activity).inflate( R.layout.col1_row, null);
        Col1CustomViewHolder viewHolder = new Col1CustomViewHolder(view, activity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Col1CustomViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder() parameters holder: "+ holder.toString() +" position: "+position);

        product = productList.get(position);
        holder.product_id.setText(product.getId());
        holder.title_tv.setText(product.getName());
        holder.des_tv.setText(product.getDes());

//        Typeface typefaceB = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-SemiBold.ttf");
//        holder.title_tv.setTypeface(typefaceB);
//        Typeface typefaceR = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-Regular.ttf");
//        holder.des_tv.setTypeface(typefaceR);

        if (product.getImg() != null && !  product.getImg().isEmpty()) {

            if (product.getImg().toString().length() > 0) {
                Log.v("EventListAdapter", "item image=" + product.get_full_img_path() );
                Picasso.get()
                        .load(product.get_full_img_path())
                        .placeholder(R.mipmap.ic_launcher)       // optional
                        .error(R.drawable.no_image)             // optional
                        .into(holder.product_iv);
            }
        }

        if(cart_summary.containsKey(product.getId()) ) {
            holder.add_btn.setVisibility(View.INVISIBLE);
            holder.added_btn.setVisibility(View.VISIBLE);
            holder.qtn_tv.setText(cart_summary.get(product.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return (null != productList ? productList.size() : 0);
    }

}

class Col1CustomViewHolder extends RecyclerView.ViewHolder{
    public final String TAG = Col1CustomViewHolder.class.getSimpleName();
    public ImageView product_iv;
    public TextView title_tv, des_tv, product_id;
    public CardView product_cv;
    public Button add_btn;

    public LinearLayout added_btn;
    public ImageButton remove_ib, add_ib;
    public TextView qtn_tv;

    CartFunctionality cartFunctionality;

    public Col1CustomViewHolder(@NonNull View itemView, Activity activity) {
        super(itemView);
        Log.d(TAG, "Col1CustomViewHolder() parameters itemView: "+ itemView.toString());

        product_iv = (ImageView) itemView.findViewById(R.id.col1_iv);
        product_id = (TextView) itemView.findViewById(R.id.col1_prod_id);
        title_tv = (TextView)itemView.findViewById(R.id.col1_tv_title);
        des_tv = (TextView) itemView.findViewById(R.id.col1_tv_des);

        add_btn = (Button)itemView.findViewById(R.id.col1_btn_add);

        added_btn= (LinearLayout) itemView.findViewById(R.id.col1_btn_added);
        remove_ib = (ImageButton) itemView.findViewById(R.id.remove_col1_ib);
        add_ib = (ImageButton) itemView.findViewById(R.id.add_col1_ib);
        qtn_tv = (TextView) itemView.findViewById(R.id.qtn_col1_tv);
        added_btn.setVisibility(View.INVISIBLE);

        cartFunctionality = new CartFunctionality(activity);

        add_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "here");
                int qtn = Integer.parseInt(qtn_tv.getText().toString());
                ++qtn;
                qtn_tv.setText(String.format("%d", qtn));
                cartFunctionality.updateQnty(product_id.getText().toString(),qtn_tv.getText().toString());
            }
        });

        remove_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtn = Integer.parseInt(qtn_tv.getText().toString());
                --qtn;
                if(qtn<=0){
                    added_btn.setVisibility(View.INVISIBLE);
                    add_btn.setVisibility(View.VISIBLE);
                    cartFunctionality.removeFromCart(product_id.getText().toString());
                }
                else {
                    qtn_tv.setText(String.format("%d", qtn));
                    cartFunctionality.updateQnty(product_id.getText().toString(),qtn_tv.getText().toString());
                }
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "add_btn onClick()");
                add_btn.setVisibility(View.INVISIBLE);
                added_btn.setVisibility(View.VISIBLE);
                int qtn = Integer.parseInt(qtn_tv.getText().toString());
                ++qtn;
                qtn_tv.setText(String.format("%d", qtn));
                cartFunctionality.addToCart(product_id.getText().toString(),qtn_tv.getText().toString());
            }
        });



        product_cv = (CardView) itemView.findViewById(R.id.col1_cv);
        product_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "product_cv onClick()");
                // TODO display product description
            }
        });
    }

}
