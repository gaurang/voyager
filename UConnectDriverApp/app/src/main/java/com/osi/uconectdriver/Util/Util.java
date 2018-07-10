package com.osi.uconectdriver.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.osi.uconectdriver.ParentActivity;
import com.osi.uconectdriver.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public static String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public static void ShowToast(ParentActivity context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    public static void ShowAlert(Context context, String Message, int alerticon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage(Message);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    // Check Valid Email Id Or Not

    public static boolean isEmailValid(CharSequence email) {
        // ////////////////////////////////////////////System.out.println("email_add valid="
        // + android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static byte[] read(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

}
