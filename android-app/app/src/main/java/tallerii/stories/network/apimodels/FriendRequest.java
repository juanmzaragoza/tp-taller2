package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public class FriendRequest {
    @SerializedName("_id")
    private String id;
    @SerializedName("date")
    private String createdTime;
    @SerializedName("user_id")
    private String senderUserId;
    @SerializedName("rcv_user_id")
    private String rcvUserId;
    private String message;
    private String picture;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("name")
    private String firstName;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRcvUserId() {
        return rcvUserId;
    }

    public void setRcvUserId(String rcvUserId) {
        this.rcvUserId = rcvUserId;
    }
}
