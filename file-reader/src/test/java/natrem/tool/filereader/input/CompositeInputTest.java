package natrem.tool.filereader.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Iterators;

public class CompositeInputTest {

    @Mock
    private FileContentInput input1;
    private Iterator<String> iterator1 = Iterators.forArray("a", "b");
    
    @Mock
    private FileContentInput input2;
    private Iterator<String> iterator2 = Iterators.forArray("c", "d");
    
    private CompositeInput COMPOSITE;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(input1.iterator()).thenReturn(iterator1);
        when(input2.iterator()).thenReturn(iterator2);        
        
        COMPOSITE = new CompositeInput();
        COMPOSITE.add(input1);
        COMPOSITE.add(input2);
    }
    
    @Test
    public void should_iterate_empty_composite() throws Exception {
        CompositeInput emptyComposite = new CompositeInput();
        Iterator<String> iterator = emptyComposite.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_iterate_all_inputs() throws Exception {
        Iterator<String> it = COMPOSITE.iterator();
        assertEquals(4, Iterators.size(it));

        InOrder inOrder = inOrder(input1, input2);
        inOrder.verify(input1).iterator();
        inOrder.verify(input2).iterator();
    }
    
    @Test
    public void should_read_all_datas() throws Exception {
        Iterator<String> it = COMPOSITE.iterator();

        assertEquals("a", it.next());
        assertEquals("b", it.next());
        assertEquals("c", it.next());
        assertEquals("d", it.next());
    }
    
    @Test
    public void should_close_all_inputs() throws Exception {
        COMPOSITE.close();
        InOrder inOrder = inOrder(input1, input2);
        inOrder.verify(input1).close();
        inOrder.verify(input2).close();
    }
}
