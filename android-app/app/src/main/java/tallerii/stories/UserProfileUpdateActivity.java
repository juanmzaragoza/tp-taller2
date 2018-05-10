package tallerii.stories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.controller.ProfileUpdateController;
import tallerii.stories.network.apimodels.ApplicationProfile;

public class UserProfileUpdateActivity extends ProfileActivity {

    private Uri filePath;
    private EditText firstName;
    private EditText lastName;

    @Override
    protected void initWithChildResource() {
        setContentView(R.layout.activity_user_update_profile);
        imageView = findViewById(R.id.profile_picture);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        setTitle("Update Profile");
    }

    @Override
    protected void setUserName(ApplicationProfile applicationProfile) {
        firstName.setText(applicationProfile.getName());
        lastName.setText(applicationProfile.getLastName());
    }


    public void chooseImage(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    public void takePhoto(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                }
                break;
        }
    }

    private void uploadImage(String imageName) {
        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Log.i("tag?","uploadaing to firebase");
            StorageReference ref = storageReference.child("images/" + imageName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            showMessage("Uploaded");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showMessage("Failed "+e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected Context getContext() {
        return UserProfileUpdateActivity.this;
    }

    @Override
    protected ProfileController getController() {
        return new ProfileUpdateController(this);
    }

    public void upload(View view) {
        ApplicationProfile applicationProfile = getProfile();
        applicationProfile.setFirstName(getStringFrom(R.id.first_name));
        applicationProfile.setLastName(getStringFrom(R.id.last_name));
        applicationProfile.setProfilePicture(UUID.randomUUID().toString());

        uploadImage(applicationProfile.getProfilePicture());

        ((ProfileUpdateController)controller).putApplicationProfile(applicationProfile);
    }
}
