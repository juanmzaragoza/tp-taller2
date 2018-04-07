package tallerii.stories.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tallerii.stories.network.tallerii.stories.network.apimodels.User;

public interface EndpointsApplicationApiRest {

    @GET(ConstantsApplicationApiRest.URL_GET_USER_BY_ID)
    Call<User> getUserById(@Query("id") long user_id);

}
