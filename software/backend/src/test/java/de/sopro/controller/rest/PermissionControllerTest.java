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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.sopro.model.Composition;
import de.sopro.model.User.User;
import de.sopro.model.send.SimpleUser;
import de.sopro.model.send.UserAuthorizations;
import de.sopro.repository.CompositionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PermissionControllerTest {

  @Autowired
  private TestData testData;
  @Autowired
  private CompositionRepository compRepo;
  @Autowired
  private PermissionController controller;
  
  @Before
  public void setUp() {
    testData.setUp();
  }
  
  @After
  public void shutDown() {
    testData.shutDown();
  }
  
  @Test
  public void getUserPermissionsTest() {

 // Mock principals for users
    Principal lukasPrincipal = Mockito.mock(java.security.Principal.class);
    Principal jimPrincipal = Mockito.mock(java.security.Principal.class);
    Principal nonExistingUserPrincipal = Mockito.mock(java.security.Principal.class);
    Mockito.when(nonExistingUserPrincipal.getName()).thenReturn("$(nadköniof");

    // create example data
    User lukas =  testData.getLukas();
    User jim = testData.getJim();
    Mockito.when(lukasPrincipal.getName()).thenReturn(lukas.getEmail());
    Mockito.when(jimPrincipal.getName()).thenReturn(jim.getEmail());
    
    Composition comp = testData.getComp();

    // get a non existing comp id
    List<Long> allCompIds = StreamSupport.stream(compRepo.findAll().spliterator(), false).map(c -> c.getId())
        .collect(Collectors.toList());

    long aNonExistingCompID = Collections.max(allCompIds) + 1;

    // check if the correct status is returned
    ResponseEntity<UserAuthorizations> response = controller.getUserPermissions(aNonExistingCompID, lukasPrincipal);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    // Check if the method acts correct for a non existing user principal
    ResponseEntity<UserAuthorizations> response2 = controller.getUserPermissions(comp.getId(),
        nonExistingUserPrincipal);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());

    // Check if the method acts correct if a user can not get the permissions if
    // he/she is not the owner of the selected composition
    ResponseEntity<UserAuthorizations> response3 = controller.getUserPermissions(comp.getId(), jimPrincipal);
    assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());

    // Check if an unregistered user can not get the user permissions for a
    // composition
    ResponseEntity<UserAuthorizations> response4 = controller.getUserPermissions(comp.getId(), null);
    assertEquals(HttpStatus.UNAUTHORIZED, response4.getStatusCode());

    // Check if it works if everything is fine
    ResponseEntity<UserAuthorizations> response5 = controller.getUserPermissions(comp.getId(), lukasPrincipal);
    assertEquals(HttpStatus.OK, response5.getStatusCode());

    UserAuthorizations authorizations = response5.getBody();

    List<SimpleUser> expectedEditors = comp.getEditors().stream().map(u -> u.createSimpleUser())
        .collect(Collectors.toList());
    List<SimpleUser> expectedViewers = comp.getViewers().stream().map(u -> u.createSimpleUser())
        .collect(Collectors.toList());

    assertEquals(expectedEditors, authorizations.getEditors());
    assertEquals(expectedViewers, authorizations.getViewers());
    

  }

  @Test
  public void createUserPermissionTest() {

 // Mock principals for users
    Principal lukasPrincipal = Mockito.mock(java.security.Principal.class);
    Principal jimPrincipal = Mockito.mock(java.security.Principal.class);
    Principal nonExistingUserPrincipal = Mockito.mock(java.security.Principal.class);
    Mockito.when(nonExistingUserPrincipal.getName()).thenReturn("$NJKLAJFKAL");

    // create example data
    User lukas =  testData.getLukas();
    User jim = testData.getJim();
    User liSi = testData.getLiSi();

    Mockito.when(lukasPrincipal.getName()).thenReturn(lukas.getEmail());
    Mockito.when(jimPrincipal.getName()).thenReturn(jim.getEmail());
    
    Composition comp = testData.getComp();

    // Check if a user cannot create permissions for him-/herself
    ResponseEntity<Void> response = controller.createUserPermission(comp.getId(), lukas.getEmail(), "editor",
        lukasPrincipal);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertFalse(comp.getEditors().contains(jim));

    // Check if the method acts correct if a permission for a non existing user is added
    ResponseEntity<Void> response2 = controller.createUserPermission(comp.getId(), "NVJAFPIOUFRA", "viewer",
        lukasPrincipal);
    assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());

    // Check if an invalid permission is handled the right way
    ResponseEntity<Void> response3 = controller.createUserPermission(comp.getId(), jim.getEmail(),
        "noValidPermission", lukasPrincipal);
    assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());

    // check if an editor permission is properly added when everything is fine
    ResponseEntity<Void> response8 = controller.createUserPermission(comp.getId(), jim.getEmail(), "editor",
        lukasPrincipal);
    assertEquals(HttpStatus.CREATED, response8.getStatusCode());
    assertTrue(comp.getEditors().contains(jim));

    // check if a viewer permission is properly added when everything is fine
    ResponseEntity<Void> response9 = controller.createUserPermission(comp.getId(), liSi.getEmail(), "viewer",
        lukasPrincipal);
    assertEquals(HttpStatus.CREATED, response9.getStatusCode());
    assertTrue(comp.getViewers().contains(liSi));
    
  }

  @Test
  public void editUserPermissionTest() {
    
    // Mock principals for users
    Principal lukasPrincipal = Mockito.mock(java.security.Principal.class);
    Principal jimPrincipal = Mockito.mock(java.security.Principal.class);
    Principal nonExistingUserPrincipal = Mockito.mock(java.security.Principal.class);
    Mockito.when(nonExistingUserPrincipal.getName()).thenReturn("$(vfsjuhia)");

    // create example data
    User lukas =  testData.getLukas();
    User jim = testData.getJim();
    User liSi = testData.getLiSi();
    Mockito.when(lukasPrincipal.getName()).thenReturn(lukas.getEmail());
    Mockito.when(jimPrincipal.getName()).thenReturn(jim.getEmail());
    
    Composition comp = testData.getComp();
    comp.getViewers().add(jim);
    jim.getViewable().add(comp);
    comp.getEditors().add(liSi);
    liSi.getEditable().add(comp);

    // get a non existing comp id
    List<Long> allCompIds = StreamSupport.stream(compRepo.findAll().spliterator(), false).map(c -> c.getId())
        .collect(Collectors.toList());

    long aNonExistingCompID = Collections.max(allCompIds) + 1;
    
    // Check if editing permissions for non existing composition is caught the right way
    ResponseEntity<Void> response1 = controller.editUserPermission(aNonExistingCompID, jim.getId(), lukasPrincipal);
    assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
    
    // Check if editing fails if the user is not author of the composition
    ResponseEntity<Void> response2 = controller.editUserPermission(comp.getId(), jim.getId(), jimPrincipal);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
    
    // Check if editing fails if an unregistered user tries to edit permissions
    ResponseEntity<Void> response3 = controller.editUserPermission(comp.getId(), jim.getId(), null);
    assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
    
    ResponseEntity<Void> response4 = controller.editUserPermission(comp.getId(), jim.getId(), lukasPrincipal);
    assertEquals(HttpStatus.OK, response4.getStatusCode());
    assertTrue(comp.getEditors().contains(jim));
    
    ResponseEntity<Void> response5 = controller.editUserPermission(comp.getId(), liSi.getId(), lukasPrincipal);
    assertEquals(HttpStatus.OK, response5.getStatusCode());
    assertTrue(comp.getViewers().contains(liSi));
    
  }

  @Test
  public void deleteUserPermissionTest() {

 // Mock principals for users
    Principal lukasPrincipal = Mockito.mock(java.security.Principal.class);
    Principal jimPrincipal = Mockito.mock(java.security.Principal.class);
    Principal nonExistingUserPrincipal = Mockito.mock(java.security.Principal.class);
    Mockito.when(nonExistingUserPrincipal.getName()).thenReturn("$jauiem");

    // create example data
    User lukas =  testData.getLukas();
    User jim = testData.getJim();
    Mockito.when(lukasPrincipal.getName()).thenReturn(lukas.getEmail());
    Mockito.when(jimPrincipal.getName()).thenReturn(jim.getEmail());
    
    Composition comp = testData.getComp();

    
    
    // Check if deletion works properly if everything is fine
    ResponseEntity<Void> successfullRemoval = controller.deleteUserPermission(comp.getId(), jim.getId(),
        lukasPrincipal);
    assertEquals(HttpStatus.OK, successfullRemoval.getStatusCode());
    assertFalse(comp.getEditors().contains(jim));
    assertFalse(comp.getViewers().contains(jim));
    
    
  }
  
  

}
