package service.interfaces;

public interface Authentication {
    boolean login(String username, String password);
    boolean register(String username, String password);
}
