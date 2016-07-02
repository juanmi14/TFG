package logic;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

/**
 *
 * @author Juanmi
 */
public class Dosages {
    //elements
    @XStreamImplicit(itemFieldName="Group")
    List<Group> groups;

    public Dosages(){}

    public Dosages(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return groups.toString();
    }
    
    
}
