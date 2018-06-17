
package tallerii.stories.network.apimodels;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Storie extends ObjectApiApp{

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

    @SerializedName("storie_type")
    public String storieType;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("user_last_name")
    public String userLastName;

    @SerializedName("user_email")
    public String userEmail;

    @SerializedName("user_picture")
    public String userPicture;

    @SerializedName("comments")
    public List<Comment> comments = new ArrayList<Comment>();

    @SerializedName("reactions")
    public List<Reaction> reactions = new ArrayList<Reaction>();

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

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public List<Reaction> getReactionsBy(String name){
        List<Reaction> result = new ArrayList<Reaction>();
        for (Reaction reaction : this.reactions) {
            if (reaction.getReaction().equals(name)) {
                result.add(reaction);
            }
        }
        return result;
    }

    public Reaction getReactionByUser(String userId){
        for (Reaction reaction : this.reactions) {
            if (reaction.getUserId().equals(userId)) {
                return reaction;
            }
        }
        return null;
    }

    public Reaction getReactionByUser(String userId, String reactionName){
        for (Reaction reaction : this.reactions) {
            if (reaction.getUserId().equals(userId)) {
                return reaction.getReaction().equals(reactionName)?reaction:null;
            }
        }
        return null;
    }





}
