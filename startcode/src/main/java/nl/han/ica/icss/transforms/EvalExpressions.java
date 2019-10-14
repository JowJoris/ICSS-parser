package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class EvalExpressions implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;
    private HashMap<String, Expression> variableValuesHashMap;

    public EvalExpressions() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new LinkedList<>();
        variableValuesHashMap = new HashMap<>();
        retrieveAllVariables(ast.root);
        findDeclarationVariables(ast.root);
    }

    private void findDeclarationVariables(ASTNode node){
        if (node instanceof Declaration){
            findVariableInDeclaration(node);
        }
        node.getChildren().forEach(this::findDeclarationVariables);
    }

    private void findVariableInDeclaration(ASTNode node) {
        VariableReference variableNode = retrieveDeclarationVariable(node.getChildren());
        if(variableNode != null) {
            node.removeChild(variableNode);
            node.addChild(setDeclarationValueFromVariable(variableNode));
        }
    }

    private ASTNode setDeclarationValueFromVariable(VariableReference variableNode) {
        if(variableValuesHashMap.containsKey(variableNode.name)){
            return variableValuesHashMap.get(variableNode.name);
        }
        return null;
    }

    private VariableReference retrieveDeclarationVariable(ArrayList<ASTNode> children) {
        for(ASTNode node: children){
            if(node instanceof VariableReference){
                return (VariableReference) node;
            }
        }
        return null;
    }

    private void retrieveAllVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            String name = ((VariableAssignment) node).name.name;
            Expression e = ((VariableAssignment) node).expression;
            variableValuesHashMap.put(name, e);
        }
        node.getChildren().forEach(this::retrieveAllVariables);
    }
}
