package tallerii.stories.network.apimodels;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Reaction extends ObjectApiApp {
    private String date;
    @SerializedName("count")
    private int count;
    private String reactionType;
    @SerializedName("react")
    private JsonElement react;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public boolean imVoted(){
        return !this.react.isJsonNull();
    }
}
