package com.osi.uconectdriver.RecyclerviewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.uconectdriver.Dataset.HistoryData;
import com.osi.uconectdriver.HistoryDetailsActivity;
import com.osi.uconectdriver.ParentActivity;
import com.osi.uconectdriver.R;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.interfaces.AsyncInterface;
import com.osi.uconectdriver.interfaces.MyInterface;
import com.osi.uconectdriver.interfaces.myUrls;

import java.util.ArrayList;

public class EarningAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface, MyInterface {
    public ArrayList<HistoryData> mDataset;
    ParentActivity context;

    private int SCROLLLASTPOS = 0;

    public EarningAdapter(ArrayList<HistoryData> myDataset, ParentActivity gcontext) {
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
                R.layout.earning_containaer, null);
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

        holder.tv_title.setText(name.createDate);
        holder.tv_source.setText(name.srcPlace);

        holder.view.setId(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkConnected(context)) {
                    Intent intent = new Intent(context,  HistoryDetailsActivity.class);
                    intent.putExtra("data", mDataset.get(view.getId()));
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

        TextView tv_title, tv_amount,tv_source ;

        View view;



        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            tv_amount = (TextView) itemLayoutView.findViewById(R.id.tv_amount);

            tv_source = (TextView) itemLayoutView.findViewById(R.id.tv_source);

        }
    }


}