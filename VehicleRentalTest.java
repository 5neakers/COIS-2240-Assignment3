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
}