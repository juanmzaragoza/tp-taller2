package tallerii.stories.fragments.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import tallerii.stories.LoginActivity;
import tallerii.stories.MainActivity;
import tallerii.stories.R;

import static android.app.Activity.RESULT_OK;

public class PostStorieFragment extends Fragment {

    private View rootView;
    private Bitmap pictureBitmap;
    private ImageView imageView;
    private View choosePictureButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get root view and then access to objects like R.id.usernameView
        rootView = inflater.inflate(R.layout.fragment_post_storie, container, false);

        return rootView;
    }

    // from camera
    public void takePhoto() {
        // check for permissions
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( PostStorieFragment.this.getContext(), android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( getActivity(), new String[] {  Manifest.permission.CAMERA  }, 0 );
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
    }

    // from gallery
    public void chooseImage() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent , 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    pictureBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    break;
                case 1:
                    Uri filePath = imageReturnedIntent.getData();
                    try {
                        pictureBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        pictureBitmap = null;
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.showMessage("Application Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                    }
                    break;
            }
            imageView.setImageBitmap(null);
            imageView.setImageBitmap(pictureBitmap);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // take picture from camera
        imageView = rootView.findViewById(R.id.storieImageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePhoto();
            }
        });

        // choose picture from library
        choosePictureButton = rootView.findViewById(R.id.choosePictureButton);
        choosePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
}

