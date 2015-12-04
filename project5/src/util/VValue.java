package util;

import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.schema.Column;

/**
 * this class is used for calculate vvalues
 * @author Mingyuanh
 *
 */
public class VValue {
	static HashMap<String,Integer>  rst = null;
	// the map contains each table's statistics
	static HashMap<String, TableInfo> map = null;
	// this is used for keep track which table's attr is mentioned in the selection
	// value stores attr name
	
	/**
	 * this method calculates all the v values for each valid attribute
	 * in the from expression 
	 * @param baseTable is the table name and the attribute name  (R.A)
	 * @return a Hashmap, whose keys are tableName.AttributeName (eg R.A)
	 * its value is the vvlaue corresponding to the key 
	 */
	public static HashMap<String,Integer> 
	getVValues(String[] baseTable,Expression[] selecTable, Expression[] joinTable    ){
		// get all the table information
		map = DBCat.tabInfo;
		// result for return
		rst = new HashMap<String, Integer>();
		// process case 1: base table
		for (String tab : baseTable){
			// split tab
			String tabName = tab.split("\\,")[0];
			String attName = tab.split("\\.")[1];
			int[] range = map.get(tabName).getRange(attName);
			//calculate v value 
			//!!!!!!!!!!!!!!!!!!
			rst.put(tabName, range[1] - range[0] + 1); 
		}
		// process case 2:  selection condition
		for(Expression ex : selecTable){
			Column left = null;
			int right = 0;
			if(ex instanceof GreaterThan ){
				if(((GreaterThan) ex).getLeftExpression() instanceof Column){
					left =(Column) ((GreaterThan)ex).getLeftExpression();
					LongValue value =(LongValue)((GreaterThan)ex).getRightExpression();
					right = (int)value.getValue();
				} else{ // left is value 
					left =(Column) ((GreaterThan)ex).getRightExpression();
					LongValue value =(LongValue)((GreaterThan)ex).getLeftExpression();
					right= (int)value.getValue();
				}
				//
				String tabName = left.toString().split("\\.")[0];
				String attName = left.toString().split("\\.")[1];
				int[] range = map.get(tabName).getRange(attName);
				
				// calculate reduction factor
				double rf =((double)(range[1] - right)) /((double)(range[1] - range[0]));
				
				if(rst.containsKey(tabName)){
					rst.put(tabName, Math.min(rst.get(tabName),(int)(rf * (range[1]-range[0]+1))));
				} else {
					rst.put(tabName, (int)(rf * (range[1]- range[0]+1)));
				}	
			} else if(ex instanceof GreaterThanEquals ){
				if(((GreaterThanEquals) ex).getLeftExpression() instanceof Column){
					left =(Column) ((GreaterThanEquals)ex).getLeftExpression();
					LongValue value =(LongValue)((GreaterThanEquals)ex).getRightExpression();
					right = (int)value.getValue();
				} else{ // left is value 
					left =(Column) ((GreaterThanEquals)ex).getRightExpression();
					LongValue value =(LongValue)((GreaterThanEquals)ex).getLeftExpression();
					right= (int)value.getValue();
				}
				//
				String tabName = left.toString().split("\\.")[0];
				String attName = left.toString().split("\\.")[1];
				int[] range = map.get(tabName).getRange(attName);
				
				// calculate reduction factor
				double rf =((double)(range[1] - right)) /((double)(range[1] - range[0]));
				
				if(rst.containsKey(tabName)){
					rst.put(tabName, Math.min(rst.get(tabName),(int)(rf * (range[1]-range[0]+1))));
				} else {
					rst.put(tabName, (int)(rf * (range[1]- range[0]+1)));
				}	
			} else if(ex instanceof MinorThan ){
				if(((MinorThan) ex).getLeftExpression() instanceof Column){
					left =(Column) ((MinorThan)ex).getLeftExpression();
					LongValue value =(LongValue)((MinorThan)ex).getRightExpression();
					right = (int)value.getValue();
				} else{ // left is value 
					left =(Column) ((MinorThan)ex).getRightExpression();
					LongValue value =(LongValue)((MinorThan)ex).getLeftExpression();
					right= (int)value.getValue();
				}
				//
				String tabName = left.toString().split("\\.")[0];
				String attName = left.toString().split("\\.")[1];
				int[] range = map.get(tabName).getRange(attName);
				
				// calculate reduction factor
				double rf =((double)(range[1] - right)) /((double)(range[1] - range[0]));
				
				if(rst.containsKey(tabName)){
					rst.put(tabName, Math.min(rst.get(tabName),(int)(rf * (range[1]-range[0]+1))));
				} else {
					rst.put(tabName, (int)(rf * (range[1]- range[0]+1)));
				} 
			} else if(ex instanceof MinorThanEquals){
			if(((MinorThanEquals) ex).getLeftExpression() instanceof Column){
				left =(Column) ((MinorThanEquals)ex).getLeftExpression();
				LongValue value =(LongValue)((MinorThanEquals)ex).getRightExpression();
				right = (int)value.getValue();
			} else{ // left is value 
				left =(Column) ((MinorThanEquals)ex).getRightExpression();
				LongValue value =(LongValue)((MinorThanEquals)ex).getLeftExpression();
				right= (int)value.getValue();
			}
			//
			String tabName = left.toString().split("\\.")[0];
			String attName = left.toString().split("\\.")[1];
			int[] range = map.get(tabName).getRange(attName);
			
			// calculate reduction factor
			double rf =((double)(range[1] - right)) /((double)(range[1] - range[0]));
			
			if(rst.containsKey(tabName)){
				rst.put(tabName, Math.min(rst.get(tabName),(int)(rf * (range[1]-range[0]+1))));
			} else {
				rst.put(tabName, (int)(rf * (range[1]- range[0]+1)));
			} 
		
		
			}
		}
		// process 3rd condition: the join condition:
		for(Expression ex : joinTable){
			Column left = (Column)(((EqualsTo)ex).getLeftExpression());
			Column right = (Column)(((EqualsTo)ex).getRightExpression());
			if(left!= null && right != null){
				String leftTabName = left.toString().split("\\.")[0];
				String rightTabName = right.toString().split("\\.")[0];
				if(!rst.containsKey(leftTabName) &&
						rst.containsKey(rightTabName)){
					rst.put(leftTabName, rst.get(rightTabName));
				} else if (!rst.containsKey(rightTabName) &&
						rst.containsKey(leftTabName)){
					rst.put(rightTabName, rst.get(leftTabName));
				} else if (rst.containsKey(rightTabName) && rst.containsKey(leftTabName)){
					int min = Math.min(rst.get(leftTabName), rst.get(rightTabName));
					rst.put(leftTabName, min);
					rst.put(rightTabName, min);
				} else { // both not exist in the selection condition
					int[] leftRange =
							map.get(leftTabName).getRange(left.toString().split("\\.")[1]);
					int[] rightRange = 
							map.get(rightTabName).getRange(right.toString().split("\\.")[1]);
					int leftVal = leftRange[1]-leftRange[0]+1;
					int rightVal = rightRange[1] - rightRange[0]+1;
					rst.put(leftTabName,Math.min(leftVal,rightVal));
					rst.put(rightTabName, Math.min(leftVal,rightVal));				
				}
			}	
		}
		return rst;
	}
}
