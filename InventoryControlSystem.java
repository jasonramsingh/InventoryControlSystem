import java.sql.*;
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

    // JDBC Connection method
    public Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/InventoryDB", "root", "Ramsin11");
    }

    // Method to print inventory report from database
    public void printInventoryReport() {
        try (Connection con = connectToDatabase()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");

            System.out.println("Inventory Report:");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                String sku = rs.getString("sku");
                String category = rs.getString("category");
                int quantity = rs.getInt("quantity");
                System.out.printf("SKU: %s, Category: %s, Quantity: %d\n", sku, category, quantity);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching inventory report: " + e.getMessage());
        }
    }

    // Method to print supplier report from database
    public void printSupplierReport() {
        try (Connection con = connectToDatabase()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM suppliers");

            System.out.println("Supplier Report:");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phone = rs.getString("phone_number");
                String email = rs.getString("email");
                System.out.printf("Name: %s, Address: %s, Phone: %s, Email: %s\n", name, address, phone, email);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching supplier report: " + e.getMessage());
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
            System.out.println("\n1. View Inventory Report\n2. View Supplier Report\n3. Add Inventory\n4. Delete Inventory\n5. Add Supplier\n6. Update Supplier\n7. Delete Supplier\n8. Post Memo\n9. View Memos\n10. Logout");
            System.out.print("Enter your choice: ");
            int invChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (invChoice == 1) {
                system.printInventoryReport();
            } else if (invChoice == 2) {
                system.printSupplierReport();
            } else if (invChoice == 3) {
                // Adding inventory
                System.out.print("Enter Product SKU: ");
                String sku = scanner.nextLine();
                System.out.print("Enter Product Category: ");
                String category = scanner.nextLine();
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                system.addInventory(sku, category, quantity);
            } else if (invChoice == 4) {
                // Deleting inventory
                System.out.print("Enter Product SKU: ");
                String sku = scanner.nextLine();
                system.deleteInventory(sku);
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
                System.out.print("Enter New Address: ");
                String address = scanner.nextLine();
                System.out.print("Enter New Phone Number: ");
                String phoneNumber = scanner.nextLine();
                System.out.print("Enter New Email: ");
                String email = scanner.nextLine();
                system.updateSupplier(name, address, phoneNumber, email);
            } else if (invChoice == 7) {
                // Deleting supplier
                System.out.print("Enter Supplier Name: ");
                String name = scanner.nextLine();
                system.deleteSupplier(name);
            } else if (invChoice == 8) {
                // Posting memo
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

    // User account management methods
    public void createUserAccount(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            logger.warning("Attempt to create duplicate username: " + username);
        } else {
            users.put(username, password);
            System.out.println("User account created.");
            logger.info("Created user account for: " + username);
        }
    }

    public boolean loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful.");
            logger.info("User logged in: " + username);
            return true;
        } else {
            System.out.println("Invalid username or password.");
            logger.warning("Failed login attempt for username: " + username);
            return false;
        }
    }

    // Inventory management methods
    public void addInventory(String sku, String category, int quantity) {
        if (inventory.containsKey(sku)) {
            System.out.println("Product SKU already exists. Consider updating its quantity.");
            logger.warning("Attempt to add duplicate SKU: " + sku);
        } else {
            InventoryItem item = new InventoryItem(sku, category, quantity);
            inventory.put(sku, item);
            System.out.println("Product " + sku + " added to inventory.");
            logger.info("Added product SKU: " + sku);
        }
    }

    public void deleteInventory(String sku) {
        if (inventory.containsKey(sku)) {
            inventory.remove(sku);
            System.out.println("Product " + sku + " removed from inventory.");
            logger.info("Deleted product SKU: " + sku);
        } else {
            System.out.println("Product SKU not found.");
            logger.warning("Attempt to delete non-existent SKU: " + sku);
        }
    }
}
