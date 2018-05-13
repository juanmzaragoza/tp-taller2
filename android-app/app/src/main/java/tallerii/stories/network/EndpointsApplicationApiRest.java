package tallerii.stories.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EndpointsApplicationApiRest {
    @Headers({"Content-Type:application/json"})
    @POST(ConstantsApplicationApiRest.POST_REGISTRATION)
    Call<JsonObject> postRegistration(@Body JsonObject parameters);

    @Headers({"Content-Type:application/json"})
    @POST(ConstantsApplicationApiRest.POST_LOGIN)
    Call<JsonObject> postLogin(@Body JsonObject parameters);

    @Headers({"Content-Type:application/json"})
    @GET(ConstantsApplicationApiRest.PROFILE_ENDPOINT)
    Call<JsonObject> getProfileById(@Path("id") String user_id);

    @Headers({"Content-Type:application/json"})
    @PUT(ConstantsApplicationApiRest.PROFILE_ENDPOINT)
    Call<JsonObject> putProfileById(@Path("id") String user_id, @Body JsonObject profile);
}
