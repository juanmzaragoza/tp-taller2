package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public class Comment extends ObjectApiApp {

    @SerializedName("storie_id")
    private String storieId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("name")
    private String userName;

    @SerializedName("last_name")
    private String userLastName;

    @SerializedName("picture")
    private String userPicture;

    @SerializedName("date")
    private String date;

    @SerializedName("message")
    private String message;

    public Comment(String storieId, String userId,String userName,String userPicture, String date, String message) {
        this.storieId = storieId;
        this.userId = userId;
        this.userName = userName;
        this.userPicture = userPicture;
        this.date = date;
        this.message = message;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

}
