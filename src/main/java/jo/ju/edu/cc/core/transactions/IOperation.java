package jo.ju.edu.cc.core.transactions;

public interface IOperation {
    public static final String COMMIT = "commit",
                               START  = "start",
                               WRITE  = "write",
                               READ   = "read",
                               SUB    = "sub",
                               ADD    = "add",
                               DIVIDE = "divide",
                               MULTI  = "multi";

}
