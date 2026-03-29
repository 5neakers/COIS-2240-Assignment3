public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

    public Vehicle(String make, String model, int year) {
        // below simply calls the new "refactored" method 
        this.make = capitalize(make);
        this.model = capitalize(model);
        
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

   //new helper method below that capitalizes so that it is not done multiple times in code 
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public void setLicensePlate(String plate) {
        if (!isValidPlate(plate)) {
            throw new IllegalArgumentException("Invalid plate format must have 3 letters followed by 3 numbers!");
        }
        this.licensePlate = plate.toUpperCase();
    }
    //new helper method for simply ensure that the plate is valid 
    private boolean isValidPlate(String plate) {
        if (plate == null || plate.isEmpty()) {
            return false;
        }
        return plate.matches("^[A-Za-z]{3}[0-9]{3}$"); //used regex essentially "does it begin with 3 letters" AND "does it end with 3 numbers" if so return true otherwise false as matches is boolean 
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getMake() { return make; }
    public String getModel() { return model;}
    public int getYear() { return year; }
    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }
}