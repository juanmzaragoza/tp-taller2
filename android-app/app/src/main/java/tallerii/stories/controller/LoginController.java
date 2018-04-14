package tallerii.stories.controller;

import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
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
                    //TODO actually use resource
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

    public void login(String username, String password) {

    }
}
