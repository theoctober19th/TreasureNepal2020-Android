package com.dracarys.treasurenepal2020.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dracarys.treasurenepal2020.R;
import com.dracarys.treasurenepal2020.entities.Treasure;
import com.google.android.gms.maps.model.Circle;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by praka on 12/24/2017.
 */

public class TreasureAdapter extends RecyclerView.Adapter<TreasureAdapter.CustomViewHolder> {

    private List<Treasure> dataList;
    private Context context;

    public TreasureAdapter(Context context,List<Treasure> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle;
        TextView txtAddress;
        private CircleImageView imgView;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.search_title);
            txtAddress = mView.findViewById(R.id.search_address);
            imgView = mView.findViewById(R.id.circularImageView);
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
        holder.txtTitle.setText(dataList.get(position).getName());
        holder.txtAddress.setText(dataList.get(position).getAddress());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getImage())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}