package tallerii.stories.network.tallerii.stories.network.apimodels;

public class User {

    private long id;
    private String username;
    private String password;
    private String facebookAuthToken;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebookAuthToken() {
        return facebookAuthToken;
    }

    public void setFacebookAuthToken(String facebookAuthToken) {
        this.facebookAuthToken = facebookAuthToken;
    }


}
