package btree;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

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
			for (LeafNode node : tree.leafLayer) {
				System.out.println(node);
			}
//			for (DataEntry data : tree.dataEntries) {
//				System.out.println(data);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testClusteredLeafConstruct() {
		File relation = new File("tests/unit/bplustree/Sailors");
		try {
			BPlusTree tree = new BPlusTree(relation, "Sailors", 0, 15, true);
			for (DataEntry d : tree.dataEntries) {
				System.out.println(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
