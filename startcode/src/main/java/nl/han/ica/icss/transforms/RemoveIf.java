package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.HashMap;

public class RemoveIf implements Transform {

    private HashMap<String, Literal> variableValuesHashMap;

    public RemoveIf() {
        variableValuesHashMap = new HashMap<>();
    }

    @Override
    public void apply(AST ast) {
        retrieveAllVariables(ast.root);
        findIfClauses(null, ast.root);
    }

    private void retrieveAllVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            String name = ((VariableAssignment) node).name.name;
            Literal l = (Literal) ((VariableAssignment) node).expression;
            variableValuesHashMap.put(name, l);
        }
        node.getChildren().forEach(this::retrieveAllVariables);
    }

    private void findIfClauses(ASTNode parent, ASTNode node) {
        if (parent instanceof Stylerule && node instanceof IfClause) {
            boolean condition = getBooleanFromCondition((IfClause) node);
            ((Stylerule) parent).body.remove(node);
            if (condition) {
                for (ASTNode child : ((IfClause) node).body) {
                    parent.addChild(child);
                    findIfClauses(parent, child);
                }
            }
        }
        for (ASTNode child : node.getChildren()) {
            findIfClauses(node, child);
        }
    }

    private boolean getBooleanFromCondition(IfClause node) {
        if (node.conditionalExpression instanceof VariableReference) {
            node.conditionalExpression = variableValuesHashMap.get(((VariableReference) node.conditionalExpression).name);
        }
        return ((BoolLiteral) node.conditionalExpression).value;
    }
}
