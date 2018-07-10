package com.app.voyager.Dataset;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by osigroups on 1/11/2016.
 */
public class CarLocationsData {
    public int driverId = 0;

    public String CountryName = "";
    public double latitude = 0;
    public double longitude = 0;
    public double old_latitude = 0;
    public double old_longitude = 0;
    public int Type = 0;
    public int distance = 0;
    public Marker marker;
    public  boolean isnew = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarLocationsData that = (CarLocationsData) o;

        return driverId == that.driverId;

    }

    @Override
    public int hashCode() {
        return driverId;
    }
}
