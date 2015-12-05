package optimizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	
	 /**
     * Helper method to enumerate all of the subsets of a given list.
     * 
     * @return a set of all subsets of the specified size
     */
    private Set<Set<Operator>> enumerateSubsets(List<Operator> childrenList, int size) {
    	Set<Set<Operator>> oldSet = new HashSet<Set<Operator>>();
    	oldSet.add(new HashSet<Operator>());
    	
    	for (int i = 0; i < size; i++) {
    		Set<Set<Operator>> newSet = new HashSet<Set<Operator>>();
    		for (Set<Operator> subset : oldSet) {
    			for (Operator op : childrenList) {
    				Set<Operator> newSubset = (Set<Operator>) (((HashSet<Operator>) subset).clone());
    				if (newSubset.add(op)) newSet.add(newSubset);
    			}
    		}
    		oldSet = newSet;
    	}
    	
    	return oldSet;
    }
}
