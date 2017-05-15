package com.zhiying.trail;
/**
 * 数据处理接口，定义所有对原始数据进行的处理条件及方式
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public interface DataClean {
	
	/**
	 * 清理GPS定位数据过于稀少的出租车
	 * 定义全天GPS数据少于等于三个的车辆数据为无效数据
	 * @param dirName 需要处理的数据文件夹名称
	 * @return true 清理成功 false 清理失败
	 */
	public boolean dropInvalidCar(String dirName);
	
	/**
	 * 清理时间无效的数据
	 * 定义时间范围 00:00:00-23:59:59为有效时间，
	 * @param dirName 需要处理的数据文件夹名称
	 * @return true 清理成功 false 清理失败
	 */
	public boolean dropInvalidTime(String dirName);
	
	/**
	 * 清理GPS定位无效数据
	 * 定义GPS数据 116±1°E,40±1°N为有效值，
	 * @param dirName 需要处理的数据文件夹名称
	 * @return true 清理成功 false 清理失败
	 */
	public boolean dropInvalidGPS(String dirName);
	
	/**
	 * 清除重复数据
	 * 采集的GPS数据可能因GPS接收器的问题，会出现完全重复（包括时间和Car）的数据，所以要把这些无效的数据完全删除
	 * @param dirName 需要处理的数据文件夹名称
	 * @return true 清理成功 false 清理失败
	 */
	public boolean dropDuplicatedData(String dirName);
	
}