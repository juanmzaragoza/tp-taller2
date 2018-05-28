package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.controller.DefaultCallback;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;

public class FCMNotificationController {
    //TODO make controller
    //Sends to partner, so that server can send to fcm. This is as stipulated in documentation for FCM
    public static void sendNotification(final StoriesAppActivity context, String topic, String title) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("to", "topic/" + topic);
        parameters.addProperty("message", title);
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
}
