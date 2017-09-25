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
import com.rakshasindhu.shoppinglistlayout.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
 * Created by Raksha Sindhu on 8/29/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements View.OnClickListener {

    private final ArrayList<Products> productsArrayList;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public ProductsAdapter(Context context, ArrayList<Products> productsArrayList){
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.productsArrayList = productsArrayList;
    }

    public interface OnRecyclerViewItemClickListener
    {
        void onItemClick(View view, String data);
    }

    private ProductsAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ProductsAdapter.OnRecyclerViewItemClickListener listener) {
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView _ID;
        final TextView name;
        final TextView quantity;
        final TextView price;
        final TextView description;
        final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this._ID =  itemView.findViewById(R.id.product_id);
            this.name = itemView.findViewById(R.id.product_name);
            this.description =  itemView.findViewById(R.id.product_description);
            this.image =itemView.findViewById(R.id.product_image);
            this.quantity =itemView.findViewById(R.id.product_quantity);
            this.price = itemView.findViewById(R.id.product_price);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder._ID.setText(productsArrayList.get(position).ID);
        holder.name.setText(productsArrayList.get(position).ProductName);
        holder.description.setText(productsArrayList.get(position).Description);
        holder.quantity.setText(productsArrayList.get(position).Quantity);
        holder.price.setText(productsArrayList.get(position).Price);
        holder.itemView.setTag(productsArrayList.get(position).ID);
        Picasso.with(context)
                .load(productsArrayList.get(position).Image)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }
}
