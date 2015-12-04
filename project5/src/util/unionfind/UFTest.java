package util.unionfind;

import static org.junit.Assert.*;

import org.junit.Test;

public class UFTest {

	//@Test
	public void testUF() {
		UnionFind uf = new UnionFind();
		assertEquals(false, uf.connected("S.A", "R.D"));
		assertEquals(2, uf.count());
		uf.union("S.A", "R.D");
		assertEquals(true, uf.connected("S.A", "R.D"));
		assertEquals(1, uf.count());
		
		assertEquals(false, uf.connected("S.B", "R.E"));
		assertEquals(3, uf.count());
		uf.union("S.B", "R.E");
		assertEquals(true, uf.connected("S.B", "R.E"));
		assertEquals(false, uf.connected("S.A", "R.E"));
		assertEquals(2, uf.count());
		
	
		
		uf.union("S.B", "S.A");
		assertEquals(1, uf.count());
		assertEquals(true, uf.connected("S.A", "R.E"));
		
		
		
	}
	
	@Test
	public void testUFElement() {
		UnionFind uf = new UnionFind();
		UFElement e1 = uf.find("S.A");
		assertEquals(null, e1.getEquality());
		assertEquals(null, e1.getLower());
		assertEquals(null, e1.getUpper());
		
		e1.setLowerBound(3);
		e1.setUpperBound(10);
		assertEquals(null, e1.getEquality());
		assertEquals(Integer.valueOf(3), e1.getLower());
		assertEquals(Integer.valueOf(10), e1.getUpper());
		
		UFElement e2 = uf.find("B.B");
		e2.setLowerBound(6);
		uf.union("S.A", "B.B");
		assertEquals(1, uf.count());
		assertEquals(Integer.valueOf(6), e1.getLower());
		assertEquals(Integer.valueOf(10), e1.getUpper());
		assertEquals(Integer.valueOf(10), e2.getUpper());
		e1.setEquality(8);
		
		for (String s : uf.componentsInfo()) {
			System.out.println(s);
		}
		
	}

}
