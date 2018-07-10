package com.app.voyager.RecyclerviewAdapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.voyager.Dataset.AddedPaymentData;
import com.app.voyager.ParentActivity;
import com.app.voyager.R;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;

import java.util.ArrayList;

public class SelectPaymntAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<AddedPaymentData> mDataset;
    ParentActivity context;

    private int SCROLLLASTPOS = 0;

    public SelectPaymntAdapter(ArrayList<AddedPaymentData> myDataset, ParentActivity gcontext) {
        mDataset = myDataset;
        this.context = gcontext;

    }

    public void add(int position, AddedPaymentData item) {
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
                R.layout.selectpaymnt_containaer, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        final AddedPaymentData name = mDataset.get(position);

        holder.tv_title.setText(name.gatewayId);
        if (name.defalt_payment == 1) {
            holder.select_rdbtn.setChecked(true);
        } else {
            holder.select_rdbtn.setChecked(false);
        }
        holder.select_rdbtn.setId(position);
        holder.select_rdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMessage = new Intent();
                intentMessage.putExtra("mDataset", mDataset.get(view.getId()));
                Log.i("------------------", "==================",mDataset.get(view.getId()));
                context.setResult(2, intentMessage);
                context.finish();

            }
        });
        //  holder.tv_description.setText(name.Description);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_description;
        View view;
        RadioButton select_rdbtn;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            select_rdbtn = (RadioButton) itemLayoutView.findViewById(R.id.select_rdbtn);
            tv_description = (TextView) itemLayoutView.findViewById(R.id.tv_description);

        }
    }


}