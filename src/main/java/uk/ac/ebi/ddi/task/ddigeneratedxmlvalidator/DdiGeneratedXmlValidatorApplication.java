package uk.ac.ebi.ddi.task.ddigeneratedxmlvalidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.ac.ebi.ddi.ddifileservice.services.IFileSystem;
import uk.ac.ebi.ddi.ddifileservice.type.ConvertibleOutputStream;
import uk.ac.ebi.ddi.task.ddigeneratedxmlvalidator.configuration.XmlValidatorTaskProperties;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.utils.Tuple;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DdiGeneratedXmlValidatorApplication implements CommandLineRunner {

	@Autowired
	private XmlValidatorTaskProperties taskProperties;

	@Autowired
	private IFileSystem fileSystem;

	public static void main(String[] args) {
		SpringApplication.run(DdiGeneratedXmlValidatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Map<String, List<Tuple>> errors = new HashMap<>();
		for (String filePath : fileSystem.listFilesFromFolder(taskProperties.getDirectory())) {
			File file = fileSystem.getFile(filePath);
			if (OmicsXMLFile.hasFileHeader(file)) {
				List<Tuple> error = OmicsXMLFile.validateSchema(file);
				error.addAll(OmicsXMLFile.validateSemantic(file));
				if (errors.containsKey(filePath)) {
					error.addAll(errors.get(filePath));
				}
				errors.put(filePath, error);
			}
		}
		if (!errors.isEmpty()) {
			ConvertibleOutputStream outputStream = new ConvertibleOutputStream();
			PrintStream printStream = new PrintStream(outputStream);
			for (String filePath: errors.keySet()) {
				for (Tuple error: errors.get(filePath)) {
					printStream.println(filePath + "|" + error.getKey() + "|" + error.getValue());
				}
			}
			printStream.close();
			String reportFile = taskProperties.getDirectory() + "/" + taskProperties.getReportName();
			fileSystem.saveFile(outputStream, reportFile);
		}
	}
}
