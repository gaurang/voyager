package com.app.voyager.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;

public class NetworkHelper {

	public static boolean isOnline(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean isWifiAvailable(Context cxt) {
		WifiManager wifiManager = (WifiManager) cxt
				.getSystemService(Context.WIFI_SERVICE);
		if ((wifiManager.isWifiEnabled() == true)) {

			/*WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			 Get the MAC ADD of WIFI 
			 Commons.MAC_ID = wifiInf.getMacAddress();*/
			return true;
		} else {
			return false;
		}
	}

	public static void startActionCall(String phoneNumber, Context cxt) {
		Uri phoneNum = Uri.parse("tel:" + phoneNumber);
		cxt.startActivity(new Intent(Intent.ACTION_CALL, phoneNum));
	}


	public static String prepareParameterizedGetUrl(String url,
			List<NameValuePair> params) {

		url+="?";
		if (params != null) {
			for(NameValuePair pair : params){
				try {
					url +=pair.getName()+"="+ URLEncoder.encode(pair.getValue(), "UTF-8")+"&";
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
					break;
				}

			}
			url=url.substring(0,url.length()-1);
		}
		return url;

	}
}
