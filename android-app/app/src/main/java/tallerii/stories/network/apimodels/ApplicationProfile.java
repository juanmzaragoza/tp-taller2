package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationProfile {

    @SerializedName("_id")
    private String id;
    @SerializedName("_rev")
    private String rev;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("last_name")
    private String lastName;
    private String name;
    private String email;
    @SerializedName("picture")
    private String profilePicture;
    private List<Story> stories = null;
    private List<Friend> friends = null;
    private String type;

    public List<Friend> getFriends() {
        return friends;
    }

    public String getId() {
        return id != null ? id : userId;
    }

    public String getRev() {
        return rev;
    }

    public String getUserId() {
        return userId != null ? userId : id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public List<Story> getStories() {
        return stories;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setFirstName(String firstName) {
        this.name = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //TODO verify if this should exist or be managed from app server
    public void addFriend(Friend friend) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(friend);
    }

    public String getType() {
        return type;
    }
}
