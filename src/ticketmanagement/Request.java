package ticketmanagement;


public class Request {
    private int request_id;
    private int created_by;
    private String description;
    private String status;
    private String created_at;
    private String modified_at;

    public Request() {
    }

    public Request(int request_id, int created_by, String description,String status) {
        this.request_id = request_id;
        this.created_by = created_by;
        this.description = description;
        this.status = status;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }
    @Override
    public String toString() {
        return "Request{" +
                "request_id=" + request_id +
                ", created_by=" + created_by +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", created_at=" + created_at +
                ", modified_at=" + modified_at +
                '}';
    }
}
