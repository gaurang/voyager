package com.app.uconect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.uconect.Dataset.HelpData;

import java.util.ArrayList;

/**
 * Created by Brij on 06-02-2016.
 */
public class help1 extends AppCompatActivity {
    HelpData helpData;
    ArrayList<HelpData> Historylist = new ArrayList<>();
    private TextView txtview;
    private TextView txtview1;
    private TextView txtview2;
    private EditText editText;
    private EditText editText2;
    private Button sendButton;
    private static String username = "aonghusindia@gmail.com";
    private static String name = "BRIJ";
    TextView headername;
    ImageView ic_back;
    TextView title, description;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpdetails);
        helpData = (HelpData) getIntent().getSerializableExtra("data");
        sendButton = (Button) findViewById(R.id.send);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        headername = (TextView) findViewById(R.id.headername);
        ic_back = (ImageView) findViewById(R.id.ic_back);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        headername.setText(helpData.supportType);
        title.setText(helpData.supportQuestion);
        if (helpData.description == null || helpData.description.equalsIgnoreCase("null"))
            description.setText("Not found!");
        else description.setText(helpData.description);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
                // after sending the email, clear the fields
            }
        });
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

            Toast.makeText(help1.this, "No email client installed.",

                    Toast.LENGTH_LONG).show();
                }

    }
}

//                String subject = txtview1.getText().toString();
//                String email = editText2.getText().toString();
//                String messageBody = editText.getText().toString();
//                sendMail(email, subject, messageBody);
//            }
//
//            private void sendMail(String email, String subject, String messageBody) {
//                Session session = createSessionObject();
//
//                try {
//                    Message message = createMessage(email, subject, messageBody,
//                            session);
//                    Log.i("AAAAAA","sssssss"+message);
//                    new SendMailTask().execute(message);
//                } catch (AddressException e) {
//                    e.printStackTrace();
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            private Message createMessage(String email, String subject,
//                                          String messageBody, Session session) throws MessagingException,
//                    UnsupportedEncodingException {
//                Message message = new MimeMessage(session);
//                message.setFrom(new InternetAddress());
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(
//                        email, email));
//                message.setSubject(subject);
//                message.setText(messageBody);
//                return message;
//            }
//
//            private Session createSessionObject() {
//                Properties properties = new Properties();
//                properties.put("mail.smtp.auth", "true");
//                properties.put("mail.smtp.starttls.enable", "true");
//                properties.put("mail.smtp.host", "smtp.gmail.com");
//                properties.put("mail.smtp.port", "587");
//
//                return Session.getInstance(properties, new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//            }
//
//            class SendMailTask extends AsyncTask<Message, Void, Void> {
//                private ProgressDialog progressDialog;
//
//                @Override
//                protected void onPreExecute() {
//                    super.onPreExecute();
//                    progressDialog = ProgressDialog.show(help1.this,
//                            "Please wait", "Sending mail", true, false);
//                }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                protected Void doInBackground(Message... messages) {
//                    try {
//                        Transport.send(messages[0]);
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                Toast.makeText(help1.this,
//                                        "Mail sent successfully", Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                        });
//                    } catch (final MessagingException e) {
//                        e.printStackTrace();
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                Toast.makeText(help1.this,
//                                        e.getClass() + " : " + e.getMessage(),
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                    return null;
//                }
//            }
//        });
//    }
//}