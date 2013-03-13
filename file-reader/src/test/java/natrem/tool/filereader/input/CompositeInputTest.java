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
    private Iterator<String> iterator1 = Iterators.forArray("a1", "a2", "a3");
    
    @Mock
    private FileContentInput input2;
    private Iterator<String> iterator2 = Iterators.forArray("b1", "b2", "b3");
    
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
        assertEquals(6, Iterators.size(it));

        InOrder inOrder = inOrder(input1, input2);
        inOrder.verify(input1).iterator();
        inOrder.verify(input2).iterator();
    }
    
    @Test
    public void should_read_all_datas() throws Exception {
        Iterator<String> it = COMPOSITE.iterator();

        assertEquals("a1", it.next());
        assertEquals("a2", it.next());
        assertEquals("a3", it.next());
        assertEquals("b1", it.next());
        assertEquals("b2", it.next());
        assertEquals("b3", it.next());
    }
    
    @Test
    public void should_close_all_inputs() throws Exception {
        COMPOSITE.close();
        InOrder inOrder = inOrder(input1, input2);
        inOrder.verify(input1).close();
        inOrder.verify(input2).close();
    }
    
    @Test
    public void should_limit_size_of_all_inputs() throws Exception {
        COMPOSITE.setMaxCount(2);
        assertEquals(4, Iterators.size(COMPOSITE.iterator()));
    }

    @Test
    public void should_limit_size_of_each_input() throws Exception {
        COMPOSITE.setMaxCount(2);
        Iterator<String> it = COMPOSITE.iterator();

        assertEquals("a1", it.next());
        assertEquals("a2", it.next());
        assertEquals("b1", it.next());
        assertEquals("b2", it.next());
    }

}
