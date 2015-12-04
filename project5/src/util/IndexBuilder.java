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
 * 
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
	
	
}
