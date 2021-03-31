package com.android.shoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shoppinglist.db.Category;
import com.android.shoppinglist.db.Items;

import java.util.List;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.MyViewHolder> {

    private Context context;
    private List<Items> itemsList;
    private HandleItemsClick clickListener;

    public ItemsListAdapter(Context context, HandleItemsClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Items> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsListAdapter.MyViewHolder holder, int position) {
        holder.tvItemName.setText(this.itemsList.get(position).itemName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(itemsList.get(position));
            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editItem(itemsList.get(position));
            }
        });

        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeItem(itemsList.get(position));
            }
        });

        if(this.itemsList.get(position).completed) {
            holder.tvItemName.setPaintFlags(holder.tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvItemName.setPaintFlags(0);
        }
    }

    @Override
    public int getItemCount() {
        if(itemsList == null || itemsList.size() == 0)
            return 0;
        else
            return itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView removeCategory;
        ImageView editCategory;

        public MyViewHolder(View view) {
            super(view);
            tvItemName = view.findViewById(R.id.tvCategoryName);
            removeCategory = view.findViewById(R.id.removeCategory);
            editCategory = view.findViewById(R.id.editCategory);

        }
    }

    public interface  HandleItemsClick {
        void itemClick(Items item);
        void removeItem(Items item);
        void editItem(Items item);
    }
}
