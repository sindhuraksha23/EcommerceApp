package com.rakshasindhu.shoppinglistlayout.adapters;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.db.SQL;
import com.rakshasindhu.shoppinglistlayout.model.MyCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.rakshasindhu.shoppinglistlayout.db.SQL.KEY_ID;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.PRICE;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.QUANTITY;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.TABLE;

/*
 * Created by Raksha Sindhu on 8/31/2017.
 */

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> implements View.OnClickListener {

    private final ArrayList<MyCart> myCartArrayList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    private SQL sqlHelper;
    private SQLiteDatabase db;
    MyCartAdapter myCartAdapter;

    public MyCartAdapter(Context context,ArrayList<MyCart> myCartArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.myCartArrayList = myCartArrayList;

    }

    public interface OnRecyclerViewItemClickListener
    {
        void onItemClick(View view, String data);
    }

    private MyCartAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(MyCartAdapter.OnRecyclerViewItemClickListener listener) {
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
    public MyCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_items,null);
        view.setOnClickListener(this);

        sqlHelper = new SQL(context);
        db = sqlHelper.getWritableDatabase();

        return new MyCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyCartAdapter.ViewHolder holder, final int position) {
        holder.Item_ID.setText(myCartArrayList.get(position).ID);
        holder.Item_name.setText(myCartArrayList.get(position).ProductName);
        holder.Item_quantity.setText(myCartArrayList.get(position).Quantity);
        holder.Item_price.setText(myCartArrayList.get(position).Price);
        holder.itemView.setTag(myCartArrayList.get(position).ID);
        Picasso.with(context)
                .load(myCartArrayList.get(position).Image)
                .into(holder.image);

        holder.required_quantity.setText(myCartArrayList.get(position).Quantity);

        final int posget=Integer.valueOf(myCartArrayList.get(position).ID);
        final int unit_price =Integer.valueOf(myCartArrayList.get(position).Price)/
                Integer.valueOf(myCartArrayList.get(position).Quantity);


        holder.remove_item_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Postion",posget+"");
                db.execSQL("DELETE FROM " + TABLE+ " WHERE "+KEY_ID+"='"+posget+"'");
                myCartArrayList.remove(position);
                MyCartAdapter.this.notifyDataSetChanged();


            }
        });



        holder.add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                int number = Integer.valueOf(holder.required_quantity.getText().toString());

                if(number>=1){
                            number+=1;
                    int final_order_price = unit_price*number;
                    holder.Item_quantity.setText(number+"");
                    holder.required_quantity.setText(number+"");
                    holder.Item_price.setText(final_order_price+"");

                    String Query_PRICE = "UPDATE "+ TABLE+ " SET " +PRICE+ "='"+final_order_price+"'"+
                            " WHERE "+ KEY_ID+ "='"+posget+"'";
                    String Query_QUANTITY = "UPDATE "+ TABLE+ " SET " +QUANTITY+ "='"+number+"'"+
                            " WHERE "+ KEY_ID+ "='"+posget+"'";

                    db.execSQL(Query_PRICE);
                    db.execSQL(Query_QUANTITY);




                }else{
                    Toast.makeText(context, "Minimum Quantity should be 1", Toast.LENGTH_LONG).show();
                }




            }
        });

        holder.subtract_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int number = Integer.valueOf(holder.required_quantity.getText().toString());


                if(number>=2) {
                     number = number - 1;
                    int final_order_price = unit_price*number;
                    holder.Item_quantity.setText(number + "");
                    holder.Item_price.setText(final_order_price+"");
                    holder.required_quantity.setText(number+"");

                    String Query_PRICE = "UPDATE "+ TABLE+ " SET " + PRICE+ "='"+final_order_price+"'"+
                            " WHERE "+ KEY_ID+ "='"+posget+"'";
                    String Query_QUANTITY = "UPDATE "+ TABLE+ " SET " + QUANTITY+ "='"+number+"'"+
                            " WHERE "+ KEY_ID+ "='"+posget+"'";

                    db.execSQL(Query_PRICE);
                    db.execSQL(Query_QUANTITY);

                }
                else {
                    Toast.makeText(context, "Minimum Quantity should be 1", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return myCartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView Item_ID;
        final TextView Item_name;
        final TextView Item_quantity;
        final TextView Item_price;
        final ImageView image;
        final Button add_item;
        final Button subtract_item;
        final Button remove_item_from_cart;
        final EditText required_quantity;

        public ViewHolder(View itemView) {

            super(itemView);

            this.Item_ID = itemView.findViewById(R.id.cart_item_id);
            this.Item_name = itemView.findViewById(R.id.cart_item_name);
            this.image =itemView.findViewById(R.id.cart_item_image);
            this.Item_quantity =itemView.findViewById(R.id.cart_item_quantity);
            this.Item_price = itemView.findViewById(R.id.cart_item_price);
            this.add_item =itemView.findViewById(R.id.button_add);
            this.subtract_item = itemView.findViewById(R.id.button_minus);
            this.remove_item_from_cart = itemView.findViewById(R.id.button_remove);
            this.required_quantity = itemView.findViewById(R.id.editText_display_qty);


        }
    }
}
