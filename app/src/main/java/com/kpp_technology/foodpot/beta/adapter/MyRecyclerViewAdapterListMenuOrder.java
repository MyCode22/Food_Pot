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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.fragmentHalamanUtama.MenuOrderActivity;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectListMenuOrder;

import java.util.ArrayList;


public class MyRecyclerViewAdapterListMenuOrder extends RecyclerView
        .Adapter<MyRecyclerViewAdapterListMenuOrder
        .DataObjectHolder> {
    private ArrayList<DataObjectListMenuOrder> mDataset;
    private static MyClickListener myClickListener;
    Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView image_write_order;
        LinearLayout remove, add;
        TextView nameItem, priceItem, jumlahItem;

        public DataObjectHolder(View itemView) {
            super(itemView);

            image_write_order = (ImageView) itemView.findViewById(R.id.image_write_order);
            remove = (LinearLayout) itemView.findViewById(R.id.remove);
            add = (LinearLayout) itemView.findViewById(R.id.add);
            nameItem = (TextView) itemView.findViewById(R.id.nameItem);
            priceItem = (TextView) itemView.findViewById(R.id.priceItem);
            jumlahItem = (TextView) itemView.findViewById(R.id.jumlahItem);


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

    public MyRecyclerViewAdapterListMenuOrder(Context applicationContext, ArrayList<DataObjectListMenuOrder> myDataset) {
        context = applicationContext;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_menu_order, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        System.out.println(position + " >>>>>>>>>> " + mDataset.get(position).getCategory());
        holder.nameItem.setText(mDataset.get(position).getItemName());
        holder.priceItem.setText(mDataset.get(position).getCurencyRetail());


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("KLIKKKKKKKK");
                    int be = Integer.valueOf(holder.jumlahItem.getText().toString());

                    int tam = be + 1;

                    // MenuOrderActivity.jumlahSemuanya = MenuOrderActivity.jumlahSemuanya + tam;
                    holder.jumlahItem.setText(String.valueOf(tam));
                    String gam = mDataset.get(position).getCurencyRetail().replace("Rp", "").replace(".", "").trim();
                    System.out.println("JADI " + gam);

                    int har = Integer.valueOf(gam);

                    int weight = Integer.valueOf(mDataset.get(position).getWeight());
                    MenuOrderActivity.updateBawah(1, har, weight);

                    // MenuOrderActivity.jumlahItem.add(position,);

                    System.out.println(position + " addd " + tam);
                    System.out.println(position + " getItem ID " + mDataset.get(position).getItemId());


                    if (MenuOrderActivity.nameItem.contains(mDataset.get(position).getItemName())) {
                        MenuOrderActivity.jumlahItem.set(position, tam);

                    } else {

                        MenuOrderActivity.nameItem.add(position, mDataset.get(position).getItemName());
                        MenuOrderActivity.hargaItem.add(position, mDataset.get(position).getCurencyRetail());
                        MenuOrderActivity.idItem.add(position, mDataset.get(position).getItemId());
                        MenuOrderActivity.jumlahItem.add(position, tam);

                    }


                } catch (Exception er) {
                    System.out.println("Errorrr " + er.getMessage());
                }


            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.jumlahItem.getText().equals("1")) {

                    System.out.println(position + "  REMOVEEE " + mDataset.get(position).getItemName());
                    MenuOrderActivity.nameItem.remove(mDataset.get(position).getItemName());
                    MenuOrderActivity.hargaItem.remove(position);
                    MenuOrderActivity.idItem.remove(position);
                    MenuOrderActivity.jumlahItem.remove(position);


                    int be = Integer.valueOf(holder.jumlahItem.getText().toString());
                    int tam = be - 1;
                    holder.jumlahItem.setText(String.valueOf(tam));
                    int har = Integer.valueOf(mDataset.get(position).getCurencyRetail().replace("Rp", "").replace(".", "").trim());

                    int weight = Integer.valueOf(mDataset.get(position).getWeight());
                    MenuOrderActivity.updateBawahRemove(1, har, weight);


                } else {
                    int be = Integer.valueOf(holder.jumlahItem.getText().toString());
                    int tam = be - 1;
                    holder.jumlahItem.setText(String.valueOf(tam));
                    int har = Integer.valueOf(mDataset.get(position).getCurencyRetail().replace("Rp", "").replace(".", "").trim());

                    int weight = Integer.valueOf(mDataset.get(position).getWeight());

                    MenuOrderActivity.updateBawahRemove(1, har, weight);

                    MenuOrderActivity.jumlahItem.set(position, tam);
                }


            }
        });


    }

    public void addItem(DataObjectListMenuOrder dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);

    }

    public void updateItem(DataObjectListMenuOrder dataObj, int index) {
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