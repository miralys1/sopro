package de.sopro.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegistrationDto{

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String password;
    
    private String title;

    @Email
    @NotEmpty
    private String email;

    public UserRegistrationDto(@NotEmpty String firstName, @NotEmpty String lastName,
                               @NotEmpty String password, @NotNull String title,
                               @Email @NotEmpty String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.title = title;
        this.email = email;
    }

    public void setFirstName(String firstName){
        this.firstName= firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastname(String lastname){
        this.lastName = lastname;
    }

    public String getLastName(){
        return lastName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }


}