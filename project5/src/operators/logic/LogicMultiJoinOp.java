package operators.logic;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import operators.Operator;
import util.Helpers;
import util.unionfind.UFElement;
import util.unionfind.UnionFind;
import visitors.PhysicalPlanBuilder;

public class LogicMultiJoinOp extends LogicOperator {

	public List<String> froms;
	public HashMap<String, Expression> resConds;
	public Expression exp;
	public UnionFind uf;
	
	public List<LogicOperator> children;
	
	public LogicMultiJoinOp(List<String> froms, 
			List<LogicOperator> tables, 
			HashMap<String, Expression> res, 
			UnionFind uf) {
		this.froms = froms;
		children = tables;
		resConds = res;
		this.uf = uf;
		
		List<Expression> lst = new ArrayList<>();
		for (String s : res.keySet())
			lst.addAll(Helpers.decompAnds(res.get(s)));
		if (!lst.isEmpty())
			exp = Helpers.genAnds(lst);
	}
	
	public List<LogicOperator> getChildrenList() {
		return children;
	}

	@Override
	public String print() {
		StringBuilder sb = new StringBuilder();
		sb.append("Join");
		if (exp != null)
			sb.append(String.format("[%s]", exp.toString()));
		else
			sb.append("[]");
		for (String s : uf.componentsInfo())
			sb.append('\n' + s);
		
		return sb.toString();
	}

	@Override
	public void printTree(PrintStream ps, int lv) {
		printIndent(ps, lv);
		ps.println(print());
		for (LogicOperator lop : children)
			lop.printTree(ps, lv + 1);
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		// TODO Auto-generated method stub
		
	}
	
}
