package tallerii.stories;

import android.util.Log;

import com.google.gson.JsonObject;
import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.tallerii.stories.network.apimodels.User;

public class RegistrationController {
    RegistrationActivity activity;

    public RegistrationController(RegistrationActivity activity) {
        this.activity = activity;
    }

    /** call api rest and check if the user id exists **/
    public void register(final String username, final String password) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRegistrationEndpoint();
        Call<ResponseBody> responseCall = endpointsApi.postRegistration(username, password);

        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JsonObject postResponse = new JsonObject().get(response.body().toString()).getAsJsonObject();
                // TODO manage app server possible errors
                activity.showMessage("Registration successful\nWelcome to stories " + username + "!!");
                activity.startMainActivity(username);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", t.toString());
                activity.showMessage(t.toString());
            }
        });

    }
}
