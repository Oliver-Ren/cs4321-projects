package util.unionfind;

/**
 * The <tt>UFElement</tt> class represents a basic element used in the
 * <em>union-find</em> data structure, and it is also the reprentation 
 * of the connected componenet in the logical join operater for the
 * unioned attributes.
 * <p>
 * It contains three prorerties: 1. the lower bound of the unioned component,
 * 2. the uppper bound. 3. the equality constraints.
 *
 * @author Chengxiang Ren, cr486
 */

public class UFElement {
    private Integer lowerBound;
    private Integer upperBound;
    private Integer equality;

    /**
     * Initializes an empty union-find element.
     */
    public UFElement() {
        lowerBound = null;
        upperBound = null;
        equality = null;
    }

    /**
     * Getter for lower bound.
     * @return lowerBound, or <tt>Null</tt> if no such constraint.
     */
    public Integer getLower() {
        return lowerBound;
    }

    /**
     * Getter for the upper bound.
     * @return upperBound, or <tt>Null</tt> if no such constraint.
     */
    public Integer getUpper() {
        return upperBound;
    }

    /**
     * Getter for the equality constraint.
     * @return equality, or <tt>Null</tt> if no such constraint.
     */
    public Integer getEquality() {
        return equality;
    }

    /**
     * Setter for the lower bound.
     * if the lower bound already exists, update the lower bound
     * only when the new valule is larger than the old value.
     * @throws IllegalArugmentException if the lower bound is greater
     *                                  than the upper bound.
     */
    public void setLowerBound(Integer l) {
    	if (l == null) return;
        if (upperBound != null && l > upperBound) {
            throw new IllegalArgumentException("lower > upper");
        }
        if (lowerBound == null || l > lowerBound) {
            lowerBound = l;
        }
    }

    /**
     * Setter for the upper bound.
     * If the upper bound already exists, update the upper bound
     * only when the new value is smaller than the old value.
     * @throws IllegalArugmentException if the upper bound is less
     *                                  than the lower bound.
     */
    public void setUpperBound(Integer u) {
    	if (u == null) return;
        if (lowerBound != null && u < lowerBound) {
            throw new IllegalArgumentException("upper < lower");
        }
        if (upperBound == null || u < upperBound) {
            upperBound = u;
        }
    }

    /**
     * Setter for the equality constraint.
     * This will also set the lower bound and the upper bound
     * to be equal to the eqaulity constraint.
     */
    public void setEquality(Integer e) {
    	if (e == null) return;
        lowerBound = null;
        upperBound = null;
        equality = e;
        setLowerBound(e);
        setUpperBound(e);
    }
    
    /**
     * Merge the element with another.
     * 
     * @throws NullPointerException if that is null.
     * @param that
     */
    public void merge(UFElement that) {
    	if (that == null) throw new NullPointerException();
    	setLowerBound(that.getLower());
    	setUpperBound(that.getUpper());
    	setEquality(that.getEquality());
    }

}
