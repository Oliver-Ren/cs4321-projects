package operators;

import java.util.List;

import net.sf.jsqlparser.schema.Table;
import util.Tuple;

public class IndexScanOperator extends Operator{
	int lowKey;
	int highKey;
	Table tab =null;
	Boolean isClustered = false; 
	int position; // the index position
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> schema() {
		// TODO Auto-generated method stub
		return null;
	}
	public IndexScanOperator(Table tab,int lowKey, 
			int highKey,Boolean isClustered,int position){
		this.lowKey = lowKey;
		this.highKey = highKey;
		this.tab = tab;
		this.isClustered = isClustered;
		this.position = position;
		
		
		
	}
}
