package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class SemanticAnalyzer extends VisitorAdaptor {
	
	boolean errorDetected=false;
	boolean returnFound=false;
	//Struct returnObjType=Tab.noType; //ovo ce da mi sluzi za proveru tipa. Mozda mora da se radi
	//provera ekvivalencije/kompatibilnosti
	boolean mainFound=false;
	int nVars;
	Obj currentMethod=null;
	Struct currentVariableDefinition = Tab.noType;
	boolean isInWhileBlock=false; //ovo ce sluziti za proveru u break i continue naredbama
	List<Obj> formParamsOfFunction = new ArrayList<>();
	List<Obj> allMethods = new ArrayList<>();
	int numberOfFormParams=0; //prati za tekucu metodu
	final int maximumVariables = 256; //nepotrebno
	Obj currentDesignator=null;
	//Obj currentArrayDesignator=null;
	Struct currentConstStructure=MyTab.noType;
	List<Obj> arrayOfDesignators=new ArrayList<>();
	List<Obj> arrayTypeDesignatorsNesting = new ArrayList<>();
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(Program program) {
		nVars = MyTab.currentScope.getnVars();
    	MyTab.chainLocalSymbols(program.getProgName().obj);
    	MyTab.closeScope();
		if(!validMainFunctionFound()) {
			report_error("Semanticka greska na liniji " + program.getLine() + 
					": ne postoji validna main funkcija u programu!", null);
		}
	}
	
	public void visit(ProgName progName) {
		progName.obj=MyTab.insert(Obj.Prog, progName.getProgName(), MyTab.noType);
		MyTab.openScope();
		report_info("Obradjuje se progname " + progName.getProgName(), progName);
	}
	
	public void visit(MethodTypeName methodTypeName) {
		methodTypeName.obj=currentMethod=MyTab.insert(Obj.Meth, methodTypeName.getMethName(),
				methodTypeName.getReturnType().struct); //metoda u tabeli simbola
    	MyTab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName() + " koja vraca "
				+ getTypeFromInt(methodTypeName.getReturnType().struct.getKind()) + ", " + methodTypeName.obj.getAdr() +
				", " + methodTypeName.obj.getLevel(), methodTypeName);
		if(methodTypeName.getMethName().equals("main") && methodTypeName.getReturnType().struct==MyTab.noType
				/*&& numberOfFormParams==0*/) { //ne moze ovo ovde, popravila si dole je i okej je!
			mainFound=true;
		}
	}
	
	public void visit(ReturnType1 retType1) { //non-void
		Obj typeNode = MyTab.find(retType1.getType().getTypeName());
		if (typeNode == MyTab.noObj) {
			report_error("Greska na liniji " + retType1.getLine() +": Nije pronadjen tip " + retType1.getType().getTypeName() + " u tabeli simbola", retType1);
			retType1.struct = MyTab.noType;
		} 
		else {
			if (Obj.Type == typeNode.getKind()) {
				retType1.struct = typeNode.getType();
			} 
			else {
				report_error("Greska: Ime " + retType1.getType().getTypeName() + " ne predstavlja tip ", retType1);
				retType1.struct = MyTab.noType;
			}
		}
	}
	
	public void visit(ReturnType2 retType2) { //void
		retType2.struct=MyTab.noType;
	}
	
	public void visit(Type type) { //int, char, bool, ...
		Obj typeNode = MyTab.find(type.getTypeName());
		if (typeNode == MyTab.noObj) { //ovde je uslov int, bool, char, ...
			report_error("Greska na liniji " + type.getLine() + ": Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", type);
			type.struct = MyTab.noType;
		} 
		else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} 
			else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				type.struct = MyTab.noType;
			}
		}
	}
	
    public void visit(MethodDecl methodDecl) {
    	if(returnFound==false) {
    		if(currentMethod.getType()!= MyTab.noType) {
    			report_error("Semanticka greska na liniji " + methodDecl.getLine() + 
    					": funkcija " + currentMethod.getName() + " nema return iskaz!", methodDecl);
    	}
    	}
    	
    	if(methodDecl.getMethodTypeName().getMethName().equals("main")) {
    		//report_info("main found and form params are " + numberOfFormParams, null);
    		if(numberOfFormParams>0) mainFound=false;
    	}
    	allMethods.add(currentMethod);
    	currentMethod.setLevel(numberOfFormParams);
		MyTab.chainLocalSymbols(currentMethod);
		MyTab.closeScope();
		returnFound = false;
		currentMethod = null; //izasli smo iz metode!
		numberOfFormParams=0;
		formParamsOfFunction.clear(); //praznimo za sledecu metodu
    }
    
    public void visit(ReturnNoExpr returnNoExpr) { //validno je samo za void funkciju
		if(currentMethod==null) {
			report_error("Greska na liniji " + returnNoExpr.getLine() + " Return naredba je validna samo ako smo unutar metode! ", returnNoExpr);
		}
    	returnFound=true;
    	if(currentMethod.getType()!=MyTab.noType) {
			report_error("Semanticka greska: funkcija " + currentMethod.getName() + " nije tipa void, mora da vrati "
					+ getTypeFromInt(currentMethod.getType().getKind()), returnNoExpr);
    	}
    }
    
    public void visit(VarDeclFirstPart1 varDeclFirstPart1) { //1 i 3 su nizovi, 2 i 4 su samo vars
		currentVariableDefinition = varDeclFirstPart1.getType().struct;
		if(!alreadyExistsInMyTab(varDeclFirstPart1.getVarName())) {
			Obj varNode = MyTab.insert(Obj.Var, 
					varDeclFirstPart1.getVarName(), new Struct(Struct.Array, currentVariableDefinition));
			varDeclFirstPart1.obj=varNode;
			report_info("Deklarisana promenljiva "+ varDeclFirstPart1.getVarName() + " niz tipa "
					+ getTypeFromInt(currentVariableDefinition.getKind()) + ", " + varNode.getAdr() +
					", " + varNode.getLevel(), varDeclFirstPart1);
		}
		else {
			report_error("Greska na liniji " + varDeclFirstPart1.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + varDeclFirstPart1.getVarName(), null);
		}
   }
    
    public void visit(VarDeclFirstPart3 varDeclFirstPart3) {
    	if(currentVariableDefinition==null) report_error("Greska na liniji " + varDeclFirstPart3.getLine()
    	+ ": nepoznat tip promenljive!", varDeclFirstPart3); //ne znam da li je ovo uopste potrebno
		if(!alreadyExistsInMyTab(varDeclFirstPart3.getVarName())) {
			Obj varNode = MyTab.insert(Obj.Var, 
					varDeclFirstPart3.getVarName(), new Struct(Struct.Array, currentVariableDefinition));
			varDeclFirstPart3.obj=varNode;
			report_info("Deklarisana promenljiva "+ varDeclFirstPart3.getVarName() + " niz tipa "
					+ getTypeFromInt(currentVariableDefinition.getKind()) + ", " + varNode.getAdr() +
					", " + varNode.getLevel(), varDeclFirstPart3);
		}
		else {
			report_error("Greska na liniji " + varDeclFirstPart3.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + varDeclFirstPart3.getVarName(), null);
		}
    }
    
    public void visit(VarDeclFirstPart2 varDeclFirstPart2) {
		currentVariableDefinition = varDeclFirstPart2.getType().struct;
		if(!alreadyExistsInMyTab(varDeclFirstPart2.getVarName())) {
		Obj varNode = MyTab.insert(Obj.Var, varDeclFirstPart2.getVarName(), currentVariableDefinition);
		varDeclFirstPart2.obj=varNode;
		report_info("Deklarisana promenljiva "+ varDeclFirstPart2.getVarName() + " tipa "
				+ getTypeFromInt(currentVariableDefinition.getKind()) + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), varDeclFirstPart2);
		}
		else {
			report_error("Greska na liniji " + varDeclFirstPart2.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + varDeclFirstPart2.getVarName(), null);
			}
    }
    
    public void visit(VarDeclFirstPart4 varDeclFirstPart2) {
    	if(currentVariableDefinition==null) report_error("Greska na liniji " + varDeclFirstPart2.getLine()
    	+ ": nepoznat tip promenljive!", varDeclFirstPart2);
		if(!alreadyExistsInMyTab(varDeclFirstPart2.getVarName())) {
		Obj varNode = MyTab.insert(Obj.Var, varDeclFirstPart2.getVarName(), currentVariableDefinition);
		varDeclFirstPart2.obj=varNode;
		report_info("Deklarisana promenljiva "+ varDeclFirstPart2.getVarName() + " tipa "
				+ getTypeFromInt(currentVariableDefinition.getKind()) + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), varDeclFirstPart2);
		}
		else {
			report_error("Greska na liniji " + varDeclFirstPart2.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + varDeclFirstPart2.getVarName(), null);
		}
    }
    
    public void visit(EndOfVarDecls endOfVarDecls) {
    	currentVariableDefinition=null;
    }
    
    public void visit(WhileStart whileStart) {
    	isInWhileBlock=true;
    }
    
    public void visit(WhileStmt whileStmt) {
    	if(whileStmt.getCondition().struct!=MyTab.boolType) {
			report_error("Greska na liniji " + whileStmt.getLine() + " : " + 
		"uslov u while petlji mora biti bool tipa, a on je " + getTypeFromInt(whileStmt.getCondition().struct.getKind()), null);			    		
    	}
    	isInWhileBlock=false;
    }
    
    public void visit(IfStatement ifStatement) {
    	Struct s1 = ifStatement.getCondition().struct;
    	if(s1!=MyTab.boolType) {
			report_error("Greska na liniji " + ifStatement.getLine() + " : " + 
		"uslov u if petlji mora biti bool tipa, a on je " + getTypeFromInt(ifStatement.getCondition().struct.getKind()), null);			    		    		
    	}
    }
    
    public void visit(IfElseStatement ifElseStatement) {
    	Struct s1 = ifElseStatement.getCondition().struct;
    	if(s1!=MyTab.boolType) {
			report_error("Greska na liniji " + ifElseStatement.getLine() + " : " + 
		"uslov u if petlji mora biti bool tipa, a on je " + getTypeFromInt(ifElseStatement.getCondition().struct.getKind()), null);			    		    		
    	}    	
    }
    
    public void visit(ConditionOne condition) {
    	condition.struct=condition.getCondTerm().struct;
    }
    
    public void visit(ConditionList condition) {
    	Struct struct1 = condition.getCondTerm().struct;
    	Struct struct2 = condition.getCondition().struct; 
    	if(struct1!=struct2) { //mislim da ovde zapravo treba neka kompatibilnost, ili ekvivalencija
			report_error("Greska na liniji " + condition.getLine() + " : " + 
		"uslovi moraju biti istog tipa! ", null);
		condition.struct=MyTab.noType; //FLAG
    	}
    	else condition.struct=struct1;
    }
    
    public void visit(CondTerm1 condTerm) {
    	Struct struct1 = condTerm.getCondTerm().struct;
    	Struct struct2 = condTerm.getCondFact().struct; 
    	if(struct1!=struct2) {
			report_error("Greska na liniji " + condTerm.getLine() + " : " + 
		"uslovi moraju biti istog tipa! ", null);
			condTerm.struct=MyTab.noType; //FLAG
    	}
    	else condTerm.struct=struct1;
    }
    
    public void visit(CondTerm2 condTerm) {
    	condTerm.struct=condTerm.getCondFact().struct;
    }
    
    public void visit(CondFact1 condFact1) {
    	if(condFact1.getExpr().struct!=MyTab.boolType) {
			report_error("Greska na liniji " + condFact1.getLine() + " : " + 
		"uslov mora biti boolean tipa ", null);
			condFact1.struct=MyTab.noType; //FLAG OVO JE BITNO
    	}
    	else condFact1.struct=condFact1.getExpr().struct;
    }
    
    public void visit(CondFact2 condFact2) {
    	Struct struct1 = condFact2.getExpr1().struct; //what
    	Struct struct2 = condFact2.getExpr().struct;
    	Relop relop=condFact2.getRelop();
    	if(!struct1.compatibleWith(struct2)) {
			report_error("Greska na liniji " + condFact2.getLine() + " : " + 
		"uslovi moraju biti kompatibilni! ", null);
			condFact2.struct=MyTab.noType;
    	}
    	else if(struct2.getKind()==Struct.Array || struct2.getKind()==Struct.Class) { //da li je provera za drugi??
    		if(!(relop instanceof RelOpEq || relop instanceof RelopNe)) {
    			report_error("Greska na liniji " + condFact2.getLine() + " : " + 
    					"za klase i nizove jedini relacioni operatori su == i != ! ", null); 
    			condFact2.struct=MyTab.noType;
    		}
    		else condFact2.struct=MyTab.boolType;
    	}
    	else condFact2.struct=MyTab.boolType;
    }
    
    public void visit(BreakStmt breakStmt) {
    	if(!isInWhileBlock) report_error("Greska:" + " break naredba nije u okruzujucem while bloku!", breakStmt);
    }
    
    public void visit(ContinueStmt continueStmt) {
    	if(!isInWhileBlock) report_error("Greska:" + " continue naredba nije u okruzujucem while bloku!", continueStmt);    	
    }
    
	public void visit(ReturnExpr returnExpr){
		if(currentMethod==null) {
			report_error("Greska na liniji " + returnExpr.getLine() + " Return naredba je validna samo ako smo unutar metode! ", returnExpr);
		}
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + 
		"tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}			  	     	
	}

	public void visit(ExprNeg exprNeg) {
		if(exprNeg.getTerm().struct!=MyTab.intType) {
			report_error("Greska na liniji " + exprNeg.getLine() + " : " + 
		"negacija je moguca samo nad integer tipom! ", null);
			exprNeg.struct=MyTab.noType;
		}
		else if(exprNeg.getTermHelper().struct!=MyTab.intType && (exprNeg.getTermHelper().struct!=MyTab.noType
				&& exprNeg.getTermHelper().struct!=MyTab.noType)) {
			report_error("Greska na liniji " + exprNeg.getLine() + " : " + 
		"negacija je moguca samo nad integer tipom! ", null);	
			exprNeg.struct=MyTab.noType;
		}
		else exprNeg.struct = MyTab.intType;
	}
	
	public void visit(ExprPos exprPos) {
		Struct struct1=exprPos.getTerm().struct;
		Struct struct2= exprPos.getTermHelper().struct;
		
		if(struct2!=MyTab.noType) //moguce je da nema sabiranja uopste
		if(!struct1.compatibleWith(struct2)) {
			report_error("Greska na liniji " + exprPos.getLine() + " : " + 
		getTypeFromInt(struct1.getKind()) + " i " + getTypeFromInt(struct2.getKind()) + 
		" tipovi nisu kompatibilni! ", null);
			exprPos.struct=MyTab.noType;
		}
		
		exprPos.struct = exprPos.getTerm().struct;
	}
	
	public void visit(TermOne termOne) {
		termOne.struct=termOne.getFactor().struct;
	}
	
	public void visit(TermList termList) {
		Struct struct1=termList.getFactor().struct;
		Struct struct2=termList.getTerm().struct;
		if(struct1!=MyTab.intType || struct2!=MyTab.intType) {
			report_error("Greska na liniji " + termList.getLine() + " : " + 
		"tipovi za ovu operaciju moraju biti int! ", null);	
			termList.struct=MyTab.noType; //valjda????
		}
		else termList.struct=MyTab.intType;
	}
	
	public void visit(TermHelper1 termHelper1) { //u slucaju exprneg mora da se proveri da li je ovo int
		Struct struct1=termHelper1.getTerm().struct;
		Struct struct2=termHelper1.getTermHelper().struct;
		/*if(!areEquivalent(struct1, struct2)) {
			report_error("Greska na liniji " + termHelper1.getLine() + " : " + 
		"tipovi nisu ekvivalentni! ", null);			
		} */
		//ova provera kompatibilnosti mora i u expr!
		if(struct2!=MyTab.noType) {
		if(!struct1.compatibleWith(struct2)) {
			report_error("Greska na liniji " + termHelper1.getLine() + " : " + 
		getTypeFromInt(struct1.getKind()) + " i " + getTypeFromInt(struct2.getKind()) + 
		" tipovi nisu kompatibilni! ", null);	
			return; //flag
		}
		if(struct1!=MyTab.intType || struct2!=MyTab.intType) {
			report_error("Greska na liniji " + termHelper1.getLine() + " : " +  
		" operandi za sabiranje i oduzimanje moraju biti tipa int! ", null);	
			return; //flag			
		}
	}
		termHelper1.struct=struct1; //usvaja strukturu onoga sto sigurno postoji
	}
	
	public void visit(TermHelper2 termHelper2) {
		termHelper2.struct=MyTab.noType; //epsilon
	}
	
	public void visit(Factor1 factor1) {
		factor1.struct=MyTab.intType; 
	}
	
	public void visit(Factor2 factor2) {
		factor2.struct=factor2.getDesignator().obj.getType();
	}
	
	public void visit(Factor3 factor3) {}
	
	public void visit(Factor4 factor4) { //charconst
		factor4.struct=MyTab.charType;
	}
	
	public void visit(Factor5 factor5) { //boolconst
		factor5.struct=MyTab.boolType;
	}
	
	public void visit(Factor6 factor6) { //expr
		factor6.struct=factor6.getExpr().struct;
	}
	
	public void visit(Factor7 factor7) { //expr mora biti tipa int
		//faktor dobija strukturu array of exprtype, a provera je da li je exprtype int
		if(factor7.getExpr().struct.getKind()!=Struct.Int) {
			report_error("Greska na liniji " + factor7.getLine() + " : " + 
		"izraz u indeksiranju mora biti int tipa, a ovde je " +
					getTypeFromInt(factor7.getExpr().struct.getKind()) , factor7);
			factor7.struct=MyTab.noType;
			return;
		}
		Struct whatElemOfArr = factor7.getType().struct;
		factor7.struct=new Struct(Struct.Array, whatElemOfArr);
	}
	
	public void visit(Factor8 factor8) {}
	
	public void visit(Factor9 factor9) {}
	
	public void visit(NoFormParam noFormParam) {
		numberOfFormParams=0;
		}
	
	public void visit(FormParams formParams) {}
	
	public void visit(FormalParamDecls formalParamDecls) {}
	
	public void visit(SingleFormalParamDecl singleFormalParamDecl) {}
	
	public void visit(FormalParamDecl1 formalParamDecl1) {//ovo je niz, ne znam da li treba da se oznace obj cvorovi...
		Obj newObject = MyTab.insert(Obj.Var, formalParamDecl1.getFormIdent(), 
				new Struct(Struct.Array, formalParamDecl1.getType().struct));
		formParamsOfFunction.add(newObject);
		report_info("added formalparam " + formalParamDecl1, null);
		numberOfFormParams++;
	}
	
	public void visit(FormalParamDecl2 formalParamDecl2) { //ovo treba da se dodaje u tabelu simbola??
		Obj newObject = MyTab.insert(Obj.Var, formalParamDecl2.getFormIdent(), formalParamDecl2.getType().struct);
		formParamsOfFunction.add(newObject);
		numberOfFormParams++;
		report_info("numberofformparams is " + numberOfFormParams, null);
	}
	
	public void visit(ConstDecl constDecl) { //treba naravno i dodavanje u tabelu simbola
		Struct firstStruct = constDecl.getConstDeclHelper().struct;
		Struct secondStruct;
		if(constDecl.getConstIdentList().obj!=MyTab.noObj)
		secondStruct = constDecl.getConstIdentList().obj.getType();
		else {
			report_error("Greska na liniji " + constDecl.getLine() + " : " + 
		"tipovi nisu ekvivalentni! ", null);
			return;
		}
			if(!firstStruct.equals(secondStruct)) {
			report_error("Greska na liniji " + constDecl.getLine() + " : " + 
		"tipovi nisu ekvivalentni! ", null);
		}
	}
	
	public void visit(ConstDeclHelper constDeclHelper) { //ovo jeste struktura to je okej
		constDeclHelper.struct=constDeclHelper.getType().struct;
		
	}
	
	public void visit(ConstIdentList1 constIdentList1) { //da li ovo treba da bude struktura ili objekat????
		Struct otherStruct=constIdentList1.getConstIdentList().obj.getType(); //flag
			if(!otherStruct.equals(MyTab.intType)) {
			report_error("Greska na liniji " + constIdentList1.getLine() + " : " + 
		"tipovi nisu ekvivalentni! ", null);
			constIdentList1.obj=MyTab.noObj;
			return; //za sad videcemo dalje
		}
		
		if(!alreadyExistsInMyTab(constIdentList1.getIdent1())) {
		Obj varNode = MyTab.insert(Obj.Con, constIdentList1.getIdent1(), MyTab.intType);
		varNode.setAdr(constIdentList1.getNumConst1());
		constIdentList1.obj=varNode;
		report_info("Deklarisana konstanta "+ varNode.getName() + " tipa int" + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), constIdentList1);
		}
		else {
			report_error("Greska na liniji " + constIdentList1.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + constIdentList1.getIdent1(), null);
		}

	}

	public void visit(ConstIdentList2 constIdentList2) { //za konstante se na adresu upisuje vrednost!!!
		Struct otherStruct=constIdentList2.getConstIdentList().obj.getType(); //flag
			if(!otherStruct.equals(MyTab.charType)) {
			report_error("Greska na liniji " + constIdentList2.getLine() + " : " + 
		"tipovi nisu ekvivalentni! ", null);
			constIdentList2.obj=MyTab.noObj;
			return; //flag
		}
		
		if(!alreadyExistsInMyTab(constIdentList2.getIdent2())) {
		Obj varNode = MyTab.insert(Obj.Con, constIdentList2.getIdent2(), MyTab.charType);
		varNode.setAdr(constIdentList2.getCharConst2().charAt(1)); //brise '
		constIdentList2.obj=varNode;
		report_info("Deklarisana konstanta "+ varNode.getName() + " tipa char" + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), constIdentList2);
		}
		else {
			report_error("Greska na liniji " + constIdentList2.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + constIdentList2.getIdent2(), null);
		}
	}

	public void visit(ConstIdentList3 constIdentList3) { //cvor objekat ima polje tip
		Struct otherStruct=constIdentList3.getConstIdentList().obj.getType(); //flag
			if(!otherStruct.equals(MyTab.boolType)) {
			report_error("Greska na liniji " + constIdentList3.getLine() + " : " + 
		"tipovi nisu ekvivalentni! ", null);
			constIdentList3.obj=MyTab.noObj;
			return; //FLAG
		}
		
		if(!alreadyExistsInMyTab(constIdentList3.getIdent3())) {
		Obj varNode = MyTab.insert(Obj.Con, constIdentList3.getIdent3(), MyTab.boolType);
		if(constIdentList3.getBoolConst3().equals("true")) varNode.setAdr(1);
		else if (constIdentList3.getBoolConst3().equals("false")) varNode.setAdr(0);
		constIdentList3.obj=varNode;
		report_info("Deklarisana konstanta "+ varNode.getName() + " tipa bool" + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), constIdentList3);
		}
		else {
			report_error("Greska na liniji " + constIdentList3.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + constIdentList3.getIdent3(), null);
		}
	}

	public void visit(ConstIdentList4 constIdentList4) {
		if(!alreadyExistsInMyTab(constIdentList4.getIdent4())) { //sve const moraju biti objekti u cupu?
		Obj varNode = MyTab.insert(Obj.Con, constIdentList4.getIdent4(), MyTab.intType);
		varNode.setAdr(constIdentList4.getNumConst4());
		constIdentList4.obj=varNode;
		report_info("Deklarisana konstanta "+ varNode.getName() + " tipa int" + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), constIdentList4);
		}
		else {
			report_error("Greska na liniji " + constIdentList4.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + constIdentList4.getIdent4(), null);
		}
	}

	public void visit(ConstIdentList5 constIdentList5) {
		if(!alreadyExistsInMyTab(constIdentList5.getIdent5())) {
		Obj varNode = MyTab.insert(Obj.Con, constIdentList5.getIdent5(), MyTab.charType);
		varNode.setAdr(constIdentList5.getCharConst5().charAt(1)); //brise '?
		constIdentList5.obj=varNode;
		report_info("Deklarisana konstanta "+ varNode.getName() + " tipa char" + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), constIdentList5);
		}
		else {
			report_error("Greska na liniji " + constIdentList5.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + constIdentList5.getIdent5(), null);

		}
	}

	public void visit(ConstIdentList6 constIdentList6) { //bool
		if(!alreadyExistsInMyTab(constIdentList6.getIdent6())) {
		Obj varNode = MyTab.insert(Obj.Con, constIdentList6.getIdent6(), MyTab.boolType);
		if(constIdentList6.getBoolConst6().equals("true")) varNode.setAdr(1);
		else if (constIdentList6.getBoolConst6().equals("false")) varNode.setAdr(0);
		constIdentList6.obj=varNode;
		report_info("Deklarisana konstanta "+ varNode.getName() + " tipa bool" + ", " + varNode.getAdr() +
				", " + varNode.getLevel(), constIdentList6);
		}
		else {
			report_error("Greska na liniji " + constIdentList6.getLine() + " : " + 
		"visestruka deklaracija sa istim imenom " + constIdentList6.getIdent6(), null);
		}
	}
	
	public void visit(PrintStmt1 printStmt1) {
		Struct struct = printStmt1.getExpr().struct;
		if(struct!=MyTab.intType && struct!=MyTab.boolType && struct!=MyTab.charType) {
			report_error("Greska na liniji " + printStmt1.getLine() + " : " + 
		"print moze samo sa tipovima int, char i bool, a dat je tip " + getTypeFromInt(struct.getKind()), null);
		}
	}
	
	public void visit(PrintStmt2 printStmt2) {
		Struct struct = printStmt2.getExpr().struct;
		if(struct!=MyTab.intType && struct!=MyTab.boolType && struct!=MyTab.charType) {
			report_error("Greska na liniji " + printStmt2.getLine() + " : " + 
		"print moze samo sa tipovima int, char i bool, a dat je tip " + getTypeFromInt(struct.getKind()), null);
		}
	}
	
	public void visit(Designator designator) {
		DesignatorList designatorList = designator.getDesignatorList();
		if(designatorList instanceof NoDesignatorListOpts) {
			designator.obj=designator.getDesignatorIdent().obj; //iz tabele simbola dobija ovo, to je okej?
			 report_info("Naisao na " + designator.obj.getName() +
			" sto je " + getKindFromInt(designator.obj.getKind())
			+ " " + getTypeFromInt(designator.obj.getType().getKind()) + " " + designator.obj.getAdr() +
			", " + designator.obj.getLevel(), designator); 
		}
		else if(designatorList instanceof DesignatorListOpts) { //treba da dobijemo element niza, to i dobijamo
			designator.obj=designator.getDesignatorList().obj; //ovim se valjda utvrdjuje da li je niz ili polje?
			report_info("Naisao na element niza " + designator.obj.getName() 
			+ ", tipa " /* +  getKindFromInt(designator.obj.getKind()) + " " */
					+ getTypeFromInt(designator.obj.getType().getKind()), designator);
		}
	}
	
	public void visit(DesId designatorIdent) {
		if(!alreadyExistsInMyTab(designatorIdent.getDesIdent())) {
			report_error("Greska na liniji " + designatorIdent.getLine() + " : " + designatorIdent.getDesIdent() +
		" se ne nalazi u tabeli simbola!", null);
			designatorIdent.obj=MyTab.noObj;
			currentDesignator=MyTab.noObj;
		}
		else {
			designatorIdent.obj=MyTab.find(designatorIdent.getDesIdent());
			currentDesignator=designatorIdent.obj; //ako je on u tabeli simbola, pise tamo da je tipa array
			if(currentDesignator.getType().getKind()==Struct.Array) {
				arrayTypeDesignatorsNesting.add(currentDesignator);
		}
		}
	}
	
	public void visit(NoDesignatorListOpts noDesignatorListOpts) {
		noDesignatorListOpts.obj=MyTab.noObj;
	}
	
	public void visit(DesignatorListOpts designatorListOpts) {
		DesignatorOptions desOpt = designatorListOpts.getDesignatorOptions();
		if(desOpt instanceof DesOpt2) {
			designatorListOpts.obj=desOpt.obj;
		}
		else designatorListOpts.obj=MyTab.noObj;
	}
	
	public void visit(DesOpt1 desOpt1) {
		//nista jer ne moze varijanta .ident za nivo a
		desOpt1.obj=MyTab.noObj;
	}
	
	public void visit(DesOpt2 desOpt2) { //niz
		if(!arrayTypeDesignatorsNesting.isEmpty()) {
			Obj obj = arrayTypeDesignatorsNesting.get(arrayTypeDesignatorsNesting.size()-1);
			desOpt2.obj = new Obj(Obj.Elem, obj.getName(), obj.getType().getElemType());
			arrayTypeDesignatorsNesting.remove(arrayTypeDesignatorsNesting.size()-1);
			return;
		}
		if(desOpt2.getExpr().struct!=MyTab.intType) {
			report_error("Greska 2 na liniji " + desOpt2.getLine() + " : " + 
		"nemoguce indeksiranje tipom koji nije int, a ovo je " + getTypeFromInt(desOpt2.getExpr().struct.getKind()), null);
			desOpt2.obj=MyTab.noObj; //flag privremeno	
			return;
		}
		desOpt2.obj=MyTab.noObj;
	} 
	
	public void visit(DesStmt1 desStmt1) {
		if(desStmt1.getDesignator().obj.getKind()!=Obj.Var && desStmt1.getDesignator().obj.getKind()!=Obj.Fld
				&& desStmt1.getDesignator().obj.getKind()!=Obj.Elem) {
			report_error("Greska na liniji " + desStmt1.getLine() +
		" : dodela je moguca samo za varijable, polja, i elementi niza, a " + desStmt1.getDesignator().obj.getName()
		+ " je " + getKindFromInt(desStmt1.getDesignator().obj.getKind()), null);			
		}
		
		if(!(desStmt1.getDesignator().obj.getType().assignableTo(desStmt1.getExpr().struct))) {
			report_error("Greska na liniji " + desStmt1.getLine() +
		" : tipovi " + getTypeFromInt(desStmt1.getDesignator().obj.getType().getKind())
		+ " i " + getTypeFromInt(desStmt1.getExpr().struct.getKind()) + " nisu kompatibilni za dodelu!", null);
			if(desStmt1.getDesignator().obj.getType().getKind()==Struct.Array 
					&& desStmt1.getExpr().struct.getKind()==Struct.Array) {
				report_info("Elementi nekompatibilnih nizova su tipa " + 
					getTypeFromInt(desStmt1.getDesignator().obj.getType().getElemType().getKind())
					+ " i " + getTypeFromInt(desStmt1.getExpr().struct.getElemType().getKind()), null);
			}
		}
	}
	
	public void visit(DesStmt3 desStmt3) { //vrati se
		if(desStmt3.getDesignator().obj.getKind()!=Obj.Var && desStmt3.getDesignator().obj.getKind()!=Obj.Fld
				&& desStmt3.getDesignator().obj.getKind()!=Obj.Elem) {
			report_error("Greska na liniji " + desStmt3.getLine() +
		" : inkrementirati se mogu samo varijable, polja, i elementi niza, a " + desStmt3.getDesignator().obj.getName()
		+ " je " + getKindFromInt(desStmt3.getDesignator().obj.getKind()), null);			
		}
		if(desStmt3.getDesignator().obj.getType()!=MyTab.intType) {
			report_error("Greska na liniji " + desStmt3.getLine() +
		" : Ne moze se raditi inkrementiranje tipa koji nije int!", null);
		}
	}
	
	public void visit(DesStmt4 desStmt4) {
		if(desStmt4.getDesignator().obj.getKind()!=Obj.Var && desStmt4.getDesignator().obj.getKind()!=Obj.Fld
				&& desStmt4.getDesignator().obj.getKind()!=Obj.Elem) {
			report_error("Greska na liniji " + desStmt4.getLine() +
		" : dekrementirati se mogu samo varijable, polja, i elementi niza, a " + desStmt4.getDesignator().obj.getName()
		+ " je " + getKindFromInt(desStmt4.getDesignator().obj.getKind()), null);			
		}

		if(desStmt4.getDesignator().obj.getType()!=MyTab.intType) {
			report_error("Greska na liniji " + desStmt4.getLine() +
		" : Ne moze se raditi dekrementiranje tipa koji nije int!", null);
		}		
	}
	
	public void visit(DesStmt5 desStmt5) { //ovo je ona najspecificnija!
		if(desStmt5.getDesignator().obj.getType().getKind()!=Struct.Array) {
			report_error("Greska na liniji " + desStmt5.getLine() +
		" : sa desne strane operatora = moze biti samo niz, a ovo je " + 
					getTypeFromInt(desStmt5.getDesignator().obj.getType().getKind()), null);
			arrayOfDesignators.clear(); //za sledecu pojavu FLAG
			return;
		}
		//array treba sad da sadrzi i noobj cvorove
		Obj desObject = desStmt5.getDesignator().obj;
		for(Obj o: arrayOfDesignators) {
			if(!o.getType().compatibleWith(desObject.getType().getElemType()) && o.getType().getKind()!=Struct.None) {
				report_error("Greska na liniji " + desStmt5.getLine() +
					" : nekompatibilni tipovi prilikom dodele: " + getTypeFromInt(desObject.getType().getKind())
					+ " i " + getTypeFromInt(o.getType().getKind()), null);
				arrayOfDesignators.clear(); //za sledecu pojavu FLAG
				return;
			}
		}
		
		int numberInList = arrayOfDesignators.size();
		int kindOfNode = desStmt5.getDesignator().getDesignatorIdent().obj.getKind();
		//OVO JE SUVISAN I NEISPRAVAN KOD, PROVERA SE RADI U CODEGEN
		if(kindOfNode==Obj.Con) {
			int numberInDesArray = desStmt5.getDesignator().getDesignatorIdent().obj.getAdr();
			if(numberInList>numberInDesArray) {
				report_error("Greska na liniji " + desStmt5.getLine() +
						" : neispravna dodela, duzine nizova se ne podudaraju: " +
						arrayOfDesignators.size()
						+ " i " + numberInDesArray, null);				
			}
		}
		//na kraju:
		arrayOfDesignators.clear(); //za sledecu pojavu
	}
	
	public void visit(DesignatorAppsList2 designatorAppsList2) {
		Obj endDesignator = designatorAppsList2.getDesignatorApps().obj;
		arrayOfDesignators.add(endDesignator); //trebalo bi da se radi rekurzivni poziv ovog obilaska
	}
	
	public void visit(DesignatorAppsList1 designatorAppsList1) { //jedan ili nijedan
		designatorAppsList1.obj=designatorAppsList1.getDesignatorApps().obj;
		arrayOfDesignators.add(designatorAppsList1.getDesignatorApps().obj);
	}
	
	public void visit(DesignatorApps1 designatorApps1) { //ovo se poziva samo u desstmt5 smeni pa moze ovde provera greske
		designatorApps1.obj=designatorApps1.getDesignator().obj;
		if(designatorApps1.obj.getKind()!=Obj.Var && designatorApps1.obj.getKind()!=Obj.Elem &&
				designatorApps1.obj.getKind()!=Obj.Fld) {
			report_error("Greska na liniji " + designatorApps1.getLine() +
		" : dekrementirati se mogu samo varijable, polja, i elementi niza, a " + designatorApps1.obj.getName()
		+ " je " + getKindFromInt(designatorApps1.obj.getKind()), null);
			designatorApps1.obj=MyTab.noObj;
		}
	}
	
	public void visit(DesignatorApps2 designatorApps2) {
		designatorApps2.obj=MyTab.noObj;		
	}
	
	public void visit(ReadStmt readStmt) {
		if(readStmt.getDesignator().obj.getKind()!=Obj.Var && readStmt.getDesignator().obj.getKind()!=Obj.Fld
				&& readStmt.getDesignator().obj.getKind()!=Obj.Elem) {
			report_error("Greska na liniji " + readStmt.getLine() +
		" : dekrementirati se mogu samo varijable, polja, i elementi niza, a " + readStmt.getDesignator().obj.getName()
		+ " je " + getKindFromInt(readStmt.getDesignator().obj.getKind()), null);			
		}
		
		if(readStmt.getDesignator().obj.getType()!=MyTab.intType && readStmt.getDesignator().obj.getType()!=MyTab.charType
				&& readStmt.getDesignator().obj.getType()!=MyTab.boolType) {
			report_error("Greska na liniji " + readStmt.getLine() +
		" : Read prihvata samo tipove char, int i bool, a ovo je " + 
					getTypeFromInt(readStmt.getDesignator().obj.getType().getKind()), null);
		}		

	}
	
	public boolean validMainFunctionFound() {
		return mainFound;
	}
	
	private String getTypeFromInt(int i) {
		String retStr="";
		switch(i) {
		case 0:
			retStr="void";
			break;
		case 1:
			retStr="int";
			break;
		case 2:
			retStr="char";
			break;
		case 3:
			retStr="array";
			break;
		case 4:
			retStr="class";
			break;
		case 5:
			retStr="bool";
			break;
		case 6:
			retStr="enum";
			break;
		case 7:
			retStr="interface";
			break;
		}
		return retStr;
	}
	
	private String getKindFromInt(int i) {
		String retStr="";
		switch(i) {
		case 0:
			retStr="Con";
			break;
		case 1:
			retStr="Var";
			break;
		case 2:
			retStr="Type";
			break;
		case 3:
			retStr="Meth";
			break;
		case 4:
			retStr="Fld";
			break;
		case 5:
			retStr="Elem";
			break;
		case 6:
			retStr="Prog";
			break;
		}
		return retStr;		
	}
	
	private boolean alreadyExistsInMyTab(String str) { //nije testirano
		Obj found = MyTab.find(str);
		if(found!=MyTab.noObj) return true;
		return false;
	}
	
	public boolean areEquivalent(Struct s1, Struct s2) {
		if(s1.getKind()!=Struct.Array) {
		if(s1.getKind()==s2.getKind()) return true;
		}
		else if(s1.getKind()==Struct.Array && s1.getElemType()==s2.getElemType()) return true;
		return false;
	}
	
	public boolean passedSemanticAnalysis() {
		return !errorDetected;
	}
}
