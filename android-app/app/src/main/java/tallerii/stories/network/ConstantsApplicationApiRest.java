package tallerii.stories.network;

public final class ConstantsApplicationApiRest {
    public static String ROOT_URL = "https://heroku-applicationserver.herokuapp.com/";
    public static final String REST_API = "api/v1/";

    public static final String PROFILE_ENDPOINT = REST_API + "profiles/{id}";
    public static final String POST_REGISTRATION = REST_API + "user";
    public static final String POST_LOGIN = REST_API + "token";
    public static final String FCM_SEND = REST_API + "notification";
    public static final String BEFRIEND = REST_API + "befriend";
    public static final String GET_FRIEND_REQUESTS = BEFRIEND + "/{id}";
    public static final String FRIEND_ACCEPT_ENDPOINT = BEFRIEND + "/requests";
    public static final String FRIEND_REQUEST_ENDPOINT = FRIEND_ACCEPT_ENDPOINT + "/{id}";
    public static final String UNFRIEND = REST_API + "friends/{id}";
    public static final String USERS = REST_API + "users/{id}";
    public static final String POST_STORIE = REST_API + "stories";
    public static final String GET_STORIES_BY_USER = REST_API + "stories/{id}";
}
