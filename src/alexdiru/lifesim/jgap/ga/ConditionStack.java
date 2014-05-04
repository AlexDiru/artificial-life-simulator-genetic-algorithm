package alexdiru.lifesim.jgap.ga;


import java.util.Stack;

class ConditionStack {

    private Stack<ConditionEvaluation> stack;

    public ConditionStack() {
        stack = new Stack<ConditionEvaluation>();
    }

    public ConditionEvaluation getCondition(int conditionId) {
        for (ConditionEvaluation conditionEvaluation : stack)
            if (conditionEvaluation.getConditionId() == conditionId)
                return conditionEvaluation;

        return null;
    }

    /**
     * The condition evaluation at the top of the stack has it's condition boolean inversed
     */
    public void inverseLatestCondition() {
        stack.peek().invertCondition();
    }

    public void pop() {
        if (stack.size() > 0)
            stack.pop();
    }

    public void addCondition(int function, boolean conditionResult) {
        stack.push(new ConditionEvaluation(function, conditionResult));
    }
}