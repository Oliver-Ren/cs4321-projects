package util;

import java.util.Collections;

import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Helpers {

	public static String getTabName(FromItem from) {
		return from.toString().split(" ")[0];
	}
	
	public static String getSelCol(SelectItem sel) {
		String[] strs = sel.toString().split(".");
		if (strs.length < 2)
			return strs[0];
		else
			return strs[1];
	}
	
	public static long getColVal(Tuple tp, String attr, String tabName) {
		return tp.cols[DBCat.schemas.get(tabName).indexOf(attr)];
	}
	
}
