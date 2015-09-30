package tests;

import Operators.ScanOperator;
import util.DBCat;
import util.Table;

public class tests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Table tab = DBCat.getTable("Sailors");
		ScanOperator sop = new ScanOperator(tab);
		sop.dump(System.out);
	}

}
