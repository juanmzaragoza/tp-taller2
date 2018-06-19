package tallerii.stories.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import tallerii.stories.activities.StoriesLoggedInActivity;

public class BasicAuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String token = StoriesLoggedInActivity.getToken();
        if (token == null) {
            return chain.proceed(request);
        } else {
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", token).build();
            return chain.proceed(authenticatedRequest);
        }
    }

}