package de.sopro.controller.rest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sopro.model.Compatibility;
import de.sopro.model.Composition;
import de.sopro.model.User;
import de.sopro.model.send.CompLists;
import de.sopro.model.send.SimpleComp;
import de.sopro.repository.CompositionEdgeRepository;
import de.sopro.repository.CompositionNodeRepository;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.TagRepository;
import de.sopro.repository.UserRepository;

@RunWith(SpringRunner.class)
// @SpringBootTest
@WebMvcTest(CompController.class)
public class CompControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CompositionRepository compRepo;
	@MockBean
	private UserRepository userRepo;
	@MockBean
	private CompositionNodeRepository nodeRepo;
	@MockBean
	private CompositionEdgeRepository edgeRepo;
	@MockBean
	private ServiceRepository serviceRepo;
	@MockBean
	private TagRepository tagRepo;
	@MockBean
	private FormatRepository formatRepo;

	@MockBean
	private Compatibility compa;

	private User u;
	private User u2;
	private Composition c1;
	private Composition c2;
	private SimpleComp sc1;
	private SimpleComp sc2;

	@Before
	public void init() {
		u = Mockito.mock(User.class);
		u2 = Mockito.mock(User.class);
		c1 = Mockito.mock(Composition.class);
		c2 = Mockito.mock(Composition.class);
		Mockito.when(c1.getName()).thenReturn("Comp1");
		Mockito.when(c1.getOwner()).thenReturn(u);
		Mockito.when(c2.getName()).thenReturn("Comp2");
		Mockito.when(c2.getOwner()).thenReturn(u2);
		List<Composition> owns = new ArrayList<>();
		owns.add(c1);
		List<Composition> views = new ArrayList<>();
		views.add(c2);
		Mockito.when(u.getOwnsComp()).thenReturn(owns);
		Mockito.when(u.getViewable()).thenReturn(views);
		Mockito.when(u.getEditable()).thenReturn(new ArrayList<>());
		userRepo.save(u);
		compRepo.save(c1);
		compRepo.save(c2);
		sc1 = Mockito.mock(SimpleComp.class);
		sc2 = Mockito.mock(SimpleComp.class);
		Mockito.when(sc1.getName()).thenReturn("Comp1");
		Mockito.when(sc2.getName()).thenReturn("Comp2");
	}

	@Test
	public void getComps_returnsComps() throws Exception {
		CompLists cList = Mockito.mock(CompLists.class);
		List<SimpleComp> owns = new ArrayList<>();
		owns.add(sc1);
		List<SimpleComp> view = new ArrayList<>();
		view.add(sc2);
		Mockito.when(cList.getviewable()).thenReturn(view);
		Mockito.when(cList.getEditable()).thenReturn(new ArrayList<>());
		// Mockito.when(cList.getOwn).thenReturn(owns);
		
		ObjectMapper mapper = new ObjectMapper();
//		String cLsitAsString = mapper.writeValueAsString(cList);
		
//		mvc.perform(get("/compositions")).andExpect(status().isOk()).andExpect(content().json(cLsitAsString));
	      
		
//		.andExpect(jsonPath("$", hasSize(2)))
//           .andExpect(jsonPath("$[0].id", is(1)))
//           .andExpect(jsonPath("$[0].username", is("Daenerys Targaryen")))
//           .andExpect(jsonPath("$[1].id", is(2)))
//           .andExpect(jsonPath("$[1].username", is("John Snow")));
//		
		MvcResult result =mvc.perform(get("/compositions"))
		        .andExpect(status().isOk())
		        .andReturn();
//
//
//		// this uses a TypeReference to inform Jackson about the Lists's generic type
//		CompLists actual = mapper.readValue(result.getResponse().getContentAsString(), CompLists.class);

//		String jsonCarArray = 
//				  "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
//				List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
		
	}

}
