package de.sopro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;
import de.sopro.model.User;
import de.sopro.repository.CompositionEdgeRepository;
import de.sopro.repository.CompositionNodeRepository;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.TagRepository;
import de.sopro.repository.UserRepository;

@SpringBootApplication
public class SpringBootWebApplication extends SpringBootServletInitializer implements CommandLineRunner {

	// repositories that are needed
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ServiceRepository serviceRepo;
	@Autowired
	private TagRepository tagRepo;
	@Autowired
	private FormatRepository formatRepo;
	@Autowired
	private CompositionRepository compRepo;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootWebApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// populate empty repositories with example data
		if (userRepo.count() == 0) {

			// add users
			User dark = new User("drake", "dunkel", "d@d.de", "Dr.", true);
			dark.setPassword("123");
			dark.setRole(new String[] { "ADMIN" });

			User dum = new User("dummi", "dumm", "du@d.com", "Prof.", false);
			dum.setPassword("password");
			dum.setRole(new String[] { "USER" });

			userRepo.save(dark);
			userRepo.save(dum);

			// add tags
			String[] ts = { "3D", "Modeller", "Visualisierung", "Modellierung" };
			List<Tag> tags = createTagList(ts);

			// add formats
			String[][] fis = { { "IFC", "2x0", "strict" }, { "BCF", "1.0", "strict" } };
			List<Format> formatIn = createFormatList(fis);

			String[][] fos = { { "IFC", "2x0", "strict" }, { "DWG", "5", "strict" } };
			List<Format> formatOut = createFormatList(fos);

			// add service
			Service s1 = new Service("TP Modeller", "1.0", tags, "TP", 153443388, "TP_Modeller_10.png", true, formatIn,
					formatOut);

			serviceRepo.save(s1);

			// add tags
			String[] ts2 = { "3D", "Modeller", "IFC" };
			tags = createTagList(ts2);

			// add formats
			String[][] fis2 = { { "IFC", "2x0", "strict" }, { "gbXML", "2", "strict" } };
			formatIn = createFormatList(fis2);

			String[][] fos2 = { { "IFC", "2x0", "strict" }, { "DWG", "5", "flexible" } };
			formatOut = createFormatList(fos2);

			// add service
			Service s2 = new Service("3D-Modeller", "3", tags, "IGD", 1531573788, "IGD_Modeller.png", false, formatIn,
					formatOut);

			serviceRepo.save(s2);

			// create Composition
			CompositionNode n1 = new CompositionNode(5, 5, s1);
			CompositionNode n2 = new CompositionNode(50, 50, s2);

			CompositionEdge e = new CompositionEdge(n1, n2);

			List<CompositionNode> nodes = new ArrayList<>();
			nodes.add(n1);
			nodes.add(n2);

			List<CompositionEdge> edges = new ArrayList<>();
			edges.add(e);

			Composition c = new Composition(dark, "MyComp", true, nodes, edges);
			compRepo.save(c);

			List<Composition> owns = dark.getOwnsComp();
			owns.add(c);
			dark.setOwnsComp(owns);
		}
	}

	/**
	 * saves and creates a list of formats given by {@code fis}
	 * 
	 * @param fis
	 *            array that contains the formats that should be saved given as
	 *            three Strings
	 * @return a list of formats
	 */
	private List<Format> createFormatList(String[][] fis) {
		List<Format> formats = new ArrayList<>();
		for (String[] s : fis) {
			Format f = new Format(s[0], s[1], s[2]);
			Format existing = formatRepo.findOneByTypeAndVersion(s[0], s[1]);
			if (existing == null) {
				formatRepo.save(f);
			} else {
				f.setId(existing.getId());
			}
			formats.add(f);
		}
		return formats;
	}

	/**
	 * saves and creates a list of tags given by {@code ts}
	 * 
	 * @param ts
	 *            array that contains the strings that should be saved as tags
	 * @return a list of tags
	 */
	private List<Tag> createTagList(String[] ts) {
		List<Tag> tags = new ArrayList<>();
		for (String s : ts) {
			Tag t = new Tag(s);
			tagRepo.save(t);
			tags.add(t);
		}
		return tags;
	}
}
