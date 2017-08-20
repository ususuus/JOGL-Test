
package BuffCOPYed;

import com.jogamp.opengl.GL2;

import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;

public class Material {

	private float ambient[];
	private float diffuse[];
	private float specular[];
	private float shine[];

	public Material(float[] ambient, float[] diffuse, float[] specular, float[] shine) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shine = shine;
	}

	public void decreaseAlpha(float fadeUnit) {
		ambient[3] -= fadeUnit;
		diffuse[3] -= fadeUnit;
		specular[3] -= fadeUnit;
	}

	public Material clone() {
		return new Material(ambient.clone(), diffuse.clone(), specular.clone(), shine.clone());
	}

	public void bind(GL2 gl) {
		gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);
		gl.glMaterialfv(GL_FRONT, GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL_FRONT, GL_SHININESS, shine, 0);
	}

	// getters and setters
	public float[] getAmbient() {
		return ambient;
	}

	public void setAmbient(float[] ambient) {
		this.ambient = ambient;
	}

	public float[] getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(float[] diffuse) {
		this.diffuse = diffuse;
	}

	public float[] getSpecular() {
		return specular;
	}

	public void setSpecular(float[] specular) {
		this.specular = specular;
	}

	public float[] getShine() {
		return shine;
	}

	public void setShine(float[] shine) {
		this.shine = shine;
	}
}
