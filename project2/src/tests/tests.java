package tests;

import Operators.ScanOperator;
import util.DBCat;
import util.Table;

public class tests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBCat catalog = DBCat.getInstance();
		String table = "Reserves";
		Table tab = catalog.getTable(table);
		ScanOperator sop = new ScanOperator(tab);
		sop.dump(System.out);
	}

}
