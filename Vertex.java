


public class Vertex {
	
		private Integer id;						// Vertex identifier
		private Vertex pred;					// Vertex predecesser
		private String colour;					// Vertex colour
		
		public Vertex(Integer i){
			id = i;
			colour = "NONE";					// Set initial colour to NONE
		}
		
		// Return Vertex ID
		public Integer getID(){
			return id;
		}
		
		// Set Vertex Colour
		public void setColour(String c){
			colour = c;
		}
		
		// Return Vertex Colour
		public String getColour(){
			return colour;
		}
		
		// Set Vertex Predecesser
		public void setPred(Vertex p){
			pred = p;
		}
		
		// Return Vertex Predecesser
		public Vertex getPred(){
			return pred;
		}
}
