package de.sopro.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Class to retrieve the information necessary to register a new User.
 */
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

    /**
     * Creates new UserRegistrationDto
     * @param firstName first name of user
     * @param lastName last name of user
     * @param password password for the new user
     * @param title optional title of new user
     * @param email email with which the new User wants to register
     */
    public UserRegistrationDto(@NotEmpty String firstName, @NotEmpty String lastName,
                               @NotEmpty String password, @NotNull String title,
                               @Email @NotEmpty String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.title = title;
        this.email = email;
    }

    ///////////////////////
    // Setter and Getter //
    ///////////////////////

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