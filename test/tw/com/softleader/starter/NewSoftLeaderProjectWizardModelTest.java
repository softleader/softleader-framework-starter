package tw.com.softleader.starter;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class NewSoftLeaderProjectWizardModelTest {

	@Test
	public void load() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		String url = NewSoftLeaderProjectWizardModelTest.class.getClassLoader().getResource("starter.xml").toString();
		NewSoftLeaderProjectWizardModel layout = new NewSoftLeaderProjectWizardModel(url);
		System.out.println(layout.size());
		layout.forEach(f -> {
			System.out.println(f);
		});
	}

}
