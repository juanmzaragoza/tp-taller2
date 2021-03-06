package tallerii.stories.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import tallerii.stories.R;
import tallerii.stories.controller.LoginController;

public class LoginActivity extends StoriesAppActivity {

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String FIELD_ID = "id";
    private static final String FIELD_EMAIL = "email";
    private final LoginController controller;

    private LoginButton fbButton;
    private CallbackManager callbackManager;

    public LoginActivity() {
        this.controller = new LoginController(this);
    }

    @Override
    protected Context getContext() {
        return LoginActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // adding event to login facebook button
        // a callback manager handle all response from init session
        this.callbackManager = CallbackManager.Factory.create();
        this.fbButton = findViewById(R.id.facebookButton);
        // set what we want to read
        this.fbButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        // what we do when response comes back
        this.fbCallBackRegister();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /** Register the main call back **/
    private void fbCallBackRegister() {
        this.fbButton.registerCallback(this.callbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //we use this to get user email as well as the rest of the info also included using the profile tracker
                    getUserProfileWith(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                     showMessage(R.string.login_fb_canceled_by_user);
                }

                @Override
                public void onError(FacebookException exception) {
                    showMessage(getApplicationContext().getResources().getString(R.string.login_fb_error) +" "+ exception.getLocalizedMessage(), Toast.LENGTH_SHORT);
                }
            });
    }

    /** get user profile using GraphRequest **/
    private void getUserProfileWith(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        long fbId = object.getLong(FIELD_ID);
                        String fbEmail = object.getString(FIELD_EMAIL);
                        String fbAccessToken = "fb " + AccessToken.getCurrentAccessToken().getToken();
                        //controller.checkFBUserExists(fbId, fbEmail);
                        // the server check if the token is valid for the user fbEmail
                        controller.login(fbEmail, fbAccessToken, fbId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showMessage("Application Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void logOutFromFB() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }

    /** Called when the user taps the Submit button **/
    public void login(View view) {
        String username = getStringFrom(R.id.usernameText);
        String password = getStringFrom(R.id.passwordText);
        controller.login(username, password);
    }
}
