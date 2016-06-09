package logic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Juanmi
 */
public class DiskDifution {
    //elements
    @XStreamAlias("Medium")
    private String medium;
    @XStreamAlias("Inoculum")
    private String inoculum;
    @XStreamAlias("Incubation")
    private String incubation;
    @XStreamAlias("Reading")
    private String reading;
    @XStreamAlias("QualityControl")
    private String qualityControl;

    public DiskDifution(){}
    
    public DiskDifution(String medium, String inoculum, String incubation, String reading, String qualityControl) {
        this.medium = medium;
        this.inoculum = inoculum;
        this.incubation = incubation;
        this.reading = reading;
        this.qualityControl = qualityControl;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getInoculum() {
        return inoculum;
    }

    public void setInoculum(String inoculum) {
        this.inoculum = inoculum;
    }

    public String getIncubation() {
        return incubation;
    }

    public void setIncubation(String incubation) {
        this.incubation = incubation;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getQualityControl() {
        return qualityControl;
    }

    public void setQualityControl(String qualityControl) {
        this.qualityControl = qualityControl;
    }
}
