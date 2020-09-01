package org.builgforsdg.team127a.easymedic.utilities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.builgforsdg.team127a.easymedic.R;
import org.builgforsdg.team127a.easymedic.entities.list;

import java.util.ArrayList;
import java.util.List;

public class adapter extends ArrayAdapter<list> {

    Activity context;
    List<list> items;
    Integer[] imageId = {
            R.drawable.girl2
    };


    public adapter(Activity mainActivity, ArrayList<list> dataArrayList) {
        super(mainActivity, 0, dataArrayList);

        this.context = mainActivity;
        this.items = dataArrayList;
    }


    private class ViewHolder {

        TextView message, name;
        ImageView image;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        adapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.list_xml, parent, false);

            holder = new adapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);

        } else {
            holder = (adapter.ViewHolder) convertView.getTag();
        }

        list productItems = items.get(position);


        holder.name.setText(productItems.getName());
        holder.message.setText(productItems.getMessage());

        holder.image.setImageResource(productItems.getImageId());

        return convertView;

    }


}