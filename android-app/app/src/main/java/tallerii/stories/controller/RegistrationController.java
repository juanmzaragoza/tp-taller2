package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.activities.LoginActivity;
import tallerii.stories.activities.RegistrationActivity;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;

public class RegistrationController {
    private RegistrationActivity activity;

    public RegistrationController(RegistrationActivity activity) {
        this.activity = activity;
    }

    /**call api rest and register the user**/
    public void register(final long id, final String username, final String password) {
        EndpointsApplicationApiRest endpointsApi = AdapterApplicationApiRest.getRawEndpoint();
        JsonObject parameters = new JsonObject();
        parameters.addProperty("id", id);
        parameters.addProperty("username", username);
        parameters.addProperty("password", password);
        Call<JsonObject> responseCall = endpointsApi.postRegistration(parameters);

        responseCall.enqueue(new DefaultCallback<JsonObject>(activity) {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        LoginController controller = new LoginController(activity);
                        controller.login(username, password);

                        activity.showMessage("Registration successful\nWelcome to stories!!");

                    }
                } else {
                    manageErrors(response);
                }
            }
        });

    }
}
