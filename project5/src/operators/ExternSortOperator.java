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
	private TupleReader tr = null;
	private List<TupleReader> buffers = 
			new ArrayList<TupleReader>(DBCat.sortBufPgs - 1);
	private int tpsPerPg = 0;
		
	@Override
	public Tuple getNextTuple() {
		if (tr == null) return null;
		try {
			return tr.read();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void reset() {
		if (tr == null) return;
		try {
			tr.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reset(long idx) {
		if (tr == null) return;
		try {
			tr.reset(idx);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
						
			try {
				TupleWriter tw = new BinaryTupleWriter(fileName(curPass, curRuns++));
				PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>(DBCat.sortBufPgs - 1, 
						tpCmp);
				
				for (TupleReader tr : buffers) {
					Tuple tp = tr.read();
					if (tp != null) {
						tp.tr = tr;
						pq.add(tp);
					}
				}
				
				while (!pq.isEmpty()) {
					Tuple tp = pq.poll();
					tw.write(tp);
					TupleReader tr = tp.tr;
					tp = tr.read();
					if (tp != null) {
						tp.tr = tr;
						pq.add(tp);
					}
				}
				
				tw.close();
				
				for (TupleReader tr : buffers)
					tr.close();
				
				for (int j = i; j < maxJ; j++) {
					File file = new File(fileName(curPass - 1, j));
					file.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			i += DBCat.sortBufPgs - 1;
		}
		
		return curRuns;
	}

	public ExternSortOperator(Operator child, List<?> orders) {
		super(child, orders);
		
		new File(localDir).mkdirs();
		tpsPerPg = Constants.PAGESZ / (
				Constants.ATTRSZ * schema().size());
		//the total number of tuples in a run(temp output file)
		int tpsPerRun = tpsPerPg * DBCat.sortBufPgs;
		//create a array to keep the sorted tuple
		List<Tuple> tps = new ArrayList<Tuple>(tpsPerRun);
				
		int i = 0;
		while (true) {
			try {
				tps.clear(); 
				int cnt = tpsPerRun;
				Tuple tp = null;
				while (cnt-- > 0 && 
						(tp = child.getNextTuple()) != null)
						tps.add(tp);
				
				if (tps.isEmpty()) break;
				
				Collections.sort(tps, tpCmp);
				
				TupleWriter tw = new BinaryTupleWriter(
						fileName(0, i++));
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
		
		int curPass = 1;
		int lastRuns = i;
		while (lastRuns > 1)
			lastRuns = merge(curPass++, lastRuns);
		
		File oldFile = new File(fileName(curPass - 1, 0));
		File newFile = new File(localDir + "final");
		oldFile.renameTo(newFile);
		
		try {
			tr = new BinaryTupleReader(localDir + "final");
		} catch (FileNotFoundException e) {
			tr = null;
		}
	}

    @Override
    public String print() {
        if (orders2 != null) {
            return String.format("ExternalSort%s", orders2.toString());
        } else if (orders != null) {
            return String.format("ExternalSort%s", orders.toString());
        } else {
            return String.format("ExternalSort[]");
        }
    }

}
