package tallerii.stories.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterApplicationApiRest {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static {
        httpClient.addInterceptor(new BasicAuthInterceptor());
    }

    public static EndpointsApplicationApiRest getRawEndpoint() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(ConstantsApplicationApiRest.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointsApplicationApiRest.class);
    }
}
