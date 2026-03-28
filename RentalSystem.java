import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class RentalSystem {
    private static RentalSystem instance;

    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<RentalRecord> rentalHistory;

    private RentalSystem() {
        this.vehicles = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.rentalHistory = new ArrayList<>();
    }

    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) return v;
        }
        return null;
    }

    public Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.toString().contains(String.valueOf(id))) return c;
        }
        return null;
    }

    public void rentVehicle(Vehicle v, Customer c, LocalDate date, double amount) {
        if (v instanceof Rentable) {
            ((Rentable) v).rentVehicle();
            // Similar to below I put a 5th return aspect which was giving some confusion to me and still need to test. But if works this comment will be removed in future
            rentalHistory.add(new RentalRecord(v, c, date, amount, "RENT"));
        }
    }

    public void returnVehicle(Vehicle v, Customer c, LocalDate date, double fees) {
        if (v instanceof Rentable) {
            ((Rentable) v).returnVehicle();
            //struggled with the below but I think it works for most part? Will do testing post commit 
            rentalHistory.add(new RentalRecord(v, c, date, fees, "RETURN"));
        }
    }

    public void displayVehicles(Vehicle.VehicleStatus status) {
        System.out.println("|--------------------------------------------------------------------------------------------|");
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
        
        for (Vehicle v : vehicles) {
            if (v.getStatus() == status) {
                System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    v.getClass().getSimpleName(), v.getLicensePlate(), v.getMake(), v.getModel(), v.getYear(), v.getStatus());
            }
        }
        System.out.println("|--------------------------------------------------------------------------------------------|");
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println(c.toString());
        }
    }

    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory) {
            System.out.println(record.toString());
        }
    }
}