package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.FormalParamDecl;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VarDeclFirstPart1;
import rs.ac.bg.etf.pp1.ast.VarDeclFirstPart2;
import rs.ac.bg.etf.pp1.ast.VarDeclFirstPart3;
import rs.ac.bg.etf.pp1.ast.VarDeclFirstPart4;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());

	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
	
		public void visit(FormalParamDecl formParamDecl){
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(VarDecl varDecl){
			count++;
		}
		
		public void visit(VarDeclFirstPart2 varDecl) {
			count++;
		}
		
		public void visit(VarDeclFirstPart4 varDecl) {
			count++;
		}

		public void visit(VarDeclFirstPart1 varDecl) {
			count++;
		}
		
		public void visit(VarDeclFirstPart3 varDecl) {
			count++;
		}

	}
}
