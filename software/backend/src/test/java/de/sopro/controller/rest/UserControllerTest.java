package de.sopro.controller.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.sopro.model.User.User;
import de.sopro.model.send.DetailUser;
import de.sopro.model.send.SimpleUser;
import de.sopro.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerTest {

  @Autowired
  private UserController controller;
  @Autowired
  private UserRepository userRepo;
  @Autowired
  private TestData testData;

  @Before
  public void setUp() {
    testData.setUp();
  }

  @After
  public void shutDown() {
    testData.shutDown();
  }

  @Test
  public void getUsersTest() {

    // Check if statusCode is correct
    ResponseEntity<Iterable<SimpleUser>> users = controller.getUsers("");
    assertEquals(HttpStatus.OK, users.getStatusCode());

    // get all users from the repository and convert both to lists in order to
    // make the handling easier
    Iterable<User> expected = userRepo.findAll();
    Iterable<SimpleUser> actual = users.getBody();

    List<Long> expectedAsList = StreamSupport.stream(expected.spliterator(), false).map(u -> u.getId())
        .collect(Collectors.toList());
    List<Long> actualAsList = StreamSupport.stream(actual.spliterator(), false).map(u -> u.getId())
        .collect(Collectors.toList());

    assertEquals(expectedAsList, actualAsList);

  }

  @Test
  public void getUserDetailsTest() {

    // create example user
    User user = testData.getLukas();
    User user2 = testData.getJim();
    Principal principal = Mockito.mock(java.security.Principal.class);
    Principal principal2 = Mockito.mock(java.security.Principal.class);
    Mockito.when(principal.getName()).thenReturn(user.getEmail());
    Mockito.when(principal2.getName()).thenReturn(user2.getEmail());

    // get all user ids in order to find the maximum
    List<Long> userIds = StreamSupport.stream(userRepo.findAll().spliterator(), false).map(u -> u.getId())
        .collect(Collectors.toList());

    long max = Collections.max(userIds);

    // this is a not existing id
    long aNonExistingId = max + 1;

    // Check if a user that is not admin can not access the user data
    ResponseEntity<DetailUser> unauthorized = controller.getUserDetails(user.getId(), principal2);
    assertEquals(HttpStatus.UNAUTHORIZED, unauthorized.getStatusCode());

    // Check if a query to get details of a not existing user is caught the
    // right way
    ResponseEntity<DetailUser> response = controller.getUserDetails(aNonExistingId, principal);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    // Check if an administrator can access the user data
    ResponseEntity<DetailUser> otherResponse = controller.getUserDetails(user.getId(), principal);
    DetailUser expectedUser = userRepo.findById(user.getId()).get().createDetailUser();
    DetailUser actualUser = otherResponse.getBody();

    // Check if the status is correct
    assertEquals(HttpStatus.OK, otherResponse.getStatusCode());
    // Check if all attributes are equal
    assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    assertEquals(expectedUser.getTitle(), actualUser.getTitle());

  }

  @Test
  public void editUserTest() {

    DetailUser dUser = testData.getLiSi().createDetailUser();
    dUser.setTitle("Frau");

    List<Long> allUserIds = StreamSupport.stream(userRepo.findAll().spliterator(), false)
        .map(u -> u.getId())
        .collect(Collectors.toList());
    
    long aNonExistingId = Collections.max(allUserIds) + 1;
    
    Principal principal = Mockito.mock(java.security.Principal.class);
    Principal principal2 = Mockito.mock(java.security.Principal.class);
    Mockito.when(principal.getName()).thenReturn(testData.getLukas().getEmail());
    Mockito.when(principal2.getName()).thenReturn(testData.getJim().getEmail());
    
    // Check if an unregistered user can not edit users
    ResponseEntity<Void> resp = controller.editUser(null, testData.getLiSi().getId(), dUser);
    assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    
    // Check if a non existing user can not be edited
    ResponseEntity<Void> response1 = controller.editUser(principal, aNonExistingId, dUser);
    assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
    
    // Check if a user that is not admin can edit a different User
    ResponseEntity<Void> response2 = controller.editUser(principal2, testData.getLiSi().getId(), dUser);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
    
    // Check if an administrator can edit users
    ResponseEntity<Void> response3 = controller.editUser(principal, testData.getLiSi().getId(), dUser);
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertEquals(userRepo.findById(testData.getLiSi().getId()).get().getTitle(), "Frau");    
    

  }
  
  @Test
  public void deleteUserTest() {
    
    Principal principal = Mockito.mock(java.security.Principal.class);
    Principal principal2 = Mockito.mock(java.security.Principal.class);
    Mockito.when(principal.getName()).thenReturn(testData.getLukas().getEmail());
    Mockito.when(principal2.getName()).thenReturn(testData.getJim().getEmail());
    
    User newUser = new User("Alfons" , "der Viertelvorzwoelfte", "alfons@lummerland.de", "Koenig", false);
    newUser.setPassword("einpasswort");
    newUser = userRepo.save(newUser);
    
    List<Long> allUserIds = StreamSupport.stream(userRepo.findAll().spliterator(), false)
        .map(u -> u.getId())
        .collect(Collectors.toList());
    
    long aNonExistingId = Collections.max(allUserIds) + 1;
    
    // Check if an unregistered user can not delete users
    ResponseEntity<Void> response = controller.deleteUser(newUser.getId(), null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertTrue(userRepo.existsById(newUser.getId()));
    
    // Check if a non existing user can not be deleted
    ResponseEntity<Void> response1 = controller.deleteUser(aNonExistingId, principal);
    assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
    
    // Check if a user that is not an administrator can not delete users
    ResponseEntity<Void> response2 = controller.deleteUser(newUser.getId(), principal2);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
    assertTrue(userRepo.existsById(newUser.getId()));
    
    // Check if a user that is admin can delete users
    ResponseEntity<Void> response3 = controller.deleteUser(newUser.getId(), principal);
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertFalse(userRepo.existsById(newUser.getId()));
    
  }

}
