public List<TreeNode> createIndexLayer(List<TreeNode> preLayer,TreeSerializer ts) throws IOException{
		List<TreeNode> newLayer = new ArrayList<TreeNode>();		
			int cnt = 0;
			List<Integer> keys = new ArrayList<Integer>();
			List<TreeNode> children = new ArrayList<TreeNode>();
			List<Integer> address = new ArrayList<Integer>(); // list of address
			for(int i = 0; i < preLayer.size(); i++){
				if (cnt == capacity){
					children.add(preLayer.get(i));
					int ads = ts.serialize(preLayer.get(i));
					address.add(ads);
					//add last key
					keys.add(preLayer.get(i).getMin());
					//create a index node
					IndexNode node = new IndexNode(order,keys,children,address);
					newLayer.add(node);
					//reset
					cnt = 0;
					keys.clear();
					children.clear();
					address.clear();
					continue;
				}
				
				if(cnt == 0){
					children.add(preLayer.get(i));	
					int ads = ts.serialize(preLayer.get(i));
					address.add(ads);
					cnt++;
				} else if (cnt < capacity ){
					//add key
					keys.add(preLayer.get(i).getMin());
					children.add(preLayer.get(i));
					int ads = ts.serialize(preLayer.get(i));
					address.add(ads);
					cnt++;
				} 
				
			}
			
			if(keys.size()!=0){
				//check the last node
				if(keys.size() >= order){
					IndexNode node = new IndexNode(order, keys, children,address);
					newLayer.add(node);
				} else {
					//the current lay only has one node		
					if(newLayer.size() == 0){
						IndexNode node = new IndexNode (order, keys, children,address);
						newLayer.add(node);
						
					} else { // need redistribution
						
						IndexNode secondLast =(IndexNode)newLayer.remove(newLayer.size()-1);
						System.out.println("my current node is" + secondLast.toString());
						List<TreeNode>secLastChildren = secondLast.children;
						for (TreeNode n : secLastChildren){
							System.out.print(((LeafNode)n).getMin() + " ");
						}
						List<Integer> secLastKeys = secondLast.keys;
						List<Integer> secLastAddress = secondLast.address;
						int numOfKeys =(secLastChildren.size() + children.size())/2-1;
						
						// build keys for last node
						List<Integer> lastNodeKeys = secLastKeys.subList(numOfKeys, secLastKeys.size());
						lastNodeKeys.addAll(keys);
						
						// build children for last node
						List<TreeNode> lastChildren = 
								secLastChildren.subList(numOfKeys+1, secLastChildren.size()-1);
						lastChildren.addAll(children);
						// build address for last node 
						List<Integer> lastAddress = secLastAddress.subList(numOfKeys, secLastAddress.size());
						lastAddress.addAll(address);
						
						//update keys for second last node 
						secLastKeys = secLastKeys.subList(0, numOfKeys);
						//update children for second last node 
						secLastChildren = secLastChildren.subList(0, numOfKeys+1);
						// update address for second last node
						secLastAddress  = secLastAddress.subList(0, numOfKeys+1);
						//add two nodes to new Layer
						newLayer.add(new IndexNode(order, secLastKeys, secLastChildren,secLastAddress));
						newLayer.add(new IndexNode(order,lastNodeKeys,lastChildren, lastAddress));
						
					}
				}
			}
		
		
		
		return newLayer;
	}