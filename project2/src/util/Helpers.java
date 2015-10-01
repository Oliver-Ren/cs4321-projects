package util;

import net.sf.jsqlparser.statement.select.FromItem;

public class Helpers {

	public static String getTabName(FromItem from) {
		return from.toString().split(" ")[0];
	}
	
}
