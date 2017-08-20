
package BuffCOPYed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.jogamp.common.nio.Buffers;

public class Parser {

	private float ns[] = new float[1];
	private float ka[] = new float[4];
	private float kd[] = new float[4];
	private float ks[] = new float[4];

	private ArrayList<Float> Vertex = new ArrayList<Float>();
	private ArrayList<Float> VertexNormal = new ArrayList<Float>();
	private ArrayList<Float> VertexTexture = new ArrayList<Float>();
	private HashMap<Integer, VertexOb> outputVertices = new HashMap<>();
	private ArrayList<Integer> vertexIndices = new ArrayList<>();
	public ArrayList<String[]> materialArr;
	
	private int currentVertexIndex = 0;

	private String mtlFileName;
	public float toppoint;
	public float bottompoint;
	public float leftpoint;
	public float rightpoint;
	public float farpoint;
	public float nearpoint;
	
	
	private boolean hasTexture = false;
	private boolean hasNormals = false;

	private int TextureCounter = 0;

	public Material fetchMaterial(String file) {
	/*	int linecounter = 0;

		try {
			FileInputStream dataStream = new FileInputStream(file);
			InputStreamReader streamReader = new InputStreamReader(dataStream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);

			String newline;
			boolean firstpass = true;
			int mtlcounter = 0;

			while (((newline = bufferedReader.readLine()) != null)) {
				linecounter++;
				newline = newline.trim();
				if (newline.length() > 0) {
					if (newline.startsWith("new")) {
						if (firstpass) {
							firstpass = false;
						} else {
							Materials.add(matset);
							matset = new mtl();
						}
						String[] coordstext = new String[2];
						coordstext = newline.split("\\s+");
						matset.name = coordstext[1];
						matset.mtlnum = mtlcounter;
						mtlcounter++;
					} else if (newline.startsWith("Ka")) {
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1; i < coordstext.length; i++) {
							coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.Ka = coords;
					} else if (newline.startsWith("Ns")) { //specular weight,	ranges between 0 and 1000
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1; i < coordstext.length; i++) {
							coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.Ka = coords;
					} else if (newline.startsWith("Kd")) {
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1; i < coordstext.length; i++) {
							coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.Kd = coords;
					} else if (newline.startsWith("Ks")) {
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1; i < coordstext.length; i++) {
							coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.Ks = coords;
					} else if (newline.charAt(0) == 'd') { //透明度
						String[] coordstext = newline.split("\\s+");
						matset.d = Float.valueOf(coordstext[1]).floatValue();
					} else if (newline.startsWith("Tr")) { //透明度 Tr=1-d
						String[] coordstext = newline.split("\\s+");
						matset.d = Float.valueOf(coordstext[1]).floatValue();
					} else if (newline.startsWith("map_Kd ")) {

						TextureCounter++;

						mtexture glTxt = new mtexture();
						glTxt.ID = TextureCounter;
						glTxt.name = newline.trim().replaceFirst("map_Kd", "");
						loadtexture(glTxt.name.trim(), glTxt);

						Textures.add(glTxt);
					}
				}
			}

			while (line != null) {

				String[] splitedLine = line.split("\\s+");

				switch (splitedLine[0]) {
				case "Ns":
					ns[0] = Float.parseFloat(splitedLine[1]);
					break;

				case "Ka":
					putValues(splitedLine, ka);
					break;

				case "Kd":
					putValues(splitedLine, kd);
					break;

				case "Ks":
					putValues(splitedLine, ks);
					break;
				}

				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (Exception e) {
			System.err.println("file: Parser.java / line: 264 / exception: " + e.toString());
		}

		return new Material(ka, kd, ks, ns);*/
	
		ArrayList<String> lines = readLines(file);

		for (String line : lines) {

			String[] splitedLine = line.split("\\s+");

			switch (splitedLine[0]) {
			case "Ns":
				ns[0] = Float.parseFloat(splitedLine[1]);
				break;

			case "Ka":
				putValues(splitedLine, ka);
				break;

			case "Kd":
				putValues(splitedLine, kd);
				break;

			case "Ks":
				putValues(splitedLine, ks);
				break;
			}
		}

		return new Material(ka, kd, ks, ns);
	}
	private ArrayList<String> readLines(String objFilePath) {
		ArrayList<String> lines = new ArrayList<>();
		String dataSource = objFilePath;
		try {
			FileInputStream dataStream = new FileInputStream(dataSource);

			InputStreamReader streamReader = new InputStreamReader(dataStream);

			BufferedReader bufferedReader = new BufferedReader(streamReader);

			String line = bufferedReader.readLine();

			while (line != null) {
				lines.add(line);
				line = bufferedReader.readLine();
			}

			bufferedReader.close();

		} catch (Exception e) {
			System.err.println("file: Parser.java / line: 264 / exception: " + e.toString());
		}
		return lines;
	}
	private void putValues(String[] source, float target[]) {
		for (int i = 1; i < source.length; ++i) {
			target[i - 1] = Float.parseFloat(source[i]);
		}
		target[3] = 1.0f;
	}

