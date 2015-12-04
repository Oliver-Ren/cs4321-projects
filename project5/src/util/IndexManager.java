package util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * The class <tt>IndexManager</tt> represents the class for managing the 
 * index information. The manager provides interfaces for getting the 
 * index informations from table name, attribute name. and add new index
 * information.
 * <p>
 * Since the index is for the relation, alias will be converted to original
 * table name.
 * 
 * @author Chengxiang Ren, (cr486)
 *
 */
public class IndexManager {
	Map<String, List<IndexInfo>> tableInfo;	// The index info for the tables.
	
	/**
	 * Initializes the IndexManager.
	 */
	public IndexManager() {
		tableInfo = new HashMap<String, List<IndexInfo>>();
	}
	
	/**
	 * Return true if the relation has index on the attribute.
	 * @param relation
	 * @param attr
	 * @return true if has index.
	 */
	public boolean hasIndex(String relation, String attr) {
		List<IndexInfo> indexInfos = getIndexInfoList(relation);
		if (indexInfos == null) return false;
		
		// find the index info with the attribute name
		for (IndexInfo info : indexInfos) {
			if (info.attr.equals(attr)) return true;
		}
		return false;
	}
	
	/**
	 * Return true if the relation has index.
	 * @param relation
	 * @return
	 */
	public boolean hasIndex(String relation) {
		return (getIndexInfoList(relation) != null);
	}
	
	/**
	 * Get the index information from relation name and the attribute name.
	 * @param relation
	 * @param attr
	 * @return the index information.
	 * 		   <tt>null</tt> if does not found.
	 */
	public IndexInfo getIndexInfo(String relation, String attr) {
		List<IndexInfo> indexInfos = getIndexInfoList(relation);
		if (indexInfos == null) return null;
		
		// find the index info with the attribute name
		for (IndexInfo info : indexInfos) {
			if (info.attr.equals(attr)) return info;
		}
		
		// Does not found
		return null;
	}
	
	
	/**
	 * Get the list of the index information of this relation.
	 * @param relation
	 * @return list of index information which belong to this table
	 * 		   <tt>null</tt> if does not found.
	 */
	public List<IndexInfo> getIndexInfoList(String relation) {
		// transfer to the origin table name
		String originTable = DBCat.origName(relation);
		if (!tableInfo.containsKey(originTable)) {
			return null;
		}
		return tableInfo.get(originTable);
	}
	
	/**
	 * Add the index information.
	 *
	 * @param relation the <em>original</em> name for the table.
	 * @param attr
	 */
	public void addIndexInfo(String relation, String attr, boolean clust, int ord) {
		// create the table item if does not exist.
		if (!tableInfo.containsKey(relation)) {
			tableInfo.put(relation, new LinkedList<IndexInfo>());
		}
		
		List<IndexInfo> indicesOfTheTable = tableInfo.get(relation);
		
		IndexInfo info = new IndexInfo(relation, attr, clust, ord);
		
		// we should add clustered at the head of the list.
		if (clust) {
			indicesOfTheTable.add(0, info);
		} else {
			indicesOfTheTable.add(info);
		}
	}
	
	
	/**
	 * Builds the index file using the index informations.
	 * The <tt>IndexBuilder</tt> class represents a module for building the
	 * index file for specified attributes.
	 * <p>
	 * The builder currently support building index using B+ tree.
	 * @param withReadable output additional human readable version if <em>true<em>
	 */
	public void BuildIndex(boolean withReadable) {
		PhysicalPlanBuilder ppb = new PhysicalPlanBuilder();
		
		// create indexes directory if does not exist,
		// else empty the directory.
		File idxsDirFile = new File(DBCat.idxsDir);
		if (!idxsDirFile.exists()) {
			idxsDirFile.mkdir();
		} else {
			Diff.cleanFolder(DBCat.idxsDir);
		}
		
		for (String relt : tableInfo.keySet()) {
			String tabPath = DBCat.dataDir + relt;
			List<IndexInfo> infoList = tableInfo.get(relt);
			for (IndexInfo ii : infoList) {
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
						System.out.println("Sorted " + tabPath + " On " + ii.attr);
						btw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				try {
					System.out.println("Curretnly is building index for table "+ idxPath);
					File indexFile = new File(idxPath);
					BPlusTree blt = new BPlusTree(new File(tabPath), 
							attrIdx, ii.order, indexFile);
					if (withReadable) {
						File humanreadable = new File(idxPath + "_humanreadable");
						PrintStream printer = new PrintStream(humanreadable);
						TreeDeserializer td = new TreeDeserializer(indexFile);
						td.dump(printer);
						// set the number of leaf nodes.
						ii.setNumOfLeafNodes(td.getNumOfLeaves());
						printer.close();
					}
					System.out.println("Building finished");
					System.out.println("Print leaf layer");
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
