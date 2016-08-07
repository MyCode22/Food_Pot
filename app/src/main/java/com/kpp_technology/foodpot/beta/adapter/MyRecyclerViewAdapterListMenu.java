package com.kpp_technology.foodpot.beta.adapter;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectListMenu;

import java.util.ArrayList;


public class MyRecyclerViewAdapterListMenu extends RecyclerView
        .Adapter<MyRecyclerViewAdapterListMenu
        .DataObjectHolder> {
    private ArrayList<DataObjectListMenu> mDataset;
    private static MyClickListener myClickListener;
    Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView image_next;
        TextView nameCate;

        public DataObjectHolder(View itemView) {
            super(itemView);

            image_next = (ImageView) itemView.findViewById(R.id.image_next);
            nameCate = (TextView) itemView.findViewById(R.id.nameCate);


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

    public MyRecyclerViewAdapterListMenu(Context applicationContext, ArrayList<DataObjectListMenu> myDataset) {
        context = applicationContext;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_menu, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        System.out.println(position+ " >>>>>>>>>> "+mDataset.get(position).getCategoryName());

        holder.nameCate.setText(mDataset.get(position).getCategoryName());
        holder.nameCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void addItem(DataObjectListMenu dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);

    }

    public void updateItem(DataObjectListMenu dataObj, int index) {
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