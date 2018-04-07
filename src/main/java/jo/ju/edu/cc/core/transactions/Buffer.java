package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Buffer extends Attributes {
    private List<Block> blocks;
    public Buffer() {
        this.blocks = new ArrayList<>();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public @NotNull JSONObject toJSON() {
        JSONObject buffer = new JSONObject();

        JSONObject blocks = new JSONObject();
        for(Block block : getBlocks()) {
            blocks.put(block.getId(), block.getValue());
        }

        buffer.put("buffer", blocks);
        return buffer;
    }
}
