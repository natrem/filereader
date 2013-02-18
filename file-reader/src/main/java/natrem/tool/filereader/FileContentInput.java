package natrem.tool.filereader;

import java.io.Closeable;

/**
 * Describes a standard input information from a File. It is Closeable as it is
 * a the input from a File and Iterable to allow reading the File line by line
 * and treating it on the fly
 * 
 * @author natrem
 * 
 */
public interface FileContentInput extends Iterable<String>, Closeable {

}
