package natrem.tool.filereader.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class CompositeInput implements FileContentInput {

    protected List<FileContentInput> inputs = new ArrayList<FileContentInput>();
    private int maxCount;

    public void add(FileContentInput input) {
        inputs.add(input);
    }

    @Override
    public Iterator<String> iterator() {
        Function<FileContentInput, FileContentInput> limiter = new LimiterFunction(maxCount);
        Iterable<FileContentInput> contents = Iterables.transform(inputs, limiter);

        return Iterables.concat(contents).iterator();
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
    
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
    
    private static class LimiterFunction implements Function<FileContentInput, FileContentInput> {
        private final int maxCount;
        
        public LimiterFunction(int maxCount) {
            this.maxCount = maxCount;
        }
        
        @Override
        public FileContentInput apply(FileContentInput input) {
            if (maxCount > 0) {
                LimitedFileInput limited = new LimitedFileInput(input);
                limited.setMaxCount(maxCount);
                return limited;
            } else {
                return input;
            }
        }
    };
}
