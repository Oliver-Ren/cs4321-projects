package operators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import net.sf.jsqlparser.statement.select.OrderByElement;
import nio.BinaryTupleReader;
import nio.BinaryTupleWriter;
import nio.TupleReader;
import nio.TupleWriter;
import util.Constants;
import util.DBCat;
import util.Tuple;

public class ExternSortOperator extends SortOperator {

	private final String id = UUID.randomUUID().
			toString().substring(0, 8);
	private final String localDir = DBCat.tempDir + 
			id + File.separator;
	private List<TupleReader> buffers;
	private int tpsPerPg = 0;
		
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void reset() {
		//TODO
	}
	
	private String fileName(int pass, int run) {
		return localDir + String.valueOf(pass) + 
				"_" + String.valueOf(run);
	}
	
	private int merge(int curPass, int lastRuns) {
		int curRuns = 0;
		int i = 0;
		while (i < lastRuns) {
			buffers.clear();
			int maxJ = Math.min(i + DBCat.sortBufPgs - 1, lastRuns);
			for (int j = i; j < maxJ; j++) {
				try {
					TupleReader tr = new BinaryTupleReader(
							fileName(curPass - 1, j));
					buffers.add(tr);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					break;
				}
			}
			
			List<Tuple> tmpBuf = new ArrayList<Tuple>(DBCat.sortBufPgs - 1);
			
			try {
				TupleWriter tw = new BinaryTupleWriter(fileName(curPass, curRuns++));
				PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>(DBCat.sortBufPgs - 1, 
						tpCmp);
				
				int nonEmpty = DBCat.sortBufPgs - 1;
				for (TupleReader tr : buffers) {
					Tuple tp = tr.read();
					if (tp == null) nonEmpty--;
					tmpBuf.add(tp);
				}
				
				while (!pq.isEmpty()) {
					tw.write(pq.poll());
					for (TupleReader tr : buffers) {
						Tuple tp = tr.read();
						if (tp != null) {
							
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public ExternSortOperator(Operator child, List<OrderByElement> orders) {
		super(child, orders);
		new File(localDir).mkdirs();
		buffers = new ArrayList<TupleReader>(DBCat.sortBufPgs - 1);
		tpsPerPg = Constants.PAGESZ / (
				Constants.ATTRSZ * schema().size());
		int tpsPerRun = tpsPerPg * DBCat.sortBufPgs;
		
		List<Tuple> tps = new ArrayList<Tuple>(tpsPerRun);
		
		int curPass = 0;
		
		int i = 0;
		while (true) {
			try {
				tps.clear();
				int cnt = tpsPerRun;
				Tuple tp = null;
				while ((tp = child.getNextTuple()) != null && 
						cnt-- > 0)
						tps.add(tp);
				
				if (tps.isEmpty()) break;
				
				Collections.sort(tps, tpCmp);
				
				TupleWriter tw = new BinaryTupleWriter(
						fileName(curPass, i++));
				for (Tuple tuple : tps)
					tw.write(tuple);
				tw.close();
								
				if (tps.size() < tpsPerRun) break;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		if (i == 0) return;
		
		if (i == 1) {
			File oldFile = new File(fileName(0, 0));
			File newFile = new File(localDir + "final");
			oldFile.renameTo(newFile);
			return;
		}
		
		curPass++;
		
	}

}
