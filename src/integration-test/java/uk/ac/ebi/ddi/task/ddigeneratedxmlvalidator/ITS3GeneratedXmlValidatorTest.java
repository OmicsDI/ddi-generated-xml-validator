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
        "s3.endpoint_url=https://s3.embassy.ebi.ac.uk",
        "s3.bucket_name=caas-omicsdi",
        "s3.region=eu-west-2",
		"validator.directory=testing/validator",
		"validator.report_file_path=testing/validator/report_result.csv"
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

        Assert.assertTrue(fileSystem.isFile(taskProperties.getReportFilePath()));
        try (InputStream stream = fileSystem.getInputStream(taskProperties.getReportFilePath())) {
            String report = IOUtils.toString(stream);
            Assert.assertTrue(report.contains("|Error|"));
        }
	}

    @After
    public void tearDown() {
        fileSystem.deleteFile(taskProperties.getReportFilePath());
    }
}
