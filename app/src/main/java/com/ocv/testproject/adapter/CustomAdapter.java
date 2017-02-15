package com.ocv.testproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ocv.testproject.ui.Details;
import com.ocv.testproject.model.Ipsum;
import com.ocv.testproject.R;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<Ipsum> list;
    private Ipsum ip;

    public CustomAdapter(Context context, List<Ipsum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ip = list.get(position);
        holder.title.setText(ip.getTitle());
        holder.date.setText(ip.getDateStamp());
        holder.content.setText(Html.fromHtml(ip.getContent()));


        if (list.get(holder.getAdapterPosition()).getImageArray() != null && list.get(holder.getAdapterPosition()).getImageArray().size() != 0) {
            Glide.with(context).load(list.get(holder.getAdapterPosition()).getImageArray().get(0)).into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public TextView content;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.content);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemView) {
                    ip = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, Details.class);
                    intent.putExtra("ipsum", ip);
                    context.startActivity(intent);


                }
            });
        }

    }
}
