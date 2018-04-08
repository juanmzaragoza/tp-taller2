package tallerii.stories.network;

import tallerii.stories.network.tallerii.stories.network.apimodels.User;
import tallerii.stories.network.tallerii.stories.network.apimodels.UserDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterApplicationApiRest {

    /** control method where we define the URL BASE of the API REST **/
    public static EndpointsApplicationApiRest setConnectionApplicationRestApi(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsApplicationApiRest.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(EndpointsApplicationApiRest.class);
    }

    /** convert yours object response class to gson objects **/
    public static Gson convertUserToGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserDeserializer());

        return gsonBuilder.create();
    }

    public static EndpointsApplicationApiRest getUserEndpoint() {
        return setConnectionApplicationRestApi(convertUserToGson());
    }
}
