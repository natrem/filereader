package natrem.tool.filereader.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;

public class CompositeInput implements FileContentInput {

    private List<FileContentInput> inputs = new ArrayList<FileContentInput>();
    
    public void add(FileContentInput input) {
        inputs.add(input);
    }

    @Override
    public Iterator<String> iterator() {
        return Iterables.concat(inputs).iterator();
    }

    @Override
    public void close() throws IOException {
        for (FileContentInput input : inputs) {
            input.close();
        }
    }

    public int size() {
        return inputs.size();
    }
}
