package de.sopro.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.sopro.model.send.CompatibilityAnswer;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompatibilityTest {

	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	FormatRepository formatRepo;
	
	@Autowired
	Compatibility compa;

	@Test
	public void compatibleFlexibleServices() throws Exception {
		List<Tag> tags = new ArrayList<>();

		Format fOut = new Format("IFC", "5", "strict");
		Format fIn = new Format("IFC", "7", "flexible");
		formatRepo.save(fOut);
		formatRepo.save(fIn);

		Service s1 = createService1(tags, fOut);
		serviceRepo.save(s1);
		Service s2 = createService2(tags, fIn);
		serviceRepo.save(s2);

		CompatibilityAnswer answer = compa.checkCompatibility(s1.getId(), s2.getId());
		assertTrue(answer.isCompatible());
		assertTrue(answer.getSuitingFormats().size() == 1);
		assertTrue(answer.getSuitingFormats().get(0).equals("IFC"));

		serviceRepo.delete(s1);
		serviceRepo.delete(s2);
		formatRepo.delete(fIn);
		formatRepo.delete(fOut);
	}

	@Test
	public void compatibleStrictServices() {
		List<Tag> tags = new ArrayList<>();

		Format fOut = new Format("IFC", "2x0", "strict");
		Format fIn = new Format("IFC", "2x0", "strict");
		formatRepo.save(fOut);
		formatRepo.save(fIn);

		Service s1 = createService1(tags, fOut);
		serviceRepo.save(s1);
		Service s2 = createService2(tags, fIn);
		serviceRepo.save(s2);

		CompatibilityAnswer answer = compa.checkCompatibility(s1.getId(), s2.getId());
		assertTrue(answer.isCompatible());
		assertTrue(answer.getSuitingFormats().size() == 1);
		assertTrue(answer.getSuitingFormats().get(0).equals("IFC"));

		serviceRepo.delete(s1);
		serviceRepo.delete(s2);
		formatRepo.delete(fIn);
		formatRepo.delete(fOut);
	}

	@Test
	public void notCompatibleStrictServices() {
		List<Tag> tags = new ArrayList<>();

		Format fOut = new Format("IFC", "2x0", "strict");
		Format fIn = new Format("DMG", "2x0", "strict");
		formatRepo.save(fOut);
		formatRepo.save(fIn);

		Service s1 = createService1(tags, fOut);
		serviceRepo.save(s1);
		Service s2 = createService2(tags, fIn);
		serviceRepo.save(s2);

		CompatibilityAnswer answer = compa.checkCompatibility(s1.getId(), s2.getId());
		assertFalse(answer.isCompatible());
		assertTrue(answer.getSuitingFormats().size() == 0);

		serviceRepo.delete(s1);
		serviceRepo.delete(s2);
		formatRepo.delete(fIn);
		formatRepo.delete(fOut);
	}

	@Test
	public void notCompatibleFlexibleServices() {
		List<Tag> tags = new ArrayList<>();

		Format fOut = new Format("IFC", "5", "strict");
		Format fIn = new Format("DMG", "7", "flexible");
		formatRepo.save(fOut);
		formatRepo.save(fIn);

		Service s1 = createService1(tags, fOut);
		serviceRepo.save(s1);
		Service s2 = createService2(tags, fIn);
		serviceRepo.save(s2);

		CompatibilityAnswer answer = compa.checkCompatibility(s1.getId(), s2.getId());
		assertFalse(answer.isCompatible());
		assertTrue(answer.getSuitingFormats().size() == 0);

		serviceRepo.delete(s1);
		serviceRepo.delete(s2);
		formatRepo.delete(fIn);
		formatRepo.delete(fOut);
	}

	@Test
	public void notCompatibleFlexibleServicesWrongVersions() {
		List<Tag> tags = new ArrayList<>();

		Format fOut = new Format("IFC", "7", "strict");
		Format fIn = new Format("IFC", "5", "flexible");
		formatRepo.save(fOut);
		formatRepo.save(fIn);

		Service s1 = createService1(tags, fOut);
		serviceRepo.save(s1);
		Service s2 = createService2(tags, fIn);
		serviceRepo.save(s2);

		CompatibilityAnswer answer = compa.checkCompatibility(s1.getId(), s2.getId());
		assertFalse(answer.isCompatible());
		assertTrue(answer.getSuitingFormats().size() == 0);

		serviceRepo.delete(s1);
		serviceRepo.delete(s2);
		formatRepo.delete(fIn);
		formatRepo.delete(fOut);
	}

	private Service createService2(List<Tag> tags, Format fIn) {
		List<Format> formatIn2 = new ArrayList<>();
		List<Format> formatOut2 = new ArrayList<>();
		formatIn2.add(fIn);
		return new Service("3D-Modeller", "3", tags, "IGD", 1531573788, "IGD_Modeller.png", formatIn2, formatOut2);
	}

	private Service createService1(List<Tag> tags, Format fOut) {
		List<Format> formatIn1 = new ArrayList<>();
		List<Format> formatOut1 = new ArrayList<>();
		formatOut1.add(fOut);
		return new Service("TP Modeller", "1.0", tags, "TP", 153443388, "TP_Modeller_10.png", formatIn1, formatOut1);
	}

}
