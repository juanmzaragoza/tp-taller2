package tallerii.stories.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tallerii.stories.network.apimodels.LoginResult;
import tallerii.stories.network.apimodels.RegistrationResult;
import tallerii.stories.network.apimodels.User;

public interface EndpointsApplicationApiRest {

    @GET(ConstantsApplicationApiRest.URL_GET_USER_BY_ID)
    Call<User> getUserById(@Query("id") long user_id);

    @Headers({"Accept:application/json"})
    @POST(ConstantsApplicationApiRest.POST_REGISTRATION)
    Call<RegistrationResult> postRegistration(@Body JsonObject parameters);

    @Headers({"Accept:application/json"})
    @POST(ConstantsApplicationApiRest.POST_LOGIN)
    Call<JsonObject> postLogin(@Body JsonObject parameters);
}
