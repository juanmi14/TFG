package jmvdeveloper.eucast.logic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;

/**
 *
 * @author Juanmi
 */
@XStreamAlias("S")
public class S {
    //elements
    @XStreamAlias("Value")
    private String value;
    @XStreamAlias("Link")
    private String link;
//    @XStreamImplicit(itemFieldName="note")
//    @XStreamConverter(NoteConverter.class)
    @XStreamAlias("Notes")
    private List<Note> notes;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "S{" + "value=" + value + ", link=" + link + ", note=" + notes + '}';
    }
}
