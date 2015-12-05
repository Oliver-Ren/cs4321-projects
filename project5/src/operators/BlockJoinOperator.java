package operators;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import util.Tuple;
import util.Constants;
import util.DBCat;

public class BlockJoinOperator extends JoinOperator {

	List<Tuple> outerBlk = null;
	private int tpsPerPg = -1;
	private int curOuterIdx = 0;
	
	private void readOuterBlk() {
		outerBlk.clear();
		int cnt = tpsPerPg;
		Tuple tp = null;
		while (cnt-- > 0 && ((tp = left.getNextTuple()) != null))
			outerBlk.add(tp);
		setOuterIdx(0);
	}
	
	private void setOuterIdx(int idx) {
		curOuterIdx = idx;
		curLeft = (idx < outerBlk.size()) ? 
				outerBlk.get(idx) : null;
	}
	
	@Override
	protected void next() {
		if (curRight != null) {
			setOuterIdx(++curOuterIdx);
			if (curLeft != null)
				return;
			
			curRight = right.getNextTuple();
			if (curRight != null) {
				setOuterIdx(0);
				return;
			}
		}
		
		readOuterBlk();
		right.reset();
		curRight = right.getNextTuple();
	}
	
	public BlockJoinOperator(Operator left, Operator right, Expression exp) {
		super(left, right, exp);
		
		int tupleSz = Constants.ATTRSZ * left.schema().size();
		tpsPerPg = DBCat.joinBufPgs * (Constants.PAGESZ / 
				tupleSz);
		outerBlk = new ArrayList<Tuple>(tpsPerPg);
		
		next();
	}

    @Override
    public String print() {
        String expression = (exp != null) ? exp.toString() : "";
        return String.format("BNLJ[" + expression + "]");
    }

}
