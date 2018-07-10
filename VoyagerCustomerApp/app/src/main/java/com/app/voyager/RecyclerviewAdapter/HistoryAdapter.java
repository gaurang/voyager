package com.app.voyager.RecyclerviewAdapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.voyager.Dataset.HistoryData;
import com.app.voyager.HistoryDetailsActivity;
import com.app.voyager.ParentActivity;
import com.app.voyager.R;
import com.app.voyager.Util.Util;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.MyInterface;
import com.app.voyager.interfaces.myUrls;

import java.util.ArrayList;


public class HistoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface, MyInterface {
    public ArrayList<HistoryData> mDataset;
    ParentActivity context;

    private int SCROLLLASTPOS = 0;

    public HistoryAdapter(ArrayList<HistoryData> myDataset, ParentActivity gcontext) {
        mDataset = myDataset;
        this.context = gcontext;

    }

    public void add(int position, HistoryData item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.history_containaer, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        final HistoryData name = mDataset.get(position);

        holder.view.setId(position);
        holder.tv_amount.setText(name.finalFare);
        holder.userpic.setImageDrawable(Drawable.createFromPath(name.driverPhoto));
        holder.tv_title.setText(name.createDate);
        holder.tv_source.setText(name.srcPlace);
        holder.car.setText(name.car);
        holder.tv_destination.setText(name.destPlace);
        holder.view.setId(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkConnected(context)) {
                    Intent intent = new Intent(context,  HistoryDetailsActivity.class);
                    intent.putExtra("data", mDataset.get(view.getId()));
                    intent.putExtra("bookingId",name.bookingId);
                    Log.i("Aaaaaaaaaaaa","aaaaaaaaa"+name.bookingId);
                    context.startActivity(intent);
                } else {
                    Util.ShowToast(context, context.getString(R.string.nointernetmessage));
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public void BindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void SetOnclicklistener() {

    }

    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_title, tv_amount,tv_source,tv_destination,car;

        View view;
        ImageView userpic;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            tv_amount = (TextView) itemLayoutView.findViewById(R.id.tv_amount);

            tv_source = (TextView) itemLayoutView.findViewById(R.id.tv_source);
            tv_destination = (TextView) itemLayoutView.findViewById(R.id.tv_destination);
            car = (TextView) itemLayoutView.findViewById(R.id.car);
            userpic = (ImageView) itemLayoutView.findViewById(R.id.userpic);
        }
    }


}