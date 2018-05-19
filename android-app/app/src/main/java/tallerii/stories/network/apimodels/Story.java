
package tallerii.stories.network.apimodels;


import com.google.gson.annotations.SerializedName;

public class Story {

    @SerializedName("_id")
    public String id;

    @SerializedName("_rev")
    public String rev;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("created_time")
    public String createdTime;

    @SerializedName("updated_time")
    public String updatedTime;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("location")
    public String location;

    @SerializedName("visibility")
    public String visibility;

    @SerializedName("multimedia")
    public String multimedia;

    @SerializedName("story_type")
    public String storyType;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("user_last_name")
    public String userLastName;

    @SerializedName("user_email")
    public String userEmail;

    @SerializedName("user_picture")
    public String userPicture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }



}
