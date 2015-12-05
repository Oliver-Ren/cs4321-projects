package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import util.DBCat;
import util.Helpers;
import util.IndexInfo;


public class SelectionOptimizer {
	// this is used for keep track each attr's name and its range
	static HashMap<String, Integer[]> attInfo; 
	static List<String> attName;
	static List<Double> cost;
	static double plainScanCost;
	public static IndexInfo whichIndexToUse(String tableName, Expression exp) {
		//TODO
		/**
		 *Integer[0] is max; Integer[1] is min
		 */
		attInfo = new HashMap<String, Integer[]>();
		attName = new ArrayList<String>();
		cost = new ArrayList<Double>();
		plainScanCost = -1;
		List<Expression> exps = Helpers.decompAnds(exp);
		if(exps == null) {
			throw new NullPointerException();
		}
		
		for(Expression ex : exps){
			// 可能有错！！！！！！！！！！
			Expression left =((BinaryExpression)ex).getLeftExpression();
			Expression right =((BinaryExpression)ex).getRightExpression();
			String attr = "";
//			// rule out the possiblity of S.a >= S.b
//			if(left instanceof Column && right instanceof Column){
//				
//			}
			if(left instanceof Column && right instanceof LongValue) {
				//String[] split =((Column)left).toString().split("\\.");
				attr = ((Column)left).toString().split("\\.")[1];				
			} else if(right instanceof Column && left instanceof LongValue){
				attr = ((Column)right).toString().split("\\.")[1];
			} else if(right instanceof Column && left instanceof Column){
				//calculate plain cost
				
				plainScanCost = (DBCat.tabInfo.get(tableName).getTpNum() *
						DBCat.tabInfo.get(tableName).getAttNum() * 4)/4096;
				continue;
			}
			String[] in = {attr};
			// !!!!!! 可能有问题
			Integer[] range = Helpers.getSelRange(ex,in);
			updateAttInfo(attr,range);			
		}

		// tpsize: tuple number  * 4 bytes * attribute number
		double tpsize = DBCat.tabInfo.get(tableName).getTpNum() *
				DBCat.tabInfo.get(tableName).getAttNum() * 4;
		// page number 
		double pageNum = tpsize / 4096;
		// calculate each attr's cost
		Set<Entry<String,Integer[]>> set = attInfo.entrySet();
		// each entry is a attr name : range
		for(Entry<String,Integer[]> entry : set){
			IndexInfo indexInfo = DBCat.idxManager.getIndexInfo(tableName, entry.getKey());	
			double localCost = -1;
			if(indexInfo == null){ // only plain scan
				
				localCost = pageNum;
				
			} else { // has index 
				int[] minMax = DBCat.tabInfo.get(tableName).getRange(entry.getKey());
				double maxRange = minMax[1] - minMax[0];
				double curLow = 0;
				double curHigh = 0;	
				//check max
				if(entry.getValue()[0] == null){
					curHigh = minMax[1];
				}
				//check min
				if(entry.getValue()[1] == null){
					curLow = minMax[0];
				}
				// calculate reduction factor
				double rf =(curHigh - curLow) /maxRange;
				
				if(indexInfo.clust){ // if cur attr has clustered index
					localCost = 3 + pageNum * rf;
				} else {
					double leafNum = indexInfo.getNumOfLeafNodes();
					localCost = 3 + (leafNum + DBCat.tabInfo.get(tableName).getTpNum())*rf;
				}
			}
			attName.add(entry.getKey());
			cost.add(localCost);	
		}
		// check which attr has the lowerest cost
		if(plainScanCost !=-1) {
			if(cost.size()!=0){
				for(double c : cost){
					if(plainScanCost < c){
						return null;
					}
				}
			}
		}
		if(cost.size()==0){
			return null;
		}
		if(attName.size() != cost.size()){
			throw new IllegalArgumentException();
		}
		int minIndex = 0;
		double minCost = cost.get(0);
		for(int i = 1; i < cost.size(); i++){
			if(minCost > cost.get(i)){
				minCost = cost.get(i);
				minIndex = i;
			}
		}
		return DBCat.idxManager.getIndexInfo(tableName, attName.get(minIndex));
		
		
		
	}
	/** 
	 * update the range for each of my attr
	 * 
	 */
	public static void updateAttInfo(String attr, Integer[] range){
		if(!attInfo.containsKey(attr)){
			Integer[] r = new Integer[2];
			for(int i = 0; i < range.length; i++){
				r[i] = range[i];
			}
			attInfo.put(attr, r);
		} else {
			Integer[] preRange = attInfo.get(attr);		
		// update min  
		if(range[0]!=null){
			if(preRange[0] == null){
				preRange[0] = range[0];
			} else {
				preRange[0] = Math.max(preRange[0],range[0]);
				attInfo.put(attr, preRange);
			}
		}
		//update max
		if(range[1]!=null){
			if(preRange[1] == null){
				preRange[1] = range[1];
			} else {
				preRange[1] = Math.min(preRange[1],range[1]);				
				attInfo.put(attr, preRange);
			}
		}			
		}
	}
}
