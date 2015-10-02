package operators;

import net.sf.jsqlparser.expression.Expression;
import util.Table;
import util.Tuple;
import visitors.ConcreteExpVisitor;

public class SelectOperator extends UnaryOperator {

	Expression exp = null;
	ConcreteExpVisitor vi = null;
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null) {
			if (exp == null) return tp;
			vi.setTuple(tp);
			exp.accept(vi);
			if (vi.getFinalCondition()) return tp;
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
		this.exp = exp;
		vi = new ConcreteExpVisitor(null, child.schema());
	}
	
}

