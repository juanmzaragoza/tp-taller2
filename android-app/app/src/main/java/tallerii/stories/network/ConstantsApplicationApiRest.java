package tallerii.stories.network;

public final class ConstantsApplicationApiRest {
    public static String ROOT_URL = "http://192.168.1.3:5858/";
    public static final String REST_API = "api/v1/";

    // TODO: modify with the real API endpoint
    public static final String KEY_GET_USER_BY_ID = "get?username=juanzaragoza@live.com.ar&password=gk3nxmbkif&facebookAuthToken=gaksrgnnbxc";
    public static final String POST_REGISTRATION = REST_API + "user";
    public static final String POST_LOGIN = REST_API + "token";

    public static final String URL_GET_USER_BY_ID = REST_API + KEY_GET_USER_BY_ID;
}
