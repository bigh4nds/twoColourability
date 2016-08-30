import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Stack;

public class TwoColourability {

	private static ArrayList<Vertex> vertices = new ArrayList<Vertex>(); 		// Holds all vertices
	private static LinkedList<Integer>[] edges;							 		// Adjacency list
	private static String filename;											 	// Graph to load
	
	public static void main(String[] args) {
		filename = args[0];														// Filename = first argument
		System.out.println("Two Colourabiity Checker");
		System.out.println("-----------------------");
		try {
			graphInit(filename);												// Initialize graph
		} catch (IOException e) {
			e.printStackTrace();
		}
		BFS();																	// Breadth First Search
	}
	
	@SuppressWarnings("unchecked")
	private static void graphInit(String filename) throws IOException{
		int num_verts = 0;
		try {
		    BufferedReader input = new BufferedReader(new FileReader(filename));
		    String str;
		    num_verts = Integer.parseInt(input.readLine());						// Parse number of vertices
		    edges = (LinkedList<Integer>[]) new LinkedList[num_verts+1];		// Create adjacency list
		    for(int i=0;i<=num_verts;i++){
		    	vertices.add(new Vertex(i));									// Create & add vertices to arraylist
		    	edges[i] = new LinkedList<Integer>();							// Initialize adjacency list
		    }
		    while ((str = input.readLine()) != null) {							// Read till no more lines
		    	int split = str.indexOf(',');									// Find index of ',' in str
		        int v1 = Integer.parseInt(str.substring(0,split));				// 1st vertex is Integer before the ','
		        int v2 = Integer.parseInt(str.substring(split+1,str.length()));	// 2nd vertex is Integer after the ','
		        edges[v1].add(v2);												// Add edge between v1 & v2
		        edges[v2].add(v1);												// Add edge between v2 & v1
		    }
		    input.close();
		} catch (IOException e) {
			System.out.println("Failed to open graph file");
		}
	}

	private static void BFS(){
		Queue<Vertex> queue=new LinkedList<Vertex>();								// Create Queue
		queue.add(vertices.get(1));													// Enqueue vertex 1 (source vertex)
		vertices.get(1).setColour("BLACK");											// Set vertex 1's colour to BLACK
		while(!queue.isEmpty()){													// While queue is not empty
			Vertex curr_vert = (Vertex) queue.remove();								// Dequeue top vertex --> curr_vert
			ListIterator<Integer> itr = edges[curr_vert.getID()].listIterator();	// Create iterator to go through edges
			while(itr.hasNext()){													// While more edges to explore
				Vertex adjVert = vertices.get(itr.next());							// Get next adjacent vertex --> adjVert
				if(adjVert.getColour().equals(curr_vert.getColour())){				// If adjVert's colour matches curr_vert's
					if(vertices.size()>50)											// colour then there is a colouring issue
						outputProblemCycleToFile(curr_vert, adjVert);				// Number of verts > 50 --> output to file
					else
						outputProblemCycleToConsole(curr_vert, adjVert);			// Number of verts <= 50 --> output to console
					return;															// Stop the Breadth First Search	
				}
				if(adjVert.getColour().equals("NONE")){								// If adjVert has not been coloured
					adjVert.setPred(curr_vert);										// Set curr_vert as adjVert's predecesser
					if(curr_vert.getColour().equals("BLACK"))						// Colour adjVert
						adjVert.setColour("WHITE");									// the opposite
					else															// colour of
						adjVert.setColour("BLACK");									// curr_vert
					queue.add(adjVert);												// Enqueue adjVert
				}
			}	
		}
		if(vertices.size()>50)
			outputCorrectToFile();													// Number of verts > 50 --> output to file
		else
			outputCorrectToConsole();												// Number of verts <= 50 --> output to console
	}
	
	private static void outputProblemCycleToFile(Vertex v1, Vertex v2){
		try{
			  FileWriter fstream = new FileWriter(filename.concat(".output"));
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write("---------------------------------\n");
			  out.write("NO, the Graph is not 2-colourable\n");
			  out.write("---------------------------------\n");
			  out.write("Output of bad cycle:\n");
			  Stack<Vertex> stack = new Stack<Vertex>();						// Create stack
			  stack.push(v2);													// Push v2 onto the stack
			  out.write(v2.getID()+"---"+v1.getID()+"\n");						// Write edge to file
			  while(v1.getID()!=v2.getID()){									// While v1 is not the same vertex as v2
				  out.write(v1.getID()+"---"+v1.getPred().getID()+"\n");		// Write edge to file
				  v1 = v1.getPred();											// v1 is set to its predecesser
				  stack.push(v2.getPred());										// Push v2's predecesser to the stack
				  v2 = v2.getPred();											// v2 is set to its predecesser
			  }
			  stack.pop();														// Ignore top vertex of stack because it is a repeat
			  while(!stack.isEmpty()){											// While the stack is not empty
				  v2 = stack.pop();												// Pop out top vertex --> v2
				  out.write(v1.getID()+"---"+v2.getID()+"\n");					// Write edge to file
				  v1 = v2;														// v1 is set to v2
			  }
			  out.close();
		  }catch (Exception e){
		  System.err.println("Error: " + e.getMessage());
	   }
	  System.out.println("Output written to text file");
	}
	
	private static void outputProblemCycleToConsole(Vertex v1, Vertex v2){
		System.out.println("---------------------------------");
		System.out.println("NO, the Graph is not 2-colourable");
		System.out.println("---------------------------------");
		System.out.println("Output of bad cycle:");
		Stack<Vertex> stack = new Stack<Vertex>();
		stack.push(v2);
		System.out.println(v2.getID()+"---"+v1.getID());
		while(v1.getID()!=v2.getID()){
			System.out.println(v1.getID()+"---"+v1.getPred().getID());
			v1 = v1.getPred();
			stack.push(v2.getPred());
			v2 = v2.getPred();
		}
		stack.pop();
		while(!stack.isEmpty()){
			v2 = stack.pop();
			System.out.println(v1.getID()+"---"+v2.getID());
			v1 = v2;
		}
	}
	
	private static void outputCorrectToFile(){
		try{
			  FileWriter fstream = new FileWriter(filename.concat(".output"));
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write("------------------------------\n");
			  out.write("YES, the Graph is 2-colourable\n");
			  out.write("------------------------------\n");
			  out.write("Output of valid colouring:\n");
			  out.write("Vertex\tColour\n");									// Write titles
			  for(int i=1;i<vertices.size();i++){								// Loop through all of the vertices
				  out.write(i+"\t");											// Write vertex ID
				  if(vertices.get(i).getColour().equals("BLACK"))				// Write vertex colour
					  	out.write("BLACK\n");
					else
						out.write("WHITE\n");
			  }	
			  out.close();
			  }catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		   }
		System.out.println("Output written to text file");
	}
	
	private static void outputCorrectToConsole(){
		System.out.println("------------------------------");
		System.out.println("YES, the Graph is 2-colourable");
		System.out.println("------------------------------");
		System.out.println("Output of valid colouring:");
		System.out.println("Vertex\tColour");
		for(int i=1;i<vertices.size();i++){
			System.out.print(i+"\t");
			if(vertices.get(i).getColour().equals("BLACK"))
			 	System.out.println("BLACK");
			else
				System.out.println("WHITE");
		}
	}

}