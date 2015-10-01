package operators;

import net.sf.jsqlparser.expression.Expression;
import util.Table;
import util.Tuple;

public class SelectOperator extends Operator {

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

	public SelectOperator(ScanOperator sop, Expression exp) {
		child = sop;
		tbs = sop.tbs;
		schema = sop.schema;
		
		this.exp = exp;
	}
	
}
