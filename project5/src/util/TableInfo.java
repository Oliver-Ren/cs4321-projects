package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TableInfo {
	String tabName;
	HashMap<String,int[]> map;
	int attNum = -1;
	int tpNum;
	public TableInfo(String tabName, String[] name, int[] low, int[] high ){
		this.tabName = tabName;
		map = new HashMap<String, int[]>();
		if(name.length!= low.length || low.length != high.length
				|| name.length != high.length){
			throw new IllegalArgumentException();
		}			
		for(int i = 0; i < name.length; i++){
			int[] range ={ low[i], high[i]};
			if(name[i]!= null) {
				map.put(name[i],range);
			}
			
		}
		attNum = name.length;
//		if(map.isEmpty()){
//			throw new IllegalArgumentException();
//		}		
	}
	/** 
	 * get the total number of attribute 
	 */
	public int getAttNum(){
		if(attNum == -1){
			throw new IllegalArgumentException();
		}
		return attNum;
	}
	/**
	 * 
	 * return an int array contains the lowest and highest value 
	 * related to the current attr name, return null if the attr
	 * is not in the table  
	 * @input: attribute name 
	 * @return: int[] int[0] is min; int[1] is high
	 */
	public int[]  getRange(String attrName){
		if(map.containsKey(attrName)){
		return map.get(attrName);
		} else {
			return null;
		}
	}
	/**
	 * return a list of attr names corresponding to this table 
	 * @return
	 */
	public String[] getAttrName(){
		Set<String> attrName = map.keySet();
		String[] list = (String[])(attrName.toArray());
		return list;
	}
	/**
	 * return the table name 
	 * @return
	 */
	public String getTableName(){
		return tabName;
	}
	/**
	 * add new attr to the current table 
	 * 
	 */
	public void addAttr(String attrName, int low, int high){
		if(map.containsKey(attrName)){
			throw new IllegalArgumentException();
		} else {
			int[] range = {low, high};
			map.put(attrName,range);
		}
	}
	
	/**
	 * set the tuple number for this table
	 */
	public void setTpNum(int num){
		tpNum = num;
	}
	/**
	 * 
	 * @return num of tps in this table
	 */
	public int getTpNum(){
		return tpNum;
	}
	
	
}
	
