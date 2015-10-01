package operators;

import java.util.List;

import net.sf.jsqlparser.statement.select.SelectItem;
import util.Helpers;
import util.Tuple;

public class ProjectOperator extends Operator {
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tp = child.getNextTuple();
		if (tp == null) return null;
		
		int[] cols = new int[schema.size()];
		int i = 0;
		for (String attr : schema)
			cols[i++] = (int) Helpers.getAttr(tp, attr, child.schema);
		
		return new Tuple(cols);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		child.reset();
	}

	public ProjectOperator(Operator child, List<SelectItem> sis) {
		this.child = child;
		tbs = child.tbs;
		for (SelectItem si : sis) {
			schema.add(Helpers.getSelCol(si));
		}
	}
	
}
