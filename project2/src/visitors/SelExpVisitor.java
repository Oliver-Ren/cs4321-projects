package visitors;

import java.util.List;

import net.sf.jsqlparser.schema.Column;
import util.Helpers;
import util.Tuple;

/**
 * Select expression visitor which takes one child 
 * and one schema.
 * @author Guantian Zheng (gz94)
 *
 */
public class SelExpVisitor extends ConcreteExpVisitor {

	private Tuple tuple = null;
	private List<String> schema = null;
	
	/**
	 * Constructor.
	 * @param schema the currrent schema
	 */
	public SelExpVisitor(List<String> schema) {
		this.schema = schema;
	}
	
	/**
	 * Reset the member tuple.
	 * @param tuple the new tuple
	 */
	public void setTuple(Tuple tuple) {
		this.tuple = tuple;
	}
	
	/**
	 * Visit a column and assigning the value of 
	 * a certain attribute.
	 */
	@Override
	public void visit(Column arg0) {
		currNumericValue = Helpers.getAttr(tuple, arg0.toString(), schema);
	}
	
}
