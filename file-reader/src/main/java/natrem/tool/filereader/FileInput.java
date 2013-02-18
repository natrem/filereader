package natrem.tool.filereader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
            throw new IllegalStateException("there is already an iterator for this FileInput. Close it first");
        }
        try {
            iterator = FileUtils.lineIterator(file);
        } catch (IOException e) {
            log.warn("Cannot iterate file " + file.getName(), e);
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

}
