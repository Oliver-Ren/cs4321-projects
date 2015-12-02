package util;

public class IndexInfo {

	public String relt = "";
	public String attr = "";
	public boolean clust = false;
	public int order = 1;
	
	public IndexInfo(String r, String a, boolean c, int o) {
		relt = r;
		attr = a;
		clust = c;
		order = o;
	}	
	
}
