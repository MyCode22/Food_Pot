package com.kpp_technology.foodpot.beta.adapter;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectInProgress;

import java.util.ArrayList;


public class MyRecyclerViewAdapterInProgress extends RecyclerView
        .Adapter<MyRecyclerViewAdapterInProgress
        .DataObjectHolder> {
    private ArrayList<DataObjectInProgress> mDataset;
    private static MyClickListener myClickListener;
    Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView title1, title2, statusPesanan;
        Button buttonDetail, buttonDriverLocation, buttonAddReview;
        CardView card_view_inProgres;

        public DataObjectHolder(View itemView) {
            super(itemView);

            title1 = (TextView) itemView.findViewById(R.id.title1);
            title2 = (TextView) itemView.findViewById(R.id.title2);
            statusPesanan = (TextView) itemView.findViewById(R.id.statusPesanan);
            buttonDetail = (Button) itemView.findViewById(R.id.buttonDetail);
            buttonDriverLocation = (Button) itemView.findViewById(R.id.buttonDriverLocation);
            buttonAddReview = (Button) itemView.findViewById(R.id.buttonAddReview);
            card_view_inProgres = (CardView) itemView.findViewById(R.id.card_view_inProgres);


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

    public MyRecyclerViewAdapterInProgress(Context applicationContext, ArrayList<DataObjectInProgress> myDataset) {
        context = applicationContext;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_in_progress_list, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        try {
            holder.title1.setText(mDataset.get(position).getTitle());
            holder.title2.setText(mDataset.get(position).getTitle2());
            holder.statusPesanan.setText(mDataset.get(position).getStatus());

            holder.buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.buttonAddReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.buttonDriverLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        } catch (Exception er) {

        }


    }

    public void addItem(DataObjectInProgress dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);

    }

    public void updateItem(DataObjectInProgress dataObj, int index) {
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