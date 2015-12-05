package operators.logic;

import java.util.List;

import net.sf.jsqlparser.statement.select.SelectItem;
import visitors.PhysicalPlanBuilder;

public class LogicProjectOp extends LogicUnaryOp {

	public List<SelectItem> sis = null;
	
	public LogicProjectOp(LogicOperator child, List<SelectItem> sis) {
		super(child);
		this.sis = sis;
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
	}

	@Override
	public String print() {
		return String.format("Project%s", 
				((sis == null) ? "[null]" : sis.toString()));
	}
	
}
