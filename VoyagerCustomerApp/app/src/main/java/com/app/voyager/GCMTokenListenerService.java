package com.app.voyager;

/**
 * Created by Brij on 12-06-2016.
 */
import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class GCMTokenListenerService extends InstanceIDListenerService {

    //If the token is changed registering the device again
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GcmIntentService.class);
        startService(intent);
    }
}
