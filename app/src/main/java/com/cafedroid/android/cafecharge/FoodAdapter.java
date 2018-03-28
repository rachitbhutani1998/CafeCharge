package com.cafedroid.android.cafecharge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rachit on 09/03/18.
 */

public class FoodAdapter extends ArrayAdapter<FoodItem> {
    public FoodAdapter(@NonNull Context context, int resource, @NonNull List<FoodItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView=convertView;
        if (itemView==null){
            itemView= LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }
        TextView nameView=itemView.findViewById(R.id.name_text);
        TextView priceView=itemView.findViewById(R.id.price_text);

        FoodItem currentItem=getItem(position);
        if (currentItem!=null){
            nameView.setText(currentItem.getName());
            priceView.setText("\u20B9"+String.valueOf(currentItem.getPrice()));
        }
        return itemView;
    }
}
