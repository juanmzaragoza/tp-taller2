package tallerii.stories.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterApplicationApiRest {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS);

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
