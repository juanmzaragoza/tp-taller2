package tallerii.stories.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.helpers.MyFirebaseInstanceIdService;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;

public class FCMNotificationController {

    //TODO make controller
    //Sends to partner, so that server can send to fcm. This is as stipulated in documentation for FCM
    public static void sendNotification(final StoriesAppActivity context, String userId, final String message) {
        DatabaseReference tokensRef = MyFirebaseInstanceIdService.getTokensRef();
        tokensRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
                JsonObject parameters = new JsonObject();
                parameters.addProperty("userFCMToken", dataSnapshot.getValue(String.class));
                parameters.addProperty("message", message);
                Call<JsonObject> responseCall = endpointsApi.sendFCMNotification(parameters);
                responseCall.enqueue(new DefaultCallback<JsonObject>(context) {
                    @Override
                    public void onResponse(Response<JsonObject> response) {
                        if(response.code() >= 400){
                            manageErrors(response);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO consider this case
            }
        });
    }
}
