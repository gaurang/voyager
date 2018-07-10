package com.app.uconect;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.Dataset.FriendDetails;
import com.app.uconect.RecyclerviewAdapter.AddFriendAdapter;

import java.util.ArrayList;


/**
 * Created by shadab.s on 14-01-2016.
 */
public class ContactActivity extends ParentActivity implements View.OnClickListener {

    TextView headername;
    ImageView ic_back;
    ArrayList<FriendDetails> Friendlist = new ArrayList<>();
    RecyclerView mrecycler_score;
    AddFriendAdapter addFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindView(null, savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        setContentView(R.layout.activity_contact);
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        mrecycler_score = (RecyclerView) findViewById(R.id.recyclerview_list);
        mrecycler_score.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                ContactActivity.this);
        mrecycler_score.setLayoutManager(mLayoutManager);
        headername.setText("CONTACTS");
        SetOnclicklistener();

    }


    @Override
    public void SetOnclicklistener() {
        ic_back.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(ContactActivity.this,
                PermisonsList[4])
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ContactActivity.this,
                PermisonsList[4])
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ContactActivity.this,
                    new String[]{PermisonsList[4]},
                    100);


        } else {
            new callFetchContalist().execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ContactActivity.this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactActivity.this, "PERMISSION Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    public void readContacts() {
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                        System.out.println("phone" + phone);
                        FriendDetails FriendDetails = new FriendDetails();
                        FriendDetails.first_name = name;
                        FriendDetails.CONTACTID = id;
                        FriendDetails.mobilenumber = phone;
                        FriendDetails.imageuri = image_uri;
                        Friendlist.add(FriendDetails);
                    }
                    pCur.close();
//                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
//                    while (emailCur.moveToNext()) {
//                        emailContact = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                        emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
//                        sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
//                        System.out.println("Email " + emailContact + " Email Type : " + emailType);
//                    }
//                    emailCur.close();
                }
//                if (image_uri != null) {
//                    System.out.println(Uri.parse(image_uri));
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(image_uri));
//                        sb.append("\n Image in Bitmap:" + bitmap);
//                        System.out.println(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                sb.append("\n........................................");
            }
        }
    }

    class callFetchContalist extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            readContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PrintMessage("Friendlist " + Friendlist.size());
            addFriendAdapter = new AddFriendAdapter(Friendlist, ContactActivity.this);
            mrecycler_score.setAdapter(addFriendAdapter);
        }
    }

}