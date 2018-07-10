package com.osi.voyagerdriver.RecyclerviewAdapter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.osi.voyagerdriver.AsyncTask.CallAPI;
import com.osi.voyagerdriver.Dataset.EmergencyContactData;
import com.osi.voyagerdriver.ProfileActivity;
import com.osi.voyagerdriver.R;
import com.osi.voyagerdriver.Util.Session;
import com.osi.voyagerdriver.Util.Util;
import com.osi.voyagerdriver.interfaces.AsyncInterface;
import com.osi.voyagerdriver.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmergencycontactAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<EmergencyContactData> mDataset;
    ProfileActivity context;
    //private static final String DELETEEMERGENCYCONTACT1= "http://192.168.1.106:8080/uc/api/cse/delDEContact";
    private LinearLayout messagelayout;
    int clickposition = 0;
    private int SCROLLLASTPOS = 0;

    public EmergencycontactAdapter(ArrayList<EmergencyContactData> myDataset, ProfileActivity gcontext) {
        mDataset = myDataset;
        this.context = gcontext;

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
                    jsonObject_main = context.getCommontHeaderParams();
                    jsonObject.put("driverId", Session.getUserID(context));
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
                    jsonObject_main = context.getCommontHeaderParams();
                    jsonObject.put("contactId", mDataset.get(clickposition).contactId);
                    jsonObject.put("eContactNumber", mDataset.get(clickposition).eContactNumber);
                    jsonObject.put("favName", mDataset.get(clickposition).eContactName);
                    if (switchCompat.isChecked())
                        jsonObject.put("trackStatus", 0);
                    else jsonObject.put("trackStatus", 1);
                    jsonObject.put("eContactName", mDataset.get(clickposition).eContactName);

                    jsonObject.put("driverId", Session.getUserID(context));
                    jsonObject_main.put("body", jsonObject);
                    context.CallADDEMERGENCYAPI(jsonObject_main);

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
                new CallAPI(DELETEEMERGENCYCONTACT, "DELETEEMERGENCYCONTACT", params, context, GetPlaces_Handler, Request.Method.POST);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
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
                    Util.ShowToast(context, "Contact deleted successfully");
                    mDataset.remove(clickposition);
                    notifyItemRemoved(clickposition);
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    context.ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETEEMERGENCY(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(context)) {
                        context.CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(context, context.getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(context, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(context, msg.getData().getString("msg"));

            }
        }
    };

}