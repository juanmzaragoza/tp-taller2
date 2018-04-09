package tallerii.stories.network.tallerii.stories.network.apimodels;

public class RegistrationResult {
    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    private String errors;
    private String username;
    private String success;
}
