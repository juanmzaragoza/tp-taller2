package tallerii.stories.helpers;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Store {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    Store() {
        pref = getApplicationContext().getSharedPreferences("stories_store", MODE_PRIVATE); // 0 - for private mode
        editor = pref.edit();
    }

    public static void save(String key, String value){
        editor.putString(key, value);
        editor.commit(); // commit changes
    }

    public static String get(String key){
        return pref.getString(key, null);
    }

    public static void remove(String key){
        editor.remove(key);
        editor.commit();
    }
}
