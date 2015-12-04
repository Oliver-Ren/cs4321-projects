package operators.logic;

import java.util.ArrayList;
import java.util.List;

import operators.Operator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LogicMultiJoinOp {

	public List<String> froms = new ArrayList<>();
	
	public LogicMultiJoinOp(List<String> froms) {
		this.froms = froms;
	}
	
	public List<LogicOperator> getChildrenList() {
		//TODO
		throw new NotImplementedException();
	}
	
}
