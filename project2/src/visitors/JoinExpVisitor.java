package visitors;

import java.util.List;

import net.sf.jsqlparser.schema.Column;
import util.Helpers;
import util.Tuple;

public class JoinExpVisitor extends ConcreteExpVisitor {

	private Tuple tp1 = null, tp2 = null;
	private List<String> scm1 = null, scm2 = null;
	
	public JoinExpVisitor(Tuple tp1, List<String> scm1, Tuple tp2, List<String> scm2) {
		this.tp1 = tp1;
		this.scm1 = scm1;
		this.tp2 = tp2;
		this.scm2 = scm2;
	}
	
	public void setTuple(Tuple tp1, Tuple tp2) {
		this.tp1 = tp1;
		this.tp2 = tp2;
	}
	
	@Override
	public void visit(Column arg0) {
		Long val = Helpers.getAttr(tp1, arg0.toString(), scm1);
		if (val == null)
			val = Helpers.getAttr(tp2, arg0.toString(), scm2);
		currNumericValue = val;
	}
}
