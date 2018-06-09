package tallerii.stories.fragments.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import tallerii.stories.R;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.helpers.LocationHelper;
import tallerii.stories.helpers.MediaFile;

import static android.app.Activity.RESULT_OK;

public class PostStorieFragment extends Fragment {

    private final static int REQUEST_CODE_TAKE_IMAGE = 0;
    private final static int REQUEST_CODE_CHOOSE_IMAGE = 1;
    private final static int REQUEST_CODE_TAKE_VIDEO = 2;
    private final static int REQUEST_CODE_CHOOSE_VIDEO = 3;

    private StoriesController controller;

    private View rootView;
    private ImageView imageView;
    private VideoView videoView;

    private View choosePictureButton;
    private View publishButton;

    private TextView locationText;
    private CheckBox visibilityCheckBox;
    private EditText titleText;
    private EditText descriptionText;
    private Switch togleTakeMedia;

    // Referer to https://github.com/ravi8x/AndroidPhotoFilters
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private boolean takeVideo = false;
    private Uri fileUri;

    Runnable takeMedia = new Runnable() { // from camera
        public void run() {
            // check for permissions
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(PostStorieFragment.this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }

            Intent takeMediaIntent;
            int requestCode;
            MediaFile mediaFile = new MediaFile(PostStorieFragment.this.getContext());
            if(takeVideo){ //record a video
                takeMediaIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                requestCode = REQUEST_CODE_TAKE_VIDEO;

                // create a file to save the image
                // TODO: commented because doesn't work
                //fileUri = mediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_VIDEO);

            } else{ // take photo
                takeMediaIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                requestCode = REQUEST_CODE_TAKE_IMAGE;

                // create a file to save the image
                // TODO: commented because doesn't work
                //fileUri = mediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_IMAGE);
            }

            // TODO: commented because doesn't work
            //takeMediaIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the media file name
            startActivityForResult(takeMediaIntent, requestCode);
        }
    };

    Runnable chooseMedia = new Runnable() { // from gallery
        public void run() {

            Intent pickMediaIntent;
            int requestCode;

            if (takeVideo) { //choose a video
                pickMediaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                requestCode = REQUEST_CODE_CHOOSE_VIDEO;
            } else {
                pickMediaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                requestCode = REQUEST_CODE_CHOOSE_IMAGE;
            }

            startActivityForResult(pickMediaIntent, requestCode);
        }
    };

    /** Is called when the user taps the Submit button **/
    Runnable publish = new Runnable() {
        public void run() {

            titleText = (EditText) rootView.findViewById(R.id.titleText);
            String title = titleText.getText().toString();

            descriptionText = (EditText) rootView.findViewById(R.id.descriptionText);
            String description = descriptionText.getText().toString();

            visibilityCheckBox = (CheckBox) rootView.findViewById(R.id.visibilityCheckBox);
            boolean isPublic = visibilityCheckBox.isChecked();

            locationText = rootView.findViewById(R.id.locationText);
            String location = locationText.getText().toString();

            if(fileUri != null){
                controller.publishStorie(fileUri, title, description, isPublic, location, "normal");
            } else{
                StoriesAppActivity activity = (StoriesAppActivity) getActivity();
                activity.showMessage("Please, choose a image to post!", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        controller = new StoriesController(this);

        // get root view and then access to objects like R.id.usernameView
        rootView = inflater.inflate(R.layout.fragment_post_storie, container, false);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent mediaReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, mediaReturnedIntent);
        if (resultCode == RESULT_OK) {
            fileUri = mediaReturnedIntent.getData();
            switch (requestCode) {
                case REQUEST_CODE_TAKE_IMAGE:
                case REQUEST_CODE_CHOOSE_IMAGE:
                    imageView.setImageURI(null);
                    imageView.setImageURI(fileUri);
                    break;
                case REQUEST_CODE_TAKE_VIDEO:
                case REQUEST_CODE_CHOOSE_VIDEO:
                    videoView.setVideoURI(fileUri);
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                        }
                    });
                    videoView.start();
                    break;
            }

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // set events
        bindImageViewAction();
        bindVideoViewAction();

        // choose a photo or a video
        togleTakeMedia = rootView.findViewById(R.id.toogleTakeMedia);
        togleTakeMedia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked will be true if the switch is in the On position
                takeVideo = isChecked;

                updateViewOnChangeMediaType();

            }
        });

        updateViewOnChangeMediaType();

        // choose picture from library
        choosePictureButton = executeActionOnClickBy(R.id.choosePictureButton,chooseMedia);

        // get location from network
        LocationHelper locationHelper = new LocationHelper(getActivity(), PostStorieFragment.this.getContext());
        locationHelper.getLocation();
        locationText = rootView.findViewById(R.id.locationText);
        locationText.setText("("+locationHelper.getLatitude()+","+locationHelper.getLongitude()+")");

        // publish button
        publishButton = executeActionOnClickBy(R.id.publishButton, publish);

    }

    private void updateViewOnChangeMediaType(){

        if(takeVideo){
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            togleTakeMedia.setText("Video");
        } else{
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            togleTakeMedia.setText("Photo");
        }

    }

    private void bindVideoViewAction(){
        // take video from camera
        videoView = (VideoView) executeActionOnTouchToBy(R.id.storieVideoView,takeMedia);
    }

    private void bindImageViewAction(){
        // take picture from camera
        imageView = (ImageView) executeActionOnClickBy(R.id.storieImageView,takeMedia);
    }

    public View executeActionOnClickBy(int id, final Runnable func){
        View view = rootView.findViewById(id);
        if(!view.hasOnClickListeners()){
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    func.run();
                }
            });
        }
        return view;
    }

    public View executeActionOnTouchToBy(int id, final Runnable func){
        View view = rootView.findViewById(id);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                func.run();
                return false;
            }

        });
        return view;
    }
}

