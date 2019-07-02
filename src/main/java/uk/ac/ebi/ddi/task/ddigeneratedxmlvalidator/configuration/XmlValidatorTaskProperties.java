package uk.ac.ebi.ddi.task.ddigeneratedxmlvalidator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("validator")
public class XmlValidatorTaskProperties {

    private String directory;

    private String reportName;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String toString() {
        return "XmlValidatorTaskProperties{" +
                "directory='" + directory + '\'' +
                ", reportName='" + reportName + '\'' +
                '}';
    }
}
