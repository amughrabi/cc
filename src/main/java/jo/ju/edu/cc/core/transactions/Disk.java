package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Disk extends Attributes {
    private List<Block> blocks;
    public Disk() {
        this.blocks = new ArrayList<>();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject disk = new JSONObject();

        JSONObject blocks = new JSONObject();
        for(Block block : getBlocks()) {
            blocks.put(block.getId(), block.getValue());
        }

        disk.put("disk", blocks);
        return disk;
    }
}
