package com.avaj.zhcettpo.adapter;

/**
 * Created by aman_AV on 30-07-2017.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avaj.zhcettpo.CircleTransform;
import com.avaj.zhcettpo.R;
import com.avaj.zhcettpo.model.Model;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by aakra on 7/27/2017.
 */

public class TeamAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Model> models;

    public TeamAdapter(Context context, ArrayList<Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            convertView=View.inflate(context, R.layout.list_members, null);
        }
        ImageView image=(ImageView) convertView.findViewById(R.id.item_image);
        TextView name=(TextView) convertView.findViewById(R.id.item_name);
        TextView desig=(TextView) convertView.findViewById(R.id.item_desig);

        Model model=models.get(position);
        //image.setImageResource(model.getImage()
         //new ImageLoadTask(model.getImage(), image).execute();
        //image.setImageBitmap(getBitmapFromURL(model.getImage()));

        Glide.with(context).load(model.getImage())
                .placeholder(R.drawable.man_team)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

        name.setText(model.getName());
        desig.setText(model.getDesig());

        return convertView;
    }


}