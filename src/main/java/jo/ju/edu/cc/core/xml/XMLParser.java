package jo.ju.edu.cc.core.xml;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

public interface XMLParser {
    public @NotNull Document parse(@NotNull String xml) throws Exception;
}
