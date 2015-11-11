package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import btree.BPlusTree;

/**
 * This is the Unit Test for the B+ Tree Indexing.
 * @author Chengxiang Ren (cr486)
 *
 */
public class BPlusTreeTest {

	@Test
	public void testUnclusteredLeafConstruct() {
		File relation = new File("tests/unit/bplustree/Boats");
		try {
			BPlusTree tree = new BPlusTree(relation, "Boats", 1, 10, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
