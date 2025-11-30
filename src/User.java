public class User {
    private String userName;
    private String userPassword;
    private String userId;
    private String userPhoneNumber;

    public User() {
    }

    public User(String userName, String userPassword, String userId, String userPhoneNumber) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userId = userId;
        this.userPhoneNumber = userPhoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }
}
