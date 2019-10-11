package nl.han.ica.icss.parser;

import java.util.Stack;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
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
        VariableAssignment variableAssignment = new VariableAssignment();
        this.currentContainer.peek().addChild(variableAssignment);
        this.currentContainer.push(variableAssignment);
    }

    @Override
    public void exitVariableassignment(ICSSParser.VariableassignmentContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterVariablereference(ICSSParser.VariablereferenceContext ctx) {
        VariableReference variableReference = new VariableReference(ctx.getText());
        this.currentContainer.peek().addChild(variableReference);
        this.currentContainer.push(variableReference);
    }

    @Override
    public void exitVariablereference(ICSSParser.VariablereferenceContext ctx) {
        this.currentContainer.pop();
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
    public void enterClassselector(ICSSParser.ClassselectorContext ctx) {
        ClassSelector classSelector = new ClassSelector(ctx.getText());
        this.currentContainer.peek().addChild(classSelector);
        this.currentContainer.push(classSelector);
    }

    @Override
    public void exitClassselector(ICSSParser.ClassselectorContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterIdselector(ICSSParser.IdselectorContext ctx) {
        IdSelector idSelector = new IdSelector(ctx.getText());
        this.currentContainer.peek().addChild(idSelector);
        this.currentContainer.push(idSelector);
    }

    @Override
    public void exitIdselector(ICSSParser.IdselectorContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterTagselector(ICSSParser.TagselectorContext ctx) {
        TagSelector tagSelector = new TagSelector(ctx.getText());
        this.currentContainer.peek().addChild(tagSelector);
        this.currentContainer.push(tagSelector);
    }

    @Override
    public void exitTagselector(ICSSParser.TagselectorContext ctx) {
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
        IfClause ifClause = new IfClause();
        this.currentContainer.peek().addChild(ifClause);
        this.currentContainer.push(ifClause);
    }

    @Override
    public void exitIfclause(ICSSParser.IfclauseContext ctx) {
        this.currentContainer.pop();
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
    }

    @Override
    public void exitLiteral(ICSSParser.LiteralContext ctx) {
    }

    @Override
    public void enterValue(ICSSParser.ValueContext ctx) {

    }

    @Override
    public void exitValue(ICSSParser.ValueContext ctx) {

    }

    @Override
    public void enterColorliteral(ICSSParser.ColorliteralContext ctx) {
        ColorLiteral colorLiteral = new ColorLiteral(ctx.getText());
        this.currentContainer.peek().addChild(colorLiteral);
    }

    @Override
    public void exitColorliteral(ICSSParser.ColorliteralContext ctx) {

    }

    @Override
    public void enterPixelliteral(ICSSParser.PixelliteralContext ctx) {
        PixelLiteral pixelLiteral = new PixelLiteral(ctx.getText());
        this.currentContainer.peek().addChild(pixelLiteral);
    }

    @Override
    public void exitPixelliteral(ICSSParser.PixelliteralContext ctx) {

    }

    @Override
    public void enterPercentageliteral(ICSSParser.PercentageliteralContext ctx) {
        PercentageLiteral percentageLiteral = new PercentageLiteral(ctx.getText());
        this.currentContainer.peek().addChild(percentageLiteral);
    }

    @Override
    public void exitPercentageliteral(ICSSParser.PercentageliteralContext ctx) {

    }

    @Override
    public void enterBoolliteral(ICSSParser.BoolliteralContext ctx) {
        BoolLiteral boolLiteral = new BoolLiteral(ctx.getText());
        this.currentContainer.peek().addChild(boolLiteral);
    }

    @Override
    public void exitBoolliteral(ICSSParser.BoolliteralContext ctx) {

    }

    @Override
    public void enterScalarliteral(ICSSParser.ScalarliteralContext ctx) {
        ScalarLiteral scalarLiteral = new ScalarLiteral(ctx.getText());
        this.currentContainer.peek().addChild(scalarLiteral);
    }

    @Override
    public void exitScalarliteral(ICSSParser.ScalarliteralContext ctx) {

    }

    @Override
    public void enterExpression(ICSSParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(ICSSParser.ExpressionContext ctx) {

    }

    @Override
    public void enterOperation(ICSSParser.OperationContext ctx) {
    }

    @Override
    public void exitOperation(ICSSParser.OperationContext ctx) {
    }

    @Override
    public void enterAddoperation(ICSSParser.AddoperationContext ctx) {
        AddOperation addOperation = new AddOperation();
        this.currentContainer.peek().addChild(addOperation);
        this.currentContainer.push(addOperation);

    }

    @Override
    public void exitAddoperation(ICSSParser.AddoperationContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterMultiplyoperation(ICSSParser.MultiplyoperationContext ctx) {
        MultiplyOperation multiplyOperation = new MultiplyOperation();
        this.currentContainer.peek().addChild(multiplyOperation);
        this.currentContainer.push(multiplyOperation);
    }

    @Override
    public void exitMultiplyoperation(ICSSParser.MultiplyoperationContext ctx) {
        this.currentContainer.pop();
    }

    @Override
    public void enterSubstractoperation(ICSSParser.SubstractoperationContext ctx) {
        SubtractOperation subtractOperation = new SubtractOperation();
        this.currentContainer.peek().addChild(subtractOperation);
        this.currentContainer.push(subtractOperation);
    }

    @Override
    public void exitSubstractoperation(ICSSParser.SubstractoperationContext ctx) {
        this.currentContainer.pop();
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
