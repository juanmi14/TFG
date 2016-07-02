package logic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanmi
 */
public class Family {
    //attributes
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private boolean defined;
    //elements
    @XStreamImplicit(itemFieldName="Advice")
    private List<String> advices;
    @XStreamAlias("DiskDifution")
    private DiskDifution diskDifution;
    @XStreamImplicit(itemFieldName="AntibioticFamily")
    private List<AntibioticFamily> antibioticFamilies;

    public Family(){}
    
    public Family(String name){
        this.name = name;
        this.defined = false;
    }
    
    public Family(String name, List<String> advices, DiskDifution diskDifution, List<AntibioticFamily> antibioticFamilies) {
        this(name);
        this.advices = advices;
        this.diskDifution = diskDifution;
        this.antibioticFamilies = antibioticFamilies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefined() {
        return defined;
    }

    public void setDefined(boolean defined) {
        this.defined = defined;
    }
    
    
    public List<String> getAdvices() {
        return advices;
    }

    public void setAdvices(List<String> advices) {
        this.advices = advices;
    }

    public DiskDifution getDiskDifution() {
        return diskDifution;
    }

    public void setDiskDifution(DiskDifution diskDifution) {
        this.diskDifution = diskDifution;
    }

    public List<AntibioticFamily> getAntibioticFamilies() {
        return antibioticFamilies;
    }

    public void setAntibioticFamilies(List<AntibioticFamily> antibioticFamilies) {
        this.antibioticFamilies = antibioticFamilies;
    }
    
    public void addAntibioticFamily(AntibioticFamily antibioticFamily){
        if(this.antibioticFamilies == null)
            this.antibioticFamilies = new ArrayList<AntibioticFamily>();
        this.antibioticFamilies.add(antibioticFamily);
    }

    public int getNumAntiMicInGruoups(){
        int n = 0;
        if(this.antibioticFamilies != null){
            for(AntibioticFamily g: antibioticFamilies){
                if(g.getAntimicrobialAgents() != null)
                    n += g.getAntimicrobialAgents().size();
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return "Familia" + "(" + (this.defined ? "D" : "ND") + "): " + this.name;
    }
}
