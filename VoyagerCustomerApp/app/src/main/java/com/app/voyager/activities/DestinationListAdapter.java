package com.app.voyager.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.voyager.R;
import com.app.voyager.Util.PreferenceHelper;

import java.util.List;

/**
 * Created by atul on 22/4/16.
 */
public class DestinationListAdapter extends ArrayAdapter {

//    ArrayList<Datum> notificationList;
    Context context;
    int quantity;
    PreferenceHelper preferenceHelper;
    public DestinationListAdapter(Context context, int resource,
                               List<String> objects) {
        super(context, resource, objects);
        this.context=context;

        preferenceHelper=new PreferenceHelper(context);

        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder=null;
        if (convertView == null) {
            // This a new view we inflate the new layout
            viewHolder=new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.destination_row_item,
                    parent, false);
            viewHolder.txtName=(TextView)convertView.findViewById(R.id.txtName);
            viewHolder.txtAddress=(TextView)convertView.findViewById(R.id.txtAddress);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtName,txtAddress;
    }


}

