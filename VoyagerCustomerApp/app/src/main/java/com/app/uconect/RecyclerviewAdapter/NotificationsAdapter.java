package com.app.uconect.RecyclerviewAdapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.uconect.Dataset.NotificationData;
import com.app.uconect.NotificationsActivity;
import com.app.uconect.ParentActivity;
import com.app.uconect.R;
import com.app.uconect.Util.Util;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.myUrls;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class NotificationsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements myUrls, AsyncInterface {
    public ArrayList<NotificationData> mDataset;
    ParentActivity context;
    NotificationsActivity noti1;

    private int SCROLLLASTPOS = 0;
    private Bitmap b;
    private Bitmap newbitmap;

    public NotificationsAdapter(ArrayList<NotificationData> myDataset, ParentActivity gcontext) {
        mDataset = myDataset;
        this.context = gcontext;

    }

    public void add(int position, NotificationData item) {
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
                R.layout.notification_containaer, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        final NotificationData name = mDataset.get(position);
        holder.view.setId(position);
        holder.webView.loadUrl("http://facebook.com");


    holder.webView.setWebViewClient(new WebViewClient() {
        public void onPageFinished (WebView view, String url){
            Picture picture = view.capturePicture();
            DisplayMetrics metrics = new DisplayMetrics();

            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            b = Bitmap.createBitmap(height / 2, width, Bitmap.Config.ARGB_8888);
            Log.i("aaaaaaaaaa", "aaaaaaaaaaa" + height / 2 + "" + width);
                Canvas c = new Canvas(b);
                picture.draw(c);
                FileOutputStream fos = null;
                try {

                    fos = new FileOutputStream("mnt/sdcard/yahoo.jpg");

                    if (fos != null) {
                        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        fos.close();
                    }
                } catch (Exception e) {

                }
                holder.notification.setImageBitmap(b);
                Log.i("aaaaaaaaaa", "aaaaaaaaaaa" + b + " " + c);

            }
        });

        holder.view.setId(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkConnected(context)) {
                    noti1.setNotifications(name.Textmessage);
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


    public class ViewHolder extends
            RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_description;
        View view;
        ImageView notification,cancel;
        WebView webView;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            notification = (ImageView) itemLayoutView.findViewById(R.id.image);
            webView = (WebView) itemLayoutView.findViewById(R.id.webview);

        }
    }


}