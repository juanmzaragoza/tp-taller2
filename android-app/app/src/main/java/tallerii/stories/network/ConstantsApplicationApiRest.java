package tallerii.stories.network;

public final class ConstantsApplicationApiRest {
    public static final String ROOT_URL = "https://heroku-applicationserver.herokuapp.com/";
    public static final String REST_API = "api/v1/";

    public static final String PROFILE_ENDPOINT = REST_API + "profiles/{id}";
    public static final String POST_REGISTRATION = REST_API + "user";
    public static final String POST_LOGIN = REST_API + "token";
    public static final String POST_STORIE = REST_API + "stories";
    public static final String GET_STORIES_BY_USER = REST_API + "stories/{id}";

}
