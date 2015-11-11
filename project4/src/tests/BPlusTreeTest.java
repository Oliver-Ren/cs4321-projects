package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * This is the Unit Test for the B+ Tree Indexing.
 * @author Chengxiang Ren (cr486)
 *
 */
public class BPlusTreeTest {

	@Test
	public void testUnclusteredLeafConstruct() {
		File relation = new File("test/unit/bplustree/Boats");
		System.out.print(relation);
	}

}
