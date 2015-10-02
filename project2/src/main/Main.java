package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import operators.Operator;
import util.DBCat;
import util.Helpers;
import util.SelState;

public class Main {

	public static void main(String[] args) {
		DBCat.getInstance();
		if (args.length >= 1)
			DBCat.resetDirs(args[0], null);
		if (args.length >= 2)
			DBCat.resetDirs(null, args[1]);
		
		try {
			CCJSqlParser parser 
				= new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Parsing statement: " + statement.toString());
				SelState ss = new SelState(statement);
				ss.root.dump(System.out);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
