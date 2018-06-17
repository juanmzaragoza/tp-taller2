package tallerii.stories.network.apimodels;

import com.google.gson.annotations.SerializedName;

public abstract class ObjectApiApp {

    @SerializedName("_id")
    public String id;

    @SerializedName("_rev")
    public String rev;

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
}
