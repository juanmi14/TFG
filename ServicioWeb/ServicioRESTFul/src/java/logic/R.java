package logic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;

/**
 *
 * @author Juanmi
 */
@XStreamAlias("R")
public class R {
    //elements
    @XStreamAlias("Value")
    private String value;
    @XStreamAlias("Link")
    private String link;
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
        return "R{" + "value=" + value + ", link=" + link + ", note=" + notes + '}';
    }
}
