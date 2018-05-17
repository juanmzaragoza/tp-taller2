package tallerii.stories.helpers;

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
        this.storageReference = storage.getReference().child("images");
    }

    public void setFirebaseImage(String imageId, ImageView imageView) {
        if (imageId != null && imageId.length() > 0) {
            StorageReference imageRef = storageReference.child(imageId);
            Glide.with(context).using(new FirebaseImageLoader())
                    .load(imageRef)
                    .error(R.drawable.ic_account_circle_white_24dp)
                    .dontAnimate().fitCenter()
                    .into(imageView)
            ;
        }
    }
}
