package operators;

import java.io.File;
import java.io.IOException;
import java.util.List;

import btree.DataEntry;
import net.sf.jsqlparser.schema.Table;
import util.Tuple;

public class IndexScanOperator extends Operator{
	Integer lowKey;
	Integer highKey;
	File indexFile; // the index file for deserializer to lacate
	Table tab =null;
	Boolean isClustered = false; 
	int position; // the index position
	DataEntry firstDataEntry;
	@Override
	public Tuple getNextTuple()  {
		// TODO Auto-generated method stub
		if(this.firstDataEntry == null){
			 return null;
		}
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
	public IndexScanOperator(Table tab,Integer lowKey, 
			Integer highKey,Boolean isClustered,int position,File indexFile){
		this.lowKey = lowKey;
		this.highKey = highKey;
		this.tab = tab;
		this.isClustered = isClustered;
		this.position = position;
		this.indexFile = indexFile;
		//call deserilizer to fetch the first data entry from the leafnode 
		
		
		
	}
}
