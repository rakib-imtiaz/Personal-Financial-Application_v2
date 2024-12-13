import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;
    private static final String USER_DATA_FILE = "users.dat";

    public UserManager() {
        this.users = new ArrayList<>();
        loadUsers();
        
        // Add default admin if no users exist
        if (users.isEmpty()) {
            users.add(new AdminUser("admin", "admin123"));
            users.add(new RegularUser("user", "user123"));
            saveUsers();
        }
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        saveUsers();
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean authenticate(String username, String password) {
        User user = getUser(username);
        return user != null && user.authenticate(password);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        if (!new File(USER_DATA_FILE).exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(USER_DATA_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            users = new ArrayList<>();
        }
    }
}
