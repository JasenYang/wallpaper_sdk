package cs.hku.wallpaper_sdk.stl_opengl.stl;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class STLObject implements Serializable {
	//三角面片法向量数据
	FloatBuffer normalBuffer;
	//三角面片法顶点数据
	FloatBuffer vertexBuffer;
	//三角面片数
	int triangleCount;
	float maxX;
	float maxY;
	float maxZ;
	float minX;
	float minY;
	float minZ;
}
