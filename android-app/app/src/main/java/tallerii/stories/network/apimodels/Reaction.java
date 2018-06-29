package tallerii.stories.network.apimodels;


import com.google.gson.annotations.SerializedName;

public class Reaction {
    @SerializedName("_id")
    private String id;
    @SerializedName("storie_id")
    private String storieId;
    @SerializedName("user_id")
    private String userId;
    private String date;
    private String reaction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
