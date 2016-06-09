package logic;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author Juanmi
 */
public abstract class AntimicrobialAgent {
    @XStreamAsAttribute
    private String name;
    
    public AntimicrobialAgent(){}

    public AntimicrobialAgent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n" + name;
    }
    
}
