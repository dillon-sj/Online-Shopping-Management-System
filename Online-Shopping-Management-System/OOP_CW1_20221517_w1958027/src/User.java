import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private boolean firstTimeLogin = true;
//    private List<Purchase> purchaseHistory; // New field

    public User(String username, String password, boolean firstTimeLogin) {
        this.username = username;
        this.password = password;
        this.firstTimeLogin = firstTimeLogin;
//        this.purchaseHistory = new ArrayList<>();
    }

    public User() {

    }

    // Getters and Setters for User
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstTime() {
        return firstTimeLogin;
    }

    public void setFirstTime(boolean firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    @Override
    public String toString() {
        return "Username: '" + username + " Password='" + password +  " First time login=" + firstTimeLogin;
    }

    //    public List<Purchase> getPurchaseHistory() {
//        return purchaseHistory;
//    }
//
//    public void addToPurchaseHistory(Purchase purchase) {
//        purchaseHistory.add(purchase);
//    }
}
