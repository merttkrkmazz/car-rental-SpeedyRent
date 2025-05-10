package model;

/**
 * Represents a user of the system with login credentials.
 */
public class User {

    /** The username of the user. */
    private final String username;

    /** The password of the user (should be stored hashed in production). */
    private final String password;

    /**
     * Constructs a new User with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }
}
