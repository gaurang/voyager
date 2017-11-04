package com.cd.voyager.common.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.cd.voyager.entities.DrLocation;
import com.cd.voyager.entities.GoogleDistance;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

public class GoogleServicesWrapper {
	
	
	public static  DistanceMatrix getDistanceMatrixFromGoogle(GeoApiContext context, List<String> origins, List<String> destinations){
		DistanceMatrix matrix = null;
		try {
		    matrix = DistanceMatrixApi.newRequest(context)
		            .origins((String[])origins.toArray())
		            .destinations((String[])destinations.toArray())
		            .mode(TravelMode.DRIVING)
		            .language("en-US")
		            .avoid(RouteRestriction.TOLLS)
		            .units(Unit.METRIC)
		            .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
		            .await();

		    //matrix.rows[0].elements[0].distance.humanReadable.endsWith("mi");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matrix;
	}
	
	public static  List<GoogleDistance> getDistanceMatrixFromGoogle(GeoApiContext context, LatLng[] origins, LatLng[] destinations){
		DistanceMatrix matrix = null;
		List<GoogleDistance>  gdList =  new ArrayList<GoogleDistance>();
		try {
		    matrix = DistanceMatrixApi.newRequest(context)
		            .origins(origins)
		            .destinations(destinations)
		            .mode(TravelMode.DRIVING)
		            .language("en-US")
		            .avoid(RouteRestriction.TOLLS)
		            .units(Unit.METRIC)
		            .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
		            .await();

		    //matrix.rows[0].elements[0].distance.humanReadable.endsWith("mi");
		for (int i = 0; i < matrix.rows[0].elements.length; i++) {
			
			if(matrix.rows[0].elements != null){
				String[] distArr = matrix.rows[0].elements[i].distance.toString().split(" ");
				String[] durArr = matrix.rows[0].elements[i].duration.toString().split(" ");
				
				GoogleDistance gd = new GoogleDistance();
				gd.setDistance(Double.valueOf(distArr[0]));
				gd.setDistanceUnits(distArr[1]);
				gd.setDuration(Double.valueOf(durArr[0]));
				gd.setDurationUnits(durArr[1]);
				gdList.add(gd);
			}
		} 
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gdList;
	}
	public static GoogleDistance  getDistanceFromGoogle(GeoApiContext context, LatLng origin, LatLng destination){
		DistanceMatrix matrix = null;
		List<GoogleDistance>  gdList =  new ArrayList<GoogleDistance>();
		GoogleDistance gd = null;
		try {
		    matrix = DistanceMatrixApi.newRequest(context)
		            .origins(origin)
		            .destinations(destination)
		            .mode(TravelMode.DRIVING)
		            .language("en-US")
		            .avoid(RouteRestriction.TOLLS)
		            .units(Unit.METRIC)
		            .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
		            .await();

		    //matrix.rows[0].elements[0].distance.humanReadable.endsWith("mi");
		    
		    Double minDist = 0d, minDur = 0d;
		for (int i = 0; i < matrix.rows[0].elements.length; i++) {
			
			
			if(matrix.rows[0].elements != null){
				String[] distArr = matrix.rows[0].elements[i].distance.toString().split(" ");
				String[] durArr = matrix.rows[0].elements[i].duration.toString().split(" ");
//				/String[] durArr = matrix.rows[0].elements[i].  .toString().split(" ");
				
				gd = new GoogleDistance();
				gd.setDistance(Double.valueOf(distArr[0]));
				gd.setDistanceUnits(distArr[1]);
				gd.setDuration(Double.valueOf(durArr[0]));
				gd.setDurationUnits(durArr[1]);		
			}
		} 
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gd;
	}
	
	public static  DrLocation getClosestDistanceMatrixFromGoogle(GeoApiContext context, LatLng[] origins, LatLng[] destinations, List<DrLocation> mapIds){
		DistanceMatrix matrix = null;
		List<GoogleDistance>  gdList =  new ArrayList<GoogleDistance>();
		DrLocation drLoc = null;
		try {
		    matrix = DistanceMatrixApi.newRequest(context)
		            .origins(origins)
		            .destinations(destinations)
		            .mode(TravelMode.DRIVING)
		            .language("en-US")
		            .avoid(RouteRestriction.TOLLS)
		            .units(Unit.METRIC)
		            .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
		            .await();

		    //matrix.rows[0].elements[0].distance.humanReadable.endsWith("mi");
		    
		    Double minDist = 0d, minDur = 0d;
		for (int i = 0; i < matrix.rows[0].elements.length; i++) {
			
			
			if(matrix.rows[0].elements != null){
				Double dist =Double.valueOf(matrix.rows[0].elements[i].distance.toString().split(" ")[0]);
				Double dur = Double.valueOf(matrix.rows[0].elements[i].duration.toString().split(" ")[0]);
				DrLocation dr = mapIds.get(i);
				
				if(i ==0){
					minDist = dist;
					minDur = dur;
					drLoc = dr;  
				}
				
				if(dur <= minDur && dist < minDist){
					minDist = dist;
					minDur = dur;
					drLoc = dr;  	
				}
				
			}
		} 
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return drLoc;
	}
	
/*		public static void main(String[] args){
			
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAylyhXnYJvyZN5itYNMv4IkmW0ydeFVDs");

			 LatLng[] origins = {new LatLng(-27.443036, 153.058430),
					 new LatLng(-27.442951, 153.057785)
			 };
			 LatLng[] destinations = {new LatLng(-27.447312, 153.056249),
					 new LatLng(-27.438998, 153.059891)         };
			 //new LatLng(-27.439647, 153.058405)
			DistanceMatrix d =  getDistanceMatrixFromGoogle(context, origins, destinations);
			
			for (int i = 0; i < d.rows.length; i++) {
				System.out.println(d.rows[i].toString());
				System.out.println(d.rows[0].elements[i].distance);
				System.out.println(d.rows[0].elements[i].duration +"   "+i);
			}
			System.out.println(d.toString());
			
		}
	*/	
		
	
	public String getDirections(GeoApiContext context, LatLng origins, LatLng destinations) {
		
	//	GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAylyhXnYJvyZN5itYNMv4IkmW0ydeFVDs");
		
		
		try {
			DirectionsResult result = DirectionsApi.newRequest(context)
		            .origin(origins)
		            .destination(destinations)
		            .mode(TravelMode.DRIVING)
		            .language("en-US")
		            .avoid(RouteRestriction.TOLLS)
		            .units(Unit.METRIC)
		            .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
		            .await();
					
//					context, "Sydney, AU",
//			        "Melbourne, AU").await();
			return result.routes[0].overviewPolyline.getEncodedPath();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

