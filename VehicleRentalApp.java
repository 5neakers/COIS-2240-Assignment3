import java.util.Scanner;
import java.time.LocalDate;

public class VehicleRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // create a single instance for singleton 
        RentalSystem rentalSystem = RentalSystem.getInstance();

        while (true) {
            System.out.println("\n1: Add Vehicle\n2: Add Customer\n3: Rent Vehicle\n4: Return Vehicle\n5: Display Available Vehicles\n6: Show Rental History\n0: Exit\n");
            
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("  1: Car\n  2: Minibus\n  3: Pickup Truck");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter license plate: ");
                    String plate = scanner.nextLine().toUpperCase();
                    System.out.print("Enter make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    Vehicle vehicle;
                    if (type == 1) {
                        System.out.print("Enter number of seats: ");
                        int seats = scanner.nextInt();
                        vehicle = new Car(make, model, year, seats);
                    } else if (type == 2) {
                        System.out.print("Is accessible? (true/false): ");
                        boolean isAcc = scanner.nextBoolean();
                        vehicle = new Minibus(make, model, year, isAcc);
                    } else if (type == 3) {
                        System.out.print("Enter the cargo size: ");
                        double size = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Has trailer? (true/false): ");
                        boolean trailer = scanner.nextBoolean();
                        vehicle = new PickupTruck(make, model, year, size, trailer);
                    } else {
                        vehicle = null;
                    }
                    
                    if (vehicle != null) {
                        vehicle.setLicensePlate(plate);
                        rentalSystem.addVehicle(vehicle);
                        System.out.println("Vehicle added successfully.");
                    }
                    break;

                case 2:
                    System.out.print("Enter customer ID: ");
                    int cid = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    String cname = scanner.nextLine();
                    rentalSystem.addCustomer(new Customer(cid, cname));
                    System.out.println("Customer added successfully.");
                    break;
                    
                case 3:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.Available);
                    System.out.print("Enter license plate: ");
                    String rPlate = scanner.nextLine().toUpperCase();
                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers();
                    System.out.print("Enter customer ID: ");
                    int rCid = scanner.nextInt();
                    System.out.print("Enter rental amount: ");
                    double rAmt = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vRent = rentalSystem.findVehicleByPlate(rPlate);
                    Customer cRent = rentalSystem.findCustomerById(rCid);

                    if (vRent != null && cRent != null) {
                        rentalSystem.rentVehicle(vRent, cRent, LocalDate.now(), rAmt);
                    }
                    break;

                case 4:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.Rented);
                    System.out.print("Enter license plate: ");
                    String retPlate = scanner.nextLine().toUpperCase();
                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers();
                    System.out.print("Enter customer ID: ");
                    int retCid = scanner.nextInt();
                    System.out.print("Enter return fees: ");
                    double fees = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vRet = rentalSystem.findVehicleByPlate(retPlate);
                    Customer cRet = rentalSystem.findCustomerById(retCid);

                    if (vRet != null && cRet != null) {
                        rentalSystem.returnVehicle(vRet, cRet, LocalDate.now(), fees);
                    }
                    break;
                    
                case 5:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.Available);
                    break;
                
                case 6:
                    rentalSystem.displayRentalHistory();
                    break;
                    
                case 0:
                    scanner.close();
                    System.exit(0);
            }
        }
    }
}