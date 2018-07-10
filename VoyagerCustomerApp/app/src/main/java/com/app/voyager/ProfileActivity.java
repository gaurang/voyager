package com.app.voyager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.voyager.AsyncTask.CallAPI;
import com.app.voyager.AsyncTask.CallAPIUploadPic;
import com.app.voyager.Dataset.SpinnerModel;
import com.app.voyager.Dataset.UserDetails;
import com.app.voyager.Util.Session;
import com.app.voyager.Util.Util;
import com.app.voyager.adapter.Countrycode_adapter;
import com.app.voyager.dialogs.ProgressDialogView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by shadab.s on 14-01-2016.
 */
public class ProfileActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back, edit_btn, buttonLoadImage;
    Button edt_password, btn_save;
    EditText edt_fname, edt_lname;
    TextView edt_emailid, edt_mobilenumber, edt_countrycode;
    Spinner spinner1;
    UserDetails userDetails;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        progressdialog = new ProgressDialogView(ProfileActivity.this, "");
        BindView(null, savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    ArrayList<SpinnerModel> Getcountrycode;

    public ArrayList<SpinnerModel> Getcountrycode() {
        String[] recourseList = this.getResources().getStringArray(R.array.CountryCodes);
        Getcountrycode = new ArrayList<>();
        try {
            // String[] g=values[position].split(",");
            //  JSONObject jsonObject = new JSONObject(COUNTRYCODE);
            //  JSONArray jsonArray = jsonObject.getJSONArray("countries");
            for (int i = 0; i < recourseList.length; i++) {
                //  JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.CountryName = recourseList[i].split(",")[0];
                spinnerModel.Code = recourseList[i].split(",")[1].toLowerCase();
                spinnerModel.Image = getResources().getIdentifier("drawable/" + recourseList[i].split(",")[1].toLowerCase(), null, getPackageName());
                if (spinnerModel.CountryName.replace("+", "").equalsIgnoreCase(userDetails.countryCode)) {
                    setselectiondefault = i;
                }
                PrintMessage("Getcountrycode " + userDetails.countryCode + "   " + setselectiondefault + " " + spinnerModel.CountryName);
                Getcountrycode.add(spinnerModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Getcountrycode;
    }

    int setselectiondefault = 0;

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        userDetails = Session.GetUserInformation(Session.getAllInfo(ProfileActivity.this));
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        edit_btn = (ImageView) findViewById(R.id.edit_btn);
        buttonLoadImage = (ImageView) findViewById(R.id.buttonLoadImage);
        edt_password = (Button) findViewById(R.id.edt_password);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        btn_save = (Button) findViewById(R.id.btn_save);
        edt_fname = (EditText) findViewById(R.id.edt_fname);
        edt_lname = (EditText) findViewById(R.id.edt_lname);
        edt_countrycode = (TextView) findViewById(R.id.edt_countrycode);
        edt_mobilenumber = (TextView) findViewById(R.id.edt_mobilenumber);
        edt_emailid = (TextView) findViewById(R.id.edt_emailid);
        Getcountrycode = Getcountrycode();
        Countrycode_adapter countrycode_adapter = new Countrycode_adapter(ProfileActivity.this, R.layout.spinner_countrycode, Getcountrycode);
        if (Session.getImagePath(ProfileActivity.this).length() > 3) {
            String PicURL = "";
            PicURL = Session.getImagePath(ProfileActivity.this);
            if (!PicURL.startsWith("http")) {
               // PicURL = WEB_SERVER_URL + WEB_SERVER_POST + Session.getImagePath(ProfileActivity.this);
                PicURL = WEB_SERVER_URL + Session.getImagePath(ProfileActivity.this);
            }
//            Picasso.with(ProfileActivity.this)
//                    .load(PicURL)
//                    .placeholder(R.drawable.defalt_user_pic)
//                    .error(R.drawable.defalt_user_pic)
//                    .into(buttonLoadImage);
        }
        // Set adapter to spinner
        spinner1.setAdapter(countrycode_adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt_countrycode.setText(Getcountrycode.get(position).CountryName);
                edt_countrycode.setCompoundDrawablesWithIntrinsicBounds(Getcountrycode.get(position).Image, 0, 0, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setSelection(setselectiondefault);
        headername.setText("PROFILE");
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        edt_password.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        buttonLoadImage.setOnClickListener(this);
        edt_countrycode.setOnClickListener(this);
        updatebuttonUi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.btn_save:
                CallValidateRegister();
                break;
            case R.id.edit_btn:
                updatebuttonUi();
                break;
            case R.id.edt_password:
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonLoadImage:
                pickImage();
                break;
            case R.id.edt_countrycode:
                PrintMessage("CalllCleick");
                spinner1.performClick();
                break;
        }

    }

    boolean isEditable = false;

    public void updatebuttonUi() {
        edt_fname.setText(userDetails.fname);
        edt_lname.setText(userDetails.lname);
        //  edt_countrycode.setText(userDetails.);
        edt_mobilenumber.setText(userDetails.mobile);
        edt_emailid.setText(userDetails.email);
        if (isEditable)
            btn_save.setVisibility(View.VISIBLE);
        else
            btn_save.setVisibility(View.GONE);
        edt_fname.setClickable(isEditable);
        edt_lname.setClickable(isEditable);
        buttonLoadImage.setClickable(isEditable);
        edt_countrycode.setClickable(isEditable);
        edt_mobilenumber.setClickable(isEditable);
        edt_emailid.setClickable(isEditable);
        spinner1.setClickable(isEditable);
        edt_fname.setEnabled(isEditable);
        edt_lname.setEnabled(isEditable);
        edt_countrycode.setEnabled(isEditable);
        edt_mobilenumber.setEnabled(isEditable);
        edt_emailid.setEnabled(isEditable);
        spinner1.setEnabled(isEditable);
        buttonLoadImage.setEnabled(isEditable);
        isEditable = !isEditable;
    }

    public void CallValidateRegister() {
//        edt_countrycode, edt_fname, edt_lname, edt_mobilenumber, edt_emailid, edt_password
        if (edt_fname.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity.this, "Enter first name");
        } else if (edt_lname.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity.this, "Enter last name");
        } else if (edt_mobilenumber.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity.this, "Enter mobile number");
        } else if (edt_emailid.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity.this, "Enter email id");
        } else if (edt_password.getText().toString().trim().length() == 0) {
            Util.ShowToast(ProfileActivity.this, "Enter password");
        } else if (edt_mobilenumber.getText().toString().trim().length() < 10) {
            Util.ShowToast(ProfileActivity.this, "Enter 10 digit mobile number");
        } else if (!Util.isEmailValid(edt_emailid.getText().toString().trim())) {
            Util.ShowToast(ProfileActivity.this, "Enter valid email id");
        } else if (edt_password.getText().toString().trim().length() < 6) {
            Util.ShowToast(ProfileActivity.this, "Password must be greater than 6 digit");
        } else {

            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(ProfileActivity.this));
                jsonObject.put("fname", edt_fname.getText().toString().trim());
                jsonObject.put("lname", edt_lname.getText().toString().trim());
                jsonObject.put("mobile", edt_mobilenumber.getText().toString().trim());
                jsonObject.put("mobile", edt_mobilenumber.getText().toString().trim());
                jsonObject.put("countryCode", edt_countrycode.getText().toString().trim());

                jsonObject_main.put("body", jsonObject);

                userDetails.fname=edt_fname.getText().toString().trim();
                userDetails.lname=edt_lname.getText().toString().trim();
                //  edt_countrycode.setText(userDetails.);
                userDetails.mobile=edt_mobilenumber.getText().toString().trim();
              userDetails.countryCode= edt_countrycode.getText().toString().trim();


                CallAPI(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Intent intent = new Intent(RegisterActivity.this, MobileVerificationActivity.class);
//            startActivity(intent);

        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(ProfileActivity.this)) {
            try {
                if (progressdialog.isShowing())
                    progressdialog.dismiss();
                progressdialog.show();
                new CallAPI(UPDATEPROFILE, "UPDATEPROFILE", params, ProfileActivity.this, GetDetails_Handler, true);
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
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Session.setAllInfo(ProfileActivity.this, msg.getData().getString("responce"));
                    Util.ShowToast(ProfileActivity.this, "Profile updated successfully");
                    updatebuttonUi();
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
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
            }
        }
    };
    Handler GetRegiseterd_Handler = new Handler() {
        public void handleMessage(Message msg) {

            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Session.setImagePath(ProfileActivity.this, msg.getData().getString("responce"));
                    Util.ShowToast(ProfileActivity.this, "Profile pic updated successfully");
                    updatebuttonUi();
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
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(ProfileActivity.this, getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(ProfileActivity.this, msg.getData().getString("msg"));
            }
        }
    };
    String tempPath = "";
    Bitmap bitmap;
    int RESULT_LOAD_IMAGE = 101;

    public void pickImage() {
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
        startActivityForResult(chooser, RESULT_LOAD_IMAGE);
        // startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
//            Uri selectedImageUri = data.getData();
//            tempPath = getPath(selectedImageUri);
//
//            buttonLoadImage.setImageURI(Uri.parse(tempPath));
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
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
                    buttonLoadImage.setImageBitmap(bitmap);
                    ProfilePicfile(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");

                    buttonLoadImage.setImageBitmap(bitmap);
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
                jsonObject.put("custometid", Session.getUserID(ProfileActivity.this));
                //  jsonObject_main.put("custometid", jsonObject_main + "");

                jsonObject_main.put("body", jsonObject);
                progressdialog.show();
                new CallAPIUploadPic(UPLOADPROFILEPIC + "/" + Session.getUserID(ProfileActivity.this), "UPLOADPROFILEPIC", jsonObject_main, ProfileActivity.this, GetRegiseterd_Handler, bitmap);
            } catch (JSONException e) {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                e.printStackTrace();
            }
//            if (bitmap != null) {
//                SendHttpRequestTask t = new SendHttpRequestTask(GetRegiseterd_Handler, ProfileActivity.this);
//                String[] params = new String[]{UPLOADPROFILEPIC, ProfilePicPath, ""};
//
//                t.execute(params);
//            }
        }
    }

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


}
