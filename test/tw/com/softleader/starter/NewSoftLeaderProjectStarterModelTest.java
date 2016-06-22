package tw.com.softleader.starter;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class NewSoftLeaderProjectStarterModelTest {

	@Test
	public void load() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		String url = NewSoftLeaderProjectStarterModelTest.class.getClassLoader().getResource("starter.xml").toString();
		NewSoftLeaderProjectStarterModel layout = new NewSoftLeaderProjectStarterModel(url);
		System.out.println(layout.size());
		layout.forEach(f -> {
			System.out.println(f);
		});
	}

}
