package tallerii.stories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AuthorizationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        /* TODO:  when the token is stored
        // go straight to main if a token is stored
        if (Util.getToken() != null) {*/
        if(false){
            activityIntent = new Intent(this, MainActivity.class);
        } else {
            // else not logged in, show login form
            activityIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}
