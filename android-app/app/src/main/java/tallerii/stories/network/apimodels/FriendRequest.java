package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public class FriendRequest {
    private String id;
    @SerializedName("_rev")
    private String rev;
    private String createdTime;
    private String senderUserId;

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setRcvUserId(String rcvUserId) {
        this.rcvUserId = rcvUserId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String rcvUserId;
    private String message;
    //TODO check serialization after changes made on server
    private String picture;
    private String fullName;

    public String getCreatedTime() {
        return createdTime;
    }

    public String getId() {
        return id;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getRcvUserId() {
        return rcvUserId;
    }

    public String getMessage() {
        return message;
    }

    public String getPicture() {
        return picture;
    }

    public String getFullName() {
        return fullName;
    }
}
