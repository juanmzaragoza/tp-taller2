package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
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

    /**call api rest and register the user**/
    public void register(final String username, final String password) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("username", username);
        parameters.addProperty("password", password);
        Call<RegistrationResult> responseCall = endpointsApi.postRegistration(parameters);

        responseCall.enqueue(new DefaultCallback<RegistrationResult>(activity) {
            @Override
            public void onResponse(Response<RegistrationResult> response) {
                if (response.isSuccessful()) {
                    RegistrationResult registrationResult = response.body();
                    if (registrationResult != null) {
                        activity.showMessage("Registration successful\nWelcome to stories " + registrationResult.getUsername() + "!!");
                        activity.startMainActivity(username);
                    }
                } else {
                    manageErrors(response);
                }
            }
        });

    }
}
