package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void addOrUpdateBlock(@NotNull Block block) {
        // blocks should be Identical
        blocks.removeIf(b -> StringUtil.isEqual(b.getId(), block.getId()));
        blocks.add(block);
    }

    public @Nullable Block getBlock(@NotNull String blockId) {
        Optional<Block> block = blocks.stream().filter(b -> StringUtil.isEqual(b.getId(), blockId)).findAny();
        return block.orElse(null);
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject disk = new JSONObject();

        JSONObject blocks = new JSONObject();
        for(Block block : getBlocks()) {
            blocks.put(block.getId(), block.getValue());
        }

        disk.put(getName(), blocks);
        return disk;
    }

    public @NotNull String getName() {
        return "disk";
    }
}
