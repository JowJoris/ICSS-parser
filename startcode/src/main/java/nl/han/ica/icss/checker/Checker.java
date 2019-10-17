package nl.han.ica.icss.checker;

import java.util.HashMap;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    private HashMap<String, Expression> variableHashMap;

    public void check(AST ast) {
        variableHashMap = new HashMap<>();
        findVariableAssignment(ast.root);

        //CH01
        checkIfVariablesAreDefined(ast.root);

        //CH03
        checkForColorsInOperations(ast.root);

        //CH05
        checkIfConditionIsBoolean(ast.root);
    }

    private void findVariableAssignment(ASTNode node) {
        //Find every node which is an instance of VariableAssignment
        if (node instanceof VariableAssignment) {
            String name = ((VariableAssignment) node).name.name;
            Expression e = ((VariableAssignment) node).expression;
            variableHashMap.put(name, e);
        }
        node.getChildren().forEach(this::findVariableAssignment);
    }

    /*CH01*/
    private void checkIfVariablesAreDefined(ASTNode node) {
        if (node instanceof VariableReference) {
            String name = ((VariableReference) node).name;

            //Set error if VariableReference node doesn't belong to VariableAssignment
            if (!variableHashMap.containsKey(name)) {
                node.setError("Variable reference: " + ((VariableReference) node).name + " is undefined");
            }
        }
    }

    /*CH02 & CH03*/
    private void checkForColorsInOperations(ASTNode node) {
        //Find every node which is an instance of Operation
        if (node instanceof Operation) {

            //CH02
            checkIfOperandsAreEqual((Operation) node);
            for (ASTNode child : node.getChildren()) {

                //CH03
                checkIfExpressionIsColor(node, (Expression) child);
            }
        }
        node.getChildren().forEach(this::checkForColorsInOperations);
    }

    /*CH02*/
    private void checkIfOperandsAreEqual(Operation node) {
        if (node.lhs instanceof VariableReference) {
            node.lhs = variableHashMap.get(((VariableReference) node.lhs).name);
        }
        if (node instanceof MultiplyOperation) {
            if (!(((MultiplyOperation) node).lhs instanceof ScalarLiteral) &&
                    !(((MultiplyOperation) node).rhs instanceof ScalarLiteral)) {
                node.setError("Multiplying must be done with scalar value");
            }
        } else if (node.rhs instanceof MultiplyOperation) {
            if (!(node.lhs.getClass().equals(((MultiplyOperation) node.rhs).lhs.getClass())) &&
                    (!node.lhs.getClass().equals(((MultiplyOperation) node.rhs).rhs.getClass()))) {
                node.setError("Addition or Subtraction must be done with equal literals");
            }
        } else if (!(node.lhs.getClass().equals(node.rhs.getClass()))) {
            node.setError("Addition or Subtraction must be done with equal literals");
        }
    }

    /*CH03*/
    private void checkIfExpressionIsColor(ASTNode node, Expression e) {
        if (e instanceof ColorLiteral) {
            node.setError("Color used in expression, value: " + ((ColorLiteral) e).value);
        } else if (e instanceof VariableReference) {
            String name = ((VariableReference) e).name;
            if (variableHashMap.get(name) instanceof ColorLiteral)
                node.setError("Variable in expression is color, variable: " + name);
        }
    }

    /*CH05*/
    private void checkIfConditionIsBoolean(ASTNode node) {
        if(node instanceof IfClause){
            if(((IfClause) node).conditionalExpression instanceof VariableReference){
                ((IfClause) node).conditionalExpression = variableHashMap.get(((VariableReference) ((IfClause) node).conditionalExpression).name);
            }
            if(!(((IfClause) node).conditionalExpression instanceof BoolLiteral)){
                node.setError("Condition must be of type Boolean, current value: " + ((IfClause) node).conditionalExpression);
            }
        }
        node.getChildren().forEach(this::checkIfConditionIsBoolean);
    }
}
