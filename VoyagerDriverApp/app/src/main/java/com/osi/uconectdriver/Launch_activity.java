package com.osi.uconectdriver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Brij on 27-02-2016.
 */
public class Launch_activity extends AppCompatActivity implements View.OnClickListener{
    private Button signin;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        signin=(Button) findViewById(R.id.signin);
        register=(Button) findViewById(R.id.register);
        signin.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == signin)
        {
            Context context=this;
            signin.setBackgroundResource(R.drawable.left_selected_tab);
            register.setBackgroundResource(R.drawable.right_unselected_tab);
            Intent intent=new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        if(v == register)
        {
            Context context=this;
            signin.setBackgroundResource(R.drawable.left_unselected_tab);
            register.setBackgroundResource(R.drawable.right_selected_tab);
            Uri uri = Uri.parse("http://192.168.1.104:8080/uc/enquiryForm"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
//            Intent intent=new Intent(context,Enquiry_activity.class);
//            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {

// make sure you have this outcommented
// super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


