package natrem.tool.filereader;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;

public class FileContentInputBuilder {

    private static final String FILE_INPUT = "fileInput";
    private static final String DIRECTORY_INPUT = "directoryInput";
    private static final String PATH = "path";

    private CompositeInput compositeInput = new CompositeInput();

    public FileContentInput buildFromConfig(
            HierarchicalConfiguration configuration) {
        addFileInputsFromConfig(configuration);
        addDirectoryInputsFromConfig(configuration);
        return build();
    }

    /**
     * Read given configuration object and add a FileInput for each
     * configurations formatted as follow: <br/>
     * <code>
     * &lt;fileInput&gt;<br/>
     * &nbsp;&nbsp;&lt;path&gt;file_path&lt;/path&gt;<br/>
     * &lt;/fileInput&gt;
     * </code>
     * 
     * @param configuration
     *            source configuration
     */
    private void addFileInputsFromConfig(HierarchicalConfiguration configuration) {
        List<HierarchicalConfiguration> fileInputConfigs = configuration
                .configurationsAt(FILE_INPUT);
        for (HierarchicalConfiguration fileConfig : fileInputConfigs) {
            String filePath = fileConfig.getString(PATH);
            addFileInput(filePath);
        }
    }

    private void addFileInput(String filePath) {
        File file = new File(filePath);
        FileContentInput input = new FileInput(file);
        compositeInput.add(input);
    }

    private FileContentInput build() {
        return compositeInput;
    }

    /**
     * Read given configuration object and add a FileInput for each
     * configurations formatted as follow: <br/>
     * <code>
     * &lt;directoryInput&gt;<br/>
     * &nbsp;&nbsp;&lt;path&gt;directory_path&lt;/path&gt;<br/>
     * &lt;/directoryInput&gt;
     * </code>
     * 
     * @param configuration
     *            source configuration
     */
    private void addDirectoryInputsFromConfig(HierarchicalConfiguration configuration) {
        List<HierarchicalConfiguration> fileInputConfigs = configuration
                .configurationsAt(DIRECTORY_INPUT);
        for (HierarchicalConfiguration fileConfig : fileInputConfigs) {
            String filePath = fileConfig.getString(PATH);
            addDirectoryInput(filePath);
        }
    }

    private void addDirectoryInput(String filePath) {
        File file = new File(filePath);
        DirectoryInput input = new DirectoryInput(file);
        compositeInput.add(input);
    }


}
