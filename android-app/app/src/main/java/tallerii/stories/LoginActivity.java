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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "tallerii.stories.loginactivity.MESSAGE";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private static final String FIELD_ID = "id";
    private static final String FIELD_EMAIL = "email";

    private Button submitButton;
    private LoginButton fbButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // adding event to submit button
        this.submitButton = (Button)findViewById(R.id.submitButton);
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        // adding event to login facebook button
        // a callback manager handle all response from init session
        this.callbackManager = CallbackManager.Factory.create();
        this.fbButton = (LoginButton) findViewById(R.id.facebookButton);
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
                    Toast.makeText(LoginActivity.this, R.string.login_fb_canceled_by_user, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(LoginActivity.this, R.string.login_fb_error + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                        /*
                         * TODO: check in the application server if the user id is registered
                         * if not => startRegistrationActivity()
                         *   -> recieve fields
                         *   -> ask for password and repeat
                         *   -> send POST request to registration
                         *   -> recieve JWT if all is OK!
                         * else => request JWT and startMainActivity
                         */
                        startMainActivity(fbEmail);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender");
        request.setParameters(parameters);
        request.executeAsync();

    }

    /** Called when the user taps the Submit button **/
    private void login(View view) {

        Intent intent = new Intent(this, MainActivity.class);

        EditText usernameText = (EditText) findViewById(R.id.usernameText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(username.equals("john") && password.equals("doe")){ // username & password correct

            this.startMainActivity(username);

        } else{

            Toast.makeText(getApplicationContext(), R.string.wrong_credentials, Toast.LENGTH_SHORT).show();

        }

    }

    protected void startMainActivity(String username) {

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();

    }
}
