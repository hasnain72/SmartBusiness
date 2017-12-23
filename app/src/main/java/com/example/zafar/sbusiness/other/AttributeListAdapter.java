package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.activity.AddProduct;
import com.example.zafar.sbusiness.activity.AddValue;
import com.example.zafar.sbusiness.activity.AddVariants;
import com.example.zafar.sbusiness.Database.DatabaseAttributes;
import com.example.zafar.sbusiness.Models.ProductAttributes;

import java.util.ArrayList;
import java.util.List;

public class AttributeListAdapter extends ArrayAdapter {
    Activity activity;
    private Context mContext;
    int images[];
    ArrayList<Integer> id = new ArrayList<>();
    List<String> options = new ArrayList<>();
    List<String> values = new ArrayList<>();
    LayoutInflater layoutInflator;

    public AttributeListAdapter(Activity activity, int[] images, ArrayList<Integer> id, ArrayList<String> options, ArrayList<String> values) {
        super(activity, R.layout.attributelist , id);
        this.activity = activity;
        this.images = images;
        this.id = id;
        this.options = options;
        this.values = values;
        layoutInflator = activity.getLayoutInflater();
    }
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = layoutInflator.inflate(R.layout.attributelist, parent, false);
        }
        TextView txt=(TextView)v.findViewById(R.id.attributeOption);
        TextView txt2 = (TextView) v.findViewById(R.id.attributeValue);
        ImageButton im=(ImageButton) v.findViewById(R.id.optionsButton);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] choices = {
                        "Update Value","Rename Option", "Remove"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        mContext = parent.getContext();
                        if(item == 0) {
                            Intent intent = new Intent(mContext , AddValue.class);
                            intent.putExtra("id",id.get(position));
                            intent.putExtra("option",options.get(position));
                            intent.putExtra("value",values.get(position));
                            mContext.startActivity(intent);
                        }
                        else if(item == 1) {
                            Intent intent = new Intent(mContext,AddVariants.class);
                            intent.putExtra("id",id.get(position));
                            intent.putExtra("option",options.get(position));
                            Log.d("YOOOO",String.valueOf(id.get(position)));
                            mContext.startActivity(intent);
                        }
                        else if(item == 2) {
                            new DatabaseAttributes(mContext).removeFromPA(new ProductAttributes(
                                    id.get(position)
                            ));
                            Log.d("YOOODLT",String.valueOf(id.get(position)));
                            Toast.makeText(mContext,"Successfully Deleted! "+String.valueOf(id.get(position)),Toast.LENGTH_SHORT).show();
                            if(mContext instanceof AddProduct) {
                                clear();
                                ((AddProduct) mContext).updateAttributeList();
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        txt.setText(options.get(position));
        txt2.setText(values.get(position));
        im.setImageResource(images[0]);
        return v;
    }
}