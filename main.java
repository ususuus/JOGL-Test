package BuffCOPYed;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.sun.javafx.geom.Vec3f;

import javafx.scene.input.KeyCode;

public class main extends GLJPanel implements GLEventListener {

	private static int width;
	private static int height;
	private FPSAnimator animator;
	private static float angleX = 0;
	private GOL chairModel = null;
	private GraphicObject x;
	private float fScaleRate=1.0f;
	private int mouseBtn=0;
	private int mouseX=0,mouseY=0;
	private Vec3f transf=new Vec3f(0,0,-1);
	private Vec3f rotatef=new Vec3f(0,0,0);
	private float mouseMoveStep=0.1f;
	private float mouseMoveRate=10f;
	private float mouseMovedX=0f;
	private float mouseMovedY=0f;
	
	private Vec3f rotatefOLD=new Vec3f(0,0,0);
	private GL2 gl;
	
	public main() {
		setFocusable(true);
		addGLEventListener(this);
		animator = new FPSAnimator(this, 60, false);
		animator.start();
		width = height = 800;
		initialActions();
	}

	

	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = GLU.createGLU();
		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(0, 2, 10, 0, 0, 0, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glLoadIdentity();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		

		gl.glTranslatef(transf.x, transf.y, -1);

		gl.glScalef(0.005f*fScaleRate, 0.005f*fScaleRate, 0.005f*fScaleRate);

		gl.glRotatef(rotatef.x, 0f, 1f, 0f);
		gl.glRotatef(rotatef.y, 1f, 0f, 0f);
		gl.glRotatef(rotatef.z, 0f, 0f, 1f);
		drawAsixes(gl);
		if (angleX > 359)
			angleX = 0;
		chairModel.golDraw(x);

		gl.glFlush();
	}

	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	public void init(GLAutoDrawable drawable) {
		
		gl = drawable.getGL().getGL2();

		// ----- Your OpenGL initialization code here -----
		gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest               

		gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL2.GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting        
		if (false == loadModels(gl)) {
			System.exit(1);
		}

		setLight(gl);
		GLU glu = GLU.createGLU();
		glu.gluPerspective(1, (double) getWidth() / getHeight(), 0.3, 50);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	private void setLight(GL2 gl) {

		gl.glEnable(GL2.GL_LIGHTING);

		float SHINE_ALL_DIRECTIONS = 1;
		float[] lightPos = { -30, 30, 30, SHINE_ALL_DIRECTIONS };
		float[] lightColorAmbient = { 0.02f, 0.02f, 0.02f, 1f };
		float[] lightColorSpecular = { 0.9f, 0.9f, 0.9f, 1f };

		// Set light parameters.
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorSpecular, 0);
		gl.glEnable(GL2.GL_LIGHT1);

	}

	private Boolean loadModels(GL2 gl) {

		chairModel=new GOL(gl, "E:/Objs/", "E:/Objs/", "E:/Objs/");
		x=chairModel.golLoad("cb.obj", "cb.mtl");
		
		if(x==null){
			System.out.println("x is null");
			System.exit(-1);
		}else{
			System.out.println(x);
		}
		if (chairModel == null) {
			return false;
		}
		return true;
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl = drawable.getGL().getGL2(); // obtiene un contexto grafico OpenGL 2
		GLU glu = new GLU();
		width = this.getWidth();
		height = this.getHeight();

		if (height == 0) {
			height = 1; // prevent divide by zero
		}

		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix

		glu.gluPerspective(45.0f, aspect, 0.1, 10.0); // fovy, aspect, zNear, zFar
		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

	}
	private void initialActions() {
		addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar()=='f' || arg0.getKeyChar()=='F'){
					fScaleRate=1.0f;
					transf.x=0f;
					transf.y=0f;
					rotatef.x=0f;
					rotatef.y=0f;
					rotatef.z=0f;
				}
			}
			
			public void keyReleased(KeyEvent arg0) {
				
			}
			
			public void keyPressed(KeyEvent arg0) {
//				gl.glPushMatrix();
//				gl.glLoadIdentity();
				if(arg0.getKeyCode()==KeyEvent.VK_UP){
					transf.y+=0.1f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
					transf.y-=0.1f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_LEFT){
					transf.x-=0.1f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_RIGHT){
					transf.x+=0.1f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_D){
					rotatef.x+=5f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_A){
					rotatef.x-=5f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_W){
					rotatef.y-=5f;
				}else if(arg0.getKeyCode()==KeyEvent.VK_S){
					rotatef.y+=5f;
				}
//				gl.glPopMatrix();
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent arg0) {
				
			}
			
			public void mouseDragged(MouseEvent arg0) {
				mouseMovedX=mouseMoveStep*(arg0.getX()-mouseX)/mouseMoveRate;
				mouseMovedY=mouseMoveStep*(arg0.getY()-mouseY)/mouseMoveRate;
				if(mouseBtn==3){
					transf.x+=mouseMovedX;
					transf.y-=mouseMovedY;
				}else if(mouseBtn==1){
					
					rotatef.x+=50*mouseMovedX;
					rotatef.y+=50*mouseMovedY;
				}
				mouseX=arg0.getX();
				mouseY=arg0.getY();
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation()>0){
					fScaleRate*=0.8f;
				}else{
					fScaleRate*=1.2f;
				}
			}
		});
		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				mouseBtn=0;
				mouseX=0;
				mouseY=0;
			}
			
			public void mousePressed(MouseEvent arg0) {
				mouseBtn=arg0.getButton();
				mouseX=arg0.getX();
				mouseY=arg0.getY();
			}
			
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
	}
	
	public void drawAsixes(GL2 gl){
		 gl.glColor3f(1f,0f,0f);
		 gl.glBegin(GL2.GL_LINES);
		 gl.glVertex3f( -100f, 0.0f, 0.0f );
		 gl.glVertex3f( 100f, 0.0f, 0.0f );
		 gl.glEnd();
		 gl.glColor3f(0f,1f,0f);
		 gl.glBegin(GL2.GL_LINES);
		 gl.glVertex3f( 0f, 100.0f, 0.0f );
		 gl.glVertex3f( 0f, -100.0f, 0.0f );
		 gl.glEnd();
		 gl.glColor3f(0f,0f,1f);
		 gl.glBegin(GL2.GL_LINES);
		 gl.glVertex3f( 0f, 0.0f, 100.0f );
		 gl.glVertex3f( 0f, 0.0f, -100.0f );
		 gl.glEnd();
	}
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.getContentPane().add(new main());
		window.setSize(width, height);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
