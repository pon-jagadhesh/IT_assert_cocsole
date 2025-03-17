package ticketmanagement;

import Main.DBConnection;
import Workers.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Tickets {
    int ticketId = Math.abs((int) System.currentTimeMillis());
    int createdBy;
    public String title;
    String description;
    public String status;
    String seen_at;
    String resolved_at;
    int assignedTo;
    String created_at;

    public Tickets(int createdBy, String title, String description) {
        this.ticketId = Math.abs((int) System.currentTimeMillis());
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.status = "Open";
        this.created_at = new Timestamp(System.currentTimeMillis()).toString();
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Tickets() {
    }




    public Tickets(int ticketId, int createdBy, String title, String description, String status, String seen_at, String resolved_at, int assignedTo, String created_at) {
        this.ticketId = ticketId;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.status = status;
        this.seen_at = seen_at;
        this.resolved_at = resolved_at;
        this.assignedTo = assignedTo;
        this.created_at = created_at;
    }

    // Method to save ticket to database
    public boolean saveToDatabase() {
        Connection conn= DBConnection.makeConnection();
        String sql = "INSERT INTO tickets (ticket_id, created_by, title, description, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.ticketId);
            stmt.setInt(2, this.createdBy);
            stmt.setString(3, this.title);
            stmt.setString(4, this.description);
            stmt.setString(5, this.status);
            stmt.setTimestamp(6, Timestamp.valueOf(this.created_at));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public void assignTo(int admin) {
        this.assignedTo = admin;
        this.status = "In Progress";
        System.out.println("Ticket assigned to admin: " + admin);
    }

    public void resolveTicket() {
        this.status = "Resolved";
        this.resolved_at = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("Ticket resolved at: " + this.resolved_at);
    }

    // Method for admin to close ticket
    public void closeTicket() {
        this.status = "Closed";
        System.out.println("Ticket closed.");
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSeen_at() {
        return seen_at;
    }

    public void setSeen_at(String seen_at) {
        this.seen_at = seen_at;
    }

    public String getResolved_at() {
        return resolved_at;
    }

    public void setResolved_at(String resolved_at) {
        this.resolved_at = resolved_at;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String toString() {
        System.out.printf("%-8s| %-20s| %-50s| %-8s| %-15s| %-10s%n",ticketId, title, description, createdBy,status);
        return "";
    }
}
