package jmvdeveloper.eucast.logic;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

/**
 *
 * @author Juanmi
 */
public class Dosages {
    //elements
    @XStreamImplicit(itemFieldName="AntibioticFamily")
    List<AntibioticFamily> antibioticFamilies;

    public Dosages(){}

    public Dosages(List<AntibioticFamily> antibioticFamilies) {
        this.antibioticFamilies = antibioticFamilies;
    }

    public List<AntibioticFamily> getAntibioticFamilies() {
        return antibioticFamilies; 
    }

    public void setAntibioticFamilies(List<AntibioticFamily> antibioticFamilies) {
        this.antibioticFamilies = antibioticFamilies;
    }

    @Override
    public String toString() {
        return antibioticFamilies.toString();
    }
    
    
}
