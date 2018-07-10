package com.osi.uconectdriver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.osi.uconectdriver.AsyncTask.CallAPI;
import com.osi.uconectdriver.AsyncTask.CallAPIUploadPic;
import com.osi.uconectdriver.Dataset.EmergencyContactData;
import com.osi.uconectdriver.RecyclerviewAdapter.EmergencycontactAdapter;
import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.Util.Util;
import com.osi.uconectdriver.adapter.PagerAdapter1;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Brij on 05-03-2016.
 */
public class ProfileActivity extends ParentActivity implements View.OnClickListener{
    private Button profile;
    private Button emergency;
    private TextView fname;
    private TextView lname;
    private TextView email;
    private ImageView ic_back,image;
    private Button change;
    private Button change1;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private ViewPager viewPager;
    private Button logout;
    private Button back;
    private EditText oldpassword;
    private EditText newpassword;
    private EditText confirmpassword;
    private EditText mobile;
    private Button addcontact;
    private TextView header;
    private TextView edit;
    private Button update;
    private Bitmap bitmap;

    private Uri filePath;
    private int RESULT_OK=-1;
    LinearLayout messagelayout;
    EmergencycontactAdapter emergencycontactAdapter;
    int CONTACT = 1;
    RecyclerView mrecycler_score;
    private JSONObject params;
    private int SELECT_PICTURE=101;
    private URL url;
    private Bitmap image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_1);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter1 adapter = new PagerAdapter1
                (getSupportFragmentManager(), 2);
        header=(TextView)findViewById(R.id.headername);
        header.setText("SETTINGS");
        viewPager.setAdapter(adapter);
        profile=(Button)findViewById(R.id.profile);
        emergency=(Button)findViewById(R.id.emergency);
        messagelayout = (LinearLayout) findViewById(R.id.messagelayout);
        ic_back=(ImageView)findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);
        profile.setOnClickListener(this);
        emergency.setOnClickListener(this);

        getDetail();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == profile)
        {
            viewPager.setCurrentItem(0);
            profile.setBackgroundResource(R.drawable.yellow_shape);
            emergency.setBackgroundResource(R.drawable.whiteshape);
            getProfile();
        }
        else if(v == emergency)
        {
            viewPager.setCurrentItem(1);
            emergency.setBackgroundResource(R.drawable.yellow_shape);
            profile.setBackgroundResource(R.drawable.whiteshape);
            getemergency();
        }
        else if(v == ic_back)
        {
            finish();
        }
    }



    private void getProfile() {
        change = (Button) findViewById(R.id.changepassword);
        change1 = (Button) findViewById(R.id.changepassword1);
        update=(Button)findViewById(R.id.update);
        oldpassword = (EditText) findViewById(R.id.oldpasswpord);
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        back=(Button) findViewById(R.id.back);
        logout=(Button) findViewById(R.id.logout);
        image=(ImageView)findViewById(R.id.imgPassengerPic);
        edit=(TextView) findViewById(R.id.edit);
        mobile=(EditText)findViewById(R.id.mobile);
        edit.setVisibility(View.VISIBLE);
        change.setVisibility(View.VISIBLE);
        update.setVisibility(View.GONE);
        try {
            url = new URL(GETPROFILEPIC + "/" + Session.getUserID(ProfileActivity.this)+"/PP");
            image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            image.setImageBitmap(getCircleBitmap(image1));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(ProfileActivity.this,Launch_activity.class);
//                startActivity(intent);
                LogoutUser();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            }
        });
        change1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile.setEnabled(true);
                image.setEnabled(true);
                image.setOnClickListener(this);
                edit.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
                change.setVisibility(View.GONE);
                update.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (v == update) {
                            getUpdate();
                        }
                    }
                });
                image.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

                        Intent cameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);

                        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
                        chooser.putExtra(Intent.EXTRA_TITLE, "Choose profile picture");

                        Intent[] intentArray = {cameraIntent};
                        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                        startActivityForResult(chooser, SELECT_PICTURE);
                    }
                });
            }
        });
    }

    private void changePassword() {
                if (oldpassword.getText().toString().length() == 0) {
                    Util.ShowToast(ProfileActivity.this, "Enter old password");
                } else if (newpassword.getText().toString().length() == 0) {
                    Util.ShowToast(ProfileActivity.this, "Enter new password");
                } else if (confirmpassword.getText().toString().length() == 0) {
                    Util.ShowToast(ProfileActivity.this, "Enter confirm Password");
                } else if (oldpassword.getText().toString().equals(newpassword.getText().toString())) {
                    Util.ShowToast(ProfileActivity.this, "Enter different password");
                }else if (!confirmpassword.getText().toString().equals(newpassword.getText().toString())) {
                    Util.ShowToast(ProfileActivity.this, "Password mismatch");
                }else {
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("driverId", Session.getUserID(ProfileActivity.this));
                        jsonObject.put("oldpassword", oldpassword.getText().toString().trim());
                        jsonObject.put("newpasswword", newpassword.getText().toString().trim());
                        jsonObject_main.put("body", jsonObject);
                        CallAPI(jsonObject_main);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity.this)) {
            try {
                new CallAPI(CHANGEPASSWORDS, "CHANGEPASSWORD", params, ProfileActivity.this, Change_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler Change_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Util.ShowToast(ProfileActivity.this, "Password change successfully");
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    finish();

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity.this)) {
                        CallSessionID(Change_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
            }
        }
    };
    private void getemergency() {
        addcontact = (Button) findViewById(R.id.addcontact);
        messagelayout = (LinearLayout) findViewById(R.id.messagelayout);
        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                ProfileActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);
        SetOnclicklistener();
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataList.size() < 2) {
                    final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                    Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                    startActivityForResult(intentPickContact, CONTACT);
                } else {
                    Util.ShowToast(ProfileActivity.this, "You can add Maximum 2 conatact");
                }
            }
        });
    }
        @Override
        public void SetOnclicklistener() {
            ic_back.setOnClickListener(this);
            addcontact.setOnClickListener(this);
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("driverId", Session.getUserID(ProfileActivity.this));
                jsonObject_main.put("body", jsonObject);
                CallGETEEMERGENCY(jsonObject_main);

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }
        String phone;
        String name;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
