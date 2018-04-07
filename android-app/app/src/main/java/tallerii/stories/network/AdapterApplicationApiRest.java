package tallerii.stories.network;

import tallerii.stories.network.tallerii.stories.network.apimodels.User;
import tallerii.stories.network.tallerii.stories.network.apimodels.UserDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterApplicationApiRest {

    /** control method where we define the URL BASE of the API REST **/
    public EndpointsApplicationApiRest setConnectionApplicationRestApi(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsApplicationApiRest.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(EndpointsApplicationApiRest.class);
    }

    /** convert yours object response class to gson objects **/
    public Gson convertUserToGson() {
        GsonBuilder gsonBuldier = new GsonBuilder();
        gsonBuldier.registerTypeAdapter(User.class, new UserDeserializer());

        return gsonBuldier.create();
    }

}
