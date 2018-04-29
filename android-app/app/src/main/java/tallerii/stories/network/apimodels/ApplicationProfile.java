package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicationProfile {

    public String id;
    public String rev;
    public String userId;
    @SerializedName("last_name")
    public String lastName;
    public String name;
    public String email;
    @SerializedName("profile_picture")
    public String profilePicture;
    public List<Story> stories = null;
    public List<Friend> friends = null;

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
}
