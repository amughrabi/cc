package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeFrameTable {
    private Map<Long, List<Operation>> table;
    private int maxListSize;

    public TimeFrameTable() {
        this.table = new LinkedHashMap<>();
    }

    public void put(long timeUnit, @NotNull Operation operation, int position) {
        List<Operation> operations = table.get(timeUnit);
        if(operations == null) {
            operations = new ArrayList<>();
        }
        boolean added = false;
        if(!inPlace(position, operations)) {
            List<Operation> newLst =  new ArrayList<>();
            for(int i = 0; i < operations.size(); i ++) {
                if(position == i && operations.get(i).isNull()) {
                    added = true;
                    newLst.add(operation);
                } else {
                    newLst.add(operations.get(i));
                }
            }
            operations = newLst;
        }

        // padding-left
        if(position > operations.size()) {
            for(int i = 0; i < position; i++) {
                operations.add(new NullOperation());
            }
        }
        if(!added)
            operations.add(operation);
        table.put(timeUnit, operations);

        // for UI, We need to know the dimensions
        if(maxListSize < operations.size()) {
            maxListSize = operations.size();
        }
    }

    private boolean inPlace(int position, List<Operation> operations) {
        for(int i = 0; i < operations.size(); i++) {
            if(position == i && operations.get(i).isNull()) {
                return false;
            }
        }
        return true;
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

    public int getMaxListSize() {
        return maxListSize;
    }

    public void setMaxListSize(int maxListSize) {
        this.maxListSize = maxListSize;
    }
}
