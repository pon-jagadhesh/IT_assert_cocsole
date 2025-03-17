package dataManagement;


import Asserts.Assert;
import Main.DBConnection;
import ticketmanagement.Request;
import ticketmanagement.Tickets;
import Workers.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataMangement {
    public static Employee currentUser;
    public static Map<String, Employee> user = new HashMap<>(10);
    public static ArrayList<Assert> assertsForSale = new ArrayList<>(10);
    public static ArrayList<Request> request = new ArrayList<Request>(10);
    public static ArrayList<Tickets> tickets = new ArrayList<Tickets>(10);
    Connection connection= DBConnection.makeConnection();



//    public static void register(){
//        GetInput.getInputString("");
//        String name = GetInput.getInputString("Enter name: ");
//        String email = GetInput.getInputString("Enter email: ");
//        String password;
//        do {
//            password = GetInput.getInputString("Enter password: ");
//        } while (password.length() <= 8);
//
//        String team = GetInput.getInputString("Enter team: ");
//        String reportingTo = GetInput.getInputString("Enter reporting to: ");
//        int credit = 0;
//
//        Employee checkEmployee = new Employee(name, email, password, team, reportingTo, credit);
//        user.put(name,checkEmployee);
//        DataMangement.currentUser = checkEmployee;
//        // saveUsersToFile();
//    }
    public void removeEmployee(String name) {
        if (user.containsKey(name)) {
            user.remove(name);
            System.out.println("Employee removed successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }


    // Getter and Setter

    public static Person getCurrentUser() {
        return currentUser;
    }
    public void displayAvailableAssets() {
        String query = "SELECT * FROM Asset WHERE status = 'Available'";

        try (
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Check if there are any available assets
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No available assets found.");
                return;
            }


            while (resultSet.next()) {
                int assetId = resultSet.getInt("asset_id");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                String lastUpdated = resultSet.getString("last_updated");
                String brandName = resultSet.getString("brandName");
                String description = resultSet.getString("description");
                int credits = resultSet.getInt("credits");

                System.out.printf("| %-10d | %-20s | %-10s | %-20s | %-10s | %-20s | %-10d |\n",
                        assetId, name, status, lastUpdated, brandName, description, credits);
            }

            System.out.println("-------------------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void displayAvailableTickets(){
        for(Tickets Tickets:tickets){
            System.out.println(Tickets.toString());
        }
    }

    public Map<String, Employee> getUser() {
        return user;
    }
    public void setUser(String key, Employee value) {
        user.put(key, value);
    }

    public void setUser(Map<String, Employee> user) {
        DataMangement.user = user;
    }

    public static ArrayList<Assert> getAssertsForSale() {
        return assertsForSale;
    }

    public static void setAssertsForSale(ArrayList<Assert> assertsForSale) {
        DataMangement.assertsForSale = assertsForSale;
    }
}
