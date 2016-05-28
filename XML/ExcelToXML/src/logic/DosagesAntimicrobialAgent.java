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
public class DosagesAntimicrobialAgent extends AntimicrobialAgent{
    //attributes
//    private String name;
    //elements
    private String link;
    private String standarDose;
    private String highDose;

    public DosagesAntimicrobialAgent(){}
    
    public DosagesAntimicrobialAgent(String name){
        super(name);
    }
    
    public DosagesAntimicrobialAgent(String name, String link, String standarDose, String highDose) {
        this(name);
        this.link = link;
        this.standarDose = standarDose;
        this.highDose = highDose;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStandarDose() {
        return standarDose;
    }

    public void setStandarDose(String standarDose) {
        this.standarDose = standarDose;
    }

    public String getHighDose() {
        return highDose;
    }

    public void setHighDose(String highDose) {
        this.highDose = highDose;
    }

    @Override
    public String toString() {
        String st = "";
        st += link != null && !link.isEmpty() ? "Link: " + link : "";
        String sd = standarDose != null && !standarDose.isEmpty() ? standarDose : "-";
        String hd = highDose != null && !highDose.isEmpty() ? highDose : "-";
        st += "\nDosages: [" + sd + "," + hd + "]";
        return super.toString() + "\n" + st; //To change body of generated methods, choose Tools | Templates.
    }
}
