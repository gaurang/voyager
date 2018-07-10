package com.app.uconect.tabfragments;

import android.content.Intent;
import android.database.Cursor;
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
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.uconect.AsyncTask.CallAPI;
import com.app.uconect.AsyncTask.CallAPIUploadPic;
import com.app.uconect.MobileVerificationActivity;
import com.app.uconect.ParentActivity;
import com.app.uconect.R;
import com.app.uconect.Util.Methods;
import com.app.uconect.Util.NetworkHelper;
import com.app.uconect.Util.PreferenceHelper;
import com.app.uconect.Util.Session;
import com.app.uconect.Util.Util;
import com.app.uconect.dialogs.ProgressDialogView;
import com.app.uconect.interfaces.AsyncInterface;
import com.app.uconect.interfaces.myUrls;

import org.apache.http.impl.client.DefaultHttpClient;
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

/**
 * Created by atul on 26/4/16.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener,myUrls,AsyncInterface {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "ProfileFragment";
    ProgressDialogView progressdialog;
    PreferenceHelper preferenceHelper;
    private EditText fname, lname, email, mobile, oldpassword, newpassword, confirmpassword;
    TextView txtedit;
    ImageView imgPassengerPic;
    private Button btnchangepassword, btnupdate, btnback, btnConfirm, btnbackupdate;
    boolean enable = false;
    LinearLayout lllayout1, lllayout2, lllayoutUpdate;
    private Button buttonChoose;
    private Button buttonUpload;
    private DefaultHttpClient mHttpClient;



    private Uri filePath;
    private int RESULT_OK=-1;
    private ImageView img;
    private String uploadImage;
    private Bitmap bm;
    private String path;

    String tempPath = "";
    Bitmap bitmap;
    int RESULT_LOAD_IMAGE = 101;
    private URL url;
    private Bitmap image;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imgPassengerPic = (ImageView) view.findViewById(R.id.imgPassengerPic);
        if (NetworkHelper.isOnline(getActivity())) {
            getDetail();
        } else {
            Methods.toastShort("Please check your internet connection..", getActivity());
        }
        initComponent(view);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return view;
    }

    private void initComponent(View view) {
        preferenceHelper = new PreferenceHelper(getActivity());
        lllayoutUpdate = (LinearLayout) view.findViewById(R.id.lllayoutUpdate);

        oldpassword = (EditText) view.findViewById(R.id.oldpasswpord);
        newpassword = (EditText) view.findViewById(R.id.newpassword);
        confirmpassword = (EditText) view.findViewById(R.id.confirmpassword);
        btnbackupdate = (Button) view.findViewById(R.id.btnbackupdate);
        btnchangepassword = (Button) view.findViewById(R.id.btnchangepassword);
        fname = (EditText) view.findViewById(R.id.fname);
        lname = (EditText) view.findViewById(R.id.lname);
        email = (EditText) view.findViewById(R.id.email);
        mobile = (EditText) view.findViewById(R.id.mobile);
        btnupdate = (Button) view.findViewById(R.id.btnupdate);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnback = (Button) view.findViewById(R.id.btnback);
        txtedit = (TextView) view.findViewById(R.id.txtedit);
        lllayout1 = (LinearLayout) view.findViewById(R.id.lllayout1);
        lllayout2 = (LinearLayout) view.findViewById(R.id.lllayout2);
        btnchangepassword.setOnClickListener(this);
        btnupdate.setOnClickListener(this);
        btnback.setOnClickListener(this);
        btnbackupdate.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        txtedit.setOnClickListener(this);
        fname.setText("" + preferenceHelper.getString("FNAME"));
        lname.setText("" + preferenceHelper.getString("LNAME"));
        email.setText("" + preferenceHelper.getString("EMAIL"));
        mobile.setText("" + preferenceHelper.getString("MOBILE"));
        mobile.setEnabled(false);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnchangepassword:
                lllayout1.setVisibility(View.GONE);
                lllayout2.setVisibility(View.VISIBLE);
                break;
            case R.id.btnupdate:
                if (NetworkHelper.isOnline(getActivity())) {
                    progressdialog = new ProgressDialogView(getActivity(), "Please wait..");
                    progressdialog.show();
                    Intent intent = new Intent(getActivity(), MobileVerificationActivity.class);
                    intent.putExtra("mobile", mobile.getText().toString().trim());
                    intent.putExtra("email", email.getText().toString().trim());
                    intent.putExtra("regId", "");
                    intent.putExtra("id", 1);
                    startActivity(intent);
                    //getUpdate();
                } else {
                    Methods.toastShort("Please check your internet connection..", getActivity());
                }
                break;
            case R.id.txtedit:
                uiComponent(true);
                break;
            case R.id.btnbackupdate:
                fname.setText("" + preferenceHelper.getString("FNAME"));
                lname.setText("" + preferenceHelper.getString("LNAME"));
                email.setText("" + preferenceHelper.getString("EMAIL"));
                mobile.setText("" + preferenceHelper.getString("MOBILE"));
                uiComponent(false);
                break;
            case R.id.btnConfirm:
                if (NetworkHelper.isOnline(getActivity())) {
                    progressdialog = new ProgressDialogView(getActivity(), "Please wait..");
                    progressdialog.show();
                    changePassword();
                    progressdialog.dismiss();
                } else {
                    Methods.toastShort("Please check your internet connection..", getActivity());

                }
                break;
            case R.id.btnback:
                lllayout1.setVisibility(View.VISIBLE);
                lllayout2.setVisibility(View.GONE);
                oldpassword.setText("");
                newpassword.setText("");
                confirmpassword.setText("");
                break;
            case R.id.imgPassengerPic:

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
    }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        InputStream stream = ((ParentActivity) getActivity()).getContentResolver().openInputStream(
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
                        imgPassengerPic.setImageBitmap(getCircleBitmap(bitmap));
                        ProfilePicfile(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        imgPassengerPic.setImageBitmap(getCircleBitmap(bitmap));
                        ProfilePicfile(bitmap);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
                ProfilePicPath = Environment.getExternalStorageDirectory().toString() + "/UconectProfile.png";
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject_main = ((ParentActivity) getActivity()).getCommontHeaderParams();
                    jsonObject.put("custometid", Session.getUserID(getContext()));
                    //  jsonObject_main.put("custometid", jsonObject_main + "");

                    jsonObject_main.put("body", jsonObject);
                    progressdialog.show();
                    new CallAPIUploadPic(UPLOADPROFILEPIC + "/" + Session.getUserID(getContext()), "UPLOADPROFILEPIC", jsonObject_main, getContext(), GetRegiseterd_Handler, bitmap);
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

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(getContext(),contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    Handler GetRegiseterd_Handler = new Handler() {
        public void handleMessage(Message msg) {

            ((ParentActivity) getContext()).PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Session.setImagePath(getContext(), msg.getData().getString("responce"));
                    Util.ShowToast(getContext(), "Profile pic updated successfully");
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity) getContext()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getContext())) {
                        ((ParentActivity) getContext()).CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        progressdialog.dismissanimation(ProgressDialogView.ERROR);
                        Util.ShowToast(getContext(), getString(R.string.nointernetmessage));
                    }
                } else {
                    progressdialog.dismissanimation(ProgressDialogView.ERROR);
                    Util.ShowToast(getContext(), msg.getData().getString("msg"));
                }
            } else {
                progressdialog.dismissanimation(ProgressDialogView.ERROR);
                Util.ShowToast(getContext(), msg.getData().getString("msg"));
            }
        }
    };

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

    private void uiComponent(boolean enable) {
        if (enable) {
            //        fname.setEnabled(false);
            //          lname.setEnabled(false);
//            email.setEnabled(true);
            imgPassengerPic.setOnClickListener(this);
            mobile.setEnabled(true);
            btnchangepassword.setVisibility(View.GONE);
            btnupdate.setVisibility(View.VISIBLE);
            lllayoutUpdate.setVisibility(View.VISIBLE);
        } else {
            fname.setEnabled(false);
            lname.setEnabled(false);
            mobile.setEnabled(false);
            btnchangepassword.setVisibility(View.VISIBLE);
            btnupdate.setVisibility(View.GONE);
            lllayoutUpdate.setVisibility(View.GONE);
        }

    }

    private void getDetail() {
        progressdialog = new ProgressDialogView(getActivity(), "Please wait..");
        progressdialog.show();
        try {
            JSONObject jsonObject_main = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject_main = ((ParentActivity) getActivity()).getCommontHeaderParams();
            jsonObject.put("customerId", Session.getUserID(getActivity()));
            jsonObject_main.put("body", jsonObject);


            CallAPI1(jsonObject_main);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CallAPI1(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                new CallAPI(UPDATEPROFILE, "UPDATEDETAILS", params, getActivity(), GetDetails_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }

    Handler GetDetails_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    try {
                        mobile.setEnabled(false);
                        fname.setEnabled(false);
                        lname.setEnabled(false);
                        try {
                            url = new URL(GETPROFILEPIC + "/" + Session.getUserID(getContext()));
                            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            imgPassengerPic.setImageBitmap(getCircleBitmap(image));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (mobile.isEnabled()) {
                            Log.v("NUMBER", "NUMBER:::" + mobile.getText().toString() + "::::NUMBER1:::+" + preferenceHelper.getString("MOBILE"));
                        } else {
                            mobile.setEnabled(true);
                            Log.v("NUMBER", "NUMBER:::" + mobile.getText().toString() + "::::NUMBER1:::+" + preferenceHelper.getString("MOBILE"));
                        }

                        JSONObject jsonObject = new JSONObject(msg.getData().getString("responce"));
                        if (jsonObject != null) {
                            if (!mobile.getText().toString().equals(preferenceHelper.getString("MOBILE"))) {
                                Intent intent = new Intent(getActivity(), MobileVerificationActivity.class);
                                intent.putExtra("email", email.getText().toString() + "");
                                intent.putExtra("regId", jsonObject.getString("regId"));
                                intent.putExtra("mobile", mobile.getText().toString() + "");
                                startActivity(intent);
                            } else {
                                preferenceHelper.addString("FNAME", jsonObject.getString("fname"));
                                fname.setText(jsonObject.getString("fname"));
                                preferenceHelper.addString("LNAME", jsonObject.getString("lname"));
                                lname.setText(jsonObject.getString("lname"));
                                preferenceHelper.addString("EMAIL", jsonObject.getString("email"));
                                email.setText(jsonObject.getString("email"));
                                preferenceHelper.addString("MOBILE", jsonObject.getString("mobile"));
                                mobile.setText(jsonObject.getString("mobile"));
                                mobile.setEnabled(false);
                                //imgPassengerPic.setImageURI(Uri.parse(jsonObject.getString("profilePicURL")));
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity) getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity) getActivity()).CallSessionID(GetDetails_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));
            }
        }
    };

    private void changePassword() {
        if (oldpassword.getText().toString().length() == 0) {
            Util.ShowToast(getActivity(), "Enter old password");
        } else if (newpassword.getText().toString().length() == 0) {
            Util.ShowToast(getActivity(), "Enter new password");
        } else if (confirmpassword.getText().toString().length() == 0) {
            Util.ShowToast(getActivity(), "Enter confirm Password");
        } else if (oldpassword.getText().toString().equals(newpassword.getText().toString())) {
            Util.ShowToast(getActivity(), "Enter different password");
        } else if (!confirmpassword.getText().toString().equals(newpassword.getText().toString())) {
            Util.ShowToast(getActivity(), "Password mismatch");
        } else {
            try {
                JSONObject jsonObject_main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject_main = ((ParentActivity) getActivity()).getCommontHeaderParams();
                jsonObject.put("customerId", Session.getUserID(getActivity()));
                jsonObject.put("oldPassword", oldpassword.getText().toString().trim());
                jsonObject.put("newPassword", newpassword.getText().toString().trim());
                jsonObject_main.put("body", jsonObject);
                Log.i("hhhhhhhh", "sssssss" + jsonObject_main);
                CallAPI(jsonObject_main);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void CallAPI(JSONObject params) {
        if (Util.isNetworkConnected(getActivity())) {
            try {
                new CallAPI(CHANGEPASSWORDS, "CHANGEPASSWORD", params, getActivity(), Change_Handler, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressdialog.dismissanimation(ProgressDialogView.ERROR);
            Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
        }
    }

    Handler Change_Handler = new Handler() {
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
//            PrintMessage("Handler " + msg.getData().toString());
            if (msg.getData().getBoolean("flag")) {
                if (msg.getData().getInt("code") == SUCCESS) {
                    Util.ShowToast(getActivity(), "Password change successfully");
                    lllayout2.setVisibility(View.GONE);
                    lllayout1.setVisibility(View.VISIBLE);
                    getActivity().finish();

                } else if (msg.getData().getInt("code") == FROMGENERATETOKEN) {
                    ((ParentActivity) getActivity()).ParseSessionDetails(msg.getData().getString("responce"));
                    try {
                        CallAPI(new JSONObject(msg.getData()
                                .getString("mExtraParam")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().getInt("code") == SESSIONEXPIRE) {
                    if (Util.isNetworkConnected(getActivity())) {
                        ((ParentActivity) getActivity()).CallSessionID(Change_Handler, msg.getData()
                                .getString("mExtraParam"));
                    } else {
                        Util.ShowToast(getActivity(), getString(R.string.nointernetmessage));
                    }
                } else {
                    Util.ShowToast(getActivity(), msg.getData().getString("msg"));
                }
            } else {
                Util.ShowToast(getActivity(), msg.getData().getString("msg"));
            }
        }
    };

}
