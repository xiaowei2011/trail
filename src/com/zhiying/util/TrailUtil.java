package com.zhiying.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.zhiying.pojo.Mbb;
import com.zhiying.pojo.Point;
import com.zhiying.pojo.Trail;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class TrailUtil {
	
	//有趣点距离阈值
	private double disMax = 2000;
	//有趣点长度阈值
	private int lenMin = 3;
	
	//获取空间相似度矩阵
	public List<List<Double>> getSimMatrix(List<Trail> trails){
		if(trails == null || trails.isEmpty()){
			return null;
		}
		int len =trails.size();
		List<List<Double>> matrix = new ArrayList<>(len);
		Double[][] array = new Double[len][len];
		for(int i = 0; i < len; i++){
			for(int j = i; j < len; j++){
				Trail a = trails.get(i);
				Trail b = trails.get(j);
				double sim = a.getSpaceSimWith(b) + a.getTimeSimWith(b);
				array[i][j] = array[j][i] = sim;
			}
		}
		for(int i = 0; i < len; i++){
			matrix.add(Arrays.asList(array[i]));
		}
		return matrix;
	}
	
	public void outputMatrix(String fileName, List<Trail> trails, List<List<Double>> matrix){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
			int len = matrix.size();
			DecimalFormat df1 = new DecimalFormat("00000000");
			writer.write("trail_id\t");
			for(int i = 0; i < trails.size(); i++){
				String name = df1.format(trails.get(i).getId());
				writer.write(name);
				if(i < trails.size() - 1){
					writer.write("\t");
				}
			}
			writer.newLine();
			DecimalFormat df2 = new DecimalFormat("0.000000");
			for(int i = 0; i < len; i++){
				List<Double> row = matrix.get(i);
				String name = df1.format(trails.get(i).getId());
				writer.write(name + "\t");
				for(int j = 0; j < len; j++){
					double sim = row.get(j);
					writer.write(df2.format(sim));
					if(j < len - 1){
						writer.write("\t");
					}
				}
				if(i < len - 1){
					writer.newLine();
				}
			}
		} catch (Exception e) {
			System.out.println("outputMatrix exception");
		}finally {
			try {
				if(writer != null){
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void generateMbbByTrail(Trail trail){
		generateMbbByTrail(trail, disMax, lenMin);
	}
	
	//生成有趣点MBB
	public void generateMbbByTrail(Trail trail, double disMax, int lenMin){
		List<Point> points = trail.getPoints();
		if(points == null || points.isEmpty()){
			return;
		}
		List<Mbb> mbbs = new ArrayList<>();
		List<Point> subPoints = new ArrayList<>();
		Point start = points.get(0);
		int len = points.size();
		if(len == 1 && lenMin == 1){
			subPoints.add(start);
			Mbb mbb = getMbbByPoint(subPoints);
			mbbs.add(mbb);
			trail.setMbbs(mbbs);
			return;
		}
		Point last = points.get(0);
		for(int i = 1; i < len; i++){
			Point point = points.get(i);
			double dis = DistanceUtil.getDistance(point, last);
			if(dis <= disMax){
				if(subPoints.isEmpty()){
					subPoints.add(start);
				}
				subPoints.add(point);
				if( i == points.size() - 1 && subPoints.size() >= lenMin){
					Mbb mbb = getMbbByPoint(subPoints);
					mbbs.add(mbb);
				}
			}else{
				if(subPoints.size() >= lenMin){
					Mbb mbb = getMbbByPoint(subPoints);
					mbbs.add(mbb);
				}
				subPoints.clear();
				start = point;
			}
			last = point;
		}
		if(!mbbs.isEmpty()){	
			trail.setMbbs(mbbs);
		}
	}
	
	private Mbb getMbbByPoint(List<Point> points){
		if(points == null || points.isEmpty()){
			return null;
		}
		Point point = points.get(0);
		double minLng = point.getLng();
		double maxLng = point.getLng();
		double minLat = point.getLat();
		double maxLat = point.getLat();
		Date minTime = point.getTime();
		Date maxTime = point.getTime();
		int len = points.size();
		for(int i = 1; i < len; i++){
			point = points.get(i);
			if(point.getLng() < minLng){
				minLng = point.getLng();
			}
			if(point.getLng() > maxLng){
				maxLng = point.getLng();
			}
			if(point.getLat() < minLat){
				minLat = point.getLat();
			}
			if(point.getLat() > maxLat){
				maxLat = point.getLat();
			}
			if(point.getTime().compareTo(minTime) < 0){
				minTime = point.getTime();
			}
			if(point.getTime().compareTo(maxTime) > 0){
				maxTime = point.getTime();
			}
		}
		Mbb mbb = new Mbb();
		mbb.setMinLng(minLng);
		mbb.setMaxLng(maxLng);
		mbb.setMinLat(minLat);
		mbb.setMaxLat(maxLat);
		mbb.setMinTime(minTime);
		mbb.setMaxTime(maxTime);
		return mbb;
	}

	public TrailUtil(double disMax, int lenMin) {
		this.disMax = disMax;
		this.lenMin = lenMin;
	}
	
	public TrailUtil(){}
	
	public double getDisMax() {
		return disMax;
	}

	public void setDisMax(double disMax) {
		this.disMax = disMax;
	}

	public int getLenMin() {
		return lenMin;
	}

	public void setLenMin(int lenMin) {
		this.lenMin = lenMin;
	}

}
