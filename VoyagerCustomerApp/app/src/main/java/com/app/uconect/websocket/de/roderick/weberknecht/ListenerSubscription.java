package com.app.uconect.websocket.de.roderick.weberknecht;

/**
 * Created by osigroups on 3/6/2016.
 */
import java.util.Map;
public interface ListenerSubscription {
    public void onMessage(Map<String, String> headers, String body);
}