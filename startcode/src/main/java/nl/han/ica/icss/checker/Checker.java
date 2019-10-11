package nl.han.ica.icss.checker;

import java.util.HashMap;
import java.util.LinkedList;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    private LinkedList<HashMap<String,ExpressionType>> variableTypes;
    private HashMap<String, ExpressionType> variableHashMap;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        variableHashMap= new HashMap<>();
        checkVariables(ast.root);
    }
/*CH01*/
    //Check if variables are defined
    private void checkVariables(ASTNode node){
        findVariableAssignment(node);
        variableTypes.add(variableHashMap);
        findVariableReference(node);
    }

    private void findVariableAssignment(ASTNode node) {
        //Find every node which is an instance of VariableAssignment
        if(node instanceof VariableAssignment){
            String name = ((VariableAssignment) node).name.name;
            Expression e = ((VariableAssignment) node).expression;
            variableHashMap.put(name, expressionType(e));
        }
        node.getChildren().forEach(this::checkVariables);
    }

    //Find every node which is an instance of VariableReference
    private void findVariableReference(ASTNode node) {
        if(node instanceof VariableReference){
            String name = ((VariableReference) node).name;
            boolean varExists = checkVariableReferenceToVariableAssignment(name);

            //Set error if VariableReference node doesn't belong to VariableAssignment
            if(!varExists){
                node.setError("Variable reference: " + ((VariableReference) node).name + " is undefined");
            }
        }
    }

    //Check if the VariableReference node also belongs to VariableAssignment
    private boolean checkVariableReferenceToVariableAssignment(String name) {
        for(HashMap<String, ExpressionType> varHash: variableTypes){
            if(varHash.containsKey(name)){
               return true;
            }
        }
        return false;
    }

    //Get the ExpressionType from Expression
    private ExpressionType expressionType(Expression e) {
        if(e instanceof BoolLiteral){
            return ExpressionType.BOOL;
        }
        if(e instanceof ColorLiteral){
            return ExpressionType.COLOR;
        }
        if(e instanceof PercentageLiteral){
            return ExpressionType.PERCENTAGE;
        }
        if(e instanceof PixelLiteral){
            return ExpressionType.PIXEL;
        }
        if(e instanceof ScalarLiteral){
            return ExpressionType.SCALAR;
        }
        return null;
    }
}
