import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.io.BufferedWriter; //had to import for the actual text document to work did some minor testing to ensure it worked then deleted txt files prior to commit 
import java.io.FileWriter;
import java.io.IOException;

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

    // for saveVehicle since we are writing to a txt file need to include an actual try and catch otherwise won't compile similar for other methods 
    private void saveVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
            writer.write(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + 
                         vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving vehicle.");
        }
    }

   
    private void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(customer.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving customer.");
        }
    }

    
    private void saveRecord(RentalRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true))) {
            writer.write(record.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving record.");
        }
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle); 
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
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
            RentalRecord record = new RentalRecord(v, c, date, amount, "RENT");
            rentalHistory.add(record);
            
            // This is where we call our new method to write to txt 
            saveRecord(record);

            //this part accomplishes the one goal of being asked to update to ensure full list in vehciles.txt 
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt"))) {
                for (Vehicle vehicle : vehicles) {
                    writer.write(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + 
                                 vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating vehicles file.");
            }
        }
    }

    public void returnVehicle(Vehicle v, Customer c, LocalDate date, double fees) {
        if (v instanceof Rentable) {
            ((Rentable) v).returnVehicle();
            //struggled with the below but I think it works for most part? Will do testing post commit 
            RentalRecord record = new RentalRecord(v, c, date, fees, "RETURN");
            rentalHistory.add(record);
            
            // again similar to above 
            saveRecord(record);

            //identical to above but required to ensure meet that it is always up to date list in vehicles.txt 
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt"))) {
                for (Vehicle vehicle : vehicles) {
                    writer.write(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + 
                                 vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating vehicles file.");
            }
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