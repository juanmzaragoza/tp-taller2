package tallerii.stories.fragments.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tallerii.stories.R;
import tallerii.stories.activities.StoriesAppActivity;
import tallerii.stories.helpers.BitmapHelper;
import tallerii.stories.interfaces.ShowStoriesAware;
import tallerii.stories.interfaces.StoriesAware;
import tallerii.stories.interfaces.ThumbnailCallback;

public class PostFlashStorieFragment extends PostStorieFragment implements StoriesAware, ShowStoriesAware, ThumbnailCallback {

    // Referer to https://github.com/ravi8x/AndroidPhotoFilters
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    /** Is called when the user taps the Submit button **/
    public PostFlashStorieFragment(){
        publish = new Runnable() {
            public void run() {

                locationText = rootView.findViewById(R.id.locationText);
                String location = locationText.getText().toString();

                if(PostFlashStorieFragment.super.fileUri != null){

                    if(!PostFlashStorieFragment.super.takeVideo && PostFlashStorieFragment.super.filteredBitmap != null){
                        PostFlashStorieFragment.super.fileUri = BitmapHelper.getImageUri(getContext(),PostFlashStorieFragment.super.filteredBitmap);
                    }
                    // fast == flash
                    controller.publishStorie(PostFlashStorieFragment.super.fileUri, "", "", true, location, "fast");

                } else{
                    StoriesAppActivity activity = (StoriesAppActivity) getActivity();
                    activity.showMessage("Please, select a  content media to post!", Toast.LENGTH_SHORT);
                }
            }
        };
    }

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_post_flash_storie, container, false);
    }

    @Override
    public void bindVideoViewAction(){

    }

    @Override
    public void bindToggleMediaAction(){
    }

    @Override
    public void updateViewOnChangeMediaType(){

    }
}

