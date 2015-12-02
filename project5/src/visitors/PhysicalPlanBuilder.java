package visitors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import operators.BlockJoinOperator;
import operators.DuplicateEliminationOperator;
import operators.ExternSortOperator;
import operators.IndexScanOperator;
import operators.Operator;
import operators.ProjectOperator;
import operators.ScanOperator;
import operators.SelectOperator;
import operators.SortMergeJoinOperator;
import operators.InMemSortOperator;
import operators.TupleJoinOperator;
import operators.logic.LogicBinaryOp;
import operators.logic.LogicDupElimOp;
import operators.logic.LogicJoinOp;
import operators.logic.LogicProjectOp;
import operators.logic.LogicScanOp;
import operators.logic.LogicSelectOp;
import operators.logic.LogicSortOp;
import operators.logic.LogicUnaryOp;
import util.DBCat;
import util.Helpers;
import util.DBCat.JoinMethod;
import util.DBCat.SortMethod;
import util.IndexInfo;

public class PhysicalPlanBuilder {

	private Operator phyOp = null;
	
	public Operator getPhyOp() {
		return phyOp;
	}
	
	public void getChild(LogicUnaryOp lop) {
		phyOp = null;
		lop.child.accept(this);
	}
	
	public Operator[] getLR(LogicBinaryOp lop) {
		Operator[] ret = new Operator[2];
		
		phyOp = null;
		lop.left.accept(this);
		ret[0] = phyOp;
		
		phyOp = null;
		lop.right.accept(this);
		ret[1] = phyOp;
		
		return ret;
	}
	
	public void visit(LogicDupElimOp lop) {
		getChild(lop);
		phyOp = new DuplicateEliminationOperator(phyOp);
	}
	
	public void visit(LogicJoinOp lop) {
		Operator[] children = getLR(lop);
		
		switch(DBCat.joinMethod) {
		case TNLJ:
			phyOp = new TupleJoinOperator(children[0], 
					children[1], lop.exp);
			break;
		case BNLJ:
			phyOp = new BlockJoinOperator(children[0], 
					children[1], lop.exp);
			break;
		case SMJ:
			List<Integer> outIdxs = new ArrayList<Integer>();
			List<Integer> inIdxs = new ArrayList<Integer>();
			Expression newExp = Helpers.procJoinConds(
					lop.exp, children[0].schema(), 
					children[1].schema(), outIdxs, inIdxs);
			
			if (outIdxs.size() != inIdxs.size())
				throw new IllegalArgumentException();
			
			if (!outIdxs.isEmpty()) {
				lop.exp = newExp;
				if (DBCat.sortMethod == SortMethod.INMEM) {
					children[0] = new InMemSortOperator(
							children[0], outIdxs);
					children[1] = new InMemSortOperator(
							children[1], inIdxs);
				}
				else {
					children[0] = new ExternSortOperator(
							children[0], outIdxs);
					children[1] = new ExternSortOperator(
							children[1], inIdxs);
				}
				phyOp = new SortMergeJoinOperator(children[0], children[1], 
						lop.exp, outIdxs, inIdxs);
			}
			else {
				System.out.println("No EqualsTo found in join");
				phyOp = new BlockJoinOperator(children[0], 
						children[1], lop.exp);
			}
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	public void visit(LogicProjectOp lop) {
		getChild(lop);
		phyOp = new ProjectOperator(phyOp, lop.sis);
	}
	
	public void visit(LogicScanOp lop) {
		phyOp = new ScanOperator(lop.tab);
	}
	
	public void visit(LogicSelectOp lop) {
		// precondition: the child of a select operator must be a scan operator.
		LogicScanOp child = (LogicScanOp) lop.child;
		ScanOperator scanOp = null;
		if (DBCat.idxSelect) {
			String currTableName = getTableName(lop);
			IndexInfo idxinfo = DBCat.getIndexInfo(currTableName);
			boolean hasIdxAttr = Helpers.hasIdxAttr(currTableName, lop.exp);
			System.out.println("Table name: " + currTableName);
			System.out.println("has index attriburte: " + hasIdxAttr);
			if (hasIdxAttr) {
				System.out.println("============= Building Index Scan operator==============");
				
				Integer[] range 
				= Helpers.bPlusKeys(idxinfo.attr, lop.exp);
				System.out.println("The range is " + range[0] + ", " + range[1]);
				System.out.println("============= End Building Index Scan operator==============");
				String idxPath = DBCat.idxsDir + idxinfo.relt + '.' + idxinfo.attr;
				File idxFile = new File(idxPath);
				int attrIdx = DBCat.schemas.get(idxinfo.relt).indexOf(idxinfo.attr);
				scanOp = new IndexScanOperator(child.tab, attrIdx, range[0], range[1],
						idxinfo.clust, idxFile);
			}
		} 
		
		// if not assigned with index scan.
		if (scanOp == null) scanOp = new ScanOperator(child.tab);
		
		phyOp = new SelectOperator(scanOp, lop.exp);
	}
	
	// Helper method for getting table from the child of a select operator.
	private String getTableName(LogicUnaryOp uop) {
		if (uop.child instanceof LogicScanOp) {
			return ((LogicScanOp) uop.child).tab.name;
		}
		return getTableName((LogicUnaryOp) uop.child);
	}
	
	public void visit(LogicSortOp lop) {
		getChild(lop);
		if (DBCat.sortMethod == SortMethod.INMEM)
			phyOp = new InMemSortOperator(phyOp, lop.orders);
		else
			phyOp = new ExternSortOperator(phyOp, lop.orders);
	}
	
}
