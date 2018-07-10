package com.app.voyager.RecyclerviewAdapter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.AddedPaymentData;
import com.app.voyager.Dataset.EmergencyContactData;
import com.app.voyager.ParentActivity;
import com.app.voyager.R;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddedPaymntmthdAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<AddedPaymentData> mDataset;
    ParentActivity context;

    private int SCROLLLASTPOS = 0;

    public AddedPaymntmthdAdapter(ArrayList<AddedPaymentData> myDataset, ParentActivity gcontext) {
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
                R.layout.addedpaymntmthd_containaer, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        final AddedPaymentData name = mDataset.get(position);

        holder.tv_title.setText(name.gatewayId);
        //  holder.tv_description.setText(name.Description);
        holder.ic_delete.setId(position);
        holder.ic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickposition = view.getId();
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = context.getCommontHeaderParams();
                    jsonObject.put("customerId", Session.getUserID(context));
                    jsonObject.put("paymentId", mDataset.get(clickposition).paymentId);
                    jsonObject_main.put("body", jsonObject);
                    CallDELETEPAYMENT(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    int clickposition = 0;

    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_description;
        View view;
        ImageView ic_delete;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            ic_delete = (ImageView) itemLayoutView.findViewById(R.id.ic_delete);
            tv_description = (TextView) itemLayoutView.findViewById(R.id.tv_description);

        }
    }

    public void CallDELETEPAYMENT(JSONObject params) {
        if (Util.isNetworkConnected(context)) {
            try {
                if (context.progressdialog.isShowing())
                    context.progressdialog.dismiss();
                context.progressdialog.show();
                new CallAPI(DELETEPAYMENT, "DELETEPAYMENT", params, context, GetPlaces_Handler, Request.Method.POST);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            context.progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(context, context.getString(R.string.nointernetmessage));
        }
    }

    ArrayList<EmergencyContactData> DataList = new ArrayList<>();

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {

            context.PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    DataList = new ArrayList<>();
                    context.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(context, "Deleted successfully");
                    mDataset.remove(clickposition);
                    notifyItemRemoved(clickposition);
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    context.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallDELETEPAYMENT(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(context)) {
                        context.CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        context.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(context, context.getString(R.string.nointernetmessage));
                    }
                } else {
                    context.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(context, msg.getData().getString("msg"));
                }
            } else {
                context.progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(context, msg.getData().getString("msg"));

            }
        }
    };

}