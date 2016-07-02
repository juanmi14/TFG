package jmvdeveloper.eucast.logic;

import com.thoughtworks.xstream.annotations.*;
import jmvdeveloper.eucast.converters.NoteConverter;

/**
 *
 * @author Juanmi
 */
@XStreamAlias("Note")
@XStreamConverter(NoteConverter.class)
public class Note {
    //attributes
//    @XStreamAsAttribute
    private String id;
    //content
    
    private String value = "-";

    public Note(){}
    
    public Note(String value){
        this.id = "0";
        this.value = value;
    }
    
    public Note(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "id: " + id + "\nValue: " + value;
    }
    
}
