package com.app.voyager.RecyclerviewAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.EmergencyContactData;
import com.app.voyager.ParentActivity;
import com.app.voyager.R;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.dialogs.ProgressDialogView;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;
import com.app.voyager.tabfragments.EmergencyContactFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmergencycontactAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<EmergencyContactData> mDataset;
    Context context;
    EmergencyContactFragment emergencyContactFragment;
    ProgressDialogView progressdialog;
    int clickposition = 0;
    private int SCROLLLASTPOS = 0;

    public EmergencycontactAdapter(ArrayList<EmergencyContactData> myDataset, Context gcontext,EmergencyContactFragment emergencyContactFragment) {
        mDataset = myDataset;
        this.context = gcontext;
        this.emergencyContactFragment=emergencyContactFragment;
    }

    public void add(int position, EmergencyContactData item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(EmergencyContactData item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.addedemergency_containaer, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        final EmergencyContactData name = mDataset.get(position);

        holder.tv_title.setText(name.eContactName);
        holder.tv_mobilenumebr.setText(name.eContactNumber);
        holder.clickview.setTag(holder.id_useridview_switch);
        holder.ic_delete.setId(position);
        holder.clickview.setId(position);
        if (name.trackStatus == 0) holder.id_useridview_switch.setChecked(false);
        else holder.id_useridview_switch.setChecked(true);
        holder.ic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickposition = view.getId();
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = ((ParentActivity)context).getCommontHeaderParams();
                    jsonObject.put("customerId", Session.getUserID(context));
                    jsonObject.put("eContactName", mDataset.get(clickposition).eContactName);
                    jsonObject.put("contactId", mDataset.get(clickposition).contactId);
                    jsonObject_main.put("body", jsonObject);
                    CallGETEEMERGENCY(jsonObject_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.clickview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickposition = view.getId();
                SwitchCompat switchCompat = (SwitchCompat) view.getTag();
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = ((ParentActivity)context).getCommontHeaderParams();
                    jsonObject.put("contactId", mDataset.get(clickposition).contactId);
                    jsonObject.put("eContactNumber", mDataset.get(clickposition).eContactNumber);
                    jsonObject.put("favName", mDataset.get(clickposition).eContactName);
                    if (switchCompat.isChecked())
                        jsonObject.put("trackStatus", 0);
                    else jsonObject.put("trackStatus", 1);
                    jsonObject.put("eContactName", mDataset.get(clickposition).eContactName);

                    jsonObject.put("customerId", Session.getUserID(context));
                    jsonObject_main.put("body", jsonObject);
                    emergencyContactFragment.CallADDEMERGENCYAPI(jsonObject_main);

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


    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_title, tv_mobilenumebr;
        ImageView ic_delete;
        View view, clickview;
        SwitchCompat id_useridview_switch;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            tv_mobilenumebr = (TextView) itemLayoutView.findViewById(R.id.tv_mobilenumebr);
            ic_delete = (ImageView) itemLayoutView.findViewById(R.id.ic_delete);
            id_useridview_switch = (SwitchCompat) itemLayoutView.findViewById(R.id.id_useridview_switch);
            clickview = itemLayoutView.findViewById(R.id.clickview);


        }
    }

    public void CallGETEEMERGENCY(JSONObject params) {
        if (Util.isNetworkConnected(context)) {
            try {
                if(((ParentActivity)context).progressdialog!=null){
                    if (((ParentActivity)context).progressdialog.isShowing())
                        ((ParentActivity)context).progressdialog.dismiss();
                    ((ParentActivity)context).progressdialog.show();
                }else{
                    progressdialog = new ProgressDialogView(context, "Please wait..");
                    progressdialog.show();
                }
                new CallAPI(DELETEEMERGENCYCONTACT, "DELETEEMERGENCYCONTACT", params, context, GetPlaces_Handler, Request.Method.POST);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ((ParentActivity)context).progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(context, context.getString(R.string.nointernetmessage));
        }
    }

    ArrayList<EmergencyContactData> DataList = new ArrayList<>();

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {

//            context.PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    DataList = new ArrayList<>();
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(context, "Contact deleted successfully");
                    mDataset.remove(clickposition);
                    notifyItemRemoved(clickposition);
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity)context).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETEEMERGENCY(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(context)) {
                        ((ParentActivity)context).CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        ((ParentActivity)context).progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(context, context.getString(R.string.nointernetmessage));
                    }
                } else {
                    ((ParentActivity)context).progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(context, msg.getData().getString("msg"));
                }
            } else {
                ((ParentActivity)context).progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(context, msg.getData().getString("msg"));

            }
        }
    };

}