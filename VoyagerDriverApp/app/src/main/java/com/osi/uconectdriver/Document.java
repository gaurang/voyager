package com.osi.uconectdriver;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.osi.uconectdriver.Util.Session;
import com.osi.uconectdriver.dialogs.ProgressDialogView;

import java.net.URL;

/**
 * Created by Brij on 03-10-2016.
 */
public class Document extends ParentActivity{
    private TextView headername;
    private ImageView ic_back;
    private URL url;
    private Bitmap image;
    ImageView card,licence,insurance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);
        progressdialog = new ProgressDialogView(Document.this, "");

        headername = (TextView) findViewById(R.id.headername);
        headername.setText("DOCUMENTS");
        if (progressdialog.isShowing())
            progressdialog.dismiss();
        progressdialog.show();
        ic_back = (ImageView) findViewById(R.id.ic_back);
        card = (ImageView) findViewById(R.id.card);
        licence = (ImageView) findViewById(R.id.licence);
        insurance = (ImageView) findViewById(R.id.insurance);
        card.setImageURI(Uri.parse(GETPROFILEPIC + "/" + Session.getUserID(Document.this)+"/ID"));
        licence.setImageURI(Uri.parse(GETPROFILEPIC + "/" + Session.getUserID(Document.this)+"/DL"));
        insurance.setImageURI(Uri.parse(GETPROFILEPIC + "/" + Session.getUserID(Document.this)+"/DI"));
        progressdialog.dismissanimation(ProgressDialogView.ERROR);
    }
}
