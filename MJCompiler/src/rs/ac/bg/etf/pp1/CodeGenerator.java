package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;

public class CodeGenerator extends VisitorAdaptor {

	Logger log = Logger.getLogger(getClass());
	
	private int mainPc;
	
	private ArrayList<Obj> listOfLeftDesignators = new ArrayList<>(); //globalna je
	
	private Obj rightDesignator;
		
	ArrayList<Integer>listOfIndicesBeforeTurn = new ArrayList<>();
	ArrayList<Integer>listOfIndicesAfterTurn = new ArrayList<>();
	ArrayList<Integer> listOfIndicesForRightDesignator = new ArrayList<>();
	
	Obj arrAdr = MyTab.insert(Obj.Var, "arrAdr", MyTab.intType);

	
	public int getMainPc(){
		return mainPc;
	}

	public void visit(ConstIdentList4 constIdentList4) { //numconst
		Obj con = MyTab.insert(Obj.Con, "$", constIdentList4.obj.getType());
		con.setLevel(0);
		con.setAdr(constIdentList4.getNumConst4());
		Code.load(con);
	}
	
	public void visit(ConstIdentList5 constIdentList5) { //charconst
		Obj con = MyTab.insert(Obj.Con, "$", constIdentList5.obj.getType());
		con.setLevel(0);
		con.setAdr(constIdentList5.getCharConst5().charAt(1));
		Code.load(con);		
	}
	
	public void visit(ConstIdentList6 constIdentList6) { //boolconst
		Obj con = MyTab.insert(Obj.Con, "$", constIdentList6.obj.getType());
		con.setLevel(0);
		String boolValue = constIdentList6.getBoolConst6();
		int trueVal = 0;
		if (boolValue.equalsIgnoreCase("true")) trueVal=1;
		con.setAdr(trueVal);
		Code.load(con);			
	}
	
	public void visit(ConstIdentList1 constIdentList1) { //numconst lista
		Obj con = MyTab.insert(Obj.Con, "$", constIdentList1.obj.getType()); //mozda treba samo inttype
		con.setLevel(0);
		con.setAdr(constIdentList1.getNumConst1());
		Code.load(con);		
	}
	
	public void visit(ConstIdentList2 constIdentList2) { //char
		Obj con = MyTab.insert(Obj.Con, "$", constIdentList2.obj.getType());
		con.setLevel(0);
		con.setAdr(constIdentList2.getCharConst2().charAt(1));
		Code.load(con);		
	}
	
	public void visit(ConstIdentList3 constIdentList3) { //bool
		Obj con = MyTab.insert(Obj.Con, "$", constIdentList3.obj.getType());
		con.setLevel(0);
		String boolValue = constIdentList3.getBoolConst3();
		int trueVal = 0;
		if (boolValue.equalsIgnoreCase("true")) trueVal=1;
		con.setAdr(trueVal);
		Code.load(con);			
	}
	
	public void visit(Factor1 factor1) { //numconst
		Obj con = MyTab.insert(Obj.Con, "$", factor1.struct);
		con.setLevel(0); //globalna?
		con.setAdr(factor1.getN1());
		Code.load(con);	//ucitava na stek
	}
	
	public void visit(Factor4 factor4) { //charconst
		Obj con = MyTab.insert(Obj.Con, "$", factor4.struct);
		con.setLevel(0); //globalna?
		con.setAdr(factor4.getC1().charAt(1));
		Code.load(con);			
	}
	
