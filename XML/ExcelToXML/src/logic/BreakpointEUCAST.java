package logic;

import java.util.List;

/**
 *
 * @author Juanmi
 */
public class BreakpointEUCAST {
    //attributes
    private int year;
    //elements
    private List<Family> families;
    private Dosages dosages;

    public BreakpointEUCAST(){
        
    }
    
    public BreakpointEUCAST(int year){
        this.year = year;
    }
    
    public BreakpointEUCAST(int year, List<Family> families, Dosages dosages) {
        this(year);
        this.families = families;
        this.dosages = dosages;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Family> getFamilies() {
        return families;
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
    }

    public Dosages getDosages() {
        return dosages;
    }

    public void setDosages(Dosages dosages) {
        this.dosages = dosages;
    }
}
