package tallerii.stories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "tallerii.stories.mainactivity.MESSAGE";

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // adding event to submit button
        submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
    }

    /** Called when the user taps the Submit button */
    private void login(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);

        EditText usernameText = (EditText) findViewById(R.id.usernameText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(username.equals("john") && password.equals("doe")){ // username & password correct

            intent.putExtra(EXTRA_MESSAGE, username);
            startActivity(intent);

        } else{

            Toast.makeText(getApplicationContext(), R.string.wrong_credentials, Toast.LENGTH_SHORT).show();

        }


    }
}
