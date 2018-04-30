package tallerii.stories;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import tallerii.stories.helpers.Store;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "tallerii.stories.loginactivity.MESSAGE";
    public static final String TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        if(intent.getStringExtra(TOKEN) != null) {
            final Store store = new Store();
            store.save("token", intent.getStringExtra(TOKEN));
        }

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.usernameView);
        textView.setText(message);

        // get bottom navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // on click one, change content
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return switchContent(item);
                }
            });
    }

    private boolean switchContent(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // change content
            case R.id.action_account:
                // change content
            case R.id.action_new:
                // change content
        }
        return true;
    }


}
