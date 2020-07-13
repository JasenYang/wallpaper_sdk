package cs.hku.wallpaper_sdk.service;

import android.util.Log;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class Mat2Angle {
//	public static void main(String[] args) {
//		double[] mat = {0.8660252, -0.0, 0.5000003,0.5000003, 0.0, -0.8660252,0.0, 1.0, 0.0};
//		transform(mat);
//	}

	public static double[] toDouble(float[] v) {
		final double[] ans = new double[v.length];
		for (int k = 0; k < v.length; k++)
			ans[k] = (double) v[k];
		return (ans);
	}
	
	public static double[] transform(double[] mat) {

		Log.i("GGGGG", "transform: "+ Arrays.toString(mat));
		double b[][] = new double[3][3];
		int index = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				b[i][j] = mat[index++];
			}
		}
		
		//Ҫ���ֻ�����������ϵ����ת�����������ֻ�����ϵ�������ת����
		double a[][] = {{1, 0, 0},{0, 0, 1},{0, -1, 0}};
		
		RealMatrix p2w = new Array2DRowRealMatrix(b);
		RealMatrix w2p = new Array2DRowRealMatrix(a);
		
		RealMatrix rotateMat = w2p.multiply(p2w).transpose();
		//������ת��
		
		
		double temp[][] = rotateMat.getData();
		//System.out.println(rotateMat);
		//System.out.println(temp[0][0]);
		
		double sy = Math.sqrt(temp[0][0]*temp[0][0] + temp[1][0]*temp[1][0]);		
		Boolean sigular = sy < 1e-6;
		double x, y, z;
		if(!sigular) {
			x = Math.atan2(temp[2][1], temp[2][2]);
			y = Math.atan2(-temp[2][0], sy);
			z = Math.atan2(temp[1][0], temp[0][0]);
		}
		else {
			x = Math.atan2(-temp[1][2], temp[1][1]);
			y = Math.atan2(-temp[2][0], sy);
			z = 0;		
		}		
		double[] res = {Math.toDegrees(x) + 90, Math.toDegrees(y), Math.toDegrees(z)};
		Log.i("GGGGG", "transform: "+x + "::: " + y + ":::::" + z);
		return res;
	}
}
