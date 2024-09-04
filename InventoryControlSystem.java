import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class InventoryControlSystem {
    private HashMap<String, InventoryItem> inventory = new HashMap<>();
    private HashMap<String, String> users = new HashMap<>();
    private HashMap<String, Supplier> suppliers = new HashMap<>(); // Stores supplier information
    private HashMap<String, String> memos = new HashMap<>(); // Stores memos with title as key
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
            logger.info("Updated inventory: SKU=" + sku + ", Category=" + category + ", New Quantity=" + quantity);
        }
    }

    // Class to represent a supplier
    class Supplier {
        String name;
        String address;
        String phoneNumber;
        String email;

        Supplier(String name, String address, String phoneNumber, String email) {
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }

        // Method to update supplier details
        void updateSupplier(String address, String phoneNumber, String email) {
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.email = email;
            logger.info("Updated supplier: " + name + " with new details: " + address + ", " + phoneNumber + ", " + email);
        }
    }

    // Method to add supplier
    public void addSupplier(String name, String address, String phoneNumber, String email) {
        if (suppliers.containsKey(name)) {
            System.out.println("Supplier already exists. Consider updating their information.");
            logger.warning("Attempt to add duplicate supplier: " + name);
        } else {
            Supplier supplier = new Supplier(name, address, phoneNumber, email);
            suppliers.put(name, supplier);
            System.out.println("Supplier " + name + " added.");
            logger.info("Added supplier: " + name);
        }
    }

    // Method to view supplier information
    public void viewSuppliers() {
        System.out.println("\nSuppliers:");
        for (Map.Entry<String, Supplier> entry : suppliers.entrySet()) {
            Supplier supplier = entry.getValue();
            System.out.println("Name: " + supplier.name + ", Address: " + supplier.address +
                               ", Phone: " + supplier.phoneNumber + ", Email: " + supplier.email);
            logger.info("Viewed supplier: " + supplier.name);
        }
    }

    // Method to update supplier information
    public void updateSupplier(String name, String newAddress, String newPhoneNumber, String newEmail) {
        if (suppliers.containsKey(name)) {
            Supplier supplier = suppliers.get(name);
            supplier.updateSupplier(newAddress, newPhoneNumber, newEmail);
            System.out.println("Supplier " + name + " updated.");
        } else {
            System.out.println("Supplier not found.");
            logger.warning("Attempt to update non-existent supplier: " + name);
        }
    }

    // Method to delete a supplier
    public void deleteSupplier(String name) {
        if (suppliers.containsKey(name)) {
            suppliers.remove(name);
            System.out.println("Supplier " + name + " deleted.");
            logger.info("Deleted supplier: " + name);
        } else {
            System.out.println("Supplier not found.");
            logger.warning("Attempt to delete non-existent supplier: " + name);
        }
    }

    // Method to post a memo
    public void postMemo(String title, String content) {
        memos.put(title, content);
        System.out.println("Memo titled '" + title + "' posted.");
        logger.info("Posted memo: " + title);
    }

    // Method to view all memos
    public void viewMemos() {
        System.out.println("\nMemos:");
        for (Map.Entry<String, String> entry : memos.entrySet()) {
            System.out.println("Title: " + entry.getKey() + "\nContent: " + entry.getValue() + "\n");
            logger.info("Viewed memo: " + entry.getKey());
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
            System.out.println("\n1. View Inventory\n2. Add Inventory\n3. Delete Inventory\n4. View Suppliers\n5. Add Supplier\n6. Update Supplier\n7. Delete Supplier\n8. Post Memo\n9. View Memos\n10. Logout");
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
                // Viewing suppliers
                system.viewSuppliers();
            } else if (invChoice == 5) {
                // Adding supplier
                System.out.print("Enter Supplier Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Supplier Address: ");
                String address = scanner.nextLine();
                System.out.print("Enter Supplier Phone Number: ");
                String phoneNumber = scanner.nextLine();
                System.out.print("Enter Supplier Email: ");
                String email = scanner.nextLine();
                system.addSupplier(name, address, phoneNumber, email);
            } else if (invChoice == 6) {
                // Updating supplier
                System.out.print("Enter Supplier Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter New Supplier Address: ");
                String newAddress = scanner.nextLine();
                System.out.print("Enter New Supplier Phone Number: ");
                String newPhoneNumber = scanner.nextLine();
                System.out.print("Enter New Supplier Email: ");
                String newEmail = scanner.nextLine();
                system.updateSupplier(name, newAddress, newPhoneNumber, newEmail);
            } else if (invChoice == 7) {
                // Deleting supplier
                System.out.print("Enter Supplier Name: ");
                String name = scanner.nextLine();
                system.deleteSupplier(name);
            } else if (invChoice == 8) {
                // Posting a memo
                System.out.print("Enter Memo Title: ");
                String title = scanner.nextLine();
                System.out.print("Enter Memo Content: ");
                String content = scanner.nextLine();
                system.postMemo(title, content);
            } else if (invChoice == 9) {
                // Viewing memos
                system.viewMemos();
            } else if (invChoice == 10) {
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

    // Method to create user accounts
    public void createUserAccount(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            logger.warning("Attempt to create duplicate user account: " + username);
        } else {
            users.put(username, password);
            System.out.println("User account created successfully.");
            logger.info("Created user account: " + username);
        }
    }

    // Method to login user
    public boolean loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            logger.info("User logged in: " + username);
            return true;
        } else {
            System.out.println("Invalid username or password.");
            logger.warning("Failed login attempt: " + username);
            return false;
        }
    }

    // Method to add inventory
    public void addInventory(String sku, String category, int quantity) {
        if (inventory.containsKey(sku)) {
            inventory.get(sku).updateQuantity(quantity);
        } else {
            InventoryItem item = new InventoryItem(sku, category, quantity);
            inventory.put(sku, item);
            logger.info("Added new inventory: SKU=" + sku + ", Category=" + category + ", Quantity=" + quantity);
        }
    }

    // Method to view inventory
    public void viewInventory() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, InventoryItem> entry : inventory.entrySet()) {
            InventoryItem item = entry.getValue();
            System.out.println("SKU: " + item.sku + ", Category: " + item.category + ", Quantity: " + item.quantity);
            logger.info("Viewed inventory: SKU=" + item.sku + ", Category=" + item.category);
        }
    }

    // Method to delete inventory
    public void deleteInventory(String sku) {
        if (inventory.containsKey(sku)) {
            inventory.remove(sku);
            System.out.println("Inventory item with SKU " + sku + " deleted.");
            logger.info("Deleted inventory item: SKU=" + sku);
        } else {
            System.out.println("Inventory item not found.");
            logger.warning("Attempt to delete non-existent inventory item: SKU=" + sku);
        }
    }
}

