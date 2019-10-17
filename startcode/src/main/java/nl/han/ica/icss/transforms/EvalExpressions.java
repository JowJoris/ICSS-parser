package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import static nl.han.ica.icss.transforms.OperationSide.LEFT;
import static nl.han.ica.icss.transforms.OperationSide.RIGHT;

public class EvalExpressions implements Transform {

    private HashMap<String, Literal> variableValuesHashMap;
    private LinkedList<Expression> list;
    private Stack<Expression> stack;

    public EvalExpressions() {
        variableValuesHashMap = new HashMap<>();
        list = new LinkedList<>();
        stack = new Stack<>();
    }

    @Override
    public void apply(AST ast) {
        retrieveAllVariables(ast.root);
        findVariablesAndOperationsInDeclarations(ast.root, ast.root.getChildren());
        findOperations(ast.root, ast.root.getChildren());
    }

    private void findVariablesAndOperationsInDeclarations(ASTNode parent, ArrayList<ASTNode> children) {
        for (ASTNode child : children) {
            if (parent instanceof Declaration) {
                if (child instanceof VariableReference) {
                    ((Declaration) parent).expression = getDeclarationValueFromVariable((VariableReference) child);
                }
                if (child instanceof Operation) {
                    findVariableReferenceInOperation((Operation) child);
                }
            }
            findVariablesAndOperationsInDeclarations(child, child.getChildren());
        }
    }

    private void findVariableReferenceInOperation(Expression parent) {
        if (parent instanceof Operation) {
            findVariableReferenceInSides(parent, ((Operation) parent).lhs, LEFT);
            if (((Operation) parent).rhs instanceof Operation) {
                for (ASTNode node : ((Operation) parent).rhs.getChildren()) {
                    findVariableReferenceInOperation((Expression) node);
                }
            } else if (((Operation) parent).rhs instanceof VariableReference) {
                findVariableReferenceInSides(parent, ((Operation) parent).rhs, RIGHT);
            }
        }
    }

    private void findVariableReferenceInSides(ASTNode parent, Expression child, OperationSide side) {
        if (child instanceof VariableReference) {
            if (side == LEFT) {
                ((Operation) parent).lhs = getDeclarationValueFromVariable((VariableReference) child);
            } else if (side == RIGHT) {
                ((Operation) parent).rhs = getDeclarationValueFromVariable((VariableReference) child);
            }
        }
    }

    private Literal getDeclarationValueFromVariable(VariableReference variableNode) {
        if (variableValuesHashMap.containsKey(variableNode.name)) {
            return variableValuesHashMap.get(variableNode.name);
        }
        return null;
    }

    private void retrieveAllVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            String name = ((VariableAssignment) node).name.name;
            Literal l = (Literal) ((VariableAssignment) node).expression;
            variableValuesHashMap.put(name, l);
        }
        node.getChildren().forEach(this::retrieveAllVariables);
    }

    private void findOperations(ASTNode parent, ArrayList<ASTNode> children) {
        for (ASTNode child : children) {
            if (parent instanceof Declaration && child instanceof Operation) {
                list.clear();
                fillStackWithOperations(null, (Operation) child);
                parent.removeChild(child);
                parent.addChild(calculatePostFixStack());
            }
            findOperations(child, child.getChildren());
        }
    }

    private PixelLiteral calculatePostFixStack() {
        for (Expression e : list) {
            if (e instanceof Operation) {
                int left = findExpressionValue(stack.peek());
                stack.pop();
                int right = findExpressionValue(stack.peek());
                stack.pop();
                stack.push(calculateOperation((Operation) e, left, right));
            } else {
                stack.push(e);
            }
        }
        return new PixelLiteral(findExpressionValue(stack.peek()));
    }

    private Expression calculateOperation(Operation e, int left, int right) {
        if (e instanceof MultiplyOperation) {
            return new ScalarLiteral(left * right);
        } else if (e instanceof AddOperation) {
            return new ScalarLiteral(left + right);
        } else {
            return new ScalarLiteral(left - right);
        }
    }

    private void fillStackWithOperations(MultiplyOperation mul, Operation node) {
        list.add(node.lhs);
        if (mul != null) {
            list.add(mul);
        }
        if (node.rhs instanceof Operation) {
            if (node instanceof MultiplyOperation) {
                fillStackWithOperations(new MultiplyOperation(), (Operation) node.rhs);
            } else {
                fillStackWithOperations(null, (Operation) node.rhs);
            }
        } else {
            list.add(node.rhs);
        }
        if (node instanceof AddOperation) {
            list.add(new AddOperation());
        } else if (node instanceof MultiplyOperation && !(node.rhs instanceof Operation)) {
            list.add(new MultiplyOperation());
        } else if (node instanceof SubtractOperation) {
            list.add(new SubtractOperation());
        }
    }

    private int findExpressionValue(Expression node) {
        if (node instanceof PixelLiteral) {
            return ((PixelLiteral) node).value;
        } else {
            return ((ScalarLiteral) node).value;
        }
    }
}
