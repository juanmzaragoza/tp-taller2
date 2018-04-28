package tallerii.stories;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

public abstract class StoriesAppActivity extends AppCompatActivity {
    abstract protected Context getContext();

    protected String getStringFrom(int resourceId) {
        EditText editText = findViewById(resourceId);
        return editText.getText().toString();
    }

    public void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String text, int length) {
        Toast.makeText(getContext(), text, length).show();
    }

    public void startMainActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }

    public void startRegistrationActivity(String username) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra(RegistrationActivity.USERNAME, username);
        startActivity(intent);
        finish();
    }
}
