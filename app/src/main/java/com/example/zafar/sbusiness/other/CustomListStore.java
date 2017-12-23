package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zafar.sbusiness.R;

public class CustomListStore extends ArrayAdapter<String> {

    private final Activity context;

    private final String[] title;
    private final Integer[] images;
    public CustomListStore(Activity context,
                              String[] titles, Integer[] images ) {
        super(context, R.layout.list_single_store, titles);
        this.context = context;
        this.title = titles;
        this.images = images;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_store, null, true);

        TextView titles = (TextView) rowView.findViewById(R.id.titles_at_store);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icons_at_store);

        titles.setText(title[position]);
        imageView.setImageResource(images[position]);

        return rowView;
    }
}