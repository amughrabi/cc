package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeFrameTable {
    private Map<Long, List<Operation>> table;

    public TimeFrameTable() {
        this.table = new LinkedHashMap<>();
    }

    public void put(long timeUnit, @NotNull Operation operation, int position) {
        List<Operation> operations = table.get(timeUnit);
        if(operations == null) {
            operations = new ArrayList<>();
        }
        // padding-left
        if(position > operations.size()) {
            for(int i = 0; i < position; i++) {
                operations.add(new NullOperation());
            }
        }

        operations.add(operation);
        table.put(timeUnit, operations);
    }

    public List<Operation> get(long timeUnit) {
        return table.get(timeUnit);
    }

    public Map<Long, List<Operation>> getTable() {
        return table;
    }

    public void setTable(Map<Long, List<Operation>> table) {
        this.table = table;
    }
}
