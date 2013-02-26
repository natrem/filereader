package natrem.tool.filereader.output;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Function;

public class SendersTest {
    
    @Mock
    private Function<String, String> transformer;

    @Mock
    private Sender<String> sender;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_create_function() throws Exception {
        Function<String, Void> output = Senders.function(transformer, sender);
        when(transformer.apply("toto")).thenReturn("titi");
        
        output.apply("toto");
        verify(transformer).apply("toto");
        verify(sender).send("titi");
    }
}
