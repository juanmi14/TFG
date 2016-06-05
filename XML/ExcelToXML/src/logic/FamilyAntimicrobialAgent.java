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
public class FamilyAntimicrobialAgent extends AntimicrobialAgent{
    //attributes
//    private String name;
    //elements
    private String link;
    private MICBreakpoint micBreakpoint;
    private String diskContent;
    private ZoneDiameterBreakpoint zoneDiameterBreakpoint;

    public FamilyAntimicrobialAgent(){}
    
    public FamilyAntimicrobialAgent(String name){
        super(name);
    }
    
    public FamilyAntimicrobialAgent(String name, String link, MICBreakpoint micBreakpoint, String diskContent, ZoneDiameterBreakpoint zoneDiameterBreakpoint) {
        this(name);
        this.link = link;
        this.micBreakpoint = micBreakpoint;
        this.diskContent = diskContent;
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

    public ZoneDiameterBreakpoint getZoneDiameterBreakpoint() {
        return zoneDiameterBreakpoint;
    }

    public void setZoneDiameterBreakpoint(ZoneDiameterBreakpoint zoneDiameterBreakpoint) {
        this.zoneDiameterBreakpoint = zoneDiameterBreakpoint;
    }
}
