package natrem.tool.filereader.input;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Iterators;

public class LimitedFileInputTest {

    @Mock
    private FileContentInput input1;
    private Iterator<String> iterator1 = Iterators.forArray("a", "b", "c", "d");
    
    private LimitedFileInput fileInput;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(input1.iterator()).thenReturn(iterator1);
        
        fileInput = new LimitedFileInput(input1);
    }

    @Test
    public void should_iterate_all_if_maxCount_not_set() throws Exception {
        assertEquals(4, Iterators.size(fileInput.iterator()));
    }

    @Test
    public void should_iterate_only_maxCount_if_set() throws Exception {
        fileInput.setMaxCount(2);
        assertEquals(2, Iterators.size(fileInput.iterator()));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void should_throw_exception_if_maxCount_negative() throws Exception {
        fileInput.setMaxCount(-1);
    }

    @Test
    public void should_close_delegated_input() throws Exception {
        fileInput.close();
        verify(input1).close();
    }
}
