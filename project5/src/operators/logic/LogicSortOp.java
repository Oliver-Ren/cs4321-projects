package operators.logic;

import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import visitors.PhysicalPlanBuilder;

public class LogicSortOp extends LogicUnaryOp {

	public List<OrderByElement> orders = null;
	
	public LogicSortOp(LogicOperator child, 
			List<OrderByElement> orders) {
		super(child);
		this.orders = orders;
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
	}
	
}