	public void visit(Factor5 factor5) {
		Obj con = MyTab.insert(Obj.Con, "$", factor5.struct);
		con.setLevel(0);
		String boolValue = factor5.getB1();
		int trueVal = 0;
		if (boolValue.equalsIgnoreCase("true")) trueVal=1;
		con.setAdr(trueVal);
		Code.load(con);
	}
	
		
	public void visit(PrintStmt1 printStmt1) {
		if(printStmt1.getExpr().struct == MyTab.intType || printStmt1.getExpr().struct==MyTab.boolType){
			Code.loadConst(5); //sirina???
			Code.put(Code.print);
		}else{
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(PrintStmt2 printStmt2) {
		if(printStmt2.getExpr().struct == MyTab.intType || printStmt2.getExpr().struct==MyTab.boolType){
			Code.loadConst(printStmt2.getN2());
			Code.put(Code.print);
		}else{
			Code.loadConst(printStmt2.getN2());
			Code.put(Code.bprint);
		}
	}
	
	public void visit(MethodTypeName methodTypeName){
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
	
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
	public void visit(MethodDecl methodDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(DesId desId) {
		boolean isArrayType=false;
		if (desId.obj.getType().getKind()==Struct.Array) isArrayType=true;
		if(isArrayType==true) {
			Obj elem=desId.obj;
			Code.load(elem);
			}
	}
	
	public void visit(StartOfDesStmt5 startOfDesStmt5) {
	}
	
	public void visit(EndOfDesStmt5 endOfDesStmt5) {
	}
	
	public void visit(DesOpt2 desOpt2) {
	}
	
	public void visit(NoDesignatorListOpts noDesignatorListOpts) {
	}
	
	public void visit(Designator designator){
	}
	
	public void visit(DesignatorApps2 designatorApps2) {
		designatorApps2.obj=MyTab.noObj; //privremeno
	}
	
	public void visit(ReturnExpr returnExpr){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnNoExpr returnNoExpr){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ExprNeg exprNeg) { //pre nego sto obidje njega, na steku je ceo term, pa je okej samo neg
		Code.put(Code.neg);
	}
	
	public void visit(ExprPos exprPos) {
		
	}
	
	public void visit(TermHelper1 termHelper1) {
		Addop operator = termHelper1.getAddop();
		if(operator instanceof AddopPlus) Code.put(Code.add);
		else Code.put(Code.sub);
	}
	
	public void visit(TermHelper2 termHelper2) {
		
	}
	
	public void visit(TermList termList) {
		//u momentu kad obilazi ovaj cvor, vec je obisao parametre, pa ovde samo gura mulop na stek
		Mulop operator = termList.getMulop();
		if(operator instanceof MulOpMul) Code.put(Code.mul);
		else if (operator instanceof MulOpDiv) Code.put(Code.div);
		else Code.put(Code.rem);
	}
	
	public void visit(TermOne termOne) {
		//samo factor
	}
	
	public void visit(DesStmt3 desStmt3) {
		if(desStmt3.getDesignator().obj.getKind()!=Obj.Elem) {
		Code.load(desStmt3.getDesignator().obj); //ucitaj na stek
		Code.put(Code.const_1); //ucitaj 1 na stek
		Code.put(Code.add); //saberi
		Code.store(desStmt3.getDesignator().obj); //ucitaj sa steka u designator
		}
		else {
			Code.put(Code.dup2); //ono pre ce generisati cvorovi???
			Code.load(desStmt3.getDesignator().obj); //ucitaj na stek
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.store(desStmt3.getDesignator().obj); //store metoda ce prepoznati da treba astore
		}
	}
	
	public void visit(DesStmt4 desStmt4) { //nisu jos definisani nizovi
		if(desStmt4.getDesignator().obj.getKind()!=Obj.Elem) {
		Code.load(desStmt4.getDesignator().obj); //ucitaj na stek
		Code.put(Code.const_1); //ucitaj 1 na stek
		Code.put(Code.sub); //oduzmi
		Code.store(desStmt4.getDesignator().obj); //ucitaj sa steka u designator
		}
		else {
			Code.put(Code.dup2); //ono pre ce generisati cvorovi
			Code.load(desStmt4.getDesignator().obj); //ucitaj na stek
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.store(desStmt4.getDesignator().obj); //store metoda ce prepoznati da treba astore
		}
	}
	
	public void visit(ReadStmt readStmt) {
		if(readStmt.getDesignator().obj.getType()!=MyTab.charType) { //int/bool
			Code.put(Code.read);
		}
		else Code.put(Code.bread); //char
		Code.store(readStmt.getDesignator().obj); //ucitaj sa steka u designator	
	}
	
	public void visit(DesStmt1 desStmt1) { //ovo je assignment designator=expr
		Code.store(desStmt1.getDesignator().obj); //upis, trebalo bi da je faktor vec na steku, a da des ne treba da se ucitava
	}
	
	public void visit(Factor2 factor2) { //ovo je smena factor=designator
		Code.load(factor2.getDesignator().obj); //ucitava des na stek
	}
	
	public void visit(Factor7 factor7) { //new type[expr] //niz
		Code.put(Code.newarray);
		Struct type=factor7.getType().struct;
		int addOn = type.getKind();
		if(addOn==Struct.Char) Code.put(0); //po definiciji newarray 0 je niz bajtova
		else Code.put(1);
		
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop); //FLAG DODATO DA SE OCISTI STEK
	}
	
	public void visit(Factor6 factor6) { //izraz, nista ne treba
	}
	
	public void visit(DesStmt5 desStmt5) {
		DesignatorAppsList designatorAppsList = desStmt5.getDesignatorAppsList();
		if(designatorAppsList instanceof DesignatorAppsList1) { //OVO RADI ISPRAVNO
			DesignatorApps designatorApps = ((DesignatorAppsList1) designatorAppsList).getDesignatorApps();
			if(designatorApps instanceof DesignatorApps1) {
				listOfLeftDesignators.add(((DesignatorApps1) designatorApps).getDesignator().obj);
			}
			else if(designatorApps instanceof DesignatorApps2) {
				listOfLeftDesignators.add(designatorApps.obj); //trebalo bi da je noobj
			}
		}
		else if(designatorAppsList instanceof DesignatorAppsList2) { //ima zarez i ima ih vise, sadrzi listu i jedan elem na kraju
			DesignatorAppsList designatorAppsListHelper = ((DesignatorAppsList2) designatorAppsList).getDesignatorAppsList();
			DesignatorApps lastOne = ((DesignatorAppsList2) designatorAppsList).getDesignatorApps();

			while(!(designatorAppsListHelper instanceof DesignatorAppsList1)) {
				DesignatorApps designatorApps = ((DesignatorAppsList2) designatorAppsListHelper).getDesignatorApps();
				if(designatorApps instanceof DesignatorApps1) {
					listOfLeftDesignators.add(((DesignatorApps1) designatorApps).getDesignator().obj);
				}
				else if(designatorApps instanceof DesignatorApps2) {
					listOfLeftDesignators.add(designatorApps.obj);
			}
				designatorAppsListHelper = ((DesignatorAppsList2) designatorAppsListHelper).getDesignatorAppsList();
		} 
			DesignatorApps designatorApps = ((DesignatorAppsList1) designatorAppsListHelper).getDesignatorApps();
			if(designatorApps instanceof DesignatorApps1) {
				listOfLeftDesignators.add(((DesignatorApps1) designatorApps).getDesignator().obj);
			}
			else if(designatorApps instanceof DesignatorApps2) {
				listOfLeftDesignators.add(designatorApps.obj); //trebalo bi da je noobj
			}
			
			Collections.reverse(listOfLeftDesignators);
			
			if(lastOne instanceof DesignatorApps1) {
				listOfLeftDesignators.add(((DesignatorApps1) lastOne).getDesignator().obj);
			}
			else if(lastOne instanceof DesignatorApps2) {
				listOfLeftDesignators.add(lastOne.obj); //trebalo bi da je noobj
			}			
		}
		
		rightDesignator = desStmt5.getDesignator().obj;
				
		indicesOfArray(listOfIndicesBeforeTurn, listOfIndicesAfterTurn); //obrce listu
		
		Code.put(Code.arraylength); //gura na stek duzinu niza, kako da je dohvatim??
		Obj varNumberOfElements = MyTab.insert(Obj.Con, "helper", MyTab.intType); //stavi elem, to je okej
		varNumberOfElements.setAdr(listOfLeftDesignators.size());
		Code.load(varNumberOfElements);
		Code.putFalseJump(Code.lt, Code.pc+5); //duzine 3
		Code.put(Code.trap); //duzine 2
		Code.put(1); //RADI OVAJ DEO
		
		int indexHelper=0; //mozda da ga inkrementiram na istim mestima kao i varijablu?
		int counter=0; //ovo ce nam menjati indexHelpera u while
		
		createMyRightIndexList();
		
		while(listOfLeftDesignators.size()>counter) {
		
		Code.load(rightDesignator); //ovo je getstatic
		indexHelper=listOfIndicesForRightDesignator.get(counter);
		Obj myIndexRight = MyTab.insert(Obj.Con, "myIndexRight" + indexHelper, MyTab.intType);
		myIndexRight.setAdr(indexHelper);
		Code.load(myIndexRight); //ovo ce biti indeks koji cemo vestacki napraviti
		
		if(rightDesignator.getType()!=MyTab.charType)
		Code.put(Code.aload);
		else Code.put(Code.baload);
				
		Code.store(listOfLeftDesignators.get(indexHelper)); //ovo je obicna var
		
		counter++;
		
	}
	listOfLeftDesignators.clear(); //flag
	}
		
	public void indicesOfArray(ArrayList<Integer> listOfIndicesBeforeTurn, ArrayList<Integer> listofIncidesAfterTurn) {
		listOfIndicesBeforeTurn.clear();
		listOfIndicesAfterTurn.clear();
		for(int k=0; k<listOfLeftDesignators.size(); k++) {
			Obj obj = listOfLeftDesignators.get(k);
			if(obj==MyTab.noObj) continue;
			if(obj.getKind()==Obj.Elem) {
				listOfIndicesBeforeTurn.add(k);
				listOfIndicesAfterTurn.add(k);				
			}
		}
		Collections.reverse(listOfIndicesAfterTurn);
	}
	
	
	public void createMyRightIndexList() {		
		listOfIndicesForRightDesignator.clear();
		
		for(int i=0; i<listOfLeftDesignators.size(); i++) {
			listOfIndicesForRightDesignator.add(i);
		}

		if(areListsTheSame()) return;
		
		for(int i=0; i<listOfLeftDesignators.size(); i++) {
			if(listOfLeftDesignators.get(i).getKind()!=Obj.Elem) listOfIndicesForRightDesignator.set(i, i);
			else {
				if(listOfLeftDesignators.get(i)==MyTab.noObj) {
					//log.info("its an empty object");
					continue;
				}
				int whereIsItInIndicesLists = listOfIndicesBeforeTurn.indexOf(i);
				Integer whatIsItInOppositeList = listOfIndicesAfterTurn.get(whereIsItInIndicesLists);
				listOfIndicesForRightDesignator.set(i, whatIsItInOppositeList);
			}
		}
	}
	
	private boolean areListsTheSame() {
		boolean state=true;
		for(int i=0; i<listOfIndicesBeforeTurn.size(); i++) {
			if(listOfIndicesBeforeTurn.get(i)!=listOfIndicesAfterTurn.get(i)) state=false;
		}
		return state;
	}
	
}
