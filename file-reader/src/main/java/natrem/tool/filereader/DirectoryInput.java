package natrem.tool.filereader;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirectoryInput implements FileContentInput {

    private static final Log log = LogFactory.getLog(DirectoryInput.class);

    private File directory;
    private DirectoryLineIterator iterator;

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
        if (iterator != null) {
            throw new IllegalStateException("there is already an iterator. Close it first");
        }
        // checkInternalState();
        iterator = new DirectoryLineIterator();
        return iterator;
    }

    private class DirectoryLineIterator implements Iterator<String> {

        private Iterator<File> fileIterator;
        private FileInput currentFileContentInput;
        // should never be null
        private Iterator<String> currentFileIterator = Collections.emptyIterator();

        public DirectoryLineIterator() {
            if (directory.canRead() && directory.isDirectory()) {
                fileIterator = FileUtils.iterateFiles(directory,
                        CanReadFileFilter.CAN_READ, null);
            } else {
                fileIterator = Collections.emptyIterator();
            }
        }

        @Override
        public boolean hasNext() {
            // checkInternalState is copied from LineIterator but is depending
            // on the contract
            // of the close method. add it when the contact is defined and with
            // tests !
            // checkInternalState();

            // if the current File Iterator is empty
            // then switch to next file
            while (!currentFileIterator.hasNext() && fileIterator.hasNext()) {
                closeCurrentFileContentInput();
                File newFile = fileIterator.next();
                setFileIterator(newFile);
            }

            return currentFileIterator.hasNext();
        }

        /**
         * @param newFile
         *            new File to use to configure the currentFileContentInput
         *            and cuurentFileIterator
         */
        private void setFileIterator(File newFile) {
            currentFileContentInput = new FileInput(newFile);
            try {
                currentFileIterator = currentFileContentInput.iterator();
            } catch (IllegalStateException e) {
                currentFileIterator = Collections.emptyIterator();
            }
        }

        /**
         * Close the current FileContentInput
         */
        private void closeCurrentFileContentInput() {
            try {
                if (currentFileContentInput != null) {
                    currentFileContentInput.close();
                }
            } catch (IOException e) {
                log.warn("Cannot close current file: " + getCurrentFileName(), e);
            }
        }

        private String getCurrentFileName() {
            if (currentFileContentInput != null) {
                return currentFileContentInput.getFile().getName();
            }
            return "";
        }

        @Override
        public String next() {
            // checkInternalState();
            if (!hasNext()) {
                throw new NoSuchElementException("No more lines");
            }
            return currentFileIterator.next();
        }

        @Override
        public void remove() {
            // checkInternalState();
            currentFileIterator.remove();
        }
        
        public void close() throws IOException {
            fileIterator = Collections.emptyIterator();
            if (currentFileContentInput != null) {
                currentFileContentInput.close();
                currentFileContentInput = null;
            }
            currentFileIterator = Collections.emptyIterator();
        }
    }

    @Override
    public void close() throws IOException {
        if (iterator != null) {
            iterator.close();
            iterator = null;
        }
    }
}
