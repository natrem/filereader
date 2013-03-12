package natrem.tool.filereader.input;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;

public class FileContentInputBuilder implements InputBuilder {

    private static final String FILE_INPUT = "fileInput";
    private static final String DIRECTORY_INPUT = "directoryInput";
    private static final String PATH = "path";
    private static final String MAX_COUNT = "maxCount";

    private CompositeInput compositeInput = new CompositeInput();

    @Override
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
    protected void addFileInputsFromConfig(HierarchicalConfiguration configuration) {
        List<HierarchicalConfiguration> fileInputConfigs = configuration
                .configurationsAt(FILE_INPUT);
        for (HierarchicalConfiguration fileConfig : fileInputConfigs) {
            String filePath = fileConfig.getString(PATH);
            FileContentInput input = createFileInput(filePath);
            FileContentInput encapsulated = encapsulate(input, fileConfig);
            compositeInput.add(encapsulated);
        }
    }

    private FileContentInput encapsulate(FileContentInput input,
            HierarchicalConfiguration fileConfig) {
        int maxCount = fileConfig.getInt(MAX_COUNT, 0);
        if (maxCount > 0) {
            LimitedFileInput limited = new LimitedFileInput(input);
            limited.setMaxCount(maxCount);
            return limited;
        }
        return input;
    }

    private FileContentInput createFileInput(String filePath) {
        File file = new File(filePath);
        FileContentInput input = new FileInput(file);
        return input;
    }

    protected FileContentInput build() {
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
    protected void addDirectoryInputsFromConfig(HierarchicalConfiguration configuration) {
        List<HierarchicalConfiguration> fileInputConfigs = configuration
                .configurationsAt(DIRECTORY_INPUT);
        for (HierarchicalConfiguration fileConfig : fileInputConfigs) {
            String filePath = fileConfig.getString(PATH);
            FileContentInput input = createDirectoryInput(filePath);
            FileContentInput encapsulated = encapsulate(input, fileConfig);
            compositeInput.add(encapsulated);
        }
    }

    private FileContentInput createDirectoryInput(String filePath) {
        File file = new File(filePath);
        DirectoryInput input = new DirectoryInput(file);
        return input;
    }


}
