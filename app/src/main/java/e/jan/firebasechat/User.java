package e.jan.firebasechat;

public class User {

    public String password;
    public String email;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {

        return password;
    }

    public String getEmail() {
        return email;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

}