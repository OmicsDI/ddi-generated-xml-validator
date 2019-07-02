package uk.ac.ebi.ddi.task.ddigeneratedxmlvalidator;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ddi.ddifileservice.services.IFileSystem;
import uk.ac.ebi.ddi.task.ddigeneratedxmlvalidator.configuration.XmlValidatorTaskProperties;

import java.io.File;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DdiGeneratedXmlValidatorApplication.class,
		initializers = ConfigFileApplicationContextInitializer.class)
@TestPropertySource(properties = {
		"s3.env_auth=true",
		"validator.directory=/testing/validator",
		"validator.report_name=report_result.csv"
})
public class ITS3GeneratedXmlValidatorTest {

	@Autowired
	private IFileSystem fileSystem;

	@Autowired
	private XmlValidatorTaskProperties taskProperties;

    @Autowired
    private DdiGeneratedXmlValidatorApplication application;

	@Before
	public void setUp() throws Exception {
        File testFile = new File(getClass().getClassLoader().getResource("generated_file.xml").getFile());
        fileSystem.copyFile(testFile, taskProperties.getDirectory() + "/generated_file.xml");
	}

	@Test
	public void contextLoads() throws Exception {
        application.run();

        String resultFilePath = taskProperties.getDirectory() + "/" + taskProperties.getReportName();
        Assert.assertTrue(fileSystem.isFile(resultFilePath));
        try (InputStream stream = fileSystem.getInputStream(resultFilePath)) {
            String report = IOUtils.toString(stream);
            Assert.assertTrue(report.contains("|Error|"));
        }
	}

    @After
    public void tearDown() throws Exception {
        String resultFilePath = taskProperties.getDirectory() + "/" + taskProperties.getReportName();
        fileSystem.deleteFile(resultFilePath);
    }
}
