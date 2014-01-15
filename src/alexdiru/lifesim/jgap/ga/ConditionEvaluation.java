package alexdiru.lifesim.jgap.ga;

import java.util.Stack;

/**
 * Used when removing duplicate conditions from a tree
 */
class ConditionEvaluation {
    private int conditionId;
    private boolean conditionResult;

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public boolean isConditionResult() {
        return conditionResult;
    }

    public void setConditionResult(boolean conditionResult) {
        this.conditionResult = conditionResult;
    }

    public ConditionEvaluation(int conditionId, boolean conditionResult) {
        this.conditionId = conditionId;
        this.conditionResult = conditionResult;
    }

    public void invertCondition() {
        conditionResult = !conditionResult;
    }

    public boolean getConditionResult() {
        return conditionResult;
    }
}