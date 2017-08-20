
package BuffCOPYed;

import java.io.File;
import java.nio.IntBuffer;
import com.jogamp.opengl.GL2;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_NEAREST;
import static com.jogamp.opengl.GL.GL_REPEAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_NORMAL_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_TEXTURE_COORD_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_VERTEX_ARRAY;

public class GOL {

	private GL2 gl;
	private Parser parser = new Parser();
	private String textureFileLocation;
	private String objFileLocation;
	private String mtlFileLocation;

	public GOL(GL2 gl, String objFileLocation, String mtlFileLocation, String textureFileLocation) {
		this.gl = gl;
		this.objFileLocation = objFileLocation;
		this.mtlFileLocation = mtlFileLocation;
		this.textureFileLocation = textureFileLocation;
		setGLTextureFlags();
	}

	public GraphicObject golLoad(String objFileName, String textureFileName) {
		return buildComponent(objFileName, textureFileName);
	}

	public void golDraw(GraphicObject component) {
		if (component.getMaterial() != null) {
			component.getMaterial().bind(gl);
		}
//		component.getTexture().bind(gl);
		gl.glBindVertexArray(component.getVertexArrayObjectID());
		gl.glDrawElements(GL_TRIANGLES, component.getTotalElements(), GL_UNSIGNED_INT, 0);
	}


	private GraphicObject buildComponent(String objFileName, String textureFIleName) {
		ObjContent objContent = parser.fetchObj(objFileLocation + objFileName);
	
		IntBuffer vaoIdBuffer = Buffers.newDirectIntBuffer(1);
		gl.glGenVertexArrays(1, vaoIdBuffer);

		IntBuffer vboIdBuffer = Buffers.newDirectIntBuffer(4);
		gl.glGenBuffers(4, vboIdBuffer);

		objContent.setVaoID(vaoIdBuffer.get(0));

		objContent.setVertexBufferID(vboIdBuffer.get(0));
		objContent.setTextureBufferID(vboIdBuffer.get(1));
		objContent.setNormalBufferID(vboIdBuffer.get(2));
		objContent.setElementBufferID(vboIdBuffer.get(3));
		
		gl.glBindVertexArray(objContent.getVaoID());

		gl.glBindBuffer(GL_ARRAY_BUFFER, objContent.getVertexBufferID());
		gl.glBufferData(GL_ARRAY_BUFFER, objContent.computeVertexBufferSize(), objContent.getVertexBufferData(), GL_STATIC_DRAW);
		gl.glVertexPointer(3, GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL_VERTEX_ARRAY);

		gl.glBindBuffer(GL_ARRAY_BUFFER, objContent.getTextureBufferID());
		gl.glBufferData(GL_ARRAY_BUFFER, objContent.computeTextureBufferSize(), objContent.getTextureBufferData(), GL_STATIC_DRAW);
		gl.glTexCoordPointer(2, GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		gl.glBindBuffer(GL_ARRAY_BUFFER, objContent.getNormalBufferID());
		gl.glBufferData(GL_ARRAY_BUFFER, objContent.computeNormalBufferSize(), objContent.getNormalBufferData(), GL_STATIC_DRAW);
		gl.glNormalPointer(GL_FLOAT, 0, 0);
		gl.glEnableClientState(GL_NORMAL_ARRAY);

		gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, objContent.getElementBufferID());
		gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, objContent.computeElementBufferSize(), objContent.getElementBufferData(),GL_STATIC_DRAW);

		Material mtlContent = null;
		if (objContent.getMtlFileName() != null) {
			mtlContent = parser.fetchMaterial(mtlFileLocation + objContent.getMtlFileName());
		}

//		Texture texture = readTexture(textureFIleName);

		return new GraphicObject(objContent.getVaoID(), objContent.computeTotalElements(), null,mtlContent);
	}


	public Texture readTexture(String textureFileName) {
		try {
			Texture resultTexture = TextureIO.newTexture(new File(textureFileLocation + textureFileName), true);
			resultTexture.enable(gl);
			return resultTexture;
		} catch (Exception e) {
			System.err.println("file: GOL.java / line: 126 / exception: " + e.toString());
			return null;
		}
	}

	public void setGLTextureFlags() {
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	}
}
