package com.osi.voyagerdriver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.osi.voyagerdriver.Dataset.HelpData;


/**
 * Created by shadab.s on 14-01-2016.
 */
public class HelpDetailsActivity extends ParentActivity implements View.OnClickListener {
    TextView headername;
    ImageView ic_back;
    HelpData helpData;
    TextView title, description;
    private EditText editText;
    private EditText editText2;
    private Button sendButton;
    private static String username = "aonghusindia@gmail.com";
    private static String name = "BRIJ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpdetails);
        helpData = (HelpData) getIntent().getSerializableExtra("data");
        BindView(null, savedInstanceState);

    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        sendButton = (Button) findViewById(R.id.send);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        headername.setText(helpData.supportType);
        title.setText(helpData.supportQuestion);
        if (helpData.description == null || helpData.description.equalsIgnoreCase("null"))
            description.setText("Not found!");
        else description.setText(helpData.description);
        SetOnclicklistener();

    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        ic_back.setOnClickListener(this);
        sendButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.send:
                sendEmail();
                break;

        }
    }

    protected void sendEmail() {
        String[] recipients = {username.toString()};

        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));

        // prompts email clients only

        email.setType("message/rfc822");

        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, "Issue");
        email.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(HelpDetailsActivity.this, "No email client installed.",

                    Toast.LENGTH_LONG).show();
        }

    }
}
