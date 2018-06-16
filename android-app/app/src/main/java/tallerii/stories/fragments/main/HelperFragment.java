package tallerii.stories.fragments.main;

import android.app.ProgressDialog;
import android.content.Context;

public class HelperFragment {

    private ProgressDialog waitToResponse;
    private final Context context;

    public HelperFragment(Context context){

        this.context = context;

        waitToResponse = new ProgressDialog(context);
        waitToResponse.setCancelable(false); // disable dismiss by tapping outside of the dialog

    }

    public void showMessageLoading(String messageLoading){
        if(waitToResponse == null){
            waitToResponse.setTitle("Loading");
            waitToResponse.setMessage(messageLoading);
            waitToResponse.show();
        }
    }

    public void dismissMessageLoading(){
        if(waitToResponse != null && waitToResponse.isShowing()){
            waitToResponse.dismiss();
            waitToResponse = null;
        }
    }
}
