package uk.ac.ebi.ddi.task.ddigeneratedxmlvalidator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("validator")
public class XmlValidatorTaskProperties {

    private String directory;

    private String reportFilePath;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    @Override
    public String toString() {
        return "XmlValidatorTaskProperties{" +
                "directory='" + directory + '\'' +
                ", reportFilePath='" + reportFilePath + '\'' +
                '}';
    }
}
