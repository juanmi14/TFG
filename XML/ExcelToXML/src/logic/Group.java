/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanmi
 */
public class Group {
    //attributes
    private String name;
    //elements
    private String notes;//notes related to the antimicrobial agents
    private List<Note> ownNotes;//own possible notes marked as superscript
    private List<AntimicrobialAgent> antimicrobialAgents;

    public Group(){}
    
    public Group(String name){
        this.name = name;
    }
    
    public Group(String name, String notes, List<Note> ownNotes, List<AntimicrobialAgent> antimicrobialAgents) {
        this(name);
        this.notes = notes;
        this.ownNotes = ownNotes;
        this.antimicrobialAgents = antimicrobialAgents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Note> getOwnNotes() {
        return ownNotes;
    }

    public void setOwnNotes(List<Note> ownNotes) {
        this.ownNotes = ownNotes;
    }

    public List<AntimicrobialAgent> getAntimicrobialAgents() {
        return antimicrobialAgents;
    }

    public void setAntimicrobialAgents(List<AntimicrobialAgent> antimicrobialAgents) {
        this.antimicrobialAgents = antimicrobialAgents;
    }

    public void addAntimicrobialAgent(AntimicrobialAgent antimicrobialAgent){
        if(this.antimicrobialAgents == null)
            this.antimicrobialAgents = new ArrayList<AntimicrobialAgent>();
        this.antimicrobialAgents.add(antimicrobialAgent);
    }
    
    @Override
    public String toString() {
        String st = "";
        st += "\nName:" + name;
        st += ownNotes != null && !ownNotes.isEmpty() ? " ^ " + ownNotes : "";
        st += notes != null && !notes.isEmpty() ? "\nNotes: " + notes : "";
        st += "\nAntimicrobialAgents: " + antimicrobialAgents;
        return st;
    }
    
    public boolean hasNotes(){
        return this.notes != null && !this.notes.isEmpty();
    }
    
}
