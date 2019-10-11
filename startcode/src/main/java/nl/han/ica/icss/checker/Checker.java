package nl.han.ica.icss.checker;

import java.util.HashMap;
import java.util.LinkedList;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    private LinkedList<HashMap<String,ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        checkVariables(ast.root);
    }

    //Check if variables are defined
    private void checkVariables(ASTNode node){
        //Find every node which is an instance of VariableAssignment
        if(node instanceof VariableAssignment){
            String name = ((VariableAssignment) node).name.name;
            Expression e = ((VariableAssignment) node).expression;
            HashMap<String, ExpressionType> hashMap = new HashMap<>();
            variableTypes.add(hashMap);
            hashMap.put(name, expressionType(e));
        }
        node.getChildren().forEach(this::checkVariables);

        //Find every node which is an instance of VariableReference
        if(node instanceof VariableReference){
           String name = ((VariableReference) node).name;
           boolean varExists = false;

           //Check if the VariableReference node also belongs to VariableAssignment
           for(HashMap<String, ExpressionType> varHash: variableTypes){
               if(varHash.containsKey(name)){
                   varExists = true;
               }
           }

           //Set error if VariableReference node doesn't belong to VariableAssignment
           if(!varExists){
               node.setError("Variable reference: " + ((VariableReference) node).name + " is undefined");
           }

        }
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
