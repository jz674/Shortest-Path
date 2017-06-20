/* Time spent on a7:  5 hours and 0 minutes.

 * Name: Anirudh Maddula, Jeffrey Zhang
 * Netid: aam252, jz674
 * What I thought about this assignment:
 * Testing was a bit difficult, since it would I would pass all the test cases all at once
 * only when I had finished most of the code.
 *
 */

package student;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Edge;
import graph.Node;

/** This class contains Dijkstra's shortest-path algorithm and some other methods. */
public class Paths {

	/** Return the shortest path from start to end, or the empty list if a path
	 * does not exist.
	 * Note: The empty list is NOT "null"; it is a list with 0 elements. */
	public static List<Node> shortestPath(Node start, Node end) {
		/* TODO Read note A7 FAQs on the course piazza for ALL details. */

		Heap<Node> F = new Heap<Node>();
		F.add(start, 0);
		//As described in the abstract version of the algorithm in the lecture 
		//notes:		
		//S will store each Node, that is either in the Frontier or Settled set,
		//as a key, along with an object of SFdata, which will hold the distance
		//of assumed "shortest" path to that node and it's back pointer
		//along that path
		HashMap<Node, SFdata> S = new HashMap<Node, SFdata>();

		SFdata orig = new SFdata(0, null);
		S.put(start, orig);

		while(F.size!=0){
			Node f = F.poll();			
			if(f==end){
				return constructPath(end, S);
			}		
			for(Edge e: f.getExits()){
				Node w = e.getOther(f);
				// get distance to F from start
				int distanceOfF = S.get(f).distance; 
				//get the length of this edge - distance from f to w aka "weight"
				int weightFtoW = e.length; 
				// distance to W from F; distanceOfW = d[w]= d[f] + wgt(f, w);
				int distanceOfW = distanceOfF + weightFtoW; 

				if(!S.containsKey(w)){
					//put distance of w into the hashSet with an SFdata,
					//also into heap
					//d[w]= d[f] + wgt(f, w);
					F.add(w, distanceOfW); 
					//add this to the Heap which holds this min distances 
					//to every node
					S.put(w, new SFdata(distanceOfW, f));
				} else if(distanceOfW < S.get(w).distance){
					F.updatePriority(w, distanceOfW);
					//orig.distance = distanceOfW;
					//orig.backPointer=f;
					S.put(w, new SFdata(distanceOfW, f)); 
					//use existing SFdata object instead - don't do this line
					//S.put(w, orig); 
				}
			}
		}
		return new LinkedList<Node>();
	}

	/** Return the path from the start node to node end.
	 *  Precondition: nData contains all the necessary information about
	 *  the path. */
	public static List<Node> constructPath(Node end, HashMap<Node,SFdata> nData){
		LinkedList<Node> path= new LinkedList<Node>();
		Node p= end;
		// invariant: All the nodes from p's successor to the end are in
		//            path, in reverse order.
		while (p != null) {
			path.addFirst(p);
			p= nData.get(p).backPointer;
		}
		return path;
	}

	/** Return the sum of the weights of the edges on path path. */
	public static int pathDistance(List<Node> path) {
		if (path.size() == 0) return 0;
		synchronized(path) {
			Iterator<Node> iter= path.iterator();
			Node p= iter.next();  // First node on path
			int s= 0;
			// invariant: s = sum of weights of edges from start to p
			while (iter.hasNext()) {
				Node q= iter.next();
				s= s + p.getEdge(q).length;
				p= q;
			}
			return s;
		}
	}

	/** An instance contains information about a node: the previous node
	 *  on a shortest path from the start node to this node and the distance
	 *  of this node from the start node. */
	private static class SFdata {
		private Node backPointer; //backpointer on path from start node to this 
		private int distance; // distance from start node to this one

		/** Constructor: an instance with distance d from the start node and
		 *  backpointer p.*/
		private SFdata(int d, Node p) {
			distance= d;     // Distance from start node to this one.
			backPointer= p;  // Backpointer on the path (null if start node)
		}

		/** return a representation of this instance. */
		public String toString() {
			return "dist " + distance + ", bckptr " + backPointer;
		}
	}
}
