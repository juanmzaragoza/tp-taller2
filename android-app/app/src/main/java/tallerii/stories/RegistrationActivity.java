package tallerii.stories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tallerii.stories.controller.RegistrationController;

public class RegistrationActivity extends StoriesAppActivity {
    public static final String USERNAME = "username";
    private final RegistrationController controller;

    @Override
    protected Context getContext() {
        return RegistrationActivity.this;
    }

    public RegistrationActivity() {
        this.controller= new RegistrationController(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(USERNAME)!= null) {
            TextView t = findViewById(R.id.usernameText);
            t.setText(bundle.getString(USERNAME));
        }
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
            controller.register(username, password);
        }
    }
}
