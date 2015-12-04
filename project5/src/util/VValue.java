package util;

import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;

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
			rst.put(tabName + "." + attName, ) 
			
		}
		
		
		
		
		return rst;
	}
}
