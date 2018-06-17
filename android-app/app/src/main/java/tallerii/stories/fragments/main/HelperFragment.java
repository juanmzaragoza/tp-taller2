package tallerii.stories.fragments.main;

import android.app.ProgressDialog;
import android.content.Context;

public class HelperFragment {

    private ProgressDialog waitToResponse;
    private final Context context;

    public HelperFragment(Context context){
        this.context = context;
    }

    public void showMessageLoading(String messageLoading){
        if(waitToResponse == null){
            waitToResponse = new ProgressDialog(context);
            waitToResponse.setCancelable(true); // disable dismiss by tapping outside of the dialog
            waitToResponse.setTitle("Loading");
            waitToResponse.setMessage(messageLoading);
            waitToResponse.show();
        }
    }

    public void dismissMessageLoading(){
        if(waitToResponse != null){
            waitToResponse.dismiss();
            waitToResponse = null;
        }
    }
}
