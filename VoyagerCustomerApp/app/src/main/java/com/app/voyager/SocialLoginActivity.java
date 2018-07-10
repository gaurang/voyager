package com.app.voyager;


import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.voyager.Dataset.LoginDetails;
import com.app.voyager.Util.Constants;
import com.app.voyager.Util.Util;
import com.app.voyager.interfaces.MyInterface;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ASHISH on 11/4/2015.
 */
public class SocialLoginActivity extends ParentActivity implements MyInterface, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 400;
    CallbackManager callbackManager;
    ImageView tv_login_fb, tv_login_googleplus;
    ArrayList<String> FriendList = new ArrayList<>();
    LoginDetails loginDetails;
    private AccessToken accessToken;
    private Profile profile;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile1, Profile profilenew) {
                            Log.v("facebook - profile 1 ", profilenew.getFirstName());
                            mProfileTracker.stopTracking();
                            profile = profilenew;
                            CallProfilefetch(profile, loginResult);
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    profile = Profile.getCurrentProfile();
                    CallProfilefetch(profile, loginResult);
                    Log.v("facebook - profile 2 ", profile.getFirstName());
                }
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });
        oncreateGoogleplus();
//        Then you can later perform the actual login, such as in a custom button's OnClickListener:


        //  LoginButton loginButton = (LoginButton)   findViewById(R.id.usersettings_fragment_login_button);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });


    }

    public void CallProfilefetch(final Profile profile, LoginResult loginResult) {
        if (profile != null) {

            loginDetails = new LoginDetails();
            PrintMessage("FAcebook data getFirstName\n Name " + profile.getFirstName()
                            + "\ngetMiddleName  " + profile.getMiddleName()
                            + "\ngetLastName  " + profile.getLastName()
                            + "\ngetId  " + profile.getId()
                            + "\ngetName  " + profile.getName()
                            + "\ngetProfilePictureUri  " + profile.getProfilePictureUri(300, 300)

            );
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            PrintMessage("FAceb" + response.toString());

                            loginDetails.facebookId = profile.getId() + "";
                            loginDetails.fName = profile.getFirstName() + "";
                            loginDetails.typelogin = 1;
                            loginDetails.lName = profile.getLastName();
                            loginDetails.gender = "";
                            loginDetails.profileUrl = profile.getProfilePictureUri(300, 300) + "";
                            try {
                                if (object.optString("email") != null && !object.optString("email").equals("null"))
                                    loginDetails.emailId = object.optString("email").toString() + "";
                            } catch (Exception e) {

                            }
                            SynData(loginDetails);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
            getFriends();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        PrintMessage(requestCode + "");
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }

        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void GetHAshKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.app.voyager",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                System.out.println("something " + something);
                System.out.println("KeyHash:"
                        + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                // String something = new //
                // String(Base64.encodeBytes(md.digest()));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
    //GooglePlusLogin

    private void getFriends() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        GraphRequest.newMyFriendsRequest(accesstoken,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                        System.out.println("jsonArray: " + jsonArray);
                        System.out.println("GraphResponse: " + response);
                    }
                }).executeAsync();
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        tv_login_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkConnected(SocialLoginActivity.this))
                    LoginManager.getInstance().logInWithReadPermissions(SocialLoginActivity.this, Arrays.asList("email", "public_profile", "user_friends"));
                else
                    Util.ShowToast(SocialLoginActivity.this, getString(R.string.nointernetmessage));
            }
        });
        tv_login_googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    int isAvailable = GooglePlayServicesUtil
                            .isGooglePlayServicesAvailable(SocialLoginActivity.this);
                    if (isAvailable == ConnectionResult.SUCCESS) {
                        signInWithGplus();
                    } else if (isAvailable == ConnectionResult.SERVICE_MISSING
                            || isAvailable == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
                            || isAvailable == ConnectionResult.SERVICE_DISABLED) {
                        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
                                SocialLoginActivity.this, 1);
                        dialog.show();
                    } else {
                        Toast.makeText(SocialLoginActivity.this, "This device is not supported.",
                                Toast.LENGTH_LONG).show();
                    }
                }


            }
        });
    }

    /* A flag indicating that a PendingIntent is in progress and prevents
   * us from starting further intents.
   */

    @Override
    public void SetOnclicklistener() {

    }

    public void oncreateGoogleplus() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }


    public void SynData(LoginDetails loginDetails) {
        PrintMessage("loginDetails 0 " + loginDetails);
        Constants.loginDetails = loginDetails;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */
    public void signOutFromGplus() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }

    /**
     * Sign-out from Facebook
     */
    public void signoutfromfb() {
        LoginManager.getInstance().logOut();
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e("", "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(true);
                        }

                    });
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        switch (peopleData.getStatus().getStatusCode()) {
            case CommonStatusCodes.SUCCESS:

                PersonBuffer personBuffer = peopleData.getPersonBuffer();
                try {
                    int count = personBuffer.getCount();
                    System.out.println("###Call123 " + "count " + count);
                    for (int i = 0; i < count; i++) {
                        System.out.println("###Call123 " + personBuffer.get(i).getDisplayName());
                        FriendList.add(personBuffer.get(i).getDisplayName());
                    }
                } finally {

                    personBuffer.close();
                }

                break;

            case CommonStatusCodes.SIGN_IN_REQUIRED:
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                break;

            default:
                Log.e("TAG", "Error when listing people: " + peopleData.getStatus());
                break;
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(true);
    }


    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                loginDetails = new LoginDetails();
                loginDetails.googleId = currentPerson.getId() + "";
                loginDetails.fName = currentPerson.getDisplayName();
                loginDetails.typelogin = 2;
                for (int i = 0; i < (currentPerson.getDisplayName() + "").split(" ").length; i++) {
                    System.out.println("Call123 " + (currentPerson.getDisplayName() + "").split(" ")[i]);
                    if (i == 0)
                        loginDetails.fName = (currentPerson.getDisplayName() + "").split(" ")[i];
                    else
                        loginDetails.lName += (currentPerson.getDisplayName() + "").split(" ")[i] + " ";
                }

                loginDetails.gender = currentPerson.getGender() + "";
                loginDetails.profileUrl = currentPerson.getImage().getUrl();
                loginDetails.emailId = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);


                System.out.println("Call123 " + "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                SynData(loginDetails);
                Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                        .setResultCallback(this);
                //  new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

        } else {

        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


}
