package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class ApplicationProfile {

    private String id;
    private String rev;
    private String userId;
    @SerializedName("last_name")
    private String lastName;
    private String name;
    private String email;
    @SerializedName("profile_picture")
    private String profilePicture;
    private List<Story> stories = null;
    private List<Friend> friends = null;
    private String firstName;

    public List<Friend> getFriends() {
        return friends;
    }

    public String getId() {
        return id;
    }

    public String getRev() {
        return rev;
    }

    public String getUserId() {
        return userId;
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
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
