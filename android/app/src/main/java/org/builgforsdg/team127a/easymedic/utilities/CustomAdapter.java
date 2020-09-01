package org.builgforsdg.team127a.easymedic.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import org.builgforsdg.team127a.easymedic.R;
import org.builgforsdg.team127a.easymedic.activities.OutputActivity;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> category;
    ArrayList<String> body;
    ArrayList<String> shortenedBody;
    ArrayList<String> fileName;
    ArrayList<String> extention;
    ArrayList<String> path;
    private static final String TAG = "CustomAdapter";


    Context context;

    public CustomAdapter(Context context, ArrayList<String> category,
                         ArrayList<String> shortenedBody,
                         ArrayList<String> body, ArrayList<String> fileName,
                         ArrayList<String> extention, ArrayList<String> path ) {
        this.context = context;
        this.category = category;
        this.body = body;
        this.fileName = fileName;
        this.shortenedBody = shortenedBody;
        this.extention = extention;
        this.path = path;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_xml, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.tvCategory.setText(category.get(position));
        holder.tvBody.setText(shortenedBody.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person tvCategory on item click
                Bundle bundle = new Bundle();


                Log.d(TAG, "longCategory.get(position) IS "+category.get(position));
                Log.d(TAG, "Body.get(position) IS "+body.get(position));
                Log.d(TAG, "extention.get(position) IS "+extention.get(position));
                Log.d(TAG, "Filename.get(position) IS "+fileName.get(position));
                Log.d(TAG, "path.get(position) IS "+path.get(position));

                Intent intent = new Intent(context.getApplicationContext(), OutputActivity.class);
                intent.putExtra("CATEGORY", category.get(position));
                intent.putExtra("BODY", body.get(position));
                intent.putExtra("EXT", extention.get(position));
                intent.putExtra("NAME", fileName.get(position));
                intent.putExtra("PATH", path.get(position));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return category.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvBody;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            tvCategory = itemView.findViewById(R.id.name);
            tvBody = itemView.findViewById(R.id.message);

        }
    }
}