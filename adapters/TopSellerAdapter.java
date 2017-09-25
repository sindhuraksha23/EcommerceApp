package com.rakshasindhu.shoppinglistlayout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.model.TopSeller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
 * Created by Raksha Sindhu on 8/30/2017.
 */

public class TopSellerAdapter extends RecyclerView.Adapter<TopSellerAdapter.ViewHolder> implements View.OnClickListener{

    public String TAG = TopSeller.class.getSimpleName();

    private final ArrayList<TopSeller> topSellerArrayList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public TopSellerAdapter(Context context, ArrayList<TopSeller> topSellerArrayList){

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.topSellerArrayList = topSellerArrayList;
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }
    private TopSellerAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(TopSellerAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view,String.valueOf(view.getTag()));
        }
        else{
            Log.e("CLICK", "ERROR");
        }
    }

    @Override
    public TopSellerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.top_seller_item, parent, false);
        ViewHolder rViewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return rViewHolder;
    }

    @Override
    public void onBindViewHolder(TopSellerAdapter.ViewHolder holder, int position) {
        holder.id.setText(topSellerArrayList.get(position).SellerId);
        holder.name.setText(topSellerArrayList.get(position).SellerName);
        holder.deal.setText(topSellerArrayList.get(position).SellerDeal);
        holder.rating.setText(topSellerArrayList.get(position).SellerRating);
        holder.itemView.setTag(topSellerArrayList.get(position).SellerId);
        Picasso.with(context)
                .load(topSellerArrayList.get(position).SellerLogo)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return topSellerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView id;
        final TextView name;
        final TextView deal;
        final TextView rating;
        final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.top_seller_id);
            this.name = itemView.findViewById(R.id.top_seller_name);
            this.deal =  itemView.findViewById(R.id.top_seller_deal);
            this.rating =  itemView.findViewById(R.id.top_seller_rating);
            this.image =  itemView.findViewById(R.id.top_seller_image);
        }
    }

}
