package jmvdeveloper.eucast.logic;

import java.util.List;

/**
 * Created by Juanmi on 17/06/2016.
 */
public class jFamilies {
    private List<String> families;
    private List<String> antibiotics;

    public jFamilies(){}

    public List<String> getFamilies() {
        return families;
    }

    public void setFamilies(List<String> families) {
        this.families = families;
    }

    public List<String> getAntibiotics() {
        return antibiotics;
    }

    public void setAntibiotics(List<String> antibiotics) {
        this.antibiotics = antibiotics;
    }
}
