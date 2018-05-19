package tallerii.stories.network;

public final class ConstantsApplicationApiRest {
    public static String ROOT_URL = "https://heroku-applicationserver.herokuapp.com/";
    public static final String REST_API = "api/v1/";

    public static final String PROFILE_ENDPOINT = REST_API + "profiles/{id}";
    public static final String POST_REGISTRATION = REST_API + "user";
    public static final String POST_LOGIN = REST_API + "token";

    // TODO: only for test
    public static final String GET_STORIES_BY_USER = "http://heroku-applicationserver.herokuapp.com/api/v1/stories/{id}";

}
