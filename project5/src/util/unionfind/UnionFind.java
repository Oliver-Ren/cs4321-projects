package util.unionfind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <tt>UnionFind</tt> class represetns a <em>union-find data structure</em>.
 * It supports the <em>union</em> and <em>find</em> and the <em>setBound</em>
 * opeartions.
 * 
 * @author Chengxiang Ren, cr486.
 */

public class UnionFind {
    //private int[] parent;   // parent[i] = parent of i.
    //private int[] rank;
    //private int count;

    private Map<String, UFElement> elements;
    private Map<UFElement, UFElement> parent;
    private Map<UFElement, Integer> rank;
    private int count;

    /**
     * Initializes an empty union find data structure.
     *
     */
    public UnionFind() {
        count = 0;
        elements = new HashMap<String, UFElement>();
        parent = new HashMap<UFElement, UFElement>();
        rank = new HashMap<UFElement, Integer>();
    }

    /**
     * Returns union find element for the attribute number.
     * if te UF element does not exist, create and return it.
     * @return the union-find element for the set that the attribute belongs.
     */
    public UFElement find(String attr) {
        if (!elements.containsKey(attr)) {
            createElement(attr);
        }
        UFElement e = elements.get(attr);
        while (e != parent.get(e)) {
            parent.put(e, parent.get(parent.get(e)));  // path compression by halving.
            e = parent.get(e);
        }
        return e;
    }

    // helper method for creating new element.
    private void createElement(String attr) {
    	UFElement curr = new UFElement();
    	elements.put(attr, curr);
    	parent.put(curr, curr);
    	count++;
    	rank.put(curr, 0);
    }
        
    /**
     * Merges the component containing element <tt>p</tt> and <tt>q</tt>
     *
     * @param p the element to be merged.
     * @param q the element to be merged.
     */
    public void union(UFElement p, UFElement q) {
    	// precondition: p and q are not null
    	if (p == null || q == null) throw new NullPointerException();
        if (p == q) return;

        merge(p, q);
        // make root of smaller rank point to root of larger rank.
        if (rank.get(p) < rank.get(q)) parent.put(p, q);
        else if (rank.get(p) < rank.get(q)) parent.put(q, p);
        else {
            parent.put(q, p);
            int pRank = rank.get(p);
            rank.put(p, pRank + 1);
        } 
        count--;
    }
    
    /**
     * Merges the two attributes.
     *
     * @param attr1 The first attribute
     * @param attr2 The second attribute
     */
    public void union(String attr1, String attr2) {
        UFElement p = find(attr1);
        UFElement q = find(attr2);
        union(p, q);
    }
    
    private void merge(UFElement p, UFElement q) {
    	// precondition: p and q are not equivalent.
    	p.merge(q);
    	q.merge(p);
    }

    /**
     * Returns the count of connected components.
     */
    public int count() {
        return count;
    }

    /**
     * Returns true if the two attributes are connnected.
     */
    public boolean connected(String attr1, String attr2) {
        return find(attr1) == find(attr2);
    }

    /**
     * Returns the set of all attributes in this union-find.
     * @return set of all attributes in the union-find.
     */
    public Set<String> attributeSet() {
        return elements.keySet();
    }
    
    /**
     * Returns the list of uf elemets which represents the connected componenets.
     * 
     * @return UF list.
     */
    public Set<UFElement> parentSet() {
    	Set<UFElement> result = new HashSet<UFElement>();
    	for (String attr : attributeSet()) {
    		UFElement curr = find(attr);
    		if (!result.contains(curr)) {
    			result.add(curr);
    		}
    	}
    	return result;
    }
    
    /**
     * returns the list of information of each connected components in the 
     * format of string.
     * @return list of string
     */
    public List<String> componentsInfo() {
    	List<String> result = new ArrayList<String>();
    	Map<UFElement, List<String>> parentMap = new HashMap<UFElement, List<String>>();
    	for (String attr : attributeSet()) {
    		UFElement curr = find(attr);
    		if (!parentMap.containsKey(curr)) {
    			parentMap.put(curr, new ArrayList<String>());
    		}
    		
    		parentMap.get(curr).add(attr);
    	}
    	
    	for (UFElement e : parentMap.keySet()) {
    		StringBuilder sb = new StringBuilder();
    		sb.append("[[");
    		for (String attr : parentMap.get(e)) {
    			sb.append(attr + ", ");
    		}
    		sb.setLength(sb.length() - 2);
    		sb.append("], equals ");
    		sb.append(e.getEquality());
    		sb.append(", min ");
    		sb.append(e.getLower());
    		sb.append(", max ");
    		sb.append(e.getUpper());
    		sb.append("]");
    		result.add(sb.toString());
    	}
    	
    	return result;
    }
    

  

}
