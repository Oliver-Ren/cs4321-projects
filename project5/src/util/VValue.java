package util;

import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
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
	/**
	 * this method calculates all the v values for each valid attribute
	 * in the from expression 
	 * @param baseTable is the table name and the attribute name  (R.A)
	 * @return a Hashmap, whose keys are tableName.AttributeName (eg R.A)
	 * its value is the vvlaue corresponding to the key 
	 */
	public static HashMap<String,Integer> 
	getVValues(String[] baseTable,Expression[] selecTable, Expression joinTable    ){
		map = DBCat.tableInfo;
		// result for return
		rst = new HashMap<String, Integer>();
		// process case 1: base table
		for (String tab : baseTable){
			// split tab
			String tabName = tab.split("\\,")[0];
			String attName = tab.split("\\.")[1];
			int[] range = map.get(tabName).getRange(attName);
			//calculate v value 
			
			rst.put(tab, range[1] - range[0] + 1); 
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
				int rf = (range[1] - right) /map.get(tabName).g 
				
			}
		}
		
		
		
		return rst;
	}
}
