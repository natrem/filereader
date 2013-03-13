package natrem.tool.filereader.input;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.Objects;

/**
 * {@link FileContentInput} to iterate through all lines of all readable files
 * in a directory. Iteration is not recursive.
 * 
 * @author natrem
 * 
 */
public class DirectoryInput extends CompositeInput implements FileContentInput {

    private File directory;

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
        if (size() == 0) {
            initContent();
        }
        return super.iterator();
    }

    private void initContent() {
        this.inputs.clear();
        FileFilter filesOnly = new ReadableFileFilter();
        for (File file : directory.listFiles(filesOnly)) {
            FileContentInput input = new FileInput(file);
            add(input);
        }
    }
    
    private static class ReadableFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isFile() && file.canRead();
        }
    }
}
