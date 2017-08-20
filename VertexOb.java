
package BuffCOPYed;

public class VertexOb {
  private float x;
  private float y;
  private float z;

  private float textureU;
  private float textureV;

  private float normalX;
  private float normalY;
  private float normalZ;

  private int index;

  public VertexOb(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public VertexOb() {
  }

  public VertexOb add(VertexOb vector) {
    x += vector.getX();
    y += vector.getY();
    z += vector.getZ();

    return this;
  }

  @Override
  public String toString() {
    return "Vertex [positionX=" + x + ", positionY=" + y + ", positionZ=" + z + ", textureU=" + textureU + ", textureV=" + textureV
      + ", normalX=" + normalX + ", normalY=" + normalY + ", normalZ=" + normalZ + ", index=" + index + "]";
  }

  public VertexOb clone() {
    return new VertexOb(x, y, z);
  }
  
  // getters & setters
  public float getX() {
    return x;
  }

  public void setX(float positionX) {
    this.x = positionX;
  }

  public float getY() {
    return y;
  }

  public void setY(float positionY) {
    this.y = positionY;
  }

  public float getZ() {
    return z;
  }

  public void setZ(float positionZ) {
    this.z = positionZ;
  }

  public float getTextureU() {
    return textureU;
  }

  public void setTextureU(float textureU) {
    this.textureU = textureU;
  }

  public float getTextureV() {
    return textureV;
  }

  public void setTextureV(float textureV) {
    this.textureV = textureV;
  }

  public float getNormalX() {
    return normalX;
  }

  public void setNormalX(float normalX) {
    this.normalX = normalX;
  }

  public float getNormalY() {
    return normalY;
  }

  public void setNormalY(float normalY) {
    this.normalY = normalY;
  }

  public float getNormalZ() {
    return normalZ;
  }

  public void setNormalZ(float normalZ) {
    this.normalZ = normalZ;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}
