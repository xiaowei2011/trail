package com.zhiying.trail;

import java.util.List;
import com.zhiying.pojo.Trail;
import com.zhiying.util.TrailUtil;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class Main {

	public static void main(String[] args) {
		String dirName = "F:\\trails";
		DataFormatter dataFormatter = new DataFormatter(dirName, "2017-05-05");
		//清理无效数据
		dataFormatter.dataClean();
		//获取路线轨迹
		List<Trail> trails = dataFormatter.getTrails();
		TrailUtil trailUtil = new TrailUtil(2500, 3);
		//计算相似度(时间+空间)，返回相似度矩阵
		List<List<Double>> matrix = trailUtil.getSimMatrix(trails);
		//输出相似度
		trailUtil.outputMatrix("F:\\sim.txt", trails, matrix);
		System.out.println("处理完成");
	}

}
