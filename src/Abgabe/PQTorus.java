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
public class PQTorus extends Interface{

	public static void create(int xval, int yval, float radius_inner, float radius_outer, float p, float q, Mesh mesh, Geometry geo)  {
		// setting minimum values
		if (xval <= 20){
			xval = 20;
		}
		if (yval <= 20){
			yval = 20;
		}
		if (radius_inner <= 0.25f) {
			radius_inner = 0.25f;
		}
		if (radius_outer <= 1f) {
			radius_outer = 1f;
		}
		if (p <= 2f) {
			p = 2f;
		}
		if (q <= 2f) {
			q = 2f;
		}
		// setting angle of the subdivions
		float grad_x = 360.0f / xval; 
		float grad_y = 360.0f / yval;

		//number of vertices
		Vector3f[] vertices = new Vector3f[xval * yval];
		//number of normals
		Vector3f[] normals = new Vector3f[xval * yval];
		
		//source http://www.blackpawn.com/texts/pqtorus/		
		int currVv = 0;
		// vertices need to be calculated in another way than the normal torus' vertices
		for (int x = 0; x < xval; x++){
			for(int y = 0; y < yval; y++){

				float radius = (float) (radius_outer * 0.5f * (2 + Math.sin(Math.toRadians(q * grad_x * x))));
				
				// think the are just some rotations
				float xouterring = (float) ( radius * Math.cos(Math.toRadians(p * grad_x * x)));
				float youterring = (float) ( radius * Math.sin(Math.toRadians(p * grad_x * x)));
				float zouterring = (float) ( Math.cos(Math.toRadians(q * grad_x * x)));
				
				Vector3f vec0 = new Vector3f(xouterring, youterring, zouterring);
				
				float xouterring1 = (float) ( radius * Math.cos(Math.toRadians(p * grad_x * (x + 0.1f))));
				float youterring1 = (float) ( radius * Math.sin(Math.toRadians(p * grad_x * (x + 0.1f))));
				float zouterring1 = (float) ( Math.cos(Math.toRadians(q * grad_x * (x + 0.1f))));
				
				Vector3f vec1 = new Vector3f(xouterring1, youterring1, zouterring1);
				
				// and calculations of new vectors
				Vector3f T = vec1.subtract(vec0);
                Vector3f N = vec1.add(vec0);
                Vector3f B = T.cross(N);
                
                // cross-product
                N = B.cross(T);
                
                // then normalized
                N = N.normalize();
                B = B.normalize();
                
                // and some more rotation
                float xold = (radius_inner * (float) Math.cos(Math.toRadians(grad_y * y))); 
                float yold = (radius_inner * (float) Math.sin(Math.toRadians(grad_y * y))); 
                
                // and calculation of the point on the outer radius
                Vector3f newpoint = N.mult(xold).add(B.mult(yold));
                
                float xinner = newpoint.x;
                float yinner = newpoint.y;
                float zinner = newpoint.z;
                                               
                float xcoor = xouterring + xinner;
                float ycoor = youterring + yinner;
                float zcoor = zouterring + zinner;
                
                // sets the value of the vertex              
                vertices[currVv]= new Vector3f(xcoor,ycoor,zcoor);

                // adding the normals the same time
                // same procedure like in the torus class
                // but vec0 (point on the outer radius) must be calculated every time
                normals[currVv] = newpoint.normalize();
                
                currVv ++;
                
			}
		}
		



		
		for (int i = 0; i < normals.length; i++) {
			System.out.println(i + ": vertex " + vertices[i] + "normal " + normals[i]);
		}
		
		
		int currVi = 0;
		// calculates needed number of indices
		int maxInd = (2 * yval * (xval-1)) + 2 * (xval-1) + yval * 2 + 2 + 1;
		//System.out.println(maxInd);
        int [] indices = new int[maxInd];
        // int [] indices = {0,5,1,6,2,7,3,8,4,9,0,5,5,10,6,11,7,12,8,13,9,14,5,10,

        // needs to change the rotation because it has shown the inner side of the torus... don't know why
        indices[0] = 0;
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
        if ( mesh.getId() != 4){
        	mesh.setId(4);
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

