package com.app.voyager.RecyclerviewAdapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.Dataset.FriendDetails;
import com.app.voyager.ParentActivity;
import com.app.voyager.R;
import com.app.voyager.Util.Util;
import com.app.voyager.interfaces.AsyncInterface;
import com.app.voyager.interfaces.myUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AddFriendAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<FriendDetails> mDataset;
    ParentActivity context;
    Handler GetSendFriendRequest_Handler = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println(msg.getData().toString());

            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {


                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData()
                                .getString("mExtraParam"));
                        int index = jsonObject.getInt("index");


                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    context.ParseSessionDetails(msg.getData().getString("responce"));

                    try {
                        CallAddFriends(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(context)) {
                        context.CallSessionID(GetSendFriendRequest_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(context, msg.getData()
                                .getString("msg"));
                    }
                } else {

                    Util.ShowToast(context, msg.getData()
                            .getString("msg"));
                }
            } else {

                Util.ShowToast(context, msg.getData()
                                .getString("msg")
                );
            }
        }
    };
    private int SCROLLLASTPOS = 0;

    public AddFriendAdapter(ArrayList<FriendDetails> myDataset, ParentActivity gcontext) {
        mDataset = myDataset;
        this.context = gcontext;

    }

    public void add(int position, FriendDetails item) {
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
                R.layout.addemgergency_containaer, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        final FriendDetails name = mDataset.get(position);
        holder.tv_username.setId(position);
        holder.tv_username.setText(name.first_name + " " + name.last_name);
        holder.tv_mobile.setText(name.mobilenumber);
        if (name.imageuri != null) {
            System.out.println(Uri.parse(name.imageuri));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(name.imageuri));
                holder.userpic.setImageBitmap(bitmap);
                System.out.println(bitmap);
            } catch (FileNotFoundException e) {
                holder.userpic.setImageResource(R.drawable.defalt_user_pic);
                e.printStackTrace();
            } catch (IOException e) {
                holder.userpic.setImageResource(R.drawable.defalt_user_pic);
                e.printStackTrace();
            }
        }else{
            holder.userpic.setImageResource(R.drawable.defalt_user_pic);
        }


//        holder.iv_addfriend.setId(position);
//        holder.iv_addfriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    JSONObject params = new JSONObject();
//                    params.put("user_id", Session.getUserID(context) + "");
//                    params.put("friend_id", mDataset.get(v.getId()).user_id + "");
//                    params.put("index", v.getId() + "");
//                    if (mDataset.get(v.getId()).is_friend == 0) {
//                        params.put("urls", "");
//                    } else {
//                        params.put("urls", "");
//                    }
//                    CallAddFriends(params);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void CallAddFriends(JSONObject params) {
        if (Util.isNetworkConnected(context)) {
            try {

                new CallAPI(params.getString("urls"), "FriendRequest", params, context, GetSendFriendRequest_Handler);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            Util.ShowToast(context, context.getString(R.string.nointernetmessage));
        }
    }

    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_username;
        TextView tv_mobile;
        View view;
        ImageView userpic;
        CheckBox iv_addfriend;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            tv_username = (TextView) itemLayoutView.findViewById(R.id.tv_username);
            tv_mobile = (TextView) itemLayoutView.findViewById(R.id.tv_mobile);
            iv_addfriend = (CheckBox) itemLayoutView.findViewById(R.id.iv_addfriend);
            userpic = (ImageView) itemLayoutView.findViewById(R.id.userpic);
        }
    }


}