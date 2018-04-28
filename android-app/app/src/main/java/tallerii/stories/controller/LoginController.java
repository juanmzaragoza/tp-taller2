package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.LoginActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.User;

public class LoginController {
    LoginActivity activity;

    public LoginController(LoginActivity activity) {
        this.activity = activity;
    }

    /** call api rest and check if the user id exists**/
    public void checkFBUserExists(long fbId, final String fbUsername) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getUserEndpoint();
        Call<User> responseCall = endpointsApi.getUserById(fbId);

        responseCall.enqueue(new DefaultCallback<User>(activity) {
            @Override
            public void onResponse(Response<User> response) {
                int code = response.code();
                if (code != 404) {
                    //TODO change here to actually login as fb instead of error
                    activity.logOutFromFB();
                    // show error message
                    //TODO find how to use resources or config in here
                    activity.showMessage(response.body().getUsername() + " already was created! Please, login with username and password");
                } else {
                    activity.startRegistrationActivity(fbUsername);
                }
            }
        });
    }

    public void login(final String username, String password) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("username", username);
        parameters.addProperty("password", password);
        Call<JsonObject> responseCall = endpointsApi.postLogin(parameters);
        responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    activity.startMainActivity(username);
                } else {
                    activity.logOutFromFB();
                    manageErrors(response);
                }
            }
        });

    }
}
