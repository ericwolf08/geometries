package Abgabe;


import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/*
 * @author Eric Wolf
 */
public class Rectangle extends Interface {
	
	public static void create(int xval, int yval, int scale, Mesh mesh, Geometry geo) {
		// setting minimum values
		if (xval <= 2){
			xval = 2;
		}
		if (yval <= 2){
			yval = 2;
		}
		if (scale <= 1){
			scale = 1;					
		}
		float deltaX = 1 / (float)(xval - 1);
		float deltaY = 1 / (float)(yval - 1);
				
		// setting the Vertex positions
		int curV = 0;
		// we need X*Y vertices
		Vector3f[] vertices = new Vector3f[xval*yval];
		
		// creating a big quader, each side with 4 vectors and their positions
		for (int y = 0; y < yval; y++){
			for (int x = 0; x < xval; x++){
				// calculates the coordinates depending on the position of the vector and the scale factor
				vertices[curV] = new Vector3f(x*deltaX*scale, y*deltaY*scale, 0);
		        curV = curV +1;
		    }
		}
		
		Vector3f[] normals = new Vector3f[xval*yval];
		
		//array for the normals
		for (int hai = 0; hai < xval*yval; hai++){
			// every normal shows to the camera, so the z value == 1
			normals[hai] = new Vector3f(0,0,1);
		}
		
				
		// returns the vertices
		
		//for (int i = 0;  i < vertices.length; i++) {
		//	System.out.println(i + vertices[i].toString());
		//}
		
		
		
		// 6,3,7,4,8,5,5,3,3,0,4,1,5,2 for 3x3
		/*
		 * needed for the size of the array, calculate how much 
		 * indices for 1 line multiplied with the number row 
		 * plus 2 extra indices for going to the beginning of a line
		 */
		int maxInd = ((yval-2)*2)+((yval-1)*(2*xval));
        int [] indices = new int[maxInd];
        //System.out.println(maxInd);
		
        /*
         * indices[c] = y*xval-1;
            	c++;
            	indices[c] = (y-1)*xval;
            	c++;
         */
        
        
        //int [] indices = {12,8,13,9,14,10,15,11,11,8,8,4,9,5,10,6,11,7,7,4,4,0,5,1,6,2,7,3};
        
        int c = 0;
        // counts from the top(yval = yval)
        for (int y = yval-1; y>0; y--){
        	// left (xval=0) to the bottom right
            for (int x = 0; x<xval; x++){
            	// always puts 2 values into the indices where one is under another
            	indices[c] = ((y)*xval)+x;
            	// increases the counter for the index of indices everytime we set a value
            	c++;   
            	indices[c] = (((y-1)*xval))+x;
            	c++;
            /*
             *  if we're on the right border we have to got back to the left
             *  but without drawing a new triangle, so we put the right border value again
             *  into the indices and bind those two to the next left border value, so that
             *  we only draw a line and can begin new next time again with the left border value
             *  sorry if its hard to understand xD
             *  example X=3, Y=3 ->6,3,7,4,8,5, 5(!) , 3(!) , 3,0,4,1,5,2
             */
            if ((y-1>0) && (indices[c-1] == (y*xval-1))){
            	indices[c] = (y*xval-1);
            	c++;
            	indices[c] = (y-1)*xval;
            	c++;
            }
            	
            // returns the path the triangle strip goes
            //for (int i=0;i<maxInd;i++){
            //		System.out.println(indices[i]);
            //}
            }
        }
        
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		
        // setting buffers
        
        
                
        //creating the geometry element with the parameters of mesh
        geo = new Geometry("wireframeGeometry", mesh);
        if ( mesh.getId() != 1){
        	mesh.setId(1);
        }
        Material mat1 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Green);
        mat1.getAdditionalRenderState().setWireframe(true);
        geo.setMaterial(mat1);

        mesh.setMode(Mesh.Mode.TriangleStrip);
        mesh.updateBound();
        mesh.setStatic();

        Main.getInstance().getRootNode().attachChild(geo);
		
	}


	
}