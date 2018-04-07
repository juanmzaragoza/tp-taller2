package tallerii.stories.network.tallerii.stories.network.apimodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject userResponseData = json.getAsJsonObject();
        JsonObject userResponse = userResponseData.getAsJsonObject("args");

        User user = new User();

        if(userResponse != null && !userResponse.isJsonNull()){

            user.setId(userResponse.get("id").getAsLong());
            user.setUsername(userResponse.getAsJsonObject().get("username").getAsString());
            user.setPassword(userResponse.getAsJsonObject().get("password").getAsString());
            user.setFacebookAuthToken(userResponse.getAsJsonObject().get("facebookAuthToken").getAsString());

        }

        return user;
    }
}
