import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.io.BufferedWriter; //had to import for the actual text document to work did some minor testing to ensure it worked then deleted txt files prior to commit 
import java.io.FileWriter;
import java.io.File;
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
        loadData(); // Call loadData() from the RentalSystem constructor
    }

    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    private void loadData() {
        // method for loading the actual customer data from the txt file. Similar to all 3 below need to make sure to actually split and trim based on format that it was stored in 
        File cFile = new File("customers.txt");
        if (cFile.exists()) {
            try (Scanner scanner = new Scanner(cFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // Format: "Customer ID: 1 | Name: John"
                    String[] parts = line.split("\\|");
                    int id = Integer.parseInt(parts[0].replace("Customer ID: ", "").trim());
                    String name = parts[1].replace("Name: ", "").trim();
                    customers.add(new Customer(id, name));
                }
            } catch (Exception e) { System.out.println("Error loading customers."); }
        }

        // main aspect of loading vehicles 
        File vFile = new File("vehicles.txt");
        if (vFile.exists()) {
            try (Scanner scanner = new Scanner(vFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] p = line.split(",");
                    // below is selecting the indivudual "parts" that correspond to the split by commas in how it is stored in the actual .txt file 
                    String type = p[0];
                    String plate = p[1];
                    String make = p[2];
                    String model = p[3];
                    int year = Integer.parseInt(p[4]);
                    
                    Vehicle v = null;
                    if (type.equals("Car")) v = new Car(make, model, year, Integer.parseInt(p[6]));
                    else if (type.equals("Minibus")) v = new Minibus(make, model, year, Boolean.parseBoolean(p[6]));
                    else if (type.equals("PickupTruck")) v = new PickupTruck(make, model, year, Double.parseDouble(p[6]), Boolean.parseBoolean(p[7]));
                    else if (type.equals("SportCar")) v = new SportCar(make, model, year, Integer.parseInt(p[6]), Integer.parseInt(p[7]), Boolean.parseBoolean(p[8]));

                    if (v != null) {
                        v.setLicensePlate(plate);
                        v.setStatus(Vehicle.VehicleStatus.valueOf(p[5]));
                        vehicles.add(v);
                    }
                }
            } catch (Exception e) { System.out.println("Error loading vehicles."); }
        }

        // main part for loading of the rental records 
        File rFile = new File("rental_records.txt");
        if (rFile.exists()) {
            try (Scanner scanner = new Scanner(rFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // given the format its stored in we need to split and trim this so its usable by the program 
                    String[] parts = line.split("\\|");
                    String type = parts[0].trim();
                    String plate = parts[1].replace("Plate: ", "").trim();
                    String name = parts[2].replace("Customer: ", "").trim();
                    LocalDate date = LocalDate.parse(parts[3].replace("Date: ", "").trim());
                    double amt = Double.parseDouble(parts[4].replace("Amount: $", "").trim());

                    Vehicle v = findVehicleByPlate(plate);
                    Customer c = null;
                    for(Customer cust : customers) if(cust.getCustomerName().equals(name)) c = cust;

                    if (v != null && c != null) {
                        rentalHistory.add(new RentalRecord(v, c, date, amt, type));
                    }
                }
            } catch (Exception e) { System.out.println("Error loading history."); }
        }
    }
//note added some changes in this commit to the save and additionally a new helper method briefly described below to ensure the type of vehicle is being considered and respective varialbes 
    private void saveVehicle(Vehicle v) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
            // I made some modifications to below for task 2 that now factors in the type of vehicle properly as was igoring that before 
            String extra = "";
            if (v instanceof SportCar) {
                SportCar s = (SportCar) v;
                extra = "," + s.getNumSeats() + ",0,false"; // below adjusts for type of vehicle properly and what needs to be stored in txt 
            } else if (v instanceof Car) extra = "," + ((Car) v).getNumSeats();
            else if (v instanceof Minibus) extra = ",false"; 
            else if (v instanceof PickupTruck) extra = "," + ((PickupTruck) v).getCargoSize() + "," + ((PickupTruck) v).hasTrailer();

            writer.write(v.getClass().getSimpleName() + "," + v.getLicensePlate() + "," + v.getMake() + "," + 
                         v.getModel() + "," + v.getYear() + "," + v.getStatus() + extra);
            writer.newLine();
        } catch (IOException e) { System.out.println("Error saving vehicle."); }
    }

    private void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(customer.toString());
            writer.newLine();
        } catch (IOException e) { System.out.println("Error saving customer."); }
    }

    private void saveRecord(RentalRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true))) {
            writer.write(record.toString());
            writer.newLine();
        } catch (IOException e) { System.out.println("Error saving record."); }
    }

    public boolean addVehicle(Vehicle vehicle) {
        // standard if statement and using existing methods to see if the plate already exists 
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("Can't add the vehicle as it already in the system");
            return false;
        }
        vehicles.add(vehicle);
        saveVehicle(vehicle); 
        return true;
    }

    public boolean addCustomer(Customer customer) {
        // similar to above utilizing existing methods to see if customer already exists
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Can;t add the customer as they already exist in the system!");
            return false;
        }
        customers.add(customer);
        saveCustomer(customer);
        return true;
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
  //modified below in this commit to allow for the ability for the vehicle to NOT be rented if already rented 
    public void rentVehicle(Vehicle v, Customer c, LocalDate date, double amount) throws IOException {
        if (v instanceof Rentable) {
        	// below is specific if statement to determine if vehicle has already been rented or not 
            if (v.getStatus() != Vehicle.VehicleStatus.Available) {
                System.out.println("Vehicle can currently is already being rented :(");
                return; 
            }

            ((Rentable) v).rentVehicle();
            
            RentalRecord record = new RentalRecord(v, c, date, amount, "RENT");
            rentalHistory.add(record);
            saveRecord(record);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt"))) {
                for (Vehicle vehicle : vehicles) {
                    saveVehicleOverride(writer, vehicle);
                }
            } catch (IOException e) {
                System.out.println("Error updating vehicles file.");
            }
        }
    }

    // given the override I have added this for type of vehicle so that it is more properly stored as was having issues with task 1 2. This allows it to factor in actual type via this helper method 
    private void saveVehicleOverride(BufferedWriter writer, Vehicle v) throws IOException {
        String extra = "";
        if (v instanceof Car) extra = "," + ((Car) v).getNumSeats();
        writer.write(v.getClass().getSimpleName() + "," + v.getLicensePlate() + "," + v.getMake() + "," + 
                     v.getModel() + "," + v.getYear() + "," + v.getStatus() + extra);
        writer.newLine();
    }

    public void returnVehicle(Vehicle v, Customer c, LocalDate date, double fees) {
        if (v instanceof Rentable) {
            ((Rentable) v).returnVehicle();
            //struggled with the below but I think it works for most part? Will do testing post commit 
            RentalRecord record = new RentalRecord(v, c, date, fees, "RETURN");
            rentalHistory.add(record);
            saveRecord(record);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt"))) {
                for (Vehicle vehicle : vehicles) {
                    saveVehicleOverride(writer, vehicle);
                }
            } catch (IOException e) { System.out.println("Error updating vehicles file."); }
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