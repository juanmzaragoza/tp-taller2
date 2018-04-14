package tallerii.stories.network.apimodels;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class RegistrationResultDeserializer implements JsonDeserializer<RegistrationResult> {

    @Override
    public RegistrationResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject userResponseData = json.getAsJsonObject();
        JsonObject userResponse = userResponseData.getAsJsonObject("args");

        RegistrationResult registrationResult = new RegistrationResult();

        if(userResponse != null && !userResponse.isJsonNull()){

            registrationResult.setErrors(userResponse.get("errors").getAsString());
            registrationResult.setUsername(userResponse.getAsJsonObject().get("username").getAsString());
            registrationResult.setSuccess(userResponse.getAsJsonObject().get("success").getAsString());
        }

        return registrationResult;
    }
}
