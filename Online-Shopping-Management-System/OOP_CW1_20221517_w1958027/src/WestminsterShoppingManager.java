/*
 * Author: Dillon Steve Juriansz
 * IIT ID: 20221517
 * UoW ID: w1958027
 * Description: This class is for the shopping manager
 * Date: 15/01/2024
 * */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;


public class WestminsterShoppingManager implements ShoppingManager {

    private ArrayList<Product> products = new ArrayList<>();

    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;

    private boolean firstTimeLogin;
    private boolean startLoop = true;
    private boolean mainLoop = true;
    private Scanner inputs = new Scanner(System.in);


    //* Method to start the main program flow
    public static void main(String[] args) {
        WestminsterShoppingManager running = new WestminsterShoppingManager();
        running.startProgram();

    }

    /**
     * Initiates the Westminster Online Shopping System program.
     * Loads user information and product data from files.
     * Provides a login menu for the manager and customers.
     * Manages program flow based on user choices.
     */
    public void startProgram() {
        loadUsersFromFile();
        boolean fileLoaded = loadProductsFromFile();
        if (fileLoaded) {
            System.out.println("\t\t\tPrevious file loaded.");
        } else {
            System.out.println("\tNo previous data found.\n\t New file loaded.");
        }
        do {
            while (startLoop) {
                System.out.println("\n\n\t=== Westminster Online Shopping System ===");
                System.out.println("\n\tLogin As:\n\n\t1. Manager\n\t2. Customer\n\t3. Exit");
                int login = getInput("Enter your choice: ", 1, 3);

                System.out.println();

                switch (login) {
                    case 1 -> {System.out.println("\nManager has been chosen\n");managerAuthentication();}

                    case 2 -> {System.out.println("Customer has been chosen\n");
                        userLoginOrRegister();

                        startLoop = false;
                    }
                    case 3 -> {
                        System.out.println("\n\tExiting program...");
                        startLoop = false;
                        mainLoop = false;
                    }
                }
            }
        } while (mainLoop);
    }


    /**
     * Authenticates the manager by requesting a password input.
     * If the entered password matches, the manager is authenticated and the main menu is displayed.
     */
    public void managerAuthentication() {
        boolean authenticated = false;

        while (!authenticated) {

            String password = getStringInput("Enter manager password: ", 0);

            if (password.equals("admin123")) {
                System.out.println("\n\tAuthentication successful!");
                authenticated = true;
                startLoop = false;
                menu();

            } else {
                System.out.println("Invalid Password. Please try again.");
            }
        }
    }

    /**
     * Displays the main menu for the manager interface.
     * Options include adding products, deleting products, displaying the product list,
     * saving products to a file, and exiting the program.
     */
    public void menu() {
        System.out.println("\n\t=== Online Shopping System Manager Interface ===");
        System.out.println("\t1. Add Product");
        System.out.println("\t2. Delete Product");
        System.out.println("\t3. Display Product List");
        System.out.println("\t4. Save Products to File");
        System.out.println("\t5. Exit");
        int choice = getInput("Enter your choice: ", 1, 5);
        processMenuChoice(choice);
    }


