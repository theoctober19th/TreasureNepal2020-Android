package com.dracarys.treasurenepal2020.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dracarys.treasurenepal2020.R;
import com.dracarys.treasurenepal2020.entities.LeaderBoard;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by praka on 12/24/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.CustomViewHolder> {

    private List<LeaderBoard> dataList;
    private Context context;

    public LeaderboardAdapter(Context context, List<LeaderBoard> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtName;
        TextView txtPoints;
        private CircleImageView imgView;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtName = mView.findViewById(R.id.leader_name);
            txtPoints = mView.findViewById(R.id.leader_score);
            imgView = mView.findViewById(R.id.leader_image);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_treasure, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.txtName.setText("Bikalpa DHakal"/* TODO add Name Here */);
        holder.txtPoints.setText("200"/* TODO add Name Here */);

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("" /*TODO add image here */)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}