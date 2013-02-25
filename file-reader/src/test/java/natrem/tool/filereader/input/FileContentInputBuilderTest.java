package natrem.tool.filereader.input;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import natrem.tool.filereader.input.CompositeInput;
import natrem.tool.filereader.input.FileContentInput;
import natrem.tool.filereader.input.FileContentInputBuilder;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Iterators;

public class FileContentInputBuilderTest {
    
    private static final String SOME_LINE = "line1";
    private static final String OTHER_LINE = "line2";

    private File validFile;
    
    private HierarchicalConfiguration configuration;
    private Node validFileInputNode;
    private Node invalidFileInputNode;
    private Node validDirectoryInputNode;

    
    @Rule
    public TemporaryFolder test_folder = new TemporaryFolder();
    
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        createTestFile();
        initConfiguration();
    }

    private void createTestFile() throws IOException {
        validFile = test_folder.newFile();
        FileUtils.writeLines(validFile, Arrays.asList(SOME_LINE, OTHER_LINE));
    }

    private void initConfiguration() {
        configuration = new HierarchicalConfiguration();

        validFileInputNode = createFileNode(validFile.getPath());
        invalidFileInputNode = createFileNode("invalid_path");
        validDirectoryInputNode = createDirectoryNode(validFile.getParent());
    }

    private Node createFileNode(String path) {
        Node pathNode = new Node("path", path);
        Node fileInputNode = new Node("fileInput");
        fileInputNode.addChild(pathNode);
        return fileInputNode;
    }

    private Node createDirectoryNode(String path) {
        Node pathNode = new Node("path", path);
        Node fileInputNode = new Node("directoryInput");
        fileInputNode.addChild(pathNode);
        return fileInputNode;
    }

    @Test
    public void should_add_file_input_from_conf() throws Exception {
        configuration.addNodes(null, Collections.singleton(validFileInputNode));
        FileContentInput input = new FileContentInputBuilder().buildFromConfig(configuration);
        assertEquals(2, Iterators.size(input.iterator()));
    }

    @Test
    public void should_add_directory_input_from_conf() throws Exception {
        configuration.addNodes(null, Collections.singleton(validDirectoryInputNode));
        FileContentInput input = new FileContentInputBuilder().buildFromConfig(configuration);
        assertEquals(2, Iterators.size(input.iterator()));
    }

    @Test
    public void should_contain_both_files() throws Exception {
        configuration.addNodes(null, Arrays.asList(validFileInputNode, invalidFileInputNode));

        FileContentInput input = new FileContentInputBuilder().buildFromConfig(configuration);
        assertTrue(input instanceof CompositeInput);
        assertEquals(2, ((CompositeInput)input).size());
    }

    @Test
    public void should_throw_exception_during_iteration_if_invalid_file() throws Exception {
        configuration.addNodes(null, Collections.singleton(invalidFileInputNode));

        FileContentInput input = new FileContentInputBuilder().buildFromConfig(configuration);
        
        Iterator<String> it = input.iterator();
        expected.expect(IllegalStateException.class);
        expected.expectMessage("FileNotFoundException");
        it.hasNext();
    }

}
