package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Block extends Attributes {
    private String value;

    public @NotNull String getId() {
        return getAttributes().get("id");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject jsObject = new JSONObject();
        jsObject.put("id", getId()).put("value", getValue());
        return jsObject;
    }

    public Block getCopy() {
        Block block = new Block();
        block.setAttributes(clone());
        block.setValue( new String(value) );
        return block;
    }
}
