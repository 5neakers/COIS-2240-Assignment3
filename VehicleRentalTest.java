import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class VehicleRentalTest {
    @Test
    void testLicensePlate() {
        //create a car to test the plate(s) 
        Car testVehicle= new Car("Toyota", "Corolla", 2011, 5); //used my personal vehicle for this :) 

        // Testing valid plates as described in task 2 
        assertDoesNotThrow(() -> testVehicle.setLicensePlate("AAA100"));
        assertEquals("AAA100", testVehicle.getLicensePlate()); //realized I mistyped plate here so had to correct that to AAA111
        assertDoesNotThrow(() -> testVehicle.setLicensePlate("ABC567"));
        assertDoesNotThrow(() -> testVehicle.setLicensePlate("ZZZ999"));
        assertThrows(IllegalArgumentException.class, () -> testVehicle.setLicensePlate("")); //test for empty string
        assertThrows(IllegalArgumentException.class, () -> testVehicle.setLicensePlate(null)); //test for null
        assertThrows(IllegalArgumentException.class, () -> testVehicle.setLicensePlate("AAA1000")); // Test case for too long 
        assertThrows(IllegalArgumentException.class, () -> testVehicle.setLicensePlate("ZZZ99"));   //test case for too short of a plate
    }
    @Test
    void testRentAndReturnVehicle() {
        Car car = new Car("Honda", "CRV", 2019, 5); //my gf car :)
        car.setLicensePlate("BOO413");
        Customer customer = new Customer(007, "Steve French");

        assertEquals(Vehicle.VehicleStatus.Available, car.getStatus(), "Vehicle should begin as available state");
        RentalSystem system = RentalSystem.getInstance(); //getting the actual instance 
        
        boolean rentResult = system.rentVehicle(car, customer, null, 100.0); //testing rental result 
        
        assertTrue(rentResult, "we should see true here to know its successful");
        assertEquals(Vehicle.VehicleStatus.Rented, car.getStatus(), "should be updated to rented");

 
        boolean doubleRentResult = system.rentVehicle(car, customer, null, 100.0);
        assertFalse(doubleRentResult, "should not be rentable ");

        boolean returnResult = system.returnVehicle(car, customer, null, 0.0); //test for actual return of vehicle 
        assertTrue(returnResult, "should then show as true, for successful return");
        assertEquals(Vehicle.VehicleStatus.Available, car.getStatus(), "vehicle should go back to available");

        // 7. Test Double Returning (Should fail)
        boolean returnAgainResult = system.returnVehicle(car, customer, null, 0.0);  //trying to return same vehicle should result in fail aka false
        assertFalse(returnAgainResult, "given we are returning here should expect false");
    }
}