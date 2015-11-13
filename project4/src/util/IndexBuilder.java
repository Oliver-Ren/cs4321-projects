package util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.OrderByElement;
import nio.BinaryTupleWriter;
import operators.Operator;
import operators.logic.LogicOperator;
import operators.logic.LogicScanOp;
import operators.logic.LogicSortOp;
import tests.Diff;
import visitors.PhysicalPlanBuilder;
import btree.BPlusTree;
import btree.TreeDeserializer;

/**
 * The <tt>IndexBuilder</tt> class represents a module for building the
 * index file for specified attributes.
 * <p>
 * The builder currently support building index using B+ tree.
 * 
 * @author Chengxiang Ren
 *
 */
public class IndexBuilder {
//	private String dbDir;
//	private String indexDir;
//	private Collection<IndexInfo> indexInfo;
//	
//	/**
//	 * Creates the IndexBuilder.
//	 * @param dbDir		directory of database
//	 * @param indexDir	directory of index
//	 * @param indexInfo Informations of index.
//	 */
//	public IndexBuilder(String dbDir, String indexDir
//			, Collection<IndexInfo>indexInfo) {
//		this.dbDir = dbDir;
//		this.indexDir = indexDir;
//		this.indexInfo = indexInfo;
//	}
	
	public static void BuildIndex(boolean withReadable) {
		PhysicalPlanBuilder ppb = new PhysicalPlanBuilder();
		
		// create indexes directory if does not exist,
		// else empty the directory.
		File idxsDirFile = new File(DBCat.idxsDir);
		if (!idxsDirFile.exists()) {
			idxsDirFile.mkdir();
		} else {
			Diff.cleanFolder(DBCat.idxsDir);
		}
		
		for (String relt : DBCat.idxInfo.keySet()) {
			String tabPath = DBCat.dataDir + relt;
			IndexInfo ii = DBCat.idxInfo.get(relt);
			int attrIdx = DBCat.schemas.get(relt).indexOf(ii.attr);
			String idxPath = DBCat.idxsDir + relt + '.' + ii.attr;
			
			
			
			if (ii.clust) {
				Table tbl = new Table(null, relt);
				Expression exp = new Column(tbl, ii.attr);
				OrderByElement obe = new OrderByElement();
				obe.setExpression(exp);
				LogicOperator lop = new LogicScanOp(DBCat.getTable(relt));
				lop = new LogicSortOp(lop, Arrays.asList(obe));
				lop.accept(ppb);
				Operator op = ppb.getPhyOp();
				
				try {
					BinaryTupleWriter btw = 
							new BinaryTupleWriter(tabPath);
					op.dump(btw);
					btw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				File indexFile = new File(idxPath);
				BPlusTree blt = new BPlusTree(new File(tabPath), 
						attrIdx, ii.order, indexFile);
				if (withReadable) {
					File humanreadable = new File(idxPath + "_humanreadable");
					PrintStream printer = new PrintStream(humanreadable);
					TreeDeserializer td = new TreeDeserializer(indexFile);
					td.dump(printer);
					printer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
