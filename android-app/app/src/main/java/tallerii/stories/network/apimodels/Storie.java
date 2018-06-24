
package tallerii.stories.network.apimodels;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Storie extends ObjectApiApp{

    @SerializedName("user_id")
    private String userId;

    @SerializedName("created_time")
    private String createdTime;

    @SerializedName("updated_time")
    private String updatedTime;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("location")
    private String location;

    @SerializedName("visibility")
    private String visibility;

    @SerializedName("multimedia")
    private String multimedia;

    @SerializedName("story_type")
    private String storieType;

    @SerializedName("name")
    private String userName;

    @SerializedName("last_name")
    private String userLastName;

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("picture")
    private String userPicture;

    @SerializedName("comments")
    private List<Comment> comments = new ArrayList<Comment>();

    @SerializedName("reactions")
    private Reactions reactions = new Reactions();

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

    public String getStorieType() {
        return storieType;
    }

    public void setStorieType(String storieType) {
        this.storieType = storieType;
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


    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }

}
