package tallerii.stories.fragments.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.activities.StoriesAppActivity;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.helpers.BitmapHelper;
import tallerii.stories.helpers.LocationHelper;
import tallerii.stories.helpers.MediaFile;
import tallerii.stories.helpers.ThumbnailItem;
import tallerii.stories.helpers.ThumbnailsAdapter;
import tallerii.stories.helpers.ThumbnailsManager;
import tallerii.stories.interfaces.StoriesAware;
import tallerii.stories.interfaces.ThumbnailCallback;
import tallerii.stories.network.apimodels.Storie;

import static android.app.Activity.RESULT_OK;

public class PostFlashStorieFragment extends PostStorieFragment implements StoriesAware, ThumbnailCallback {

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
}

