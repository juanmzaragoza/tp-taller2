package tallerii.stories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // adding event to login facebook button
        // a callback manager handle all response from init session
        this.callbackManager = CallbackManager.Factory.create();
        this.fbButton = findViewById(R.id.facebookButton);
        // set what we want to read
        this.fbButton.setReadPermissions(Arrays.asList(EMAIL,PUBLIC_PROFILE));
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
    private void getUserProfileWith(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        long fbId = object.getLong(FIELD_ID);
                        String fbEmail = object.getString(FIELD_EMAIL);
                        String fbAccessToken = AccessToken.getCurrentAccessToken().getToken();
                        controller.checkFBUserExists(fbId, fbEmail);
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

    public void showMessage(int resId) {
        Toast.makeText(LoginActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String text, int length) {
        Toast.makeText(LoginActivity.this, text, length).show();
    }

    public void logOutFromFB() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }

    /** Called when the user taps the Submit button **/
    public void login(View view) {

        EditText usernameText = findViewById(R.id.usernameText);
        EditText passwordText = findViewById(R.id.passwordText);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        //TODO delegate to testable class that manages login logic
        if(username.equals("john") && password.equals("doe")){ // username & password correct
            this.startMainActivity(username);
        } else{
            Toast.makeText(getApplicationContext(), R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
        }

    }

    //TODO extract activity change to common method for all activities
    // I imagine a parent class with constants of all available activities,
    // and a method to get activities that the specific class should access, to check if it should
    // actually switch to them. Then for parameters we just use a map, and each activity that
    // receives parameters has constants for the keys that it can/should receive.
    // Such a class would be the one accessed by the 'controllers' that i made up,
    // and it would have as public the methods used by them, and protected the other common methods.
    protected void startMainActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }

    protected void startRegistrationActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }
}
