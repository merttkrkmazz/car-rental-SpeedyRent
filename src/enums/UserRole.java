package enums; 

public enum UserRole {
    ADMIN,
    GUEST;  


    public String getDisplayName() {
        switch (this) {
            case ADMIN:
                return "Administrator";
            case GUEST:
                return "Guest";
            default:
                throw new IllegalArgumentException("Unknown UserRole: " + this);
        }
    }
}
