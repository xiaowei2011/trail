package com.zhiying.trail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.zhiying.pojo.Point;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class DataCleanImpl implements DataClean{
	
	private int minCarAmount = 3;
	
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private Validate timeValidate = new TimeValidate();
	private Validate gpsValidate = new GPSValidate();
	
	//删除目录下少于三个文件的文件夹（一个文件代表一条轨迹）
	@Override
	public boolean dropInvalidCar(String dirName) {
		try {
			File root = new File(dirName);
			File[] files = root.listFiles();
			if (files == null) {
				System.out.println("没有数据需要处理");
				return true;
			}
			boolean state = true;
			for (File file : files) {
				if (file.isDirectory()) {
					List<String> fileNames = DataFormatter.getFiles(file.getAbsolutePath());
					if (fileNames.size() < minCarAmount) {
						state = state && dropFile(file);
					}
				}
			}
			return state;
		} catch (Exception e) {
			System.out.println("dropInvalidCar exception");
			return false;
		}
	}
	
	//定义时间范围为00:00:00-23:59:59为有效时间，删除包含无效的时间的文件
	@Override
	public boolean dropInvalidTime(String dirName) {
		return dropInvalid(dirName, timeValidate);
	}
	
	//定义GPS数据为116±1°E,40±1°N为有效值，因为采集的数据为北京范围的出租车的轨迹信息，删除包含无效GPS的文件
	@Override
	public boolean dropInvalidGPS(String dirName) {
		return dropInvalid(dirName, gpsValidate);
	}
	
	//删除文件中完全重复的点
	@Override
	public boolean dropDuplicatedData(String dirName) {
		try {
			File root = new File(dirName);
			List<String> fileNames = DataFormatter.getFiles(root.getAbsolutePath());
			BufferedReader reader = null;
			Set<Point> points = new LinkedHashSet<>();
			boolean flag = false;
			for(String name : fileNames){
				StringBuilder sb = null;
				try{
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
					String data;
					sb= new StringBuilder();
					while((data = reader.readLine())!=null){
						String[] s = data.split(",");
						Point point = new Point();
						point.setTime(dateFormat.parse(s[0]));
						point.setLng(Double.valueOf(s[1]));
						point.setLat(Double.valueOf(s[2]));
						if(points.contains(point)){
							flag = true;
						}else{	
							sb.append(data + "\r\n");
							points.add(point);
						}
					}
					sb.delete(sb.length()-2, sb.length());
				}finally{
					if(reader != null){		
						reader.close();
					}
				}
				if(flag){
					BufferedWriter writer = null;
					try{
						writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)));
						writer.write(sb.toString());
					}finally{
						if(writer != null){	
							writer.close();
						}
					}
				}
				points.clear();
			}
			return true;
		}catch (Exception e) {
			System.out.println("dropDuplicatedData exception");
			return false;
		}
	}
	
	private boolean dropFile(File file){
		if(file.isFile()){
			return file.delete();
		}else{
			File[] files = file.listFiles();
			boolean state = true;
			if(files != null){
				for(File f : files){
					state = state && dropFile(f);
				}
			}
			return file.delete() && state;
		}
	}
	public boolean dropInvalid(String dirName, Validate validate){
		try {
			File root = new File(dirName);
			List<String> fileNames = DataFormatter.getFiles(root.getAbsolutePath());
			List<String> invalidFiles = new ArrayList<>();
			BufferedReader reader = null;
			for(String name : fileNames){
				try{			
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
					String data;
					while((data = reader.readLine())!=null){
						String[] sb = data.split(",");
						if(!validate.validate(sb)){
							invalidFiles.add(name);
						}
					}
				}finally{
					if(reader != null){
						reader.close();
					}
				}
			}
			File file = null;
			for(String name : invalidFiles){
				file = new File(name);
				file.delete();
			}
			return true;
		} catch (Exception e) {
			System.out.println("dropInvalidTimeOrGPS exception");
			return false;
		} 
	}

	public int getMinCarAmount() {
		return minCarAmount;
	}

	public void setMinCarAmount(int minCarAmount) {
		this.minCarAmount = minCarAmount;
	}
	
	private interface Validate{
		boolean validate(String[] sb);
	}
	
	private class TimeValidate implements Validate{
		@Override
		public boolean validate(String[] sb) {
			try{
				dateFormat.setLenient(false);
				dateFormat.parse(sb[0]);
			}catch(Exception e){
				return false;
			}
			return true;
		}
		
	}

	private class GPSValidate implements Validate{
		@Override
		public boolean validate(String[] sb) {
			try{
				double lng = Double.valueOf(sb[1]);
				double lat = Double.valueOf(sb[2]);
				if(lng - 116 < -1 || lng - 116 > 1 || lat - 40 < -1 || lat - 40 > 1){
					return false;
				}
				return true;
			}catch(Exception e){
				return false;
			}
		}
		
	}

}




