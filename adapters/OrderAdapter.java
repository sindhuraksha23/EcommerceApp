package com.rakshasindhu.shoppinglistlayout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.model.Order;

import java.util.ArrayList;

/*
 * Created by Raksha Sindhu on 8/30/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<Order> orderArrayList;
    private final LayoutInflater layoutInflater;

    public OrderAdapter(Context context,ArrayList<Order> orderArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.orderArrayList = orderArrayList;
    }

    //define interface
    public interface OnRecyclerViewItemClickListener
    {
        void onItemClick(View view, String data);
    }

    private OrderAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OrderAdapter.OnRecyclerViewItemClickListener listener) {
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
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.order_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        holder.id.setText(orderArrayList.get(position).OrderID);
        holder.name.setText(orderArrayList.get(position).ItemName);
        holder.quantity.setText(orderArrayList.get(position).ItemQuantity);
        holder.prize.setText(orderArrayList.get(position).FinalPrice);
        holder.status.setText(orderArrayList.get(position).OrderStatus);
        holder.itemView.setTag(orderArrayList.get(position).OrderID);

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        final TextView id;
        final TextView name;
        final TextView quantity;
        final TextView status;
        final TextView prize;

        public ViewHolder(View itemView) {
            super(itemView);
            this.id =  itemView.findViewById(R.id.order_id);
            this.name = itemView.findViewById(R.id.order_name);
            this.quantity =  itemView.findViewById(R.id.order_quantity);
            this.prize =  itemView.findViewById(R.id.order_prize);
            this.status =  itemView.findViewById(R.id.order_status);
        }
    }

    public void notifyData(ArrayList<Order> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.orderArrayList = myList;
        notifyDataSetChanged();
    }
}
