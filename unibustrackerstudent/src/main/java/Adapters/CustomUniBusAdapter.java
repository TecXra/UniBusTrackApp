package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import Model.UBus;

import java.util.ArrayList;


/**
 * Created by Ali ( 03154342359 ) on 7/26/2016.
 */
public class CustomUniBusAdapter extends ArrayAdapter<UBus> {

    private Context context;
   private ArrayList<UBus> Buslist;

    public CustomUniBusAdapter(Context context, int textViewResourceId, ArrayList<UBus> list) {
        super(context, textViewResourceId, list);

        this.context = context;
        this.Buslist = list;

    }

    public int getCount(){
        return Buslist.size();
    }

    public UBus getItem(int position){
        return Buslist.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(Buslist.get(position).getName());
        label.setGravity(Gravity.CENTER);
        label.setTextSize(40);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(30);
        label.setGravity(Gravity.CENTER);
        label.setText(Buslist.get(position).getName());

        return label;
    }


}