	public ObjContent fetchObj(String objFileName) {
		iniArray();
		int facecounter = 0;
		try {
			FileInputStream dataStream = new FileInputStream(objFileName);
			InputStreamReader streamReader = new InputStreamReader(dataStream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			boolean firstpass = true;
			String newline;
			while ((newline = bufferedReader.readLine()) != null) {
				if (newline.length() > 0) {
					newline = newline.trim();
					if (newline.startsWith("v ")) {// LOADS VERTEX COORDINATES
						float coords[] = new float[4];
						newline = newline.substring(2, newline.length());
						StringTokenizer st = new StringTokenizer(newline, " ");
						for (int i = 0; st.hasMoreTokens(); i++) {
							coords[i] = Float.parseFloat(st.nextToken());
							Vertex.add(coords[i]);
						}
						if (firstpass) {
							rightpoint = coords[0];
							leftpoint = coords[0];
							toppoint = coords[1];
							bottompoint = coords[1];
							nearpoint = coords[2];
							farpoint = coords[2];
							firstpass = false;
						}
						if (coords[0] > rightpoint)
							rightpoint = coords[0];
						if (coords[0] < leftpoint)
							leftpoint = coords[0];
						if (coords[1] > toppoint)
							toppoint = coords[1];
						if (coords[1] < bottompoint)
							bottompoint = coords[1];
						if (coords[2] > nearpoint)
							nearpoint = coords[2];
						if (coords[2] < farpoint)
							farpoint = coords[2];
					} else if (newline.startsWith("vt")) {// LOADS VERTEX TEXTURE COORDINATES
						float coords[] = new float[4];
						newline = newline.substring(3, newline.length());
						StringTokenizer st = new StringTokenizer(newline, " ");
						for (int i = 0; st.hasMoreTokens(); i++) {
							coords[i] = Float.parseFloat(st.nextToken());
							VertexTexture.add(coords[i]);
						}
					} else if (newline.startsWith("vn")) {// LOADS VERTEX NORMALS COORDINATES
						float coords[] = new float[4];
						newline = newline.substring(3, newline.length());
						StringTokenizer st = new StringTokenizer(newline, " ");
						for (int i = 0; st.hasMoreTokens(); i++) {
							coords[i] = Float.parseFloat(st.nextToken());
							VertexNormal.add(coords[i]);
						}
					} else if (newline.startsWith("f ")) {// LOADS FACES COORDINATES
						facecounter++;
						newline = newline.substring(2, newline.length());
						StringTokenizer st = new StringTokenizer(newline, " ");
						int count = st.countTokens();
						int v[] = new int[count];
						int vt[] = new int[count];
						int vn[] = new int[count];
						for (int i = 0; i < count; i++) {
							char chars[] = st.nextToken().toCharArray();
							StringBuffer sb = new StringBuffer();
							char lastchar = 'x';
							for (int k = 0; k < chars.length; k++) {
								if (chars[k] == '/' && lastchar == '/') { /* f v1//vn1 v2//vn2 v3//vn3 这种场景？//中间插入0？*/
									sb.append('0');
								}
								lastchar = chars[k];
								sb.append(lastchar);
							}
							StringTokenizer st2 = new StringTokenizer(sb.toString(), "/");
							int num = st2.countTokens();
							v[i] = Integer.parseInt(st2.nextToken());
							if (num > 1) {
								vt[i] = Integer.parseInt(st2.nextToken());
							} else {
								vt[i] = 0;
							}
							if (num > 2) {
								vn[i] = Integer.parseInt(st2.nextToken());
							} else {
								vn[i] = 0;
							}
							VertexOb vertexOb = new VertexOb();

							vertexOb.setX(Vertex.get((v[i] - 1) * 3));
							vertexOb.setY(Vertex.get(((v[i] - 1) * 3) + 1));
							vertexOb.setZ(Vertex.get(((v[i] - 1) * 3) + 2));

							if (VertexTexture.size() > 0) {
								vertexOb.setTextureU(VertexTexture.get((vt[i] - 1) * 2));
								vertexOb.setTextureV(VertexTexture.get(((vt[i] - 1) * 2) + 1));
							}

							if (VertexNormal.size() > 0) {
								vertexOb.setNormalX(VertexNormal.get((vn[i] - 1) * 3));
								vertexOb.setNormalY(VertexNormal.get(((vn[i] - 1) * 3) + 1));
								vertexOb.setNormalZ(VertexNormal.get(((vn[i] - 1) * 3) + 2));
							}
							if (outputVertices.keySet().contains(vertexOb)) {
								vertexOb.setIndex(currentVertexIndex);
								vertexIndices.add(currentVertexIndex);
							} else {
								vertexOb.setIndex(currentVertexIndex);
								outputVertices.put(currentVertexIndex, vertexOb);
								vertexIndices.add(currentVertexIndex);
								++currentVertexIndex;
							}
						}
					} else if (newline.startsWith("mtllib")) {// LOADS MATERIALS
						mtlFileName = newline.split("\\s+")[1];
//						 if (mtlFileName != null){
//							 loadmaterials();
//						 }
					} else if (newline.startsWith("usemtl")) {// USES MATELIALS
//						 String[] coords = new String[2];
//						 String[] coordstext = new String[3];
//						 coordstext = newline.split("\\s+");
//						 coords[0] = coordstext[1];
//						 coords[1] = facecounter + "";
//						 materialArr.add(coords);
					}
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println("Failed to read file: " + e.toString());
		} catch (NumberFormatException e) {
			System.out.println("Malformed OBJ file: " + e.toString() + "\r \r" + e.getMessage());
		}
		return genObjContent();
	}

	/*private void loadmaterials() {
		FileReader frm;
		String refm = "E:/Objs/"+mtlFileName;

		try {
			frm = new FileReader(refm);
			BufferedReader brm = new BufferedReader(frm);
			materials = new MtlLoader(brm, "E:/Objs/");
			frm.close();
		} catch (IOException e) {
			System.out.println("Could not open mtl file: " + refm);
			materials = null;
		}
	}*/
	private void iniArray() {
		Vertex = new ArrayList<>();
		VertexNormal = new ArrayList<>();
		VertexTexture = new ArrayList<>();
		vertexIndices = new ArrayList<>();
		outputVertices = new HashMap<>();
		currentVertexIndex = 0;
	}

	private ObjContent genObjContent() {
		ArrayList<Float> coordinates = new ArrayList<>();
		ArrayList<Float> normals = new ArrayList<>();
		ArrayList<Float> textures = new ArrayList<>();

		for (int index : vertexIndices) {
			VertexOb tempVertex = outputVertices.get(index);
			
			coordinates.add(tempVertex.getX());
			coordinates.add(tempVertex.getY());
			coordinates.add(tempVertex.getZ());

			textures.add(tempVertex.getTextureU());
			textures.add(tempVertex.getTextureV());

			normals.add(tempVertex.getNormalX());
			normals.add(tempVertex.getNormalY());
			normals.add(tempVertex.getNormalZ());
		}

		ObjContent result = new ObjContent();

		
		result.setMtlFileName(mtlFileName);

		result.setVertexBufferData(Buffers.newDirectFloatBuffer(convertToFloatArray(coordinates)));
		result.setTextureBufferData(Buffers.newDirectFloatBuffer(convertToFloatArray(textures)));
		result.setNormalBufferData(Buffers.newDirectFloatBuffer(convertToFloatArray(normals)));
		result.setElementBufferData(Buffers.newDirectIntBuffer(convertToIntArray(vertexIndices)));

		return result;
	}

	private int[] convertToIntArray(ArrayList<Integer> container) {
		int arraySize = container.size();
		int[] intArray = new int[arraySize];

		int i = 0;
		for (int val : container) {
			intArray[i] = val;
			++i;
		}
		container.clear();//soway
		return intArray;
	}

	private float[] convertToFloatArray(ArrayList<Float> container) {
		int arraySize = container.size();
		float[] floatArray = new float[arraySize];

		int i = 0;
		for (float val : container) {
			floatArray[i] = val;
			++i;
		}
		container.clear();//soway
		return floatArray;
	}
}
