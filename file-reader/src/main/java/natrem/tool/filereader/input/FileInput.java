package natrem.tool.filereader.input;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link FileContentInput} to iterate through all lines of a file on demand.
 * 
 * @author natrem
 *
 */
public class FileInput implements FileContentInput {
    
    private static final Log log = LogFactory.getLog(FileInput.class);

    private File file;
    private LineIterator iterator = null; 
    
    public FileInput(File file) {
        this.file = file;
    }

    @Override
    public LineIterator iterator() {
        if (iterator != null) {
            throw new IllegalStateException("there is already an iterator. Close it first");
        }
        try {
            iterator = FileUtils.lineIterator(file);
        } catch (IOException e) {
            log.warn("Cannot iterate file " + file.getName(), e);
            throw new IllegalStateException(e);
        }
        return iterator;
    }

    @Override
    public void close() throws IOException {
        if (iterator != null) {
            iterator.close();
            iterator = null;
        }
    }
    
    public File getFile() {
        return file;
    }

}
