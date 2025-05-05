package enums;

public enum FuelType {

    PETROL, DIESEL, ELECTRIC, HYBRID, CNG, LPG;
    
    
    public String getDisplayName() {
        switch (this) {
            case PETROL:
                return "Petrol";
            case DIESEL:
                return "Diesel";
            case ELECTRIC:
                return "Electric";
            case HYBRID:
                return "Hybrid";
            case CNG:
                return "Compressed Natural Gas (CNG)";
            case LPG:
                return "Liquefied Petroleum Gas (LPG)";
            default:
                throw new IllegalArgumentException("Unknown FuelType: " + this);
        }
    }
}
