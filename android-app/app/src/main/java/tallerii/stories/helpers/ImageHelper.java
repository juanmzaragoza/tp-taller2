package tallerii.stories.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import tallerii.stories.R;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.controller.ProfileUpdateController;

public class ImageHelper {

    private Context context;
    private StorageReference storageReference;

    public ImageHelper(Context context) {
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    public void setFirebaseImage(final String imageId, final ImageView imageView) {
        if (imageId != null && imageId.length() > 0) {
            final StorageReference imageRef = storageReference.child("media").child(imageId);
            imageRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    // Metadata now contains the metadata for 'images/forest.jpg'
                    if(storageMetadata.getContentType().contains("image")){
                        Glide.with(context).load(imageRef).into(imageView);
                        imageView.setVisibility(View.VISIBLE);
                    } else{
                        imageView.setVisibility(View.GONE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred
                    Log.i("FIREBASE","An error ocurrer on get "+imageId);
                    imageView.setVisibility(View.GONE);
                }
            });

        }
    }

    public void setFirebaseVideo(final String videoId, final VideoView videoView) {
        if (videoId != null && videoId.length() > 0) {
            final StorageReference videoRef = storageReference.child("media").child(videoId);
            videoRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    // Metadata now contains the metadata
                    if(storageMetadata.getContentType().contains("video")){
                        videoView.setVideoURI(videoRef.getDownloadUrl().getResult());
                        videoView.setVisibility(View.VISIBLE);
                    } else{
                        videoView.setVisibility(View.GONE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred
                    Log.i("FIREBASE","An error ocurred on get "+videoId);
                    videoView.setVisibility(View.GONE);
                }
            });

        }
    }

    public void uploadMedia(String mediaName, Uri filePath, final Runnable onSuccessFunction, final Runnable onErrorFunction) {

        final StoriesAppActivity activity = (StoriesAppActivity) context;
        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        Log.i("FIREBASE","Uploading to firebase: " + mediaName);

        StorageReference ref = storageReference.child("media").child(mediaName);

        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        activity.showMessage("Uploaded",5);
                        onSuccessFunction.run();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        activity.showMessage("Failed "+e.getMessage());
                        onErrorFunction.run();
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
