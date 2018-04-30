package tallerii.stories.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tallerii.stories.network.apimodels.ApplicationProfile;
import tallerii.stories.network.apimodels.LoginResult;
import tallerii.stories.network.apimodels.RegistrationResult;
import tallerii.stories.network.apimodels.User;

public interface EndpointsApplicationApiRest {

    @GET(ConstantsApplicationApiRest.URL_GET_USER_BY_ID)
    Call<User> getUserById(@Query("id") long user_id);

    @Headers({"Accept:application/json"})
    @POST(ConstantsApplicationApiRest.POST_REGISTRATION)
    Call<JsonObject> postRegistration(@Body JsonObject parameters);

    @Headers({"Accept:application/json"})
    @POST(ConstantsApplicationApiRest.POST_LOGIN)
    Call<JsonObject> postLogin(@Body JsonObject parameters);

    @GET(ConstantsApplicationApiRest.PROFILE_ENDPOINT)
    Call<ApplicationProfile> getProfileById(@Path("id") String user_id);

    @Headers({"Accept:application/json"})
    @PUT(ConstantsApplicationApiRest.PROFILE_ENDPOINT)
    Call<JsonObject> putProfileById(@Path("id") String user_id, @Body JsonObject profile);
}
