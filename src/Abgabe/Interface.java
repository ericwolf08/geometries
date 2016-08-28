package Abgabe;


import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/*
 * @author Eric Wolf
 */
public class Interface extends SimpleApplication implements InputListener {
	
	public Mesh mesh;
	public Geometry geo;
	
	// default values
	public int scale = 1;
	public int xval = 10;
	public int yval = 10;
	
	public float radius = 1f;
	
	public float radius_outer = 1f;
	public float radius_inner = 0.25f;
	
	public float p = 2;
	public float q = 3;
	
	
	
	@Override
	public void simpleInitApp() {
		mesh = new Mesh(); 
		Rectangle.create(xval, yval, scale, mesh, geo);
		//Sphere.create(xval, yval, radius, mesh, geo);
		//Torus.create(xval, yval, radius_inner, radius_outer, mesh, geo);
		//PQTorus.create(xval, yval, radius_inner, radius_outer, p, q, mesh, geo);
			
	       
		// standard values sucks... makes the camera faster
	    flyCam.setMoveSpeed(100); 
	    
	    guiNode.detachAllChildren();
	    getHudText();
	    this.initKeys();
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		// so that only the manual hud is visible
		super.simpleUpdate(tpf);	
		guiNode.detachAllChildren();
		getHudText();
	}
	
	private void getHudText() {
		BitmapText hudText = new BitmapText(guiFont, false);
		
		hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
		hudText.setColor(ColorRGBA.White); // font color
		
		String text = "Eric";
		// checks what geometries is actual
		if (mesh.getId() == 1) {
			text += "\nRectangle";
		}
		else if (mesh.getId() == 2) {
				text += "\nSphere";
		}
		else if (mesh.getId() == 3) {
			text += "\nTorus";
		}
		else if (mesh.getId() == 4) {
			text += "\nPQTorus";
		}
		text += "\nswitch_Rectangle: " + "[F5]";
		text += "\nswitch_Sphere: " + "[F6]";
		text += "\nswitch_Torus: " + "[F7]";
		text += "\nswitch_PQTorus: " + "[F8]";
		text += "\nX: " + xval + " [1+,2-]";
		text += "\nY: " + yval + " [3+,4-]";
		// show stuff depending on geometry
		if (mesh.getId() == 1) {
			text += "\nScale: " + scale + " [5+,6-]";
		}
		if (mesh.getId() == 2) {
			text += "\nRadius: " + radius + " [7+,8-]";
		}
		if (mesh.getId() == 3) {
			text += "\nRadius_inner: " + radius_inner + " [I+,O-]";
			text += "\nRadius_outer: " + radius_outer + " [K+,L-]";
		}
		if (mesh.getId() == 4) {
			text += "\nRadius_inner: " + radius_inner + " [I+,O-]";
			text += "\nRadius_outer: " + radius_outer + " [K+,L-]";
			text += "\nP: " + p + " [F1+,F2-]";
			text += "\nQ: " + q + " [F3+,F4-]";
		}
		
		hudText.setText(text);	
		
		hudText.setLocalTranslation(5, hudText.getLineHeight()+250, 0); // position
		guiNode.attachChild(hudText);
	}
	


