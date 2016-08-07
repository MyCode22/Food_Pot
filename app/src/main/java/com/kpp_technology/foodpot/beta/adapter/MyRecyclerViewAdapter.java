package com.kpp_technology.foodpot.beta.adapter;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.fragmentHalamanUtama.ListMenuRestActivity;
import com.kpp_technology.foodpot.beta.itemObject.DataObject;

import java.util.ArrayList;


public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;
    Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView image_mercant;

        public DataObjectHolder(View itemView) {
            super(itemView);

            image_mercant = (ImageView) itemView.findViewById(R.id.image_mercant);


            //  itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }*/
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerViewAdapter(Context applicationContext, ArrayList<DataObject> myDataset) {
        context = applicationContext;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_home_list, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        System.out.println(position + " >>> LOGOOOO " + mDataset.get(position).getLogo());

        Bitmap bmp = BitmapFactory.decodeFile(mDataset.get(position).getLogo());
        holder.image_mercant.setImageBitmap(bmp);
        holder.image_mercant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("MErchant id " + mDataset.get(position).getMerchantId());
                    System.out.println("MErchant Nama " + mDataset.get(position).getMerchantName());

                    Intent pinda = new Intent(context, ListMenuRestActivity.class);
                    pinda.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pinda.putExtra("merchant_id", mDataset.get(position).getMerchantId());
                    context.startActivity(pinda);

                } catch (Exception er) {
                    System.out.println("Error clikcc " + er.getMessage());
                }
            }
        });


    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);

    }

    public void updateItem(DataObject dataObj, int index) {
        mDataset.set(index, dataObj);
        notifyDataSetChanged();

    }

    public void changePosition(int positionAwal, int positionAkhir) {
        notifyItemMoved(positionAwal, positionAkhir);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}