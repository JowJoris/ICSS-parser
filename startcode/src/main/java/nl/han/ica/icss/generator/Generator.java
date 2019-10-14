package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.ArrayList;

public class Generator {

    private String string = "";

    public String generate(AST ast) {
        generateString(ast.root);
        return string + "}";
    }

    private void generateString(ASTNode node) {
        if (node instanceof Selector) {
        	if(string.endsWith(";\n")){
        		string += "}\n\n";
			}
            generateSelectorString(node);
        }

        else if (node instanceof Declaration) {
        	generateBodyString(node);
		}
        node.getChildren().forEach(this::generateString);
    }

    private void generateSelectorString(ASTNode node) {
        if (node instanceof ClassSelector) {
            string += ((ClassSelector) node).cls + "{";
        }

        else if (node instanceof IdSelector) {
            string += ((IdSelector) node).id + "{";
        }

        else if (node instanceof TagSelector) {
            string += ((TagSelector) node).tag + "{";
        }
        string += "\n";
    }

    private void generateBodyString(ASTNode node) {
        if (node instanceof Declaration) {
            generateDeclarationString(node.getChildren());
        }
    }

    private void generateDeclarationString(ArrayList<ASTNode> nodes) {
    	for(ASTNode node: nodes) {
			if (node instanceof PropertyName) {
				string += "\t" + ((PropertyName) node).name + ": ";
			}
			if (node instanceof Expression) {
				generateLiteralString(node);
                string += ";\n";
			}
		}
    }

    private void generateLiteralString(ASTNode node) {
        if (node instanceof BoolLiteral) {
            string += ((BoolLiteral) node).value;
        } else if (node instanceof ColorLiteral) {
            string += ((ColorLiteral) node).value;
        } else if (node instanceof PercentageLiteral) {
            string += ((PercentageLiteral) node).value + "%";
        } else if (node instanceof PixelLiteral) {
            string += ((PixelLiteral) node).value +"px";
        } else if (node instanceof ScalarLiteral) {
            string += ((ScalarLiteral) node).value;
        }
    }
}
