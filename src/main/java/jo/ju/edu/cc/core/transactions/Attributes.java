package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Attributes {
    private Map<String, String> attributes;
    public Attributes() {
        attributes = new HashMap<>();
    }

    public void put(@NotNull String key, @NotNull String value) {
        attributes.put(key, value);
    }

    public @Nullable String get(@NotNull String  key) {
        return attributes.get(key);
    }

    public @NotNull Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(@NotNull Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public @NotNull abstract JSONObject toJSON();

    @Override
    public @NotNull String toString() {
        return toJSON().toString();
    }

    @Override
    public @NotNull Map<String, String> clone() {
        Map<String, String> map = new HashMap<>();
        map.putAll(attributes);
        return map;
    }
}
