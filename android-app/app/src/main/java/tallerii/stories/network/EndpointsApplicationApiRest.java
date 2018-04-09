package tallerii.stories.network;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tallerii.stories.network.tallerii.stories.network.apimodels.User;

public interface EndpointsApplicationApiRest {

    @GET(ConstantsApplicationApiRest.URL_GET_USER_BY_ID)
    Call<User> getUserById(@Query("id") long user_id);

    @FormUrlEncoded
    @POST(ConstantsApplicationApiRest.POST_REGISTRATION)
    Call<ResponseBody> postRegistration(@Field("user")String username,
                                        @Field("password")String password);
}
