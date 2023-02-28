package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Log4JUtils;


public class RuleVisitor extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(RuleVisitor.class);

	public int printCallCount=0;
	public int varDeclCount=0;
	
	@Override
    public void visit(VarDeclFirstPart4 VarDeclFirstPart4) {
		varDeclCount++;
		log.info("posecena " + VarDeclFirstPart4);
	}
	
	@Override
    public void visit(VarDeclFirstPart3 VarDeclFirstPart3) {
		varDeclCount++;
		log.info("posecena " + VarDeclFirstPart3);		
	}

	
	@Override
    public void visit(VarDeclHelper VarDeclHelper) { 
    }

	@Override
	public void visit(DeclarationList DeclarationList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ReturnType ReturnType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorIdent DesignatorIdent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Mulop Mulop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorOptions DesignatorOptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Relop Relop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormalParamDecl FormalParamDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StatementList StatementList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Addop Addop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor Factor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CondTerm CondTerm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TermHelper TermHelper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorApps DesignatorApps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Term Term) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Condition Condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ActualParsApp ActualParsApp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExtendsType ExtendsType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclList VarDeclList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormalParamList FormalParamList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Expr Expr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorList DesignatorList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList ConstIdentList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorStatement DesignatorStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ActualPars ActualPars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclList2 VarDeclList2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Statement Statement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDecl VarDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassDecl ClassDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CondFact CondFact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorAppsList DesignatorAppsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MethodDeclList MethodDeclList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormPars FormPars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelopLe RelopLe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelopLs RelopLs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelopGe RelopGe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelopGr RelopGr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelopNe RelopNe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelOpEq RelOpEq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AddopMinus AddopMinus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AddopPlus AddopPlus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AssignOp AssignOp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MulOpMod MulOpMod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MulOpDiv MulOpDiv) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MulOpMul MulOpMul) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ReturnType2 ReturnType2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ReturnType1 ReturnType1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Label Label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList6 ConstIdentList6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList5 ConstIdentList5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList4 ConstIdentList4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList3 ConstIdentList3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList2 ConstIdentList2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstIdentList1 ConstIdentList1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ActualParsApp2 ActualParsApp2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ActualParsApp1 ActualParsApp1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ActualPars1 ActualPars1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ActualPars2 ActualPars2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor9 Factor9) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor8 Factor8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor7 Factor7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor6 Factor6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor5 Factor5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor4 Factor4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor3 Factor3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor2 Factor2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor1 Factor1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CondFact2 CondFact2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CondFact1 CondFact1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CondTerm2 CondTerm2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CondTerm1 CondTerm1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConditionOne ConditionOne) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConditionList ConditionList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TermOne TermOne) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TermList TermList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TermHelper2 TermHelper2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TermHelper1 TermHelper1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExprPos ExprPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExprNeg ExprNeg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NoDesignatorListOpts NoDesignatorListOpts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorListOpts DesignatorListOpts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Designator Designator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesId DesId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesOpt2 DesOpt2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesOpt1 DesOpt1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorApps2 DesignatorApps2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorApps1 DesignatorApps1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorAppsList1 DesignatorAppsList1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesignatorAppsList2 DesignatorAppsList2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesStmt5 DesStmt5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesStmt4 DesStmt4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesStmt3 DesStmt3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesStmt2 DesStmt2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesStmt1 DesStmt1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StmtListStmt StmtListStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhileStmt WhileStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ReadStmt ReadStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ContinueStmt ContinueStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BreakStmt BreakStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IfStatement IfStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IfElseStatement IfElseStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ReturnNoExpr ReturnNoExpr) {
		// TODO Auto-generated method stub
		log.info("return void called");
		
	}

	@Override
	public void visit(ReturnExpr ReturnExpr) {
		log.info("return value called");		
	}

	@Override
	public void visit(PrintStmt2 PrintStmt2) {
		// TODO Auto-generated method stub
		printCallCount++;
	}

	@Override
	public void visit(PrintStmt1 PrintStmt1) {
		// TODO Auto-generated method stub
		printCallCount++;
	}

	@Override
	public void visit(ErrorStmt ErrorStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DesStmt DesStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NoStmt NoStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Statements Statements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormalParamDecl2 FormalParamDecl2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormalParamDecl1 FormalParamDecl1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SingleFormalParamDecl SingleFormalParamDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormalParamDecls FormalParamDecls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NoFormParam NoFormParam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FormParams FormParams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MethodDecl MethodDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NoMethodDecl NoMethodDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MethodDeclarations MethodDeclarations) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Type Type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclFirstPart2 VarDeclFirstPart2) {
		// TODO Auto-generated method stub
		varDeclCount++;
		log.info("posecena " + VarDeclFirstPart2);
	}

	@Override
	public void visit(VarDeclFirstPart1 VarDeclFirstPart1) {
		// TODO Auto-generated method stub
		varDeclCount++;
		log.info("posecena " + VarDeclFirstPart1);
	}

	@Override
	public void visit(VarDeclarations2 VarDeclarations2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclarations1 VarDeclarations1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclList1 VarDeclList1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclList22 VarDeclList22) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(VarDeclList21 VarDeclList21) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExtendsType2 ExtendsType2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExtendsType1 ExtendsType1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassDecl2 ClassDecl2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassDecl1 ClassDecl1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConstDecl ConstDecl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NoDeclarationList NoDeclarationList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DeclarationList3 DeclarationList3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DeclarationList2 DeclarationList2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DeclarationList1 DeclarationList1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Program Program) {
		// TODO Auto-generated method stub
		
	}

}
