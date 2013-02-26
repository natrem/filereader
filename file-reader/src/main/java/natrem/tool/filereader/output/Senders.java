package natrem.tool.filereader.output;

import com.google.common.base.Function;

public final class Senders {
    private Senders() {}
    
    public static <T> Function<String, Void> function(final Function<String, T> transformer, final Sender<T> sender) {
        return new Function<String, Void>() {
            @Override
            public Void apply(String input) {
                sender.send(transformer.apply(input));
                return null;
            }
        };
    }
    

}
