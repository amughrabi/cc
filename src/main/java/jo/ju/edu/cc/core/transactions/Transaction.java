package jo.ju.edu.cc.core.transactions;

import org.json.JSONObject;

import java.util.List;

public class Transaction extends Attributes {

    private List<Operation> operations;

    public String getId() {
        return getAttributes().get("id");
    }

    public long getAccessTimeUnit() {
        return Long.parseLong( getAttributes().get("accessTimeUnit") );
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public JSONObject toJSON() {
        JSONObject transaction = new JSONObject();
        transaction.put("id", getId())
                  .put("accessTimeUnit", getAccessTimeUnit());
        JSONObject operationObject;
        JSONObject operationsObject = new JSONObject();
        int id = 1;
        for(Operation operation : getOperations()) {
            operationObject = new JSONObject();
            operationObject.put("type", operation.getType());
            operationObject.put("var", operation.getVariable());
            operationObject.put("value", operation.getValue());
            operationsObject.put("" + id, operationObject);
            id++;
        }

        transaction.put("operations", operationsObject);

        return transaction;
    }
}
