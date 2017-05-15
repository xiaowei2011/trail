package com.zhiying.trail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhiying.pojo.Point;
import com.zhiying.pojo.Trail;
import com.zhiying.util.DistanceUtil;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class DataFormatter {

	//数据所在文件夹
	private String path = "F:\\trails"; 
	//日期
	private String day = "2016-02-26";
	
	private final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//清理无效数据
	public void dataClean(){
		dataClean(path);
	}
	public void dataClean(String dirName){
		DataClean dataClean = new DataCleanImpl();
		dataClean.dropInvalidCar(dirName);
		dataClean.dropInvalidGPS(dirName);
		dataClean.dropInvalidTime(dirName);
		dataClean.dropDuplicatedData(dirName);
	}
	public List<Trail> getTrails(){
		return getTrails(path);
	}

	public List<Trail> getTrails(String path) {
		List<List<String>> files = listFiles(path);
		List<Trail> trails = new ArrayList<Trail>();
		int id = 1;
		int dayInc = 0; 
		for(List<String> dir : files){
			for (String name : dir) {
				BufferedReader br = null;
				try {
					Trail trail = new Trail();
					br = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
					trail.setId(id++);
					trail.setName(name.substring(name.lastIndexOf('\\') + 1, name.length() - 4));
					String data;
					List<Point> plist = new ArrayList<Point>();
					double distance = 0;
					while ((data = br.readLine()) != null) {
						Point p = new Point();
						String[] sb = data.split(",");
						Date time = sdf.parse(day + " " + sb[0]);
						time.setTime(time.getTime() + dayInc * 24 * 60 * 60 * 1000);
						p.setTime(time);
						p.setLng(Double.valueOf(sb[1]));
						p.setLat(Double.valueOf(sb[2]));
						if(!plist.isEmpty()){
							Point last = plist.get(plist.size()-1);
							distance += DistanceUtil.getDistance(p, last);
						}
						plist.add(p);
					}
					trail.setDistance(distance);
					trail.setPoints(plist);
					trails.add(trail);
				} catch (Exception e) {
					System.out.println("get trail exception");
					e.printStackTrace();
				}finally {
					if(br != null){
						try {
							br.close();
						} catch (IOException e) {
							System.out.println("close br exception");
						}
					}
				}
			}
			dayInc++;
		}
		return trails;
	}

	public List<List<String>> listFiles(String dirName){
		List<List<String>> files = new ArrayList<>();
		List<String> fs = new ArrayList<>();
		File file = new File(dirName);
		File[] fileArray = file.listFiles();
		if(fileArray == null){
			System.out.println(dirName + "下没有文件");
			return files;
		}
		for(File f : fileArray){
			if(f.isDirectory()){
				files.add(getFiles(f.getAbsolutePath()));
			}else{
				fs.add(f.getAbsolutePath());
			}
		}
		files.add(fs);
		return files;
	}
	
	/**
	 * 获取文件夹下所有的轨迹文件
	 * @param filePath
	 * @return fileName
	 */
	public static List<String> getFiles(String filePath) {
		List<String> filelist = new ArrayList<>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		if(files == null){
			System.out.println(filePath + "下没有文件");
			return filelist;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				List<String> fs = getFiles(file.getAbsolutePath());
				filelist.addAll(fs);
			} else {
				filelist.add(file.getAbsolutePath());
			}
		}
		return filelist;
	}

	public DataFormatter(String path, String day) {
		super();
		this.path = path;
		this.day = day;
	}

	public DataFormatter() {
		super();
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
