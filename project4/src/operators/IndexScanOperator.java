package operators;

import java.io.File;
import java.io.IOException;
import java.util.List;

import btree.DataEntry;
import btree.Rid;
import btree.TreeDeserializer;
import util.Table;
import util.Tuple;

public class IndexScanOperator extends ScanOperator{
	Integer lowKey;
	Integer highKey;
	int attrIdx;	// The index of the attribute column.
	File indexFile; // the index file for deserializer to lacate
	Boolean isClustered = false; 
	Rid CurRid;
	TreeDeserializer ts;  // tree serializer used for get the dataentry
	@Override
	public Tuple getNextTuple()  {
		// TODO Auto-generated method stub
		if(this.CurRid == null){
			 return null;
		}
		if(isClustered) {
			Tuple curr = super.tab.nextTuple();
			if (curr != null && curr.cols[attrIdx] < highKey) return curr;
			return null;
		} else {
			Rid temp = CurRid;
			try {
				CurRid = ts.getNextRid();
			} catch(IOException e){
				e.printStackTrace();
			}
			 
			return super.tab.nextTuple(temp);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		tab.reset();
	}

	@Override
	public List<String> schema() {
		// TODO Auto-generated method stub
		return null;
	}
	public IndexScanOperator(Table tab, int attrIdx, Integer lowKey, 
			Integer highKey,Boolean isClustered,File indexFile){
		super(tab);
		this.attrIdx = attrIdx;
		this.lowKey = lowKey;
		this.highKey = highKey;
		this.isClustered = isClustered;
		this.indexFile = indexFile;
		//call deserilizer to fetch the first rid from the leafnode 
		try{
			ts = new TreeDeserializer(indexFile,lowKey,highKey);
			CurRid = ts.getNextRid();
		} catch(IOException e ){
			e.printStackTrace();
		}
		
		
		
	}
}