	private void initKeys() {
		// setting keys
		inputManager.addMapping("scale-up", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping("scale-down", new KeyTrigger(KeyInput.KEY_6));
		
		inputManager.addMapping("plus-x", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("minus-x", new KeyTrigger(KeyInput.KEY_2));
		
		inputManager.addMapping("plus-y", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("minus-y", new KeyTrigger(KeyInput.KEY_4));
		
		inputManager.addMapping("radius-plus", new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping("radius-minus", new KeyTrigger(KeyInput.KEY_8));
		
		inputManager.addMapping("radius_inner-plus", new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping("radius_inner-minus", new KeyTrigger(KeyInput.KEY_O));
		
		inputManager.addMapping("radius_outer-plus", new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping("radius_outer-minus", new KeyTrigger(KeyInput.KEY_L));
		
		inputManager.addMapping("P-plus", new KeyTrigger(KeyInput.KEY_F1));
		inputManager.addMapping("P-minus", new KeyTrigger(KeyInput.KEY_F2));
		
		inputManager.addMapping("Q-plus", new KeyTrigger(KeyInput.KEY_F3));
		inputManager.addMapping("Q-minus", new KeyTrigger(KeyInput.KEY_F4));
		
		inputManager.addMapping("switch Rectangle", new KeyTrigger(KeyInput.KEY_F5));
		inputManager.addMapping("switch Sphere", new KeyTrigger(KeyInput.KEY_F6));
		inputManager.addMapping("switch Torus", new KeyTrigger(KeyInput.KEY_F7));
		inputManager.addMapping("switch PQTorus", new KeyTrigger(KeyInput.KEY_F8));
		
		
		
		// Add the names to the action listener.
		inputManager.addListener(actionListener, new String[] { "scale-up", "scale-down", "plus-x", "minus-x", "plus-y", "minus-y",
				"Rectangle", "Sphere", "Torus", "PQTorus", "radius-plus", "radius-minus", "radius_inner-plus", "radius_inner-minus",
				"radius_outer-plus", "radius_outer-minus", "P-plus", "P-minus", "Q-plus","Q-minus", "switch Rectangle",
				"switch Sphere", "switch Torus", "switch PQTorus"});
	}
	
		
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			
			// Switch geometry
			if (name.equals("switch Rectangle") && !keyPressed && mesh.getId() != 1) {
				rootNode.detachAllChildren();
				if (xval <= 2){
					xval = 2;
				}
				if (yval <= 2){
					yval = 2;
				}
				mesh = new Mesh();
				Rectangle.create(xval, yval, scale, mesh, geo);				
			}
			
			if (name.equals("switch Sphere") && !keyPressed && mesh.getId() != 2) {
				rootNode.detachAllChildren();
				if (xval <= 3){
					xval = 3;
				}
				if (yval <= 3){
					yval = 3;
				}
				mesh = new Mesh();
				Sphere.create(xval, yval, radius, mesh, geo);
			}
			
			if (name.equals("switch Torus") && !keyPressed && mesh.getId() != 3) {
				rootNode.detachAllChildren();
				if (xval <= 5){
					xval = 5;
				}
				if (yval <= 5){
					yval = 5;
				}
				mesh = new Mesh();
				Torus.create(xval, yval, radius_inner, radius_outer, mesh, geo);			
			}
			if (name.equals("switch PQTorus") && !keyPressed && mesh.getId() != 4) {
				rootNode.detachAllChildren();
				if (xval <= 20){
					xval = 20;
				}
				if (yval <= 20){
					yval = 20;
				}
				mesh = new Mesh();
				PQTorus.create(xval, yval, radius_inner, radius_outer, p, q, mesh, geo);
			}
	
			
			// scale
			if (name.equals("scale-up") && !keyPressed && (mesh.getId() == 1)) {
				scale += 1;
				reload();				
			}
			if (name.equals("scale-down") && !keyPressed && (mesh.getId() == 1)) {
				// minimum value	
				if (scale <= 1){
					scale = 1;					
				}
				else {
					scale -= 1;
				}
				reload();
					
			}
				
			// X
			if (name.equals("plus-x") && !keyPressed) {
				xval += 1;
				reload();
					
			}
			if (name.equals("minus-x") && !keyPressed) {
				xval -= 1;
				// minimum values for every object	
				if (xval <= 2 && mesh.getId() == 1) {
					xval = 2;
				}
				else if (xval <= 3 && mesh.getId() == 2) {
					xval = 3;
				}
				else if (xval <= 5 && mesh.getId() == 3) {
					xval = 5;
				}
				else if (xval <= 20 && mesh.getId() == 4) {
					xval = 20;
				}
				reload();
			}
				
			// Y
			if (name.equals("plus-y") && !keyPressed) {
				yval += 1;
				reload();
			}
			if (name.equals("minus-y") && !keyPressed) {
				yval -= 1;
				// minimum values for every object	
				if (yval <= 2 && mesh.getId() == 1) {
					yval = 2;
				}
				else if (yval <= 3 && mesh.getId() == 2) {
					yval = 3;
				}
				else if (yval <= 5 && mesh.getId() == 3) {
					yval = 5;
				}
				else if (yval <= 20 && mesh.getId() == 4) {
					yval = 20;
				}
				reload();
			}
			
			// Radius_inner
			if (name.equals("radius_inner-plus") && !keyPressed && ((mesh.getId() == 3) || (mesh.getId() == 4))) {
				radius_inner += 0.1;
				reload();
			}
			if (name.equals("radius_inner-minus") && !keyPressed && ((mesh.getId() == 3) || (mesh.getId() == 4))) {
				radius_inner -= 0.1f;
			if (radius_inner <= 0.25f) {
				radius_inner = 0.25f;
			}
			reload();
			}
			
			// Raidus_outer			
			if (name.equals("radius_outer-minus") && !keyPressed && ((mesh.getId() == 3) || (mesh.getId() == 4))) {
					radius_outer -= 0.1f;
				if (radius_outer <= 1f) {
					radius_outer = 1f;
				}
				reload();

			}
			if (name.equals("radius_outer-plus") && !keyPressed && ((mesh.getId() == 3) || (mesh.getId() == 4))) {
				radius_outer += 0.1;
				reload();
			}
			
				
			// Radius Sphere
			if (name.equals("radius-minus") && !keyPressed && (mesh.getId() == 2)) {
				radius -= 0.1f;
				if (radius <= 0.1f) {
					radius = 0.1f;
				}
				reload();

			}
			if (name.equals("radius-plus") && !keyPressed && (mesh.getId() == 2)) {
				radius += 0.1;
				reload();
			}
			
			// P
			if (name.equals("P-minus") && !keyPressed && (mesh.getId() == 4)) {
				p -= 1;
				if (p <= 2f) {
					p = 2f;
				}
				reload();
			}
			if (name.equals("P-plus") && !keyPressed && (mesh.getId() == 4)) {
				p += 1;
				reload();
			}
			
			//Q
			if (name.equals("Q-minus") && !keyPressed && (mesh.getId() == 4)) {
				q -= 1;
				if (q <= 2f) {
					q = 2f;
				}
				reload();

			}
			if (name.equals("Q-plus") && !keyPressed && (mesh.getId() == 4)) {
				q += 1;
				reload();
			}
		}
		
	};
	
	
	private void reload() {
		// creates geometry new if data is changed like scale or radius..
		if (mesh.getId() == 1) {
			Rectangle.create(xval, yval, scale, mesh, geo);
		}
		else if (mesh.getId() == 2) {
			Sphere.create(xval, yval, radius, mesh, geo);
		}
		else if (mesh.getId() == 3) {
			Torus.create(xval, yval, radius_inner, radius_outer, mesh, geo);
		}
		else if (mesh.getId() == 4) {
			PQTorus.create(xval, yval, radius_inner, radius_outer, p, q, mesh, geo);
		}

	}
	
}
