package com.osi.uconectdriver.RecyclerviewAdapter;

/**
 * Created by Brij on 28-03-2016.
 */
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osi.uconectdriver.Dataset.ChangeVehicleData;
import com.osi.uconectdriver.LandingPageActivity;
import com.osi.uconectdriver.ParentActivity;
import com.osi.uconectdriver.R;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.interfaces.AsyncInterface;
import com.osi.uconectdriver.interfaces.myUrls;

import java.util.ArrayList;

public class ChangeVehicleAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<ChangeVehicleData> mDataset;
    ParentActivity context;

    private int SCROLLLASTPOS = 0;

    public ChangeVehicleAdapter(ArrayList<ChangeVehicleData> myDataset, ParentActivity gcontext) {
        this.mDataset = myDataset;
        this.context = gcontext;

    }

    public void add(int position, ChangeVehicleData item) {
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
                R.layout.change_container, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        final ChangeVehicleData name = mDataset.get(position);
        holder.tv_title.setText(name.model);
        //   holder.tv_description.setText(name.Description);
        holder.view.setId(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LandingPageActivity.class);
                intent.putExtra("key","0");
                Session.setDefaultVehicleId(context,name.id);
                Session.setDefaultVehicle(context,name.model);
                intent.putExtra("data", mDataset.get(view.getId()));
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_title;

        View view;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);


        }
    }


}