package natrem.tool.filereader.input;

import java.io.IOException;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.collect.Iterators;

public class LimitedFileInput implements FileContentInput {
    private FileContentInput delegated;
    private int maxCount;

    public LimitedFileInput(FileContentInput delegated) {
        this.delegated = delegated;
    }

    @Override
    public Iterator<String> iterator() {
        Iterator<String> result = delegated.iterator();
        if (maxCount > 0) {
            result = Iterators.limit(result, maxCount);
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        delegated.close();
    }

    /**
     * Set the maximum number of lines to read per file.
     * <code>0</code> value is considered as infinity.
     * @param maxCount maximum number of lines to read
     * @throws IllegalArgumentException if maxCount is negative
     */
    public void setMaxCount(int maxCount) {
        checkArgument(maxCount >= 0, "illegal maxCount " + maxCount);
        this.maxCount = maxCount;
    }

}