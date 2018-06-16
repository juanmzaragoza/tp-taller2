package tallerii.stories.helpers;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}