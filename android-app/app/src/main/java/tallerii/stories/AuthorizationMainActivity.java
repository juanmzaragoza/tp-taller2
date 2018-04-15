package tallerii.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

/*
 * -----------------
 * Troubleshooting
 * -----------------
 * To fix The key hash … does not match any stored key hashes
 * See https://stackoverflow.com/questions/29554338/the-key-hash-does-not-match-any-stored-key-hashes
 *
 */
public class AuthorizationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        /* TODO:  when the token is stored
        // go straight to main if a token is stored
        if (Util.getTokenx() != null) {*/

        if (false) {
            activityIntent = new Intent(this, MainActivity.class);
        } else {// else not logged in
            // clean fb access token because we handle the access to the application

            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
            //show login form
            activityIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }

}
