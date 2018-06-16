package tallerii.stories.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import tallerii.stories.R;
import tallerii.stories.activities.StoriesAppActivity;

public class ImageHelper {

    private Context context;
    private StorageReference storageReference;

    public ImageHelper(Context context) {
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    public void setFirebaseImage(String imageId, ImageView imageView) {
        if (imageId != null && imageId.length() > 0) {
            StorageReference imageRef = storageReference.child("media").child(imageId);
            GlideApp
                .with(context)
                .load(imageRef)
                .placeholder(R.drawable.ic_account_circle_white_24dp)
                .into(imageView)
            ;
        } else {
            imageView.setImageResource(R.drawable.ic_account_circle_white_24dp);
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
