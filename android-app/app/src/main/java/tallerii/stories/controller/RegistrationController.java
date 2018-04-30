package tallerii.stories.controller;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import tallerii.stories.RegistrationActivity;
import tallerii.stories.helpers.Store;
import tallerii.stories.network.AdapterApplicationApiRest;
import tallerii.stories.network.EndpointsApplicationApiRest;
import tallerii.stories.network.apimodels.RegistrationResult;

public class RegistrationController {
    private RegistrationActivity activity;

    public RegistrationController(RegistrationActivity activity) {
        this.activity = activity;
    }

    /**call api rest and register the user**/
    public void register(final long id, final String username, final String password) {
        final Store store = new Store();
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
                        store.save("token",response.body().getAsJsonObject("token").get("token").getAsString());
                        activity.showMessage("Registration successful\nWelcome to stories!!");
                        activity.startMainActivity(username);
                    }
                } else {
                    manageErrors(response);
                }
            }
        });

    }
}
