package com.app.uconect.Dataset;

import java.io.Serializable;

/**
 * Created by osigroups on 1/25/2016.
 */
// public int 0-PENDING , 1-CANCEL, 2- SUCCESS
public class HelpData implements Serializable{
    public int supportId = 0;
    public String supportType = "";
    public String supportQuestion = "";
    public String description = "";
    public String supportFor = "";

}
