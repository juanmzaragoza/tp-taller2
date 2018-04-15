package tallerii.stories.controller;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tallerii.stories.RegistrationActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.RegistrationResult;
import tallerii.stories.network.apimodels.ServerError;

public class RegistrationController {
    private RegistrationActivity activity;
    private Gson gson = new Gson();

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
                if (response.isSuccessful()) {
                    RegistrationResult registrationResult = response.body();
                    if (registrationResult != null) {
                        activity.showMessage("Registration successful\nWelcome to stories " + registrationResult.getUsername() + "!!");
                        activity.startMainActivity(username);
                    }
                } else {
                    assert response.errorBody() != null;
                    try {
                        ServerError error = gson.fromJson(response.errorBody().string(), ServerError.class);
                        activity.showMessage(error.getMessage());
                    } catch (Exception e) {
                        activity.showMessage(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResult> call, Throwable t) {
                Log.e("Error", t.toString());
                activity.showMessage(t.toString());
            }
        });

    }
}
