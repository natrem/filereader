package natrem.tool.filereader.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterators;

public class FileInputTest {
    
    private static final String SOME_LINE = "line1";
    private static final String OTHER_LINE = "line2";
    private static final String ANOTHER_LINE = "line3";

    private File VALID_FILE;
    private FileContentInput VALID_FILE_INPUT;
    
    @Before
    public void createTestFile() throws IOException {
        VALID_FILE = File.createTempFile("unit_test_", ".txt");
        VALID_FILE.deleteOnExit();
        FileUtils.writeLines(VALID_FILE, Arrays.asList(SOME_LINE, OTHER_LINE, ANOTHER_LINE));
        
        VALID_FILE_INPUT = new FileInput(VALID_FILE);
    }
    
    @Test(expected=IllegalStateException.class)
    public void should_throw_IllegalStateException_if_file_deleted() throws Exception {
        VALID_FILE.delete();
        VALID_FILE_INPUT.iterator();
    }

    @Test
    public void should_get_valid_iterator() throws Exception {
        Iterator<String> it = VALID_FILE_INPUT.iterator();
        assertNotNull(it);
    }

    @Test
    public void should_iterate_all_lines() throws Exception {
        Iterator<String> it = VALID_FILE_INPUT.iterator();
        assertEquals(3, Iterators.size(it));
    }

    @Test
    public void should_read_all_lines_in_order() throws Exception {
        Iterator<String> it = VALID_FILE_INPUT.iterator();
        
        assertTrue(it.hasNext());
        assertEquals(SOME_LINE, it.next());
        assertTrue(it.hasNext());
        assertEquals(OTHER_LINE, it.next());
        assertTrue(it.hasNext());
        assertEquals(ANOTHER_LINE, it.next());
        assertFalse(it.hasNext());
    }

    @Test(expected=IllegalStateException.class)
    public void should_throw_IllegalStateException_if_two_iterators_on_the_same_FileInput() throws Exception {
        VALID_FILE_INPUT.iterator();
        VALID_FILE_INPUT.iterator();
    }
    
    @Test
    public void should_get_a_new_iterator_if_the_input_was_closed() throws Exception {
        Iterator<String> it1 = VALID_FILE_INPUT.iterator();
        VALID_FILE_INPUT.close();
        Iterator<String> it2 = VALID_FILE_INPUT.iterator();
        assertNotSame(it1, it2);
    }
    
    @Test
    public void should_not_throw_exception_if_close_when_no_iterator() throws Exception {
        VALID_FILE_INPUT.close();
    }

}
