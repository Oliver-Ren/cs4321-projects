package visitors;

import java.util.List;

import net.sf.jsqlparser.schema.Column;
import util.Helpers;
import util.Tuple;

/**
 * The join expression visitor. It stores two tuple,
 * and two corresponding schema.
 * @author Guantian Zheng (gz94)
 *
 */
public class JoinExpVisitor extends ConcreteExpVisitor {

	private Tuple tp1 = null, tp2 = null;
	private List<String> scm1 = null, scm2 = null;
	
	/**
	 * Constructor.
	 * @param tp1 tuple 1
	 * @param scm1 schema 1
	 * @param tp2 tuple 2
	 * @param scm2 schema 2
	 */
	public JoinExpVisitor(List<String> scm1, List<String> scm2) {
		this.scm1 = scm1;
		this.scm2 = scm2;
	}
	
	/**
	 * Reset the tuples without changing the schema
	 * @param tp1 the left tuple
	 * @param tp2 the right tuple
	 */
	public void setTuple(Tuple tp1, Tuple tp2) {
		this.tp1 = tp1;
		this.tp2 = tp2;
	}
	
	/**
	 * Visit a column and search for the value of a certain
	 * attribute.
	 */
	@Override
	public void visit(Column arg0) {
		Long val = Helpers.getAttrVal(tp1, arg0.toString(), scm1);
		if (val == null)
			val = Helpers.getAttrVal(tp2, arg0.toString(), scm2);
		currNumericValue = val;
	}
}
