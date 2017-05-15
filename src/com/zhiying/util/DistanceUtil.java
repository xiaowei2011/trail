package com.zhiying.util;

import com.zhiying.pojo.Point;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class DistanceUtil {
	
	//地球半径
	public static final double RADIUS = 6378137;
	
	/**
	 * 角度转化为弧度
	 * @param 角度
	 * @return 弧度
	 */
	public static double radian(double angle){
		return angle * Math.PI / 180.0;
	}
	
	public static double getDistance(Point p1, Point p2){
		double radLat1 = radian(p1.getLat());
        double radLat2 = radian(p2.getLat());
        double a = radLat1 - radLat2;
        double b = radian(p1.getLng()) - radian(p2.getLng());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
         Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
	}
}
