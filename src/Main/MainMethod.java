package Main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Asserts.Assert;
import Workers.*;
import dataManagement.DataMangement;
import ticketmanagement.Tickets;

import static Asserts.Assert.Status.*;


public class MainMethod {
    public static void main(String[] args) throws SQLException {

        DataMangement dataMangement = new DataMangement();
        Connection connection = DBConnection.makeConnection();
        PreparedStatement statement;

        loop1:
        while (true) {

            DataMangement.currentUser = null;
            String emails = GetInput.getInputString("enter the email address; ");
            String password = GetInput.getInputString("enter the password ;");
//            String emails = "jaga@gamil.com";
//            String password = "jaga@123";


            String query = "select * from Employee where email=? and password_hash=?";

            statement = connection.prepareStatement(query);

            statement.setString(1, emails);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            boolean check = false;

          while (resultSet.next()) {
                if (resultSet.getString("email").equals(emails) && resultSet.getString("password_hash").equals(password)) {
                    System.out.println("You have successfully logged in!");
                    if (resultSet.getInt("role_id")==3) {
                        DataMangement.currentUser = new SuperAdmin(resultSet.getString("name"), resultSet.getString("password_hash"), 3, resultSet.getString("phone_number"), resultSet.getString("email"), resultSet.getInt("credits"),resultSet.getInt("worker_id"));
                    } else if (resultSet.getInt("role_id")==1) {
                        DataMangement.currentUser = new Admin(resultSet.getString("name"), resultSet.getString("password_hash"), 1, resultSet.getString("phone_number"), resultSet.getString("email"), resultSet.getInt("credits"),resultSet.getInt("worker_id"));
                    } else if (resultSet.getInt("role_id")==2) {
                        DataMangement.currentUser = new Employee(resultSet.getString("name"), resultSet.getString("password_hash"), 2, resultSet.getString("phone_number"), resultSet.getString("email"),resultSet.getInt("credits"),resultSet.getInt("worker_id"));
                    }
                    check = true;
                }
            }
            System.out.println(DataMangement.currentUser);
            if (!check) {
                System.out.println("Invalid username or password!");
                continue loop1;
            }
            break;
        }
        if (DataMangement.currentUser.getRole()==3) {
            System.out.println("super admin logged in!");
            loop2:while (true) {
                System.out.println("1) Add assert for sales");
                System.out.println("2) see request ");
                System.out.println("3) see employee");
                System.out.println("4) Add employee");
                System.out.println("5) Remove employee");
                System.out.println("6) Logout");
                int choice1 = GetInput.getInputNumbers("Enter your choice: ");
                if (choice1 >= 7) {
                    System.out.println("invalid input");
                    continue loop2;
                }
                switch (choice1) {
                    case 1:
                        GetInput.getInputString("");
                        String productName = GetInput.getInputString("Enter the product name: ");
                        String brandName = GetInput.getInputString("Enter the Brand name: ");
                        int credits = GetInput.getInputNumbers("Enter the credits required: ");
                        GetInput.getInputString("");
                        String model = GetInput.getInputString("Enter the model: ");
                        String description = GetInput.getInputString("Enter the description: ");
                        Assert newAssert = new Assert(productName, AVAILABLE, brandName, description, credits);
                        System.out.println(((SuperAdmin) (DataMangement.getCurrentUser())).addAssertForSale(newAssert));
                        break;
                    case 2:
                        if(!DataMangement.request.isEmpty())
//                            printHeaderForRequest();
                            System.out.println();
                        else{
                            System.out.println("No request available");
                            break;
                        }
                        Admin.seeTickets();
                        break;

                    case 3:
                        Admin.seeAllEmployees();
                        break;

                    case 4:
                        GetInput.getInputString("");
                        String name = GetInput.getInputString("Enter the name: ");
                        String email = GetInput.getInputString("Enter the email: ");
                        String password=GetInput.getInputString("Enter the password: ");
                        String role = GetInput.getInputString("Enter role: ");
                        role = role.equals("Employee")? "2" : role.equals("Admin")? "1" : "3";
                        int credit =GetInput.getInputNumbers("Enter the credit: ");
                        String phone_number=GetInput.getInputString("Enter the phone number: ");

                        Admin.addEmployee(new Employee(name, password, Integer.parseInt(role), phone_number, email, credit,0));
                        break;

                    case 5:
                        GetInput.getInputString("");
                        ((Admin)DataMangement.getCurrentUser()).removeEmployee(GetInput.getInputNumbers("Enter the id of the employee: "));
                        break ;
                    default:
                        break loop2;

                }
            }
        }
        else if (DataMangement.currentUser.getRole()==1) {
            System.out.println("admin logged in!");

            loop2:while (true) {
                System.out.println("1) Add assert for sales");
                System.out.println("2) see request ");
                System.out.println("3) see employee");
                System.out.println("4) send response");
                System.out.println("5) see all tickets");
                System.out.println("6) Ticket Handling") ;
                System.out.println("7) Add credits to employee");
                System.out.println("8) Logout");
                int choice2=GetInput.getInputNumbers("Enter your choice: ");
                if(choice2>8){
                    System.out.println("invalid input");
                    continue loop2;
                }
                switch (choice2) {
                    case 1:
                        GetInput.getInputString("");
                        String productName = GetInput.getInputString("Enter the product name: ");
                        String brandName = GetInput.getInputString("Enter the Brand name: ");
                        int credits = GetInput.getInputNumbers("Enter the credits required: ");
                        GetInput.getInputString("");
                        String model = GetInput.getInputString("Enter the model: ");
                        String description = GetInput.getInputString("Enter the description: ");
                        Assert newAssert = new Assert(productName, AVAILABLE, brandName, description, credits);
                        System.out.println(((SuperAdmin) (DataMangement.getCurrentUser())).addAssertForSale(newAssert));
                        break;
                    case 2:
                        if (!DataMangement.request.isEmpty())
//                            printHeaderForRequest();
                            System.out.println("hi");
                        else {
                            System.out.println("No request available");
                            break;
                        }
                        Admin.seeTickets();
                        break;
                    case 3:
                        Admin.seeAllEmployees();
                        break;
                    case 4:
                        if (DataMangement.request.isEmpty()){
//                            printHeaderForRequest();
                            System.out.println();
        }
                        else {
                            System.out.println("No request available");
                            break;
                        }
                        Admin.seeTickets();
                        int id;
                        String action;
                            id = GetInput.getInputNumbers("Enter the Ticket id: ");
                            GetInput.getInputString("");
                            action = GetInput.getInputString("Enter the action: ");

                        statement = connection.prepareStatement("Select * from Tickets where ticket_id =?");
                        statement.setInt(1, id);
                        ResultSet resultSet = statement.executeQuery();
                        if (!resultSet.next()) {
                            break ;
                        }
                        ((Admin) DataMangement.getCurrentUser()).ticketHandling(new Tickets(
                                        resultSet.getInt("ticket_id"),
                                        resultSet.getInt("created_by"),
                                        resultSet.getString("Title"),
                                        resultSet.getString("description"),
                                        resultSet.getString("status"),
                                        resultSet.getTimestamp("seen_at") != null ? resultSet.getTimestamp("seen_at").toString() : null,
                                        resultSet.getTimestamp("resolved_at") != null ? resultSet.getTimestamp("resolved_at").toString() : null,
                                        resultSet.getInt("assigned_to"),
                                        resultSet.getTimestamp("created_at").toString()
                                ),
                                action);
                      break;


//                    case 5:
//                        // DataMangement.loadAllFromFile();
//                        if(!DataMangement.tickets.isEmpty())
//                            printHeaderForTickets();
//                        else{
//                            System.out.println("No tickets available");
//                            break;
//                        }
//                        dataMangement.displayAvailableTickets();
//                        break ;
//                    case 6:
//                        if(!DataMangement.tickets.isEmpty())
//                            printHeaderForTickets();
//                        else{
//                            System.out.println("No tickets available");
//                            break;
//                        }
//                        dataMangement.displayAvailableTickets();
//                        int op=GetInput.getInputNumbers("Enter the ticket to handle");
//                        Admin.ticketHandling(op);
//                        break;
                    case 7:
                        GetInput.getInputString("");
                        connection.prepareStatement("Select * Employee where worker_id = ?");
                        int employeeId = GetInput.getInputNumbers("Enter the employee ID to add credits: ");
                        int creditsToAdd = GetInput.getInputNumbers("Enter the number of credits to add: ");
                        ((Admin)DataMangement.currentUser)
                                .addCreditsToEmployee(employeeId,creditsToAdd);
                        break;
                }
            }
        }

        else{
            System.out.println("employee logged in!");
//            emploop:while(true) {
            boolean success = DataMangement.currentUser.raiseTicket("System Crash", "The system keeps crashing after login.");

            if (success) {
                System.out.println("Employee " + DataMangement.currentUser.getName() + " successfully raised a ticket.");
            } else {
                System.out.println("Failed to raise the ticket.");
            }
                System.out.println("1) Buy assert");
                System.out.println("2) view my assert");
                System.out.println("3) view assert available for sales");
                System.out.println("4) request for credits");
                System.out.println("5) view response");
                System.out.println("6) Profile");
                System.out.println("7) Rise ticket");
                System.out.println("8) See ticket history");
                System.out.println("9) Logout");
                int choice3 = GetInput.getInputNumbers("Enter your choice: ");
                if(choice3>9){
                    System.out.println("invalid input");
//                    continue emploop;
                }
                switch (choice3) {
                    case 1:
                        System.out.println("Available asserts");
                        if(!DataMangement.assertsForSale.isEmpty()) {
                            System.out.println("Available Assets:");
                            printHeaderForAsserts();
                        }
                        else{
                            System.out.println("No Asserts available");
                            break;
                        }
                        dataMangement.displayAvailableAssets();
                        System.out.println("Available Balance: "+DataMangement.currentUser.getCredits());
                        int assetId = GetInput.getInputNumbers("Enter your which assert you need buy: ");
                        int choice= GetInput.getInputNumbers("Press 1 to confirm buy or press 2 back: ");
                        boolean successFinder = false;
                        if(choice==1)
                            successFinder = DataMangement.currentUser.buyAsset(assetId);
                        if (successFinder) {
                            System.out.println("Asset purchase and assignment successful.");
                        } else {
                            System.out.println("Asset purchase and assignment failed.");
                        }
                        GetInput.getInputString("");
                        break;
                    case 2:
                        if(!DataMangement.assertsForSale.isEmpty())
                            printHeaderForAsserts();
                        else{
                            System.out.println("No Asserts available");
                            break;
                        }
                        DataMangement.currentUser.viewMyAssets();
                        break;
                    case 3:
                        dataMangement.displayAvailableAssets();
                        break;
                    case 4:
//                        GetInput.getInputString("");
//                        String description= GetInput.getInputString("Why u need credits? Give detailed view: ");
//                        int prioriesOption=GetInput.getInputNumbers("Enter your priority: \n 1) high\t \n 2) medium\t \n 3) low\t ");
//                        String priority=prioriesOption == 1? " High " : prioriesOption==2? " Medium " : "Low";
//                        System.out.println(DataMangement.currentUser.askRequest(description, priority));
                        // DataMangement.saveRequestsToFile();
                        break;
//                    case 5:
//                        if(DataMangement.currentUser.response.isEmpty()){
//                            DataMangement.currentUser.viewResponse();
//                            System.out.println("Their is No response");
//                            break;
//                        }
//                        DataMangement.currentUser.viewResponse();
//                        GetInput.getInputString("");
//                        String clear= GetInput.getInputString("Enter your clear all press '1' " +
//                                "or press any key to exit : ");
//                        if (clear.equals("1")) {
//                            DataMangement.currentUser.clearResponse();
//                        }
//                        break;
                    case 6:
                        DataMangement.currentUser.EmployeeInfo();
                        break;
                    case 7:
                        GetInput.getInputString("");
                        String title= GetInput.getInputString("Enter your title: ");
                        String des=GetInput.getInputString("Enter your description: ");
//                        String priories=priorieOption == 1? " High " : priorieOption==2? " Medium " : "Low";
                        System.out.println(DataMangement.currentUser.raiseTicket(title, des));
                        break;
//                    case 8:
//
//                        printHeaderForTickets();
//                        DataMangement.currentUser.viewTicketHistory();
//
//                        break ;
//                    default:
//                        System.out.println("Exiting...");
//                        break emploop;
//                }
//            }
        }
        GetInput.getInputString("");
    }
}
public static void printHeaderForRequest() {
    System.out.printf("%-12s| %-35s| %-10s| %-15s%n", "Request ID", "Description", "Priority", "Employee");
    System.out.println(" ---------------------------------------------------------------------- ");
}
public static void printHeaderForTickets() {
    System.out.printf("%-8s| %-20s| %-50s| %-8s| %-15s| %-10s%n","Ticket Id", "Title", "Description", "Priority", "Employee","Status");
    System.out.println(" ---------------------------------------------------------------------- ");
}
public static void printHeaderForAsserts() {
    System.out.println("-------------------------------------------------------------");
    System.out.printf("| %-10s | %-20s | %-10s | %-20s | %-10s | %-20s | %-10s |\n",
            "Asset ID", "Name", "Status", "Last Updated", "Brand", "Description", "Credits");
    System.out.println("-------------------------------------------------------------");
    }
            }

