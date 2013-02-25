package natrem.tool.filereader.input;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import natrem.tool.filereader.input.DirectoryInput;
import natrem.tool.filereader.input.FileContentInput;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DirectoryInputTest {

    private static final String SOME_FILE_LINE1 = "file1_line1";
    private static final String SOME_FILE_LINE2 = "file1_line2";
    private static final String OTHER_FILE_LINE1 = "file2_line1";
    private static final String OTHER_FILE_LINE2 = "file2_line2";
    private static final String OTHER_FILE_LINE3 = "file2_line3";

    private File SOME_FILE;
    private File OTHER_FILE;
    private File DIRECTORY_DOES_NOT_EXIST;
    private FileContentInput VALID_DIRECTORY_INPUT;

    private File NON_READABLE_DIRECTORY;

    @Rule
    public TemporaryFolder test_folder = new TemporaryFolder();
    
    @Before
    public void createTestFiles() throws IOException {
        SOME_FILE = test_folder.newFile();
        FileUtils.writeLines(SOME_FILE, Arrays.asList(SOME_FILE_LINE1, SOME_FILE_LINE2));

        OTHER_FILE = test_folder.newFile();
        FileUtils.writeLines(OTHER_FILE, Arrays.asList(OTHER_FILE_LINE1, OTHER_FILE_LINE2, OTHER_FILE_LINE3));

        DIRECTORY_DOES_NOT_EXIST = new File("");
        VALID_DIRECTORY_INPUT = new DirectoryInput(test_folder.getRoot());
        
        NON_READABLE_DIRECTORY = test_folder.newFolder();
        NON_READABLE_DIRECTORY.setReadable(false);
    }

    @Test(expected=IllegalArgumentException.class)
    public void should_throw_exception_if_directory_does_not_exist() throws Exception {
        new DirectoryInput(DIRECTORY_DOES_NOT_EXIST);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void should_throw_exception_if_directory_is_a_file() throws Exception {
        new DirectoryInput(SOME_FILE);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void should_throw_exception_if_directory_is_not_readable() throws Exception {
        new DirectoryInput(NON_READABLE_DIRECTORY);
    }
    
    @Test
    public void should_get_valid_non_empty_iterator() throws Exception {
        Iterator<String> it = VALID_DIRECTORY_INPUT.iterator();
        assertNotNull(it);
        assertTrue(it.hasNext());
    }
    
    @Test
    public void should_iterate_all_lines() throws Exception {
        Iterator<String> it = VALID_DIRECTORY_INPUT.iterator();
        int nbLines = 0;
        while (it.hasNext()) {
            nbLines++;
            it.next();
        }
        assertEquals(5, nbLines);
    }
    
    @Test
    public void should_read_all_lines_in_any_order() throws Exception {
        String[] expectedContent = new String[] {
                SOME_FILE_LINE1, SOME_FILE_LINE2,
                OTHER_FILE_LINE1, OTHER_FILE_LINE2, OTHER_FILE_LINE3};

        Iterator<String> it = VALID_DIRECTORY_INPUT.iterator();
        List<String> actualContent = new ArrayList<String>();
        while (it.hasNext()) {
            String line = it.next();
            actualContent.add(line);
        }
        
        assertThat(actualContent, hasItems(expectedContent));
    }

    @Test(expected=IllegalStateException.class)
    public void should_throw_IllegalStateException_if_two_iterators_on_the_same_DirectoryInput() throws Exception {
        VALID_DIRECTORY_INPUT.iterator();
        VALID_DIRECTORY_INPUT.iterator();
    }

    @Test
    public void should_get_a_new_iterator_if_the_input_was_closed() throws Exception {
        Iterator<String> it1 = VALID_DIRECTORY_INPUT.iterator();
        VALID_DIRECTORY_INPUT.close();
        Iterator<String> it2 = VALID_DIRECTORY_INPUT.iterator();
        assertNotSame(it1, it2);
    }

    @Test
    public void should_not_throw_exception_if_close_when_no_iterator() throws Exception {
        VALID_DIRECTORY_INPUT.close();
    }

    @Test
    public void should_not_rethrow_IOException() throws Exception {
        Iterator<String> it = VALID_DIRECTORY_INPUT.iterator();
        while (it.hasNext()) {
            it.next();
            SOME_FILE.delete();
            OTHER_FILE.delete();
        }
        assertFalse(it.hasNext());
    }
}
