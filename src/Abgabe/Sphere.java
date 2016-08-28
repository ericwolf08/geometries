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
public class Sphere extends Interface{


	public static void create(int xval, int yval, float radius, Mesh mesh, Geometry geo) {
		// setting minimum values
		if (xval < 3){
			xval = 3;
		}
		if (yval < 3){
			yval = 3;
		}
		if (radius <= 0.1f) {
			radius = 0.1f;
		}
		
		// setting angle of the subdivions
		float grad_x = 180.0f / xval; 
		float grad_y = 360.0f / yval;
		
		//origin of the sphere needed to calculate normals
		Vector3f origin = new Vector3f(0,0,0);
		//number of vertices
		Vector3f[] vertices = new Vector3f[(((xval - 1) * yval) + 2)];
		
		//coordinates of the polars
		vertices[0] = new Vector3f(0, radius, 0); 
		vertices[(((xval - 1) * yval) + 1)] = new Vector3f(0, -radius, 0); 
		
		int currVv = 1;
		// over xsubdiv
		for (int x = 0; x < xval-1; x++){
			// over ysubdiv
			for (int y = 0; y < yval; y++){
				// from wikipedia
				
				float xc = (float)(radius * (float)Math.sin(Math.toRadians(grad_x) * (x+1)) * (float)Math.sin(Math.toRadians(grad_y) * y) );
				float yc = (float)(radius * (float)Math.cos(Math.toRadians(grad_x) * (x + 1)));
				float zc = (float)(radius * (float)Math.sin(Math.toRadians(grad_x) * (x+1)) * (float)Math.cos(Math.toRadians(grad_y) * y) );
				vertices[currVv] = new Vector3f(xc, yc, zc);					
				currVv ++;
			}
		}
		//for (int i = 0; i < vertices.length; i++) {
		//	System.out.println(i + vertices[i].toString());
		//}
		
		Vector3f[] normals = new Vector3f[(((xval - 1) * yval) + 2)];
		
		// setting the normals for each vertex
		for (int i = 0; i < vertices.length; i++) {
			// calculates a vector from the origin to the vertex and then normalized is the normal of the vertex
			// source: analina
			normals[i] = vertices[i].subtract(origin).normalize();
		}
		
		// calculates needed number of indices
		int maxInd = 1 + (yval*2) + 1 + ( ( yval * 2) + 2) * (xval - 2) + 2 + (yval * 2)
;
		// System.out.println(maxInd);
        int [] indices = new int[maxInd];
        // int [] indices = {1,0,3,0,2,0,1,1,1,4,2,5,3,6,1,4,4,4,7,6,7,5,7,4};

        
        // always beginning the indices with 1
        indices[0] = 1;
        
        int currVi = 1;
        
        // values for the north pole
        for (int y = yval; y > 0; y--){
        	
        	indices[currVi] = 0;
        	// System.out.println(indices[currVi]);
        	currVi ++;
        	// counts round the first ring under the pole values always: 1,2,3....
        	indices[currVi] = y;
        	// System.out.println(indices[currVi]);
        	currVi ++;
        	
        }
        
        // take the indices one more time to jump from pole counting to rectangle counting
        indices[currVi] = 1;
        // System.out.println(indices[currVi]);
        currVi ++;
        
        // begin rectangle phase
        // puts first the value of the top ring then the value of the bottom ring and goes like the circle        
		for (int x = 0; x < xval-2; x++){
			for (int y = 1; y < yval+1; y++){
				indices[currVi] = x*yval +y;
				//System.out.println(indices[currVi]);
				currVi++;
				indices[currVi] = (x+1)*yval+y;
				//System.out.println(indices[currVi]);
				currVi++;
			}
			
			// puts the last value two more times to indices to change the ring
			indices[currVi] = x*yval+1;
	        //System.out.println(indices[currVi]);
	        currVi ++;
	        indices[currVi] = ((x+1)*yval+1);
	        //System.out.println(indices[currVi]);
	        currVi ++;
			
        }


		// two more values need to switch to the south pole calculation
		
		indices[currVi] = indices[currVi-1];
		//System.out.println(indices[currVi]);
        currVi ++;
        indices[currVi] = indices[currVi-1];
        //System.out.println(indices[currVi]);
        currVi ++;
        
        // south pole is like the north, always the value of the pole then one of the last ring
        for (int y = 1; y < yval+1; y++){
        	indices[currVi] = vertices.length -1;
        	//System.out.println(indices[currVi]);
        	currVi ++;
        	indices[currVi] = (vertices.length) -1 -y;
        	//System.out.println(indices[currVi]);
        	currVi ++;
        }
        
        //for (int i = 0; i < indices.length; i++){
        //	System.out.print(indices[i] + ",");
        //}
  
		

		// setting buffers
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		mesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
		mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
              
        //creating the geometry element with the parameters of mesh
        geo = new Geometry("wireframeGeometry", mesh);
        if ( mesh.getId() != 2){
        	mesh.setId(2);
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


