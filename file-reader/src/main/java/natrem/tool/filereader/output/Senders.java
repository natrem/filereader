package natrem.tool.filereader.output;

import natrem.tool.filereader.transform.Transformer;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public final class Senders {
    private Senders() {}
    
    public static <T> Function<String, Void> function(final Transformer<String, T> transformer, final Sender<T> sender) {
        return Functions.compose(sender, transformer);
    }
    
}
