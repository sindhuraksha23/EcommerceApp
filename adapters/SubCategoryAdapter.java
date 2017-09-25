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
import com.rakshasindhu.shoppinglistlayout.model.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
 * Created by Raksha Sindhu on 8/29/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> implements View.OnClickListener {

    private final ArrayList<SubCategory> subcategoryList;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public SubCategoryAdapter(Context context, ArrayList<SubCategory> subcategoryList){
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.subcategoryList = subcategoryList;
    }
    public interface OnRecyclerViewItemClickListener
    {
        void onItemClick(View view, String data);
    }
    private SubCategoryAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(SubCategoryAdapter.OnRecyclerViewItemClickListener listener) {
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

        final TextView ID;
        final TextView name;
        final TextView description;
        final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ID = itemView.findViewById(R.id.sub_category_id);
            this.name =  itemView.findViewById(R.id.sub_category_name);
            this.image =  itemView.findViewById(R.id.sub_category_image);
            this.description =  itemView.findViewById(R.id.sub_category_description);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_list_item,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ID.setText(subcategoryList.get(position).ID);
        holder.name.setText(subcategoryList.get(position).SubCatagoryName);
        holder.description.setText(subcategoryList.get(position).SubCatagoryDiscription);
        holder.itemView.setTag(subcategoryList.get(position).ID);
        Picasso.with(context)
                .load(subcategoryList.get(position).CatagoryImage)
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

}