    /**
     * Processes the menu choice made by the manager.
     * Calls corresponding methods based on the chosen option.
     *
     * @param choice (int) the menu choice made by the manager.
     */
    public void processMenuChoice(int choice) {
        switch (choice) {
            case 1 -> addProductMenu();
            case 2 -> deleteProductMenu();
            case 3 -> displayProductList();
            case 4 -> {saveProductToFile(products);
                menu();
            }
            case 5 -> {
                System.out.println("\n\tExiting program...");
                mainLoop = false;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }


    /**
     * Displays the menu for adding a product and prompts the user to choose the product category.
     * Based on the choice, it calls the corresponding method to add either electronics or clothing product.
     */
    public void addProductMenu() {
        System.out.println("\n\tChoose product type to add:\n\t1. Electronics\n\t2. Clothing");
        int productType = getInput("Enter your choice: ", 1, 2);

        switch (productType) {
            case 1 -> addElectronicsProduct();
            case 2 -> addClothingProduct();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }


    /**
     * Adds an electronics product to the system by gathering the required details from the user.
     * Validates the product ID and checks for duplicates before adding the product.
     */
    public void addElectronicsProduct() {
        System.out.println("\n\tEnter product details for Electronics:");
        String productId = getValidElectronicsProductId();

        // Check if the product ID already exists
        if (productExists(productId)) {
            System.out.println("Product ID '" + productId + "' already exists. Please enter a different one.");
            addElectronicsProduct();
            return;
        }
        String productName = getStringInput("\nEnter Product Name: ", 3);
        int availableItems = getInput("\nEnter Available Number of Items: ", 1, 1000);
        double price = getPriceInput("\nEnter Price of Item: ");
        String brand = getStringInput("\nEnter Item Brand Name: ", 3);
        int warrantyPeriod = getInput("\nEnter Item Warranty Period (in weeks): ", 0, 1000);

        Electronics electronics = new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
        addProduct(electronics);
        System.out.println("\n\tElectronics product added successfully!\n");
        menu();
    }

    /**
     * Gets a valid electronics product ID from the user, ensuring it starts with 'A' followed by at least 3 digits.
     *
     * @return (String) valid electronics product ID.
     */
    private String getValidElectronicsProductId() {
        String productId = null;
        boolean validInput = false;
        do {
            try {
                productId = getStringInput("\nEnter Product ID \n(must start with 'A' followed by at least 3 digits): ", 4);
                productId = productId.substring(0, 1).toUpperCase() + productId.substring(1);

                if (!productId.matches("A\\d{3,}")) {
                    System.out.println("Wrong ID format. Please enter a valid Electronics product ID.");
                } else {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!validInput);
        return productId;
    }




    /**
     * Adds a clothing product to the system by gathering the required details from the user.
     * Validates the product ID and checks for duplicates before adding the product.
     */
    public void addClothingProduct() {
        System.out.println("\n\tEnter product details for Clothing:");
        String productId = getValidClothingProductId();

        if (productExists(productId)) {
            System.out.println("Product ID '" + productId + "' already exists. Please enter a different one.");
            addClothingProduct();
            return;
        }
        String productName = getStringInput("\nEnter Product Name: ", 3);
        int availableItems = getInput("\nEnter Available Number of Items: ", 1, 1000);
        double price = getPriceInput("\nEnter Price of Item: ");
        String size = getSizeInput();
        //*list of sizes and selecting from it using int
        String color = getStringInput("\nEnter Item Color: ", 3);

        Clothing clothing = new Clothing(productId, productName, availableItems, price, size, color);
        addProduct(clothing);
        System.out.println("\n\tClothing product added successfully!\n");
        menu();
    }

    /**
     * Gets a valid clothing product ID from the user, ensuring it starts with 'B'.
     *
     * @return (String) valid clothing product ID.
     */
    private String getValidClothingProductId() {
        String productId = null;
        boolean validInput = false;
        do {
            try {
                productId = getStringInput("\nEnter Product ID \n(must start with 'B' followed by at least 3 digits):", 2);
                productId = productId.substring(0, 1).toUpperCase() + productId.substring(1);
                if (!productId.matches("B\\d{3,}")) {
                    System.out.println("Wrong ID format. Please enter a valid Clothing product ID.");
                } else {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!validInput);
        return productId;
    }


    /**
     * Adds a product to the system if the product limit is not reached.
     * Displays a message if the maximum product limit is reached.
     *
     * @param product (Product) the product to be added.
     */
    public void addProduct(Product product) {
        if (products.size() < 50) {
            products.add(product);
        } else {
            System.out.println("Maximum products limit reached!");
        }
    }

    /**
     * Checks if a product with the given product ID already exists in the system.
     *
     * @param productId (String) the product ID to check.
     * @return (boolean) true if the product already exists, false otherwise.
     */
    private boolean productExists(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initiates the process of deleting a product by displaying available product IDs,
     * taking user input, and confirming the deletion.
     */
    public void deleteProductMenu() {
        boolean availableProducts = displayAvailableProductIds();
        boolean validProductId = false;
        String productId;
        if (!availableProducts) {
            menu();
        }else {

            do {
                System.out.print("Enter Product ID to delete: ");
                productId = inputs.next().toUpperCase();


                validProductId = productExists(productId);


                if (!validProductId) {
                    System.out.println("Invalid Product ID. Please enter a valid ID.");
                    displayAvailableProductIds();
                }
            } while (!validProductId);


            confirmDeleteProduct(findProductById(productId));
        }
    }

    /**
     * Displays the available product IDs or notifies if no products are available.
     */
    private boolean displayAvailableProductIds() {
        if (products.isEmpty()) {
            System.out.println("\nNo products available. Please add products first.");
            return false;
        } else {
            System.out.println("\nAvailable Product IDs:");


            for (Product product : products) {
                System.out.print(product.getProductId() + " ");
            }

            System.out.println();
            return true;
        }
    }

    /**
     * Finds a product by its ID in the system.
     *
     * @param productId (String) the product ID to search for.
     * @return (Product) the found product or null if not found.
     */
    private Product findProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null; //* Product not found
    }

    /**
     * Deletes a product by asking for confirmation and then removing it from the system.
     *
     * @param product (Product) the product to be deleted.
     */
    public void confirmDeleteProduct(Product product) {
        System.out.println("Are you sure you want to delete this product? (Y/N)");
        String confirmation = inputs.next().toUpperCase();

        if (confirmation.equals("Y")) {
            System.out.println("\nDeleted product information:");
            System.out.println(product.toString());

            products.remove(product);

            System.out.println("Product deleted successfully!");
            System.out.println("Total number of products left in the system: " + products.size());

            menu();
        } else if (confirmation.equals("N")) {
            System.out.println("Deletion canceled");
            menu();
        } else {
            System.out.println("Invalid choice. Please try again.");
            confirmDeleteProduct(product);
        }
    }

    /**
     * Displays the list of products, sorted by product ID, or notifies if no products are available.
     */
    public void displayProductList() {
        if (products.isEmpty()) {
            System.out.println("No products available. Please add products first.");
        } else {
            Collections.sort(products, Comparator.comparing(Product::getProductId));

            System.out.println("\n=== Product List ===");

            for (Product product : products) {
                System.out.println(product.toString());
            }
        }
        menu();
    }

    /**
     * Saves the product details to a file, including additional fields for specific product types.
     *
     * @param products (ArrayList<Product>) the list of products to save to the file.
     */
    public void saveProductToFile(ArrayList<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productDetails.txt"))) {
            for (Product product : products) {
                writer.write(product.getClass().getSimpleName() + "," +
                        product.getProductId() + "," +
                        product.getProductName() + "," +
                        product.getAvailableItems() + "," +
                        product.getPrice());

                if (product instanceof Electronics electronics) {
                    writer.write("," + electronics.getBrand() + "," + electronics.getWarrantyPeriod());
                } else if (product instanceof Clothing clothing) {
                    writer.write("," + clothing.getSize() + "," + clothing.getColor());
                }

                writer.newLine();
            }
            System.out.println("\n===Product details saved to file successfully!===");
        } catch (IOException e) {
            System.out.println("\n===Error saving product details to file: " + e.getMessage()+"===");
        }
    }

    /**
     * Loads product details from a file and adds them to the system.
     *
     * @return (boolean) true if loading is successful, false otherwise.
     */
    public boolean loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("productDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String className = parts[0];
                String productId = parts[1];
                String productName = parts[2];
                int availableItems = Integer.parseInt(parts[3]);
                double price = Double.parseDouble(parts[4]);

                if ("Electronics".equals(className)) {
                    String brand = parts[5];
                    int warrantyPeriod = Integer.parseInt(parts[6]);
                    Electronics electronics = new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
                    addProduct(electronics);
                } else if ("Clothing".equals(className)) {
                    String size = parts[5];
                    String color = parts[6];
                    Clothing clothing = new Clothing(productId, productName, availableItems, price, size, color);
                    addProduct(clothing);
                }

            }

            System.out.println("\n\t===Product details loaded from file successfully!===");
            return true;


        } catch (IOException | NumberFormatException e) {
            System.out.println("Sorry the product details from file "
                    + e.getMessage()+"\nIgnore if this is your first time running the program.\n");
            return false;

        }

    }

    /**
     * Validates and retrieves integer input from the user within a specified range.
     *
     * @param message (String) the message to display to the user.
     * @param start   (int) the starting value of the allowed range.
     * @param end     (int) the ending value of the allowed range.
     * @return (int) the validated user input.
     */
    private int getInput(String message, int start, int end) {
        int userInput = 0;
        boolean validInput = false;

        do {
            try {
                System.out.print(message);
                userInput = inputs.nextInt();
                inputs.nextLine();

                if (userInput >= start && userInput <= end) {
                    validInput = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + start + " and " + end + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                inputs.nextLine();
            }
        } while (!validInput);

        return userInput;
    }

    /**
     * Validates and retrieves string input from the user with a specified minimum length.
     *
     * @param message  (String) the message to display to the user.
     * @param minLength (int) the minimum length allowed for the input.
     * @return (String) the validated user input.
     */
    private String getStringInput(String message, int minLength) {
        String userInput = "";

        do {
            System.out.print(message);
            userInput = inputs.nextLine();

            if (userInput.length() < minLength) {
                System.out.println("Input must be at least " + minLength + " characters long. Please try again.");
            }
        } while (userInput.length() < minLength);

        return userInput;
    }

    /**
     * Validates and retrieves price input from the user.
     *
     * @param message (String) the message to display to the user.
     * @return (double) the validated user input.
     */
    private double getPriceInput(String message) {
        double userInput = 0.0;
        boolean validInput = false;

        do {
            try {
                System.out.print(message);
                userInput = inputs.nextDouble();
                inputs.nextLine();

                if (userInput < 0.0) {
                    System.out.println("Invalid price. Please enter a non-negative value.");
                } else if (userInput == 0.0) {
                    System.out.println("Invalid price. Please enter a non-zero value.");
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                inputs.nextLine();
            }
        } while (!validInput);

        return userInput;
    }

    /**
     * Validates and retrieves size input from the user.
     *
     * @return (String) the validated user input for size.
     */
    private String getSizeInput() {
        String size = "";
        boolean validInput = false;

        do {
            try {

                // Display the size options
                System.out.println("\nSelect Size of Item:");
                System.out.println("1. Small");
                System.out.println("2. Medium");
                System.out.println("3. Large");
                System.out.println("4. XL");
                System.out.print("Enter the corresponding number: ");

                // Get user input
                int sizeChoice = inputs.nextInt();
                inputs.nextLine();

                // Validate the input and set the size
                switch (sizeChoice) {
                    case 1 -> {
                        size = "Small";
                        validInput = true;
                    }
                    case 2 -> {
                        size = "Medium";
                        validInput = true;
                    }
                    case 3 -> {
                        size = "Large";
                        validInput = true;
                    }
                    case 4 -> {
                        size = "XL";
                        validInput = true;
                    }
                    default -> System.out.println("Invalid size selection. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                inputs.nextLine();
            }
        } while (!validInput);

        return size;
    }



    //!USER LOGIN PART

    /**
     * Prompts the user to check if they want to sign up with an existing account or need to register.
     * If the user has an account, it initiates the login process; otherwise, it triggers the registration process.
     */
    private void userLoginOrRegister() {
        System.out.println("\tDo you have an account?\n\t1. Yes\n\t2. No");
        int choice = getInput("Enter your choice: ", 1, 2);

        if (choice == 1) {

            userLogin();
        } else {
            registerUser();
        }
    }

    /**
     * Handles the user login process, prompting for username and password.
     * Validates the login credentials, allowing the user to register if not found.
     * Updates user data if it's the first login and initiates the shopping interface.
     */
    private void userLogin() {
    System.out.println("\n\tLogin Menu\n");

    String username = getStringInput("Enter your username: ", 3);
    String password = getStringInput("Enter your password: ", 6);

    // Check if the user exists
    User foundUser = findUserByUsername(username);

    while (foundUser == null || !foundUser.getPassword().equals(password)) {
        System.out.println("\nIncorrect username or password.\n");

        // Ask if the user wants to register
        System.out.println("\tDo you want to register?\n\t1. Yes\n\t2. No");
        int registerChoice = getInput("Enter your choice: ", 1, 2);

        if (registerChoice == 1) {
            registerUser();
            return; // Return to exit the login process after registration
        }

        username = getStringInput("Enter your username: ", 3);
        password = getStringInput("Enter your password: ", 6);
        foundUser = findUserByUsername(username);
    }
    firstTimeLogin = false;
    System.out.println("\n\tLogin successful!");

    // If it's the first time logging in, set firstTime to false
    if (foundUser.isFirstTime()) {
        foundUser.setFirstTime(false);
        saveUsersToFile();
    }

    currentUser = foundUser;
    new WestminsterShoppingGUI(products,firstTimeLogin);
    startLoop = false;
}


    /**
     * Handles the user registration process, prompting for a new username and password.
     * Validates username availability, allowing the user to login if the username is taken.
     * Creates a new user, adds it to the user list, and saves the updated list to a file.
     * Initiates the shopping interface upon successful registration.
     */
    private void registerUser() {
        String username;
        System.out.println("\n\tRegister Menu\n");

        do {
            username = getStringInput("Enter a new username: ", 3);

            // Check if the username is already taken
            if (findUserByUsername(username) != null) {
                System.out.println("\tUsername already exists. Do you want to login instead?\n\t1. Yes\n\t2. No");
                int loginChoice = getInput("Enter your choice: ", 1, 2);

                if (loginChoice == 1) {
                    userLogin();
                    return;
                }
            }
        } while (findUserByUsername(username) != null);

        String password = getStringInput("Enter a password (at least 6 characters): ", 6);
        User newUser = new User(username, password, true);
        users.add(newUser);


        saveUsersToFile();
        firstTimeLogin = true;
        System.out.println("\n\tUser registration successful!");
        currentUser = newUser;
        new WestminsterShoppingGUI(products,firstTimeLogin);
        startLoop = false;

    }

    /**
     * Searches for a user in the list based on the provided username.
     *
     * @param username (String) the username to search for.
     * @return (User) the found user or null if the user is not found.
     */
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Loads user details from a file and populates the 'users' list.
     * Each line in the file is expected to contain a username and password separated by a comma.
     */
    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("userDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                User user = new User(username, password, false);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Sorry the user details from file " + e.getMessage() +"\nIgnore if this is your first time running the program." );
        }
    }


    /**
     * Saves user details from the 'users' list to a file.
     * Each user's username and password are written on a new line, separated by a comma.
     */
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userDetails.txt"))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user details to file: " + e.getMessage());
        }
    }
}