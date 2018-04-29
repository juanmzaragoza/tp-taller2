package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.LoginActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;

public class LoginController {
    LoginActivity activity;

    public LoginController(LoginActivity activity) {
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
                    activity.startMainActivity(username);
                    // if fb token is ok but user doesnt exists
                } else if(response.code() == 409){
                    activity.startRegistrationActivity(username, id);
                    // if error
                } else {
                    activity.logOutFromFB();
                    manageErrors(response);
                }
            }
        });

    }
}
