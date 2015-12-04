package optimizer;

import java.util.List;

import operators.Operator;

/** 
 * The <tt>JoinOptimizer</tt> class represents the class for
 * optimizing the join order and the join implementation.
 * 
 * @author Chengxiang Ren (cr486)
 *
 */
public class JoinOptimizer {
	
	/**
	 * Rearrange the children list for the optimal join order.
	 * 
	 * @param childrenList list of children operators to be joined.
	 */
	public static void getOptJoinOrder(List<Operator> childrenList) {
		
		// iterate through the list
		for (int i = 0; i < childrenList.size(); i++) {
			
		}
	}
}
