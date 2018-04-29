package tallerii.stories.network.apimodels;

import java.util.List;

public class ApplicationProfile {

    public String id;
    public String rev;
    public String userId;
    public String lastName;
    public String name;
    public String email;
    public String profilePicture;
    public List<Story> stories = null;

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
        return String.join(" ", name, lastName);
    }
}
