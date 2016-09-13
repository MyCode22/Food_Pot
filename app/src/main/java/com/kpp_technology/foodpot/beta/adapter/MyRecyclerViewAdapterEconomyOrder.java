package com.kpp_technology.foodpot.beta.adapter;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.fragmentHalamanUtama.EconomyOrder;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectEconomyOrder;

import java.util.ArrayList;


public class MyRecyclerViewAdapterEconomyOrder extends RecyclerView
        .Adapter<MyRecyclerViewAdapterEconomyOrder
        .DataObjectHolder> {
    private ArrayList<DataObjectEconomyOrder> mDataset;
    private static MyClickListener myClickListener;
    Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView imageDriver;
        TextView nameDriver, regionDriver;
        RadioGroup myRadioGroup;
        RadioButton standBy, pickup;
        LinearLayout layoutUtama, layoutAbu2;

        public DataObjectHolder(View itemView) {
            super(itemView);

            imageDriver = (ImageView) itemView.findViewById(R.id.imageDriver);
            nameDriver = (TextView) itemView.findViewById(R.id.nameDriver);
            regionDriver = (TextView) itemView.findViewById(R.id.regionDriver);
            myRadioGroup = (RadioGroup) itemView.findViewById(R.id.myRadioGroup);
            standBy = (RadioButton) itemView.findViewById(R.id.standBy);
            pickup = (RadioButton) itemView.findViewById(R.id.pickup);
            layoutUtama = (LinearLayout) itemView.findViewById(R.id.layoutUtama);
            layoutAbu2 = (LinearLayout) itemView.findViewById(R.id.layoutAbu2);


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

    public MyRecyclerViewAdapterEconomyOrder(Context applicationContext, ArrayList<DataObjectEconomyOrder> myDataset) {
        context = applicationContext;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_economy_driver, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.nameDriver.setText(mDataset.get(position).getName_driver());
        holder.regionDriver.setText(mDataset.get(position).getRegio_driver());

        if (mDataset.get(position).getPilih().equals("orange")) {
            holder.layoutUtama.setBackgroundResource(R.color.orangeTrans);
        } else {
            holder.layoutUtama.setBackgroundColor(Color.TRANSPARENT);
        }


        if (mDataset.get(position).getPickupstandby().equals("standby")) {
            holder.layoutAbu2.setVisibility(View.GONE);
            holder.standBy.setChecked(true);


            holder.layoutUtama.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("POSITION " + position);

                    EconomyOrder.changeBagroun(position, context);

                }
            });


        } else {
            holder.layoutAbu2.setBackgroundResource(R.color.black_overlay);
            holder.pickup.setChecked(true);
        }
        holder.myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.pickup) {

                } else if (i == R.id.standBy) {

                }
            }
        });

    }

    public void addItem(DataObjectEconomyOrder dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);

    }

    public void updateItem(int index) {
        DataObjectEconomyOrder objectEconomyOrder = new DataObjectEconomyOrder(mDataset.get(index).getId_driver(), mDataset.get(index).getName_driver(), mDataset.get(index).getRegio_driver(), mDataset.get(index).getPickupstandby(), mDataset.get(index).getActive_hour(), "orange");

        mDataset.set(index, objectEconomyOrder);
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