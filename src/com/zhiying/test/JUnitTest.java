package com.zhiying.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.zhiying.pojo.Point;
import com.zhiying.pojo.Trail;
import com.zhiying.trail.DataClean;
import com.zhiying.trail.DataCleanImpl;
import com.zhiying.trail.DataFormatter;
import com.zhiying.util.DistanceUtil;
import com.zhiying.util.TrailUtil;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class JUnitTest {
	
	@Test
	public void test12(){
		String dirName = "F:\\trails";
		DataFormatter dataFormatter = new DataFormatter(dirName, "2017-05-05");
		//清理无效数据
		dataFormatter.dataClean();
		//获取路线轨迹
		List<Trail> trails = dataFormatter.getTrails();
		TrailUtil trailUtil = new TrailUtil(2500, 3);
		//计算相似度(时间+空间)，返回相似度矩阵
		List<List<Double>> matrix = trailUtil.getSimMatrix(trails);
		for(List<Double> row : matrix){
			System.out.println(row);
		}
		//输出相似度
		trailUtil.outputMatrix("F:\\sim.txt", trails, matrix);
		System.out.println("处理完成");
	}

	@Test
	public void test11(){
		String dirName = "F:\\trails";
		DataFormatter dataFormatter = new DataFormatter(dirName, "2017-05-05");
		dataFormatter.dataClean();
		List<Trail> trails = dataFormatter.getTrails();
		Trail a = trails.get(0);
		Trail b = trails.get(1);
		show(trails);
		System.out.println("0");
		show(a.getMbbs());
		System.out.println("1");
		show(b.getMbbs());
		System.out.println(a.getSpaceSimWith(b));
		System.out.println(a.getTimeSimWith(b));
	}
	
	@Test
	public void test10(){
		String dirName = "F:\\trails";
		DataFormatter dataFormatter = new DataFormatter(dirName, "2017-05-05");
		dataFormatter.dataClean();
		List<Trail> trails = dataFormatter.getTrails();
		int count = 0;
		for(Trail trail : trails){
			if(trail.getMbbs() != null && !trail.getMbbs().isEmpty()){
				System.out.println("mbbs");
				count++;
				show(trail.getMbbs());
			}
		}
		System.out.println(count);
		System.out.println((double)count/trails.size());
	}
	
	@Test
	public void test9(){
		DataClean dataClean = new DataCleanImpl();
		String dirName = "F:\\trails";
//		System.out.println(DataFormatter.getFiles(dirName).size());
//		dataClean.dropInvalidCar(dirName);
//		System.out.println(DataFormatter.getFiles(dirName).size());
//		dataClean.dropInvalidGPS(dirName);
//		System.out.println(DataFormatter.getFiles(dirName).size());
//		dataClean.dropInvalidTime(dirName);
//		System.out.println(DataFormatter.getFiles(dirName).size());
		dataClean.dropDuplicatedData(dirName);
	}
	
	@Test
	public void test8(){
		HashMap<String, String> map = new HashMap<>();
		map.put("aa", "nn");
		DataFormatter dataFormatter = new DataFormatter();
		List<Trail> trails = dataFormatter.getTrails();
		System.out.println(trails.size());
		TrailUtil trailUtil = new TrailUtil();
		trailUtil.setDisMax(2500);
		List<List<Double>> matrix = trailUtil.getSimMatrix(trails);
		int count = 0;
		double total = 0;
		for(List<Double> row : matrix){
			for(double sim : row){
				if(sim > 0){
					count++;
				}
				total++;
			}
		}
		System.out.println(count);
		System.out.println(count/total);
	}
	
	@Test
	public void test7(){
		DataFormatter dataFormatter = new DataFormatter();
		List<Trail> trails = dataFormatter.getTrails();
//		TrailUtil trailUtil = new TrailUtil();
		int count = 0;
		double total = 0;
		for(Trail a : trails){
			List<Point> points = a.getPoints();
			for(int i = 1; i < points.size(); i++){
				double dis = DistanceUtil.getDistance(points.get(i - 1), points.get(i));
				count++;
				total += dis;
				System.out.println(dis);
			}
		}
		System.out.println(count);
		System.out.println(total/count);
	}
	
	@Test
	public void test6(){
		DataFormatter dataFormatter = new DataFormatter();
		List<Trail> trails = dataFormatter.getTrails();
//		TrailUtil trailUtil = new TrailUtil();
		int count = 0;
		double total = 0;
		for(Trail a : trails){
			for(Trail b : trails){
				for(Point pa : a.getPoints()){
					for(Point pb : b.getPoints()){
						double dis = DistanceUtil.getDistance(pa, pb);
						count++;
						total += dis;
						System.out.println(dis);
					}
				}
			}
		}
		System.out.println(trails.get(1).getSpaceSimWith(trails.get(2)));
		System.out.println(total/count);
	}
	
	@Test
	public void test5(){
		DataFormatter dataFormatter = new DataFormatter();
		List<Trail> trails = dataFormatter.getTrails();
		show(trails);
		TrailUtil trailUtil = new TrailUtil();
		List<List<Double>> matrix = trailUtil.getSimMatrix(trails);
		trailUtil.outputMatrix("F:\\sim.txt", trails, matrix);
	}
	
	@Test
	public void test4(){
		DataClean dataClean = new DataCleanImpl();
		String dirName = "E:\\trails";
		//System.out.println(dataClean.dropInvalidCar(dirName));
		//System.out.println(dataClean.dropInvalidTime(dirName));
		//System.out.println(dataClean.dropInvalidGPS(dirName));
		System.out.println(dataClean.dropDuplicatedData(dirName));
	}
	
	@Test
	public void test3() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("HH:m:ss");
		dateFormat.setLenient(false);
		System.out.println(dateFormat.parse("12: 20:32"));
	}
	
	@Test
	public void test2(){
		DataFormatter dataFormatter = new DataFormatter();
		//dataFormatter.setPath("E:\\trails");
		List<Trail> trails = dataFormatter.getTrails();
		Trail trail = trails.get(2);
		TrailUtil trailUtil = new TrailUtil();
		trailUtil.generateMbbByTrail(trail);
		System.out.println(trail);
		System.out.print("Mbbs:");
		show(trail.getMbbs());
	}
	@Test
	public void test1(){
		DataFormatter dataFormatter = new DataFormatter();
		List<Trail> trails = dataFormatter.getTrails();
		show(trails);
		int count = 0;
		System.out.println("Mbbs:");
		for(Trail trail : trails){
			if(trail.getMbbs() != null && !trail.getMbbs().isEmpty()){	
				System.out.print("mbbs: ");
				show(trail.getMbbs());
				count++;
			}
		}
		System.out.println("数量："+count);
	}
	
	private <E> void show(Iterable<E> it){
		if(it==null || !it.iterator().hasNext()){
			System.out.println(it);
			return;
		}
		for(E e : it){		
			System.out.println(e);
		}
	}
}
