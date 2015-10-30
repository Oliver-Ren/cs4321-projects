package operators;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import util.Helpers;
import util.Tuple;
import visitors.JoinExpVisitor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import nio.BinaryTupleReader;
import nio.TupleReader;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

public class SortMergeJoinOperator extends JoinOperator {
	int partitionIndex;  // the index of the first tuple in the current partition
	int curRightIndex;  // the index of the current tuple of left table
	//TupleReader leftReader; // the tuple reader for left table 
	//TupleReader rightReader; //the  tuple reader for the right table
	Expression exp;  // my expression
	List<EqualsTo> equalExps; 
	JoinExpVisitor jv;
	// = =============some problems
	List<Integer> leftOrders = null; // the order of attributes in left table 
	List<Integer> rightOrders = null;// the order of attributes in right table 
	/**
	 * scan a list of expressions and keep the equals expression
	 * 
	 * @return
	 */
	public List<EqualsTo> getEqualExps(List<Expression> exp){
		if(exp == null) return null;
		for(Expression e : exp ){
			if(e instanceof EqualsTo){
				EqualsTo eq = (EqualsTo) e;
				equalExps.add(eq);
			}
		}
		return equalExps;
		
	}
	
	@Override
	public Tuple getNextTuple()  {
		Tuple rst = null;
		Tuple leftTp = left.getNextTuple();
		Tuple rightTp = right.getNextTuple();
		TupleComp cp  = new TupleComp(leftOrders,rightOrders);
		
		
		while(leftTp !=null && rightTp !=null){ //当两个table都没EOF
			while(leftTp!= null && rightTp!=null 
					&& cp.compare(leftTp,rightTp)<0){ // left[i] < right[j]
				leftTp = left.getNextTuple();
			}
			while(leftTp!=null && rightTp!=null &&
					cp.compare(leftTp, rightTp)>0){// left[i]>right[j]
				rightTp = right.getNextTuple();
				curRightIndex++;
			}
			// check if reaches EOF
			if(leftTp ==null || rightTp ==null) break;
			
			// keep track my current right tuple index
			partitionIndex = curRightIndex;
			while(rightTp != null && cp.compare(leftTp,rightTp) == 0){
				// compare other non equality condition
				if(Helpers.getJoinRes(leftTp, rightTp, exp, jv)){
					rst = joinTp(leftTp,rightTp);
				}
				if(rst!= null) return rst;
				rightTp= right.getNextTuple();
				curRightIndex++;
			}
			//reset my right tuple index
			SortOperator x = (SortOperator)right;
			x.reset(partitionIndex);
			leftTp = left.getNextTuple();	
		}
		return null;
	}
	public SortMergeJoinOperator(Operator left, 
			Operator right, Expression exp,List<Integer> leftOrders,
			List<Integer> rightOrders) {
		super(left, right, exp);
		//EqualsTo et = (EqualsTo) exp;
		//left.schema().contains(et.getLeftExpression());
		this.exp = exp;
		// decompose all the end expression to a list
		List<Expression> list = Helpers.decompAnds(exp); 
		equalExps = getEqualExps(list); // assign the equalExpression to my list
		this.jv = new JoinExpVisitor(left.schema(),right.schema());
		partitionIndex = 0;
		curRightIndex =0;
		this.leftOrders = leftOrders;
		this.rightOrders =rightOrders;
		
		
	}
	
	// comparator class to compare one tuple from the left table and another from the 
	// right table
	public class TupleComp implements Comparator<Tuple>{
		List<Integer> leftOrders = null; // the order of attributes in left table 
		List<Integer> rightOrders = null;// the order of attributes in right table 
		@Override
		public int compare(Tuple left, Tuple right) {
			// verify length
			if (left.length() != right.length()){
				try {
					throw new Exception("Comparing tuples of different lengths");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			}
			// compare each attribue based on order
			for( int i = 0; i< leftOrders.size();i++){
				int leftVal = left.cols[leftOrders.get(i)];
				int rightVal = right.cols[rightOrders.get(i)];
				int cmp = Integer.compare(leftVal, rightVal);
				if(cmp != 0) return cmp;		
			}
			return 0;
				
			
		}
		
		public TupleComp(List<Integer> leftOrders, List<Integer> rightOrders){
				this.leftOrders = leftOrders;
				this.rightOrders = rightOrders;
				
		}
		
	}
	
}
