package tallerii.stories.controller;

import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.LoginActivity;
import tallerii.stories.R;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.LoginResult;
import tallerii.stories.network.apimodels.ServerError;
import tallerii.stories.network.apimodels.User;

public class LoginController {
    LoginActivity activity;
    Gson gson = new Gson();

    public LoginController(LoginActivity activity) {
        this.activity = activity;
    }

    /** call api rest and check if the user id exists **/
    public void checkFBUserExists(long fbId, final String fbUsername) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getUserEndpoint();
        Call<User> responseCall = endpointsApi.getUserById(fbId);

        responseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (code != 404) {
                    //TODO change here to actually login as fb instead of error
                    activity.logOutFromFB();
                    // show error message
                    //TODO find how to use resources or config in here
                    activity.showMessage(response.body().getUsername() + " already was created! Please, login with username and password");
                } else{
                    activity.startRegistrationActivity(fbUsername);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", t.toString());
                activity.showMessage("Error on getUserById() " + t.toString(), Toast.LENGTH_LONG);
            }
        });

    }

    public void login(final String username, String password) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("username", username);
        parameters.addProperty("password", password);
        Call<JsonObject> responseCall = endpointsApi.postLogin(parameters);
        responseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    activity.startMainActivity(username);
                } else{
                    activity.logOutFromFB();
                    assert response.errorBody() != null;
                    try {
                        ServerError error = gson.fromJson(response.errorBody().string(), ServerError.class);
                        activity.showMessage(error.getMessage(), Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        activity.showMessage(e.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Error", t.toString());
                activity.showMessage("Error on getUserById() " + t.toString(), Toast.LENGTH_LONG);
            }
        });

    }
}
