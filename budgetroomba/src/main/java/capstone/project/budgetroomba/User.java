package capstone.project.budgetroomba;

public class User {
    public String email, password, repeatPassword;

    public User()
    {

    }

    public User(String email, String password, String repeatPassword)
    {
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }
}
