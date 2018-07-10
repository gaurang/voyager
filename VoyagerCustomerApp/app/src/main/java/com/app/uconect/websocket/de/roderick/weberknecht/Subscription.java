package com.app.uconect.websocket.de.roderick.weberknecht;

/**
 * Created by osigroups on 3/6/2016.
 */
public class Subscription {

    private String id;

    private String destination;

    private ListenerSubscription callback;

    public Subscription(String destination, ListenerSubscription callback){
        this.destination = destination;
        this.callback = callback;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public ListenerSubscription getCallback() {
        return callback;
    }
}
