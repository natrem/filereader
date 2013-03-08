package natrem.tool.filereader.input;

import org.apache.commons.configuration.HierarchicalConfiguration;

public interface InputBuilder {

    FileContentInput buildFromConfig(HierarchicalConfiguration configuration);

}