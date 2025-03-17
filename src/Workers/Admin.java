package Workers;

import Asserts.Assert;
import Main.DBConnection;
import dataManagement.DataMangement;
import ticketmanagement.Tickets;

import java.sql.*;
import java.time.LocalDateTime;

public class Admin extends Employee {
    static Connection connection = DBConnection.makeConnection();
    public Admin(String name, String password, int role, String phone_number, String email,int credits, int id)  {
        super(name, password, role, phone_number, email,credits, id);
    }

    public static void seeAllEmployees() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery("Select * from Employee");
        while (resultSet.next()){
            new Employee(resultSet.getString("name"), resultSet.getString("password_hash"), resultSet.getInt("role_id"), resultSet.getString("phone_number"), resultSet.getString("email"), resultSet.getInt("credits"), resultSet.getInt("worker_id")).EmployeeInfo();
            System.out.println("===================================");
        }
    }

    public String addAssertForSale(Assert asserts) throws SQLException {
        DataMangement.assertsForSale.add(asserts);
        PreparedStatement statement = connection.prepareStatement("insert into Assets(name,status,brandName,descrition,credits) values(?,?,?,?,?);");
        statement.setString(1,asserts.getName());
        statement.setString(2, asserts.status.toString());
        statement.setString(3,asserts.getBrandName());
        statement.setString(4,asserts.getDescription());
        statement.setInt(5,asserts.getCredits());
        statement.executeUpdate();
        return asserts.getName() + " Successfully added ";
    }
    public static void seeTickets() throws SQLException {
        ResultSet resultSet=connection.createStatement().executeQuery("select * from Tickets");
        while (resultSet.next()){
//            Tickets tickets=new Tickets();
            System.out.println(resultSet.getString("ticket_id")+"\t"+resultSet.getString("Title")+"\t"+resultSet.getString("description")+"\t"+resultSet.getString("status"));
        }
    }
    public static void addEmployee(Employee employee) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into Employee(name,password_hash,role_id,phone_number,email,credits) values(?,?,?,?,?,?);");
        statement.setString(1,employee.getName());
        statement.setString(2,employee.getPassword());
        statement.setInt(3,employee.getRole());
        statement.setString(4,employee.getPhone_number());
        statement.setString(5,employee.getEmail());
        statement.setInt(6,employee.getCredits());
        statement.executeUpdate();
        System.out.println("Employee Added Successfully");
    }

    public void removeEmployee(int  id) {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from Employee where worker_id=?");
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Employee Removed Successfully");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public void ticketHandling(Tickets ticket, String action) {
        Connection conn = DBConnection.makeConnection();
        String sql = null;

        switch (action.toLowerCase()) {
            case "assign":
                ticket.assignTo(this.getId());
                sql = "UPDATE Tickets SET assigned_to = ?, status = ? WHERE ticket_id = ?";
                break;
            case "resolve":
                ticket.resolveTicket();
                sql = "UPDATE Tickets SET status = ?, resolved_at = ? WHERE ticket_id = ?";
                break;
            case "close":
                ticket.closeTicket();
                sql = "UPDATE Tickets SET status = ? WHERE ticket_id = ?";
                break;
            default:
                System.out.println("Invalid action.");
                return;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticket.getStatus()); // Common for all cases

            if (action.equalsIgnoreCase("assign")) {
                stmt.setInt(1, ticket.getAssignedTo()); // Assigned Admin ID
                stmt.setString(2, ticket.getStatus());
                stmt.setInt(3, ticket.getTicketId());
            } else if (action.equalsIgnoreCase("resolve")) {
                stmt.setString(1, ticket.getStatus());
                stmt.setTimestamp(2, Timestamp.valueOf(ticket.getResolved_at()));
                stmt.setInt(3, ticket.getTicketId());
            } else {
                stmt.setInt(2, ticket.getTicketId());
            }

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Ticket updated successfully in the database.");
            } else {
                System.out.println("Failed to update the ticket.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCreditsToEmployee(int employeeId,int creditsToAdd) {


        String selectQuery = "SELECT * FROM Employee WHERE worker_id = ?";
        String updateQuery = "UPDATE Employee SET credits = credits + ? WHERE worker_id = ?";

        try {
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery) ;

            selectStatement.setInt(1, employeeId);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                updateStatement.setInt(1, creditsToAdd);
                updateStatement.setInt(2, employeeId);

                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Credits added successfully!");
                } else {
                    System.out.println("Failed to add credits.");
                }
            } else {
                System.out.println("Employee with ID " + employeeId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addTicketComment(int ticketId, int adminId, String comment) {
        String query = "INSERT INTO comments (ticket_id, user_id, comment, commented_at) VALUES (?, ?, ?, ?)";

        try (
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ticketId);
            statement.setInt(2, adminId);
            statement.setString(3, comment);
            statement.setTimestamp(4,Timestamp.valueOf(LocalDateTime.now())); // commented_at

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Comment added successfully!");
                return true;
            } else {
                System.out.println("Failed to add comment.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

