package com.medici.assessment;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static io.restassured.RestAssured.*;
//import static org.hamcrest.Matchers.*;

public class ApiTests {

    // Base URL for the CRUD operations
    private final String baseUrl = "https://crudcrud.com/api/e918226c57dc4ca8b901050aa60a2fbc/users/";

    // Logger for logging messages
    private final Logger logger = Logger.getLogger(ApiTests.class.getName());

    // Test method to perform CRUD operations
    @Test(priority = 1)
    public void testCrudOperations() {
        // SoftAssert to collect multiple failures without halting the entire test suite
        SoftAssert softAssert = new SoftAssert();
        
        // Create a user and extract the user ID
        String userId = createUser(softAssert);
        
        // Read the created user using the extracted user ID
        readUser(softAssert, userId);
        
        // Update the created user using the extracted user ID
        updateUser(softAssert, userId);
        
        // Delete the created user using the extracted user ID
        deleteUser(softAssert, userId);
        
        // Assert all soft assertions at the end of the test method
        softAssert.assertAll();
    }

    // Method to create a user and return the user ID
    private String createUser(SoftAssert softAssert) {
        // Test data for creating a user
        HashMap<String, String> userData = generateUserData();
        String userId = null;

        try {
            // Send POST request to create user and extract the user ID
            userId = given()
                    .contentType("application/json")
                    .body(userData)
                    .when()
                    .post(baseUrl)
                    .then()
                    .statusCode(201)
                    .extract().path("_id");

            // Log success message
            logger.log(Level.INFO, "User created successfully with ID: " + userId);

            // Assert that user ID is not null
            softAssert.assertNotNull(userId, "User ID should not be null");
        } catch (Exception e) {
            // Log error message
            logger.log(Level.SEVERE, "Error occurred while creating user: " + e.getMessage());
            
            // Fail the test with a descriptive message
            softAssert.fail("Failed to create user: " + e.getMessage());
        }

        return userId;
    }

    // Method to read a user using the provided user ID
    private void readUser(SoftAssert softAssert, String userId) {
        try {
            // Send GET request to read user details
            given()
                    .when()
                    .get(baseUrl + userId)
                    .then()
                    .statusCode(200)
                    .log().all();

            // Log success message
            logger.log(Level.INFO, "User read successfully with ID: " + userId);
        } catch (Exception e) {
            // Log error message
            logger.log(Level.SEVERE, "Error occurred while reading user: " + e.getMessage());
            softAssert.fail("Failed to read user: " + e.getMessage());
        }
    }

    // Method to update a user using the provided user ID
    private void updateUser(SoftAssert softAssert, String userId) {
        // Test data for updating a user
        HashMap<String, String> updatedUserData = generateUserData();
        updatedUserData.put("username", "UpdatedUser");
        updatedUserData.put("firstname", "UpdatedFirstName");

        try {
            // Send PUT request to update user details
            given()
                    .contentType("application/json")
                    .body(updatedUserData)
                    .when()
                    .put(baseUrl + userId)
                    .then()
                    .statusCode(200)
                    .log().all();

            // Log success message
            logger.log(Level.INFO, "User updated successfully with ID: " + userId);
        } catch (Exception e) {
            // Log error message
            logger.log(Level.SEVERE, "Error occurred while updating user: " + e.getMessage());
            softAssert.fail("Failed to update user: " + e.getMessage());
        }
    }

    // Method to delete a user using the provided user ID
    private void deleteUser(SoftAssert softAssert, String userId) {
        try {
            // Send DELETE request to delete user
            given()
                    .when()
                    .delete(baseUrl + userId)
                    .then()
                    .statusCode(204)
                    .log().all();

            // Log success message
            logger.log(Level.INFO, "User deleted successfully with ID: " + userId);
        } catch (Exception e) {
            // Log error message
            logger.log(Level.SEVERE, "Error occurred while deleting user: " + e.getMessage());
            softAssert.fail("Failed to delete user: " + e.getMessage());
        }
    }

    // Method to generate test data for creating/updating a user
    private HashMap<String, String> generateUserData() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("username", "TestUser");
        userData.put("firstname", "John");
        userData.put("surname", "Doe");
        userData.put("mobile_number", "1234567890");
        return userData;
    }
}

       
