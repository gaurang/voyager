package com.app.voyager.Util;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by atul on 14/4/16.
 */
public class SettingsTabContent implements TabHost.TabContentFactory {
    private Context mContext;

    public SettingsTabContent(Context context) {
        mContext = context;
    }

    @Override
    public View createTabContent(String tag) {
        View v = new View(mContext);
        return v;
    }
}