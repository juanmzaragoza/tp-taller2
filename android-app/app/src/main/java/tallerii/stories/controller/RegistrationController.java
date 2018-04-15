package tallerii.stories.controller;

import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.RegistrationActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.RegistrationResult;

public class RegistrationController {
    private RegistrationActivity activity;

    public RegistrationController(RegistrationActivity activity) {
        this.activity = activity;
    }

    /** call api rest and check if the user id exists **/
    public void register(final String username, final String password) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("username", username);
        parameters.addProperty("password", password);
        Call<RegistrationResult> responseCall = endpointsApi.postRegistration(parameters);

        responseCall.enqueue(new Callback<RegistrationResult>() {
            @Override
            public void onResponse(Call<RegistrationResult> call, Response<RegistrationResult> response) {
                RegistrationResult registrationResult = response.body();
                if (registrationResult != null) {
                    activity.showMessage("Registration successful\nWelcome to stories " + registrationResult.getUsername() + "!!");
                    activity.startMainActivity(username);
                }
                // TODO manage app server possible errors and etc
            }

            @Override
            public void onFailure(Call<RegistrationResult> call, Throwable t) {
                Log.e("Error", t.toString());
                activity.showMessage(t.toString());
            }
        });

    }
}
