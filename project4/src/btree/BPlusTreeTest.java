package btree;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;

/**
 * This is the Unit Test for the B+ Tree Indexing.
 * @author Chengxiang Ren (cr486)
 *
 */
public class BPlusTreeTest {

	//@Test
	public void testUnclusteredLeafConstruct() {
		File relation = new File("tests/unit/bplustree/Boats");
		File output = null;
		try {
			BPlusTree tree = new BPlusTree(relation, 1, 10, output);
			for (TreeNode node : tree.leafLayer) {
				System.out.println(node);
				System.out.println();
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
		File output = null;
		try {
			BPlusTree tree = new BPlusTree(relation, 0, 15, output);
			for (DataEntry d : tree.dataEntries) {
				System.out.println(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test case for verification of functionality of index layer.
	 */
	//@Test
	public void testIndexLayer() {
		File relation = new File("tests/unit/bplustree/Boats");
		File output = null;
		File result = new File("tests/unit/bplustree/IndexLayerResult");
		
		try {
			PrintStream printer = new PrintStream(result);
			BPlusTree tree = new BPlusTree(relation, 1, 10, output);
			List<TreeNode> index = tree.createIndexLayer(tree.leafLayer, tree.ts);
			printer.println("---------Last Index layer " +
					"is index nodes---------");
			for (TreeNode node : index) {
				printer.print(node);
				printer.println();
			}
			
			for (TreeNode node : tree.leafLayer) {
				printer.println(node);
				printer.println();
			}
			
			printer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test case for deserializer.
	 */
	@Test
	public void testDeserializeRoot() {
		File indexFile = new File("tests/unit/deserialize/Sailors.A");
		File output = null;
		File result = new File("tests/unit/deserialize/result");
		try {
			PrintStream printer = new PrintStream(result);
			TreeDeserializer td = new TreeDeserializer(indexFile);
			td.dump(printer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
