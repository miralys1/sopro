package de.sopro.controller.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.sopro.controller.rest.ServiceController;
import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;
import de.sopro.model.send.SendService;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.TagRepository;
import de.sopro.repository.UserRepository;
import junit.framework.Assert;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ServiceControllerTest {

  // TODO Create fictional formats for testing (currently, there are no formats
  // in the testing methods)

  @Autowired
  private ServiceRepository serviceRepo;
  @Autowired
  private ServiceController controller;
  @Autowired
  private TagRepository tagRepo;
  @Autowired
  private FormatRepository formatRepo;
  @Autowired
  private TestData testData;
  @Autowired
  private UserRepository userRepo;

  @Before
  public void setUp() {
    testData.setUp();
  }

  @After
  public void shutDown() {
    testData.shutDown();
  }

  @Test
  public void getServicesTest() {

    // Get all services stored in the repo directly and via the controller and
    // check if they return identical id's
    ResponseEntity<Iterable<SendService>> response = controller.getServices();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<Long> actual = StreamSupport.stream(response.getBody().spliterator(), false).map(s -> s.getId())
        .collect(Collectors.toList());
    List<Long> expected = StreamSupport.stream(serviceRepo.findAll().spliterator(), false)
            .filter(s -> !s.getName().equals("Service nicht existent")).map(s -> s.getId())
        .collect(Collectors.toList());

    assertEquals(expected, actual);

  }

  @Test
  public void getServiceDetailsTest() {

    // Fetch example service
    Service s = testData.getTpModeller();

    // create the service details directly and indirectly
    SendService expectedService = s.createSendService();
    SendService actualService = controller.getServiceDetails(s.getId()).getBody();

    // Compare all attributes
    assertEquals(expectedService.getId(), actualService.getId());
    assertEquals(expectedService.getName(), actualService.getName());
    assertEquals(expectedService.getDate(), actualService.getDate());
    assertEquals(expectedService.getLogo(), actualService.getLogo());
    assertEquals(expectedService.getOrganisation(), actualService.getOrganisation());
    assertEquals(expectedService.getVersion(), actualService.getVersion());

    for (String t : expectedService.getTags()) {
      assertTrue(actualService.getTags().contains(t));
    }

    for (Format f : expectedService.getFormatIn()) {
      assertTrue(actualService.getFormatIn().contains(f));
    }

    for (Format f : expectedService.getFormatOut()) {
      assertTrue(actualService.getFormatOut().contains(f));
    }

  }

  @Test
  public void deleteServiceTest() {

    // Mock principals
    Principal adminPrincipal = Mockito.mock(Principal.class);
    Mockito.when(adminPrincipal.getName()).thenReturn(testData.getLukas().getEmail());
    Principal userPrincipal = Mockito.mock(Principal.class);
    Mockito.when(userPrincipal.getName()).thenReturn(testData.getJim().getEmail());

    List<Tag> tags = Collections.singletonList(testData.getTag1());
    List<Format> formatIn = Collections.singletonList(testData.getIfc());
    List<Format> formatOut = Collections.singletonList(testData.getIfc());

    // Create a new service, save it in the repo and store the id
    Service s = new Service("neuerService", "1", tags, "org", 1, "logo", false, formatIn, formatOut);
    serviceRepo.save(s);
    long id = s.getId();

    // It should not be possible to delete a service as a normal user
    ResponseEntity<Void> response = controller.deleteService(id, userPrincipal);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertTrue(serviceRepo.existsById(id));

    // It should also not be possible to delete a service as an unregistered user
    ResponseEntity<Void> response2 = controller.deleteService(id, null);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
    assertTrue(serviceRepo.existsById(id));

    // It should be possible to exist
    ResponseEntity<Void> response3 = controller.deleteService(id, adminPrincipal);
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertFalse(serviceRepo.existsById(id));

  }

  // @Test
  public void createServicesTest() {

    Principal admin = Mockito.mock(Principal.class);
    Principal user = Mockito.mock(Principal.class);
    Mockito.when(admin.getName()).thenReturn(testData.getLukas().getEmail());
    Mockito.when(user.getName()).thenReturn(testData.getJim().getEmail());

    long maxId = Long.MIN_VALUE;
    for (Service serv : serviceRepo.findAll()) {
      maxId = Math.max(maxId, serv.getId());
    }

    List<Format> formatIn = Collections.singletonList(testData.getIfc());
    List<Format> formatOut = Collections.singletonList(testData.getIfc());
    List<String> tags = Collections.singletonList("atagthatshouldbedeletedafterwards");

    // Create two send services and store them in a list
    SendService s = new SendService(maxId + 1, "testservice", "1", tags, "orga", 1, "logo", "false", formatIn,
        formatOut);

    List<SendService> services = Collections.singletonList(s);

    // An unregistered user should not be able to create services
    ResponseEntity<Void> response = controller.createServices(services, null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    ResponseEntity<Void> response2 = controller.createServices(services, user);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());

    ResponseEntity<Void> response3 = controller.createServices(services, admin);
    assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
    assertTrue(serviceRepo.findById(maxId + 1).isPresent());

    serviceRepo.delete(serviceRepo.findById(maxId + 1).get());
    tagRepo.delete(new Tag("atagthatshouldbedeletedafterwards"));

  }

  @Test
  public void editServiceTest() {

    Principal user = Mockito.mock(Principal.class);
    Principal admin = Mockito.mock(Principal.class);
    Mockito.when(user.getName()).thenReturn(testData.getJim().getEmail());
    Mockito.when(admin.getName()).thenReturn(testData.getLukas().getEmail());

    // Create a new service, save it in the repo and store the id of it
    Service s = testData.getTpModeller();
    long id = s.getId();

    // Create a new service with the same id, but with different attribute
    // values
    SendService s1 = s.createSendService();
    s1.setName("doesNotMatter");
    s1.setOrganisation("doesAlsoNotMatter");

    ResponseEntity<Void> response = controller.editService(id, s1, null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    ResponseEntity<Void> response2 = controller.editService(id, s1, user);
    assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());

    ResponseEntity<Void> response3 = controller.editService(id, s1, admin);
    assertEquals(HttpStatus.OK, response3.getStatusCode());

    // compare it to the expected
    Optional<Service> o2 = serviceRepo.findById(id);

    if (!o2.isPresent())
      fail();

    SendService s2 = o2.get().createSendService();

    assertEquals(s1.getName(), s2.getName());
    assertEquals(s1.getVersion(), s2.getVersion());

    for (String t : s1.getTags()) {
      assertTrue(s2.getTags().contains(t));
    }

    assertEquals(s1.getOrganisation(), s2.getOrganisation());
    assertEquals(s1.getDate(), s2.getDate());
    assertEquals(s1.getLogo(), s2.getLogo());

    for (Format f : s1.getFormatIn()) {
      assertTrue(s2.getFormatIn().contains(f));
    }

    for (Format f : s1.getFormatOut()) {
      assertTrue(s2.getFormatOut().contains(f));
    }

  }

}
