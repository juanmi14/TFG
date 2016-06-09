package converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import logic.Note;

/**
 *
 * @author Juanmi
 */
public class NoteConverter implements Converter{
    public boolean canConvert(final Class clazz) {
    	return clazz.equals(Note.class);
    }

    public void marshal(final Object value,
    		final HierarchicalStreamWriter writer,
    		final MarshallingContext context) {
    	final Note note = (Note) value;
    	writer.addAttribute("id", note.getId());
        writer.setValue(note.getValue() != null ? note.getValue() : "-");
    }
//    @Override
//    public String toString(Object obj) {
//            return ((Note) obj).getValue();
//    }
//
//    @Override
//    public Object fromString(String value) {
//        return new Note(value);
//    }
//
//    @Override
//    public boolean canConvert(Class type) {
//            return type.equals(Note.class);
//    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
