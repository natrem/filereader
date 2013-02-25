package natrem.tool.filereader.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.CanReadFileFilter;

import com.google.common.collect.Iterators;

/**
 * {@link FileContentInput} to iterate through all lines of all readable files
 * in a directory. Iteration is not recursive.
 * 
 * @author natrem
 * 
 */
public class DirectoryInput implements FileContentInput {

    private File directory;
    private List<LineIterator> lineIterators;

    public DirectoryInput(File directory) {
        Objects.requireNonNull(directory);
        boolean isValid = directory.isDirectory();
        isValid &= directory.canRead();
        if (!isValid) {
            throw new IllegalArgumentException("must be a directory");
        }
        this.directory = directory;
    }

    @Override
    public Iterator<String> iterator() {
        if (lineIterators != null) {
            throw new IllegalStateException("there is already an iterator. Close it first");
        }
        
        lineIterators = new ArrayList<LineIterator>();
        
        for (Iterator<File> fileIterator = FileUtils.iterateFiles(directory, CanReadFileFilter.CAN_READ, null); fileIterator.hasNext();) {
            File f = fileIterator.next();
            lineIterators.add(new FileInput(f).iterator());
        }
        
        Iterator<String> iterator = Iterators.concat(lineIterators.iterator());
        
        return iterator;
    }

    @Override
    public void close() throws IOException {
        if (lineIterators != null) {
            for (LineIterator lineIterator : lineIterators) {
                lineIterator.close();
            }
            lineIterators = null;
        }
    }
}
