public class Task {
    private int id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getCreatedAt() {
        return createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    
    
}
