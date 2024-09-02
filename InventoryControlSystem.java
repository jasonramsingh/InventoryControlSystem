import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

public class InventoryControlSystem {
    private HashMap<String, InventoryItem> inventory = new HashMap<>();
    private HashMap<String, String> users = new HashMap<>(); // Stores username and password
    private static Logger logger = LogManager.getLogger();

    // Class to represent an inventory item with SKU, category, and quantity
    class InventoryItem {
        String sku;
        String category;
        int quantity;

        InventoryItem(String sku, String category, int quantity) {
            this.sku = sku;
            this.category = category;
            this.quantity = quantity;
        }

        // Method to update quantity
        void updateQuantity(int change) {
            this.quantity += change;
            // Log the quantity change
            logger.info("Updated inventory: SKU=" + sku + ", Category=" + category + ", New Quantity=" + quantity);
        }
    }

    // Method to add inventory with SKU, category, and quantity
    public void addInventory(String sku, String category, int quantity) {
        InventoryItem item = inventory.getOrDefault(sku, new InventoryItem(sku, category, 0));
        item.updateQuantity(quantity);
        inventory.put(sku, item);

        System.out.println(quantity + " units of " + sku + " added to inventory.");
        logger.info("Added inventory: SKU=" + sku + ", Category=" + category + ", Quantity=" + quantity);

        // Check inventory levels for alerts
        checkInventoryLevels(item);
    }

    // Method to delete inventory by SKU
    public void deleteInventory(String sku) {
        if (inventory.containsKey(sku)) {
            inventory.remove(sku);
            System.out.println(sku + " removed from inventory.");
            logger.info("Deleted inventory: SKU=" + sku);
        } else {
            System.out.println("Product SKU not found.");
            logger.warning("Attempt to delete non-existent SKU: " + sku);
        }
    }

    // Method to check inventory levels and provide alerts
    private void checkInventoryLevels(InventoryItem item) {
        if (item.quantity < 5) {
            System.out.println("Warning: Low inventory for SKU " + item.sku + ". Consider placing a quick order.");
            logger.warning("Low inventory alert for SKU=" + item.sku);

            // Offer quick order option
            System.out.print("Would you like to place a quick order for 10 more units? (yes/no): ");
            try (Scanner scanner = new Scanner(System.in)) {
				String response = scanner.nextLine();
				if (response.equalsIgnoreCase("yes")) {
				    addInventory(item.sku, item.category, 10);
				    System.out.println("Quick order placed for SKU " + item.sku + ".");
				    logger.info("Quick order placed for SKU=" + item.sku);
				}
			}
        } else if (item.quantity > 20) {
            System.out.println("Notice: High inventory for SKU " + item.sku + ". Potential overstock.");
            logger.warning("High inventory alert for SKU=" + item.sku);
        }
    }

    // Method to view current inventory
    public void viewInventory() {
        System.out.println("\nCurrent Inventory:");
        for (InventoryItem item : inventory.values()) {
            System.out.println("SKU: " + item.sku + ", Category: " + item.category + ", Quantity: " + item.quantity);
            logger.info("Viewed inventory: SKU=" + item.sku + ", Category=" + item.category + ", Quantity=" + item.quantity);
        }
    }

    // Method to create a new user account
    public void createUserAccount(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            logger.warning("Attempt to create duplicate username: " + username);
        } else {
            users.put(username, password);
            System.out.println("User account created for " + username + ".");
            logger.info("Created user account: " + username);
        }
    }

    // Method to log in a user
    public boolean loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            logger.info("User logged in: " + username);
            return true;
        } else {
            System.out.println("Invalid username or password.");
            logger.warning("Failed login attempt for username: " + username);
            return false;
        }
    }

    public static void main(String[] args) {
        LogManager.setup();
        InventoryControlSystem system = new InventoryControlSystem();
        Scanner scanner = new Scanner(System.in);

        // User creation and login loop
        while (true) {
            System.out.println("\n1. Create User Account\n2. Login\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Create a new user account
                System.out.print("Enter Username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                system.createUserAccount(username, password);
            } else if (choice == 2) {
                // User login
                System.out.print("Enter Username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                if (system.loginUser(username, password)) {
                    // User successfully logged in
                    break;
                }
            } else if (choice == 3) {
                // Exit the program
                System.out.println("Exiting the system. Goodbye!");
                logger.info("System exited by user");
                System.exit(0);
            } else {
                System.out.println("Invalid choice. Please try again.");
                logger.warning("Invalid menu choice made by user");
            }
        }

        // Inventory management loop after successful login
        while (true) {
            System.out.println("\n1. View Inventory\n2. Add Inventory\n3. Delete Inventory\n4. Logout");
            System.out.print("Enter your choice: ");
            int invChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (invChoice == 1) {
                system.viewInventory();
            } else if (invChoice == 2) {
                // Adding inventory
                System.out.print("Enter Product SKU: ");
                String sku = scanner.nextLine();
                System.out.print("Enter Product Category: ");
                String category = scanner.nextLine();
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                system.addInventory(sku, category, quantity);
            } else if (invChoice == 3) {
                // Deleting inventory
                System.out.print("Enter Product SKU: ");
                String sku = scanner.nextLine();
                system.deleteInventory(sku);
            } else if (invChoice == 4) {
                // Logout
                System.out.println("Logging out...");
                logger.info("User logged out");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
                logger.warning("Invalid menu choice made by user");
            }
        }

        scanner.close();
    }
}
