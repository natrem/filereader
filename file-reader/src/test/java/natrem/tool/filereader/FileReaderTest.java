package natrem.tool.filereader;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import natrem.tool.filereader.input.FileContentInput;
import natrem.tool.filereader.input.InputBuilder;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

public class FileReaderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    private File configFile;

    private FileReader fileReader;
    
    @Before
    public void initConf() throws IOException {
        fileReader = spy(new FileReader());
        
        configFile = temporaryFolder.newFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
        writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        writer.newLine();
        writer.write("<config>");
        writer.write("<inputs><input1 /></inputs>");
        writer.write("</config>");
        writer.newLine();
        writer.close();
    }

    @Test
    public void should_delegate_initialisation_to_other_init_method() throws Exception {
        doNothing().when(fileReader).init(any(HierarchicalConfiguration.class));
        fileReader.init(configFile.getAbsolutePath());
        
        verify(fileReader).init(any(HierarchicalConfiguration.class));
    }
    
    @Test
    public void should_init_inputs_by_calling_builder() throws Exception {
        InputBuilder builder = mock(InputBuilder.class);
        fileReader.setInputBuilder(builder);

        ArgumentCaptor<HierarchicalConfiguration> config = ArgumentCaptor.forClass(HierarchicalConfiguration.class);
        fileReader.init(configFile.getAbsolutePath());
        verify(builder).buildFromConfig(config.capture());
        assertTrue(config.getValue().containsKey("input1"));
    }
    
    @Test
    public void should_not_call_function_if_input_is_null() throws Exception {
        @SuppressWarnings("unchecked")
        Function<String, Void> treatment = mock(Function.class);
        fileReader.apply(treatment);
        verify(treatment, never()).apply(anyString());
    }

    @Test
    public void should_init_input_then_call_function() throws Exception {
        InputBuilder builder = mock(InputBuilder.class);
        fileReader.setInputBuilder(builder);

        // init fileReader input
        FileContentInput input = mock(FileContentInput.class);
        when(builder.buildFromConfig(any(HierarchicalConfiguration.class))).thenReturn(input);
        fileReader.init(configFile.getAbsolutePath());

        // set input
        when(input.iterator()).thenReturn(Iterators.forArray("toto"));
        
        @SuppressWarnings("unchecked")
        Function<String, Void> treatment = mock(Function.class);
        fileReader.apply(treatment);
        verify(treatment).apply("toto");
    }
    
}
