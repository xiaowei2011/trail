package com.zhiying.pojo;
import java.util.Date;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class Mbb {
	//最大经度
	private double maxLng;
	//最小经度
	private double minLng;
	//最小纬度
	private double maxLat;
	//最大纬度
	private double minLat;
	//最小时间
	private Date minTime;
	//最大时间
	private Date maxTime;
	
	/**
	 * 获得对象与参数对象的空间重叠
	 * @param mbb
	 * @return 是否重叠
	 */
	public int getGpsSimWith(Mbb mbb){
		double minLng1 = mbb.minLng;
		double maxLng1 = mbb.maxLng;
		double minLat1 = mbb.minLat;
		double maxLat1 = mbb.maxLat;
		if(maxLng < minLng1 || minLng > maxLng1 || maxLat < minLat1 || minLat > maxLat1){
			return 0;
		}
		return 1;
	}
	
	/**
	 * 获得对象与参数对象之间的时间重叠
	 * @param mbb
	 * @return 重叠时间
	 */
	public double getTimeSimWith(Mbb mbb){
		if(getGpsSimWith(mbb) != 1){
			return 0;
		}
		long max1 = maxTime.getTime();
		long min1 = minTime.getTime();
		long max2 = mbb.maxTime.getTime();
		long min2 = mbb.minTime.getTime();
		if(max1 < min2 || min1 > max2){
			return 0;
		}
		double duration;
		long low = Math.max(min2, min1);
		if(min1 <= max2 && max2 <= max1){
			duration = max2 - low;
		}else{
			duration = max1 - low;
		}
		return duration;
	}
	
	public double getMaxLng() {
		return maxLng;
	}
	public void setMaxLng(double maxLng) {
		this.maxLng = maxLng;
	}
	public double getMinLng() {
		return minLng;
	}
	public void setMinLng(double minLng) {
		this.minLng = minLng;
	}
	public double getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}
	public double getMinLat() {
		return minLat;
	}
	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}
	public Date getMinTime() {
		return minTime;
	}
	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}
	public Date getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}
	
	@Override
	public String toString() {
		return "Mbb [maxLng=" + maxLng + ", minLng=" + minLng + ", maxLat=" + maxLat + ", minLat=" + minLat
				+ ", minTime=" + minTime + ", maxTime=" + maxTime + "]";
	}
}