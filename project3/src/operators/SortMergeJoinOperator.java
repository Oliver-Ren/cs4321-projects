package operators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import util.Helpers;
import util.SelState;
import util.Tuple;
import visitors.JoinExpVisitor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import nio.BinaryTupleReader;
import nio.BinaryTupleWriter;
import nio.FormatConverter;
import nio.TupleReader;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

public class SortMergeJoinOperator extends JoinOperator {
	//int cnt = 0;// debug perpiose
	int partitionIndex = 0;  // the index of the first tuple in the current partition
	int curRightIndex = 0;  // the index of the current tuple of left table
	//TupleReader leftReader; // the tuple reader for left table 
	//TupleReader rightReader; //the  tuple reader for the right table
	Expression exp;  // my expression
	//List<EqualsTo> equalExps; 
	JoinExpVisitor jv;
	
	Tuple leftTp = null;
	Tuple rightTp = null;
	
	TupleComp cp = null;
	
	// = =============some problems
	List<Integer> leftOrders = null; // the order of attributes in left table 
	List<Integer> rightOrders = null;// the order of attributes in right table 
	/**
	 * scan a list of expressions and keep the equals expression
	 * 
	 * @return
	 * @throws IOException 
	 */
//	public List<EqualsTo> getEqualExps(List<Expression> exp){
//		if(exp == null) return null;
//		for(Expression e : exp ){
//			if(e instanceof EqualsTo){
//				EqualsTo eq = (EqualsTo) e;
//				equalExps.add(eq);
//			}
//		}
//		return equalExps;
//		
//	}
	public void dumpLeft() {
		BinaryTupleWriter tw;
		try{
			tw  = new BinaryTupleWriter("benchmarking/samples/temp/dumpleft");
			Tuple t;
			while((t = left.getNextTuple())!=null){
				
				tw.write(t);
			}
			
			tw.close();
			FormatConverter.binToNormal("benchmarking/samples/temp/dumpleft", 
					"benchmarking/samples/temp/dumpRight_humanreadable");
			SortOperator sp = (SortOperator)left;
			sp.reset(0);
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		
		
	}
		
	@Override
	public Tuple getNextTuple()  {		
		while(leftTp !=null && rightTp !=null){ //当两个table都没EOF
			//((SortOperator) right).reset(partitionIndex);
			//curRightIndex = partitionIndex;
			if(cp.compare(leftTp, rightTp)!=0){
				while (leftTp != null && 
						cp.compare(leftTp, rightTp) < 0) { // left[i] < right[j]
					System.out.println(leftTp.toString() + ' ' + rightTp.toString() + " <");
					leftTp = left.getNextTuple();
				}
				
				if (leftTp == null) return null;
				
				while(rightTp!=null &&
						cp.compare(leftTp, rightTp)>0){// left[i]>right[j]
					System.out.println(leftTp.toString() + ' ' + rightTp.toString() + " >");
					rightTp = right.getNextTuple();
					curRightIndex++;
				}
				//update my partition
				partitionIndex = curRightIndex;
			}
			
			// check if reaches EOF
			if(rightTp ==null) return null;
			
			//reset my right tuple index
			
			if (cp.compare(leftTp, rightTp) != 0) continue;
			
			//if (partitionIndex == -1)
				//partitionIndex = curRightIndex;
			
			// keep track my current right tuple index
			 System.out.println("new partition " + partitionIndex);
			
			while(rightTp != null && cp.compare(leftTp,rightTp) == 0){
				System.out.println(leftTp.toString() + ' ' + rightTp.toString() + " =");
				// compare other non equality condition
				Tuple rst = null;
				if(exp == null || 
						Helpers.getJoinRes(leftTp, rightTp, exp, jv)){
					rst = joinTp(leftTp,rightTp);
				}
				rightTp = right.getNextTuple();
				curRightIndex++;
				if(rightTp == null) {
					((SortOperator) right).reset(partitionIndex);
					leftTp = left.getNextTuple();
					curRightIndex = partitionIndex;
					System.out.println("the partition is reset");
				} else if(cp.compare(leftTp, rightTp)!=0){
					((SortOperator) right).reset(partitionIndex);
					leftTp = left.getNextTuple();
					curRightIndex = partitionIndex;
					System.out.println("the partition is reset");
				}
				if(rst!= null) return rst;
			}
		}
		
		return null;
	}
	public SortMergeJoinOperator(Operator left, 
			Operator right, Expression exp,List<Integer> leftOrders,
			List<Integer> rightOrders) {
		super(left, right, exp);
		
		this.leftOrders = leftOrders;
		this.rightOrders = rightOrders;
		cp  = new TupleComp(leftOrders,rightOrders);
		
		System.out.println(left.schema());
		System.out.println(right.schema());
		System.out.println(leftOrders);
		System.out.println(rightOrders);
		
		System.out.println("------------");
		
		//EqualsTo et = (EqualsTo) exp;
		//left.schema().contains(et.getLeftExpression());
		// decompose all the end expression to a list
		//equalExps = getEqualExps(list); // assign the equalExpression to my list
		this.jv = new JoinExpVisitor(left.schema(),right.schema());
		
		leftTp = left.getNextTuple();
		rightTp = right.getNextTuple();
		
		System.out.println("I'm in sort merge join");
		dumpLeft();
		
		
	}
	
	// comparator class to compare one tuple from the left table and another from the 
	// right table
	public class TupleComp implements Comparator<Tuple>{
		List<Integer> leftOrders = null; // the order of attributes in left table 
		List<Integer> rightOrders = null;// the order of attributes in right table 
		@Override
		public int compare(Tuple left, Tuple right) {
			
			
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

	@Override
	protected void next() {
		throw new UnsupportedOperationException();
	}
	
}
