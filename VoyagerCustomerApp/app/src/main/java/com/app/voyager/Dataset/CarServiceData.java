package com.app.voyager.Dataset;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by osigroups on 3/12/2016.
 */
public class CarServiceData implements Serializable {
     public String attrName, attrValue1;
    public ArrayList<CarSubServiceData> carSubServiceDatas=new ArrayList<>();
}
