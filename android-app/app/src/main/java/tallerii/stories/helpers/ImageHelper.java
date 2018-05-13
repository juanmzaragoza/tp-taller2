package tallerii.stories.helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import tallerii.stories.R;

public class ImageHelper {

    private Context context;
    private StorageReference storageReference;

    public ImageHelper(Context context) {
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    public void setFirebaseImage(String imageId, ImageView imageView) {
        Glide.with(context).using(new FirebaseImageLoader())
                .load(storageReference.child("images/" + imageId))
                .error(R.drawable.ic_account_circle_white_24dp).dontAnimate().fitCenter()
                .into(imageView)
        ;
    }
}
