package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafar.sbusiness.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterProductAttributes extends ArrayAdapter {
    Activity activity;
    int images[];
    List<String> media = new ArrayList<>();
    LayoutInflater layoutInflator;
    public CustomAdapterProductAttributes(Activity activity, int images[], List<String> media) {
        super(activity, R.layout.list_delete_added_sql, media);
        this.activity = activity;
        this.images = images;
        this.media = media;
        layoutInflator = activity.getLayoutInflater();
    }
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = layoutInflator.inflate(R.layout.list_delete_added_sql, parent, false);
        }
        TextView txt=(TextView)v.findViewById(R.id.attributeOption);
        ImageButton im=(ImageButton) v.findViewById(R.id.deleteButton);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(parent.getContext(),"Removed  : "+media.get(position),Toast.LENGTH_SHORT).show();
                media.remove(position);
                notifyDataSetChanged();
            }
        });
        txt.setText(media.get(position));
        im.setImageResource(images[0]);
        return v;
    }
}