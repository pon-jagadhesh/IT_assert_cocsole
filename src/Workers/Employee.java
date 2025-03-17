package Workers;

import Asserts.Assert;
import Main.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Employee extends Person {
    private int id;
    private String email;
    private String phone_number;
    private int role;
    private String hireDate;
    private int credits;
    private ArrayList<Assert> asserts=new ArrayList<>();
    Connection connection=DBConnection.makeConnection();

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Employee(String name, String password, int role, String phone_number, String email, int credits, int id) {
        super(name, password);
        this.phone_number = phone_number;
        this.role = role;
        this.email = email;
        this.id = id;
        this.credits=credits;
    }
    public boolean buyAsset( int assetId) {
        int employeeId=this.getId();
        String checkAssetQuery = "SELECT * FROM Asset WHERE asset_id = ? AND status = 'Available'";
        String checkEmployeeCreditsQuery = "SELECT credits FROM Worker WHERE worker_id = ?";
        String updateAssetQuery = "UPDATE Asset SET status = 'Und' WHERE asset_id = ?";
        String deductCreditsQuery = "UPDATE Worker SET credits = credits - ? WHERE worker_id = ?";
        String assignAssetQuery = "INSERT INTO AssignedAssets (worker_id, asset_id, assigned_at) VALUES (?, ?, ?)";

        try {
             PreparedStatement checkAssetStmt = connection.prepareStatement(checkAssetQuery);
             PreparedStatement checkEmployeeCreditsStmt = connection.prepareStatement(checkEmployeeCreditsQuery);
             PreparedStatement updateAssetStmt = connection.prepareStatement(updateAssetQuery);
             PreparedStatement deductCreditsStmt = connection.prepareStatement(deductCreditsQuery);
             PreparedStatement assignAssetStmt = connection.prepareStatement(assignAssetQuery);

            // Step 1: Check if the asset is available
            checkAssetStmt.setInt(1, assetId);
            ResultSet assetResult = checkAssetStmt.executeQuery();
            if (!assetResult.next()) {
                System.out.println("Asset is not available.");
                return false;
            }

            int assetCredits = assetResult.getInt("credits");

            checkEmployeeCreditsStmt.setInt(1, employeeId);
            ResultSet employeeResult = checkEmployeeCreditsStmt.executeQuery();
            if (!employeeResult.next()) {
                System.out.println("Employee not found.");
                return false;
            }

            int employeeCredits = employeeResult.getInt("credits");
            if (employeeCredits < assetCredits) {
                System.out.println("Not enough credits to buy the asset.");
                return false;
            }

            updateAssetStmt.setInt(1, assetId);
            int updatedRows = updateAssetStmt.executeUpdate();
            if (updatedRows == 0) {
                System.out.println("Failed to update asset status.");
                return false;
            }

            deductCreditsStmt.setInt(1, assetCredits);
            deductCreditsStmt.setInt(2, employeeId);
            updatedRows = deductCreditsStmt.executeUpdate();
            if (updatedRows == 0) {
                System.out.println("Failed to deduct credits.");
                return false;
            }

            assignAssetStmt.setInt(1, employeeId);
            assignAssetStmt.setInt(2, assetId);
            assignAssetStmt.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            updatedRows = assignAssetStmt.executeUpdate();
            if (updatedRows == 0) {
                System.out.println("Failed to assign asset.");
                return false;
            }

            System.out.println("Asset purchased and assigned successfully.");
            return true;

         }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public ArrayList<Assert> getAsserts() {
        return asserts;
    }

    public void setAsserts(ArrayList<Assert> asserts) {
        this.asserts = asserts;
    }

    public boolean raiseTicket(String title, String description) {
        Connection conn = DBConnection.makeConnection();
        int ticketId = Math.abs((int) System.currentTimeMillis()); // Generate unique ticket ID
        String createdAt = new Timestamp(System.currentTimeMillis()).toString();
        String status = "Open";

        String sql = "INSERT INTO Tickets (ticket_id, created_by, title, description, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            stmt.setInt(2,  id);
            stmt.setString(3, title);
            stmt.setString(4, description);
            stmt.setString(5, status);
            stmt.setTimestamp(6, Timestamp.valueOf(createdAt));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Ticket raised successfully! Ticket ID: " + ticketId);
                return true;
            } else {
                System.out.println("Failed to raise the ticket.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void EmployeeInfo(){

            System.out.println("===================================");
            System.out.println("|       Employee Information      |");
            System.out.println("===================================");
            System.out.printf("| %-15s | %-15s |\n", "Field", "Value");
            System.out.println("-----------------------------------");
            System.out.printf("| %-15s | %-15s |\n", "Name", getName());
            System.out.printf("| %-15s | %-15d |\n", "Employee ID", id);
            System.out.printf("| %-15s | %-15s |\n", "Email", getEmail());
            System.out.printf("| %-15s | %-15d |\n", "Credit", credits);
//            System.out.printf("| %-15s | %-15s |\n", "Reporting To", getReportingTo());
            System.out.println("===================================");

    }

    public void viewMyAssets() {
        int employeeId=getId();
        String query = "SELECT a.asset_id, a.name, a.status, a.brandName, a.description, a.credits " +
                "FROM Asset a " +
                "JOIN AssignedAssets aa ON a.asset_id = aa.asset_id " +
                "WHERE aa.worker_id = ?";

        try (
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, employeeId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No assets assigned to you.");
                    return;
                }

                System.out.println("Your Assigned Assets:");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-10s | %-10s | %-20s | %-10s |\n",
                        "Asset ID", "Name", "Status", "Brand", "Description", "Credits");
                System.out.println("-------------------------------------------------------------");

                while (resultSet.next()) {
                    int assetId = resultSet.getInt("asset_id");
                    String name = resultSet.getString("name");
                    String status = resultSet.getString("status");
                    String brandName = resultSet.getString("brandName");
                    String description = resultSet.getString("description");
                    int credits = resultSet.getInt("credits");

                    System.out.printf("| %-10d | %-20s | %-10s | %-10s | %-20s | %-10d |\n",
                            assetId, name, status, brandName, description, credits);
                }

                System.out.println("-------------------------------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
