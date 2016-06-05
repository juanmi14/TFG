/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author Juanmi
 */
public class DiskDifution {
    //elements
    private String medium;
    private String inoculum;
    private String incubation;
    private String reading;
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
