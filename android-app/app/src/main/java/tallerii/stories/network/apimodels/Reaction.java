package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public class Reaction extends ObjectApiApp{

    @SerializedName("storie_id")
    public String storieId;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("date")
    public String date;

    @SerializedName("reaction")
    public String reaction;

    public String getStorieId() {
        return storieId;
    }

    public void setStorieId(String storieId) {
        this.storieId = storieId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }


}
