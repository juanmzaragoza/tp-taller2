package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public class Friend {
    @SerializedName("_id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("last_name")
    private String lastName;
    private String name;
    private String picture;

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public String getPicture() {
        return picture;
    }

    public String getId() {
        return id;
    }
}
