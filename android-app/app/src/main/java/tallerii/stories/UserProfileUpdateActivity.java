package tallerii.stories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
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
    }

    @Override
    protected void setUserName(ApplicationProfile applicationProfile) {
        firstName.setText(applicationProfile.getName());
        lastName.setText(applicationProfile.getLastName());
    }


    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String imageName) {
        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + imageName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileUpdateActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileUpdateActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        applicationProfile.setFirstName(getStringFrom(R.id.first_name));
        applicationProfile.setLastName(getStringFrom(R.id.last_name));
        applicationProfile.setProfilePicture(UUID.randomUUID().toString());

        uploadImage(applicationProfile.getProfilePicture());

        ((ProfileUpdateController)controller).putApplicationProfile(applicationProfile);
    }
}
