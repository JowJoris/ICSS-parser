package nl.han.ica.icss.parser;

import java.util.Stack;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast;

    //Use this to keep track of the parent nodes when recursively traversing the ast
    private Stack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new Stack<>();
    }

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet stylesheet = new Stylesheet();
        this.ast.root = stylesheet;
        this.currentContainer.push(stylesheet);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        Stylerule stylerule = new Stylerule();
        this.currentContainer.peek().addChild(stylerule);
        this.currentContainer.push(stylerule);
    }

    @Override
    public void exitStylerule(ICSSParser.StyleruleContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterVariableassignment(ICSSParser.VariableassignmentContext ctx) {

    }

    @Override
    public void exitVariableassignment(ICSSParser.VariableassignmentContext ctx) {

    }

    @Override
    public void enterVariablereference(ICSSParser.VariablereferenceContext ctx) {

    }

    @Override
    public void exitVariablereference(ICSSParser.VariablereferenceContext ctx) {

    }

    @Override
    public void enterSelector(ICSSParser.SelectorContext ctx) {
        Selector selector;
        if (ctx.getText().startsWith("#")) {
            selector = new IdSelector(ctx.getText());
        } else if (ctx.getText().startsWith(".")) {
            selector = new ClassSelector(ctx.getText());
        } else {
            selector = new TagSelector(ctx.getText());
        }
        this.currentContainer.peek().addChild(selector);
        this.currentContainer.push(selector);
    }

    @Override
    public void exitSelector(ICSSParser.SelectorContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterBody(ICSSParser.BodyContext ctx) {
    }

    @Override
    public void exitBody(ICSSParser.BodyContext ctx) {
    }

    @Override
    public void enterIfclause(ICSSParser.IfclauseContext ctx) {

    }

    @Override
    public void exitIfclause(ICSSParser.IfclauseContext ctx) {

    }

    @Override
    public void enterConditionmet(ICSSParser.ConditionmetContext ctx) {

    }

    @Override
    public void exitConditionmet(ICSSParser.ConditionmetContext ctx) {

    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        Declaration declaration = new Declaration(ctx.getText());
        this.currentContainer.peek().addChild(declaration);
        this.currentContainer.push(declaration);
    }

    @Override
    public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterProperty(ICSSParser.PropertyContext ctx) {
        PropertyName propertyName = new PropertyName(ctx.getText());
        this.currentContainer.peek().addChild(propertyName);
        this.currentContainer.push(propertyName);
    }

    @Override
    public void exitProperty(ICSSParser.PropertyContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterLiteral(ICSSParser.LiteralContext ctx) {
        Literal literal;
        if (ctx.getText().startsWith("#")) {
            literal = new ColorLiteral(ctx.getText());
        } else if (ctx.getText().endsWith("px")) {
            literal = new PixelLiteral(ctx.getText());
        } else if (ctx.getText().endsWith("%")) {
            literal = new PercentageLiteral(ctx.getText());
        } else if (ctx.getText().equals("TRUE") || ctx.getText().equals("FALSE")) {
            literal = new BoolLiteral(ctx.getText());
        } else {
            literal = new ScalarLiteral(ctx.getText());
        }
        this.currentContainer.peek().addChild(literal);
        this.currentContainer.push(literal);
    }

    @Override
    public void exitLiteral(ICSSParser.LiteralContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterOperation(ICSSParser.OperationContext ctx) {

    }

    @Override
    public void exitOperation(ICSSParser.OperationContext ctx) {

    }

    @Override
    public void enterOperator(ICSSParser.OperatorContext ctx) {

    }

    @Override
    public void exitOperator(ICSSParser.OperatorContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode node) {

    }

    @Override
    public void visitErrorNode(ErrorNode node) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {

    }
}
