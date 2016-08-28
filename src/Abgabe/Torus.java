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
public class Torus extends Interface{

	public static void create(int xval, int yval, float radius_inner, float radius_outer, Mesh mesh, Geometry geo) {
		// setting minimum values
		if (xval < 5){
			xval = 5;
		}
		if (yval < 5){
			yval = 5;
		}
		if (radius_inner <= 0.25f) {
			radius_inner = 0.25f;
		}
		if (radius_outer <= 1f) {
			radius_outer = 1f;
		}
		// setting angle of the subdivions
		float grad_x = 360.0f / xval; 
		float grad_y = 360.0f / yval;

		//number of vertices
		Vector3f[] vertices = new Vector3f[xval * yval];
		
		
		int currVv = 0;
		// over xsubdiv
		for (int x = 0; x < xval; x++){
			// over ysubdiv
			for (int y = 0; y < yval; y++){
				// from wikipedia				
				float xc = (float) ((radius_outer + radius_inner * Math.cos(Math.toRadians(grad_y * y)))*Math.cos(Math.toRadians(grad_x * x))); 
				float yc = (float) ((radius_outer + radius_inner * Math.cos(Math.toRadians(grad_y * y)))*Math.sin(Math.toRadians(grad_x * x)));
				float zc = (float) (radius_inner * Math.sin(Math.toRadians(grad_y * y)));

				vertices[currVv] = new Vector3f(xc, yc, zc);					
				currVv ++;
			}
		}

		Vector3f[] normals = new Vector3f[xval * yval];
		
		
		int currVt = 0;
		// over xsubdiv
		for (int x = 0; x < xval; x++){
			// for every ysubdiv value setting these values of point p of the inner radius
			for (int y = 0; y < yval; y++){
				float tx = (float) (radius_outer * Math.cos(Math.toRadians(grad_x * x)));
				float ty = (float) (radius_outer * Math.sin(Math.toRadians(grad_x * x)));
				float tz = (float) 0;
				
				normals[currVt] = new Vector3f(tx, ty, tz);
				currVt ++;
			}
		}

		// setting the normals for each vertex by subtracting the related normal
		for (int i = 0; i < vertices.length; i++) {
			// calculates a vector from the origin of the inner radius to the vertex and then normalized is the normal of the vertex
			// source: analina
			normals[i] = vertices[i].subtract(normals[i]).normalize();
		}
		
		//for (int i = 0; i < normals.length; i++) {
		//	System.out.println("vertex " + vertices[i] + "normal " + normals[i]);
		//}
		
		
		int currVi = 0;
		// calculates needed number of indices
		int maxInd = 2 * yval * xval + 2 * xval;
		//System.out.println(maxInd);
        int [] indices = new int[maxInd];
        // int [] indices = {0,5,1,6,2,7,3,8,4,9,0,5,5,10,6,11,7,12,8,13,9,14,5,10,
        // 10,15,11,16,12,17,13,18,14,19,10,15,15,20,16,21,17,22,18,23,19,24,15,20,20,0,21,1,22,2,23,3,24,4,20,0};
        
      
        // some procedure like in rectangle
        // puts first the value of the top ring then the value of the bottom ring and goes like the circle        
		for (int x = 0; x < xval-1; x++){
			for (int y = 0; y < yval; y++){
				indices[currVi] = x*yval+y;
				//System.out.println(indices[currVi]);
				currVi++;
				indices[currVi] = (x+1)*yval+y;
				//System.out.println(indices[currVi]);
				currVi++;
			}
			
			// puts the last value two more times to indices to change the ring
			indices[currVi] = x*yval;
	        //System.out.println(indices[currVi]);
	        currVi ++;
	        indices[currVi] = ((x+1)*yval);
	        //System.out.println(indices[currVi]);
	        currVi ++;
			
        }
		
		// last values must be set by ourself, because it hast to fit with the first ring (0,1,2,3,4) if x = 5, y = 5
		for(int y = 0; y < yval; y++){               
                indices[currVi] = yval *(xval-1) + y;
                currVi ++;
                indices[currVi] = y;
                currVi ++;
               
        }
		
		// adds the first two values again to end the last triangle in a circle
		indices[currVi] = yval * (xval-1);
        currVi ++;
        indices[currVi] = 0;
        currVi ++;
       
        

        
        //for (int i = 0; i < indices.length; i++){
        //	System.out.print(indices[i] + ",");
        //}
  
	
		// setting buffers
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		mesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
		mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
                
        //creating the geometry element with the parameters of mesh
        geo = new Geometry("wireframeGeometry", mesh);
        if ( mesh.getId() != 3){
        	mesh.setId(3);
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
