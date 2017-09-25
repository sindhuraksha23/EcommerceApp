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
import com.rakshasindhu.shoppinglistlayout.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements View.OnClickListener{

    private final ArrayList<Category> categoryList;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList){
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.categoryList = categoryList;
    }
    //define interface
    public interface OnRecyclerViewItemClickListener
    {
        void onItemClick(View view, String data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
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
        final TextView description;
        final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this._ID = itemView.findViewById(R.id.category_id);
            this.name = itemView.findViewById(R.id.category_name);
            this.image = itemView.findViewById(R.id.category_image);
            this.description = itemView.findViewById(R.id.category_description);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder._ID.setText(categoryList.get(position).ID);
        holder.name.setText(categoryList.get(position).CatagoryName);
        holder.description.setText(categoryList.get(position).CatagoryDiscription);
        holder.itemView.setTag(categoryList.get(position).ID);
        Picasso.with(context)
                .load(categoryList.get(position).CatagoryImage)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

}
