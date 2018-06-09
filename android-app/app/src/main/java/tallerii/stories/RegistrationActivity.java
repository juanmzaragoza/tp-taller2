package tallerii.stories;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import tallerii.stories.controller.RegistrationController;

public class RegistrationActivity extends StoriesAppActivity {

    public static final String USERNAME = "username";
    public static final String FBID = "id";

    private final RegistrationController controller;

    private long id;

    public RegistrationActivity() {
        this.controller = new RegistrationController(this);
    }

    @Override
    protected Context getContext() {
        return RegistrationActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Bundle bundle = getIntent().getExtras();
        setTitle("Register");
        if (bundle != null && bundle.getString(USERNAME) != null) {
            TextView t = findViewById(R.id.usernameText);
            t.setText(bundle.getString(USERNAME));
        }
        this.id = bundle.getLong(FBID);
    }

    public void register(View v) {
        String username = getStringFrom(R.id.usernameText);
        String password = getStringFrom(R.id.passwordText);
        String confirmPassword = getStringFrom(R.id.confirmPasswordText);
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showMessage("Please complete empty fields");
        } else if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match");
        } else {
            controller.register(this.id, username, password);
        }
    }
}
