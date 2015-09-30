package Operators;

import net.sf.jsqlparser.expression.Expression;
import util.Table;
import util.Tuple;

public class SelectOperator extends Operator {

	ScanOperator child = null;
	Expression exp = null;
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null) {
			// create a visitor with current tp (as a constructor parameter?)
			// walk this.exp
			// if visitor says true, return tp
		}
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		child.reset();
	}

	public SelectOperator(Table tab, Expression exp) {
		child = new ScanOperator(tab);
		this.exp = exp;
	}
	
}