//            Uri selectedImageUri = data.getData();
//            tempPath = getPath(selectedImageUri);
//
//            buttonLoadImage.setImageURI(Uri.parse(tempPath));
        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data.getData() != null) {

                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    InputStream stream = getContentResolver().openInputStream(
                            data.getData());
//

                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o = new BitmapFactory.Options();
                    o.inSampleSize = 2;
                    o.inJustDecodeBounds = false;
                    o.inPreferredConfig = Bitmap.Config.RGB_565;
                    o.inDither = true;
                    bitmap = BitmapFactory.decodeStream(stream, null, o);
                    stream.close();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


                    // We need to recyle unused bitmaps
                    //
                    image.setImageBitmap(getCircleBitmap(bitmap));
                    ProfilePicfile(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");

                    image.setImageBitmap(getCircleBitmap(bitmap));
                    ProfilePicfile(bitmap);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
            ProfilePicPath = Environment.getExternalStorageDirectory().toString() + "/UconectProfile.png";
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("driverId", Session.getUserID(ProfileActivity.this));
                //  jsonObject_main.put("custometid", jsonObject_main + "");

                jsonObject_main.put("body", jsonObject);

                new CallAPIUploadPic(UPLOADPROFILEPIC + "/" + Session.getUserID(ProfileActivity.this)+"/PP", "UPLOADPROFILEPIC", jsonObject_main, ProfileActivity.this, GetRegiseterd_Handler, bitmap);
            } catch (JSONException e) {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                e.printStackTrace();
            }
//            if (bitmap != null) {
//                SendHttpRequestTask t = new SendHttpRequestTask(GetRegiseterd_Handler, getContext());
//                String[] params = new String[]{UPLOADPROFILEPIC, ProfilePicPath, ""};
//
//                t.execute(params);
//            }
        }
    }
    Handler GetRegiseterd_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Session.setImagePath(ProfileActivity.this, msg.getData().getString("responce"));
                    Util.ShowToast(ProfileActivity.this, "Profile pic updated successfully");
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {

                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {

                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {

                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
            }
        }
    };

    String ProfilePicPath = "";

    public void ProfilePicfile(Bitmap bitmap) {
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, "UconectProfile.png");
        try {

            try {
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
        }
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        return output;
    }


    public void CallADDEMERGENCYAPI(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity.this)) {
            try {
                new CallAPI(ADDEMERGENCYCONTACT, "ADDEMERGENCYCONTACT", params, ProfileActivity.this, GetResend_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
        }
    }

    Handler GetResend_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {

                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                    try {
                        JSONObject jsonObject_main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject_main = getCommontHeaderParams();
                        jsonObject.put("driverId", Session.getUserID(ProfileActivity.this));
                        jsonObject_main.put("body", jsonObject);
                        CallGETEEMERGENCY(jsonObject_main);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallADDEMERGENCYAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity.this)) {
                        CallSessionID(GetResend_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));

            }
        }
    };

    public void CallGETEEMERGENCY(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity.this)) {
            try {
                new CallAPI(GETEMERGENCYCONTACT, "GETEMERGENCYCONTACT", params, ProfileActivity.this, GetPlaces_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
        }
    }

    ArrayList<EmergencyContactData> DataList = new ArrayList<>();

    Handler GetPlaces_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    DataList = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(msg.getData().getString("responce"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EmergencyContactData emergencyContactData = new EmergencyContactData();
                            emergencyContactData.contactId = jsonObject.getInt("contactId");
                            emergencyContactData.trackStatus = jsonObject.getInt("trackStatus");
                            emergencyContactData.driverId = jsonObject.getString("driverId");
                            emergencyContactData.eContactName = jsonObject.getString("eContactName");
                            emergencyContactData.eContactNumber = jsonObject.getString("eContactNumber");
                            DataList.add(emergencyContactData);
                        }

                        if (DataList.size() > 0) {
                            messagelayout.setVisibility(View.GONE);
                            emergencycontactAdapter = new EmergencycontactAdapter(DataList, ProfileActivity.this);
                            mrecycler_score.setAdapter(emergencycontactAdapter);
                        } else {
                            messagelayout.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        messagelayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                    //   Util.ShowToast(AddFavorPlacesActivity.this, msg.getData().getString("responce"));

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallGETEEMERGENCY(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity.this)) {
                        CallSessionID(GetPlaces_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));

            }
        }
    };
    private void getDetail() {
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = getCommontHeaderParams();
            jsonObject.put("driverId", Session.getUserID(ProfileActivity.this));
            jsonObject_main.put("body", jsonObject);
            CallAPI1(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CallAPI1(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity.this)) {
            try {
                new CallAPI(GETDRIVERDETAILS, "GETDRIVERDETAILS", params, ProfileActivity.this, GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
        }
    }
    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    // Session.setAllInfo(ProfileActivity.this,msg.getData().getString("responce"));
                    //Intent intent = new Intent(ProfileActivity.this, LandingPageActivity.class);
                    try {
                        fname = (TextView) findViewById(R.id.fname);
                        lname = (TextView) findViewById(R.id.lname);
                        email = (TextView) findViewById(R.id.email);
                        mobile=(EditText) findViewById(R.id.mobile);
                        mobile.setEnabled(false);
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        fname.setText(jsonObject.getString("fname"));
                        lname.setText(jsonObject.getString("lname"));
                        email.setText(jsonObject.getString("email"));
                        mobile.setText(jsonObject.getString("phone"));
                        getProfile();
//                        intent.putExtra("mobile", jsonObject.getString("mobile"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(intent);
                    //    finish();
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(ProfileActivity.this)) {
                        CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
            }
        }
    };
    private void getUpdate() {
        mobile = (EditText) findViewById(R.id.mobile);
        if (mobile.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity.this, "Enter Mobile Number");
        } else {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("driverId", Session.getUserID(ProfileActivity.this));
                jsonObject.put("phone", mobile.getText().toString().trim());
                jsonObject_main.put("body", jsonObject);
                CallAPI2(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void CallAPI2(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity.this)) {
            try {
                new CallAPI(UPDATEPROFILE, "UPDATEDETAILS", params, ProfileActivity.this, Update_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
        }
    }

Handler Update_Handler = new Handler() {
    public void handleMessage(Message msg) {

        PrintMessage("Handler " + msg.getData().toString());
        if (msg.getData().getBoolean("flag")) {
            if (msg.getData().getInt("code") == SUCCESS) {
                Util.ShowToast(ProfileActivity.this, "Mobile number changed successfully");
                update.setVisibility(View.GONE);
                getDetail();
                change.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);

            } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {





                ParseSessionDetails(msg.getData().getString("responce"));
                try {
                    CallAPI(new JSONObject(msg.getData()
                            .getString("mExtraParam")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                if (Util.isNetworkConnected(ProfileActivity.this)) {
                    CallSessionID(Update_Handler, msg.getData()
                            .getString("mExtraParam"));
                } else {
                    Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                }
            } else {
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
            }
        } else {
            Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
        }
    }
};
    }

