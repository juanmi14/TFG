package logic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;

/**
 *
 * @author Juanmi
 */
@XStreamAlias("FamilyAntimicrobialAgent")
public class FamilyAntimicrobialAgent extends AntimicrobialAgent{
    //attributes
//    private String name;
    //elements
    @XStreamAlias("Link")
    private String link;
    @XStreamAlias("Notes")
    private List<Note> notes;
    @XStreamAlias("MICBreakpoint")
    private MICBreakpoint micBreakpoint;
    @XStreamAlias("DiskContent")
    private String diskContent;
    @XStreamAlias("ZoneDiameterBreakpoint")
    private ZoneDiameterBreakpoint zoneDiameterBreakpoint;

    public FamilyAntimicrobialAgent(){}
    
    public FamilyAntimicrobialAgent(String name){
        super(name);
    }
    
    public FamilyAntimicrobialAgent(String name, String link, MICBreakpoint micBreakpoint, String diskContent, List<Note> notes, ZoneDiameterBreakpoint zoneDiameterBreakpoint) {
        this(name);
        this.link = link;
        this.micBreakpoint = micBreakpoint;
        this.diskContent = diskContent;
        this.notes = notes;
        this.zoneDiameterBreakpoint = zoneDiameterBreakpoint;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public MICBreakpoint getMicBreakpoint() {
        return micBreakpoint;
    }

    public void setMicBreakpoint(MICBreakpoint micBreakpoint) {
        this.micBreakpoint = micBreakpoint;
    }

    public String getDiskContent() {
        return diskContent;
    }

    public void setDiskContent(String diskContent) {
        this.diskContent = diskContent;
    }
    
    public List<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public ZoneDiameterBreakpoint getZoneDiameterBreakpoint() {
        return zoneDiameterBreakpoint;
    }

    public void setZoneDiameterBreakpoint(ZoneDiameterBreakpoint zoneDiameterBreakpoint) {
        this.zoneDiameterBreakpoint = zoneDiameterBreakpoint;
    }
}
