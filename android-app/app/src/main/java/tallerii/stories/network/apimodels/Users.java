package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("_id")
    private String id;
    @SerializedName("last_name")
    private String lastName;
    private String name;
    private String picture;

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
