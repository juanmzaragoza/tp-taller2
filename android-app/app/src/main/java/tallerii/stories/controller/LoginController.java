package tallerii.stories.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.LoginActivity;
import tallerii.stories.activities.StoriesAppActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;

public class LoginController {
    StoriesAppActivity activity;

    public LoginController(StoriesAppActivity activity) {
        this.activity = activity;
    }

    // login with username and password
    public void login(final String username, String password) {
        signIn(username,password,0);
    }

    // login with fb auth token
    public void login(final String username, String password, long id) {
        signIn(username,password,id);
    }

    private void signIn(final String username, String password, final long id) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("username", username);
        parameters.addProperty("password", password);
        Call<JsonObject> responseCall = endpointsApi.postLogin(parameters);
        responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                // if username and password match
                if (response.isSuccessful() && response.code() == 201) {
                    /* Response
                     *  {
                     *      "metadata": {
                     *          "version": "v1"
                     *      },
                     *      "userId" : "5ae66a31d4ef925dac59a94b"
                     *      "token": {
                     *          "expiresAt": 3600,
                     *          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7fSwiaWF0IjoxNTI1MDQ5NjkxLCJleHAiOjE1MjUwNTMyOTF9.enwol4ZJS1qBgkaNmvxZkrewGBR053icNWho_f3gK0k"
                     *      }
                     *  }
                     */
                    JsonObject tokenObject = response.body().getAsJsonObject("token");
                    String token = tokenObject.get("token").getAsString();
                    JsonElement userId = tokenObject.get("id");
                    activity.startMainActivity(username, token, userId != null ? userId.getAsString() : "Error");
                    // if fb token is ok but user doesnt exists
                } else if(response.code() == 409){
                    activity.startRegistrationActivity(username, id);
                    // if error
                } else {
                    activity.logOutFromFB();
                    manageErrors(response);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                super.onFailure(call, t);
                activity.logOutFromFB();
            }
        });

    }
}
