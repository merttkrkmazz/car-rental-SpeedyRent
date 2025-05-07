package enums;  

public enum TransmissionType {
    MANUAL, 
    AUTOMATIC, 
    SEMI_AUTOMATIC; 


    public String getDisplayName() {
        switch (this) {
            case MANUAL:
                return "Manual";
            case AUTOMATIC:
                return "Automatic";
            case SEMI_AUTOMATIC:
                return "Semi-Automatic";
            default:
                throw new IllegalArgumentException("Unknown TransmissionType: " + this);
        }
    }
}
