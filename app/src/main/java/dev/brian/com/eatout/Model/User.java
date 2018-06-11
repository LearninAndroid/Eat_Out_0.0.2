package dev.brian.com.eatout.Model;

public class User {
    public User() {
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Name;
    private String Password;

    public User(String name, String password){
        this.Name = name;
        this.Password = password;
    }

}
