package tallerii.stories.network;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tallerii.stories.network.apimodels.Comment;
import tallerii.stories.network.apimodels.FriendRequest;
import tallerii.stories.network.apimodels.Storie;

public interface EndpointsApplicationApiRest {

     String CONTENT_TYPE_APPLICATION_JSON = "Content-Type:application/json";
    String ACCEPT_APPLICATION_JSON = "Accept:application/json";

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.POST_REGISTRATION)
    Call<JsonObject> postRegistration(@Body JsonObject parameters);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.POST_LOGIN)
    Call<JsonObject> postLogin(@Body JsonObject parameters);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.FCM_SEND)
    Call<JsonObject> sendFCMNotification(@Body JsonObject notification);

    @Headers({ACCEPT_APPLICATION_JSON})
    @GET(ConstantsApplicationApiRest.USERS)
    Call<JsonObject> getUsers(@Path("id")String userId);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @GET(ConstantsApplicationApiRest.PROFILE_ENDPOINT)
    Call<JsonObject> getProfileById(@Path("id") String user_id);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @PUT(ConstantsApplicationApiRest.PROFILE_ENDPOINT)
    Call<JsonObject> putProfileById(@Path("id") String user_id, @Body JsonObject profile);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.BEFRIEND)
    Call<JsonObject> postFriendRequest(@Body FriendRequest request);

    @Headers({ACCEPT_APPLICATION_JSON})
    @GET(ConstantsApplicationApiRest.GET_FRIEND_REQUESTS)
    Call<JsonObject> getFriendRequestsByUserId(@Path("id") String user_id);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.FRIEND_ACCEPT_ENDPOINT)
    Call<JsonObject> acceptFriendRequest(@Body JsonObject requestId);

    @DELETE(ConstantsApplicationApiRest.FRIEND_REQUEST_ENDPOINT)
    Call<JsonObject> declineFriendRequest(@Path("id") String requestId);

    @DELETE(ConstantsApplicationApiRest.UNFRIEND)
    Call<JsonObject> unfriend(@Path("id")String friendshipId);

    @Headers({ACCEPT_APPLICATION_JSON})
    @GET(ConstantsApplicationApiRest.GET_STORIES_BY_USER)
    Call<List<Storie>> getStoriesByUserId(@Path("id") String user_id);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.POST_STORIE)
    Call<Storie> postStorie(@Body JsonObject parameters);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.POST_STORIE_COMMENT)
    Call<Comment> postStorieComment(@Body JsonObject parameters);

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST(ConstantsApplicationApiRest.POST_STORIE_REACTION)
    Call<Storie> postStorieReaction(@Body JsonObject parameters);

    @Headers({ACCEPT_APPLICATION_JSON})
    @GET(ConstantsApplicationApiRest.GET_COMMENTS_BY_STORIE)
    Call<List<Comment>> getStorieComments(@Path("id") String storieId);

    @Headers({ACCEPT_APPLICATION_JSON})
    @GET(ConstantsApplicationApiRest.GET_STORIES_BY_USER)
    Call<List<Storie>> getStoriesByUserId(@Path("id") String userId, @Query("story_type") String type);

}
