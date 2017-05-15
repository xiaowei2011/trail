package com.zhiying.pojo;

import java.util.List;
import com.zhiying.util.TrailUtil;

/**
 * @author lizhiying
 * @date 2017/5/4
 * @version 2.0
 */
public class Trail {
	
	private int id;
	
	private String name;
	
	private double distance;
	//轨迹包含的点(按时间排序)
	private List<Point> points;
	//轨迹包含的最小包围盒
	private List<Mbb> mbbs;
	
	//计算空间相似度
	public double getSpaceSimWith(Trail trail){
		if(this.equals(trail) || points.equals(trail.points)){
			return 1;
		}
		if(mbbs == null || mbbs.size() < 1 || trail.mbbs == null || trail.mbbs.size() < 1){
			return 0;
		}
		double overlap = 0;
		List<Mbb> a, b;
		if(mbbs.size() > trail.mbbs.size()){
			a = mbbs;
			b = trail.mbbs;
		}else{
			a = trail.mbbs;
			b = mbbs;
		}
		for(Mbb ma : a){
			for(Mbb mb : b){
				if(ma.getGpsSimWith(mb) == 1){
					overlap++;
					break;
				}
			}
		}
		return overlap / a.size();
	}
	
	//计算时间相似度
	public double getTimeSimWith(Trail trail){
		if(this.equals(trail) || points.equals(trail.points)){
			return 1;
		}
		if(mbbs == null || mbbs.size() < 1 || trail.mbbs == null || trail.mbbs.size() < 1){
			return 0;
		}
		double overlap = 0;
		double duration = 0;
		for(Mbb ma : mbbs){
			for(Mbb mb : trail.mbbs){
				double sim = ma.getTimeSimWith(mb);
				overlap += sim;
				if(sim > 0){			
					long da = ma.getMaxTime().getTime() - ma.getMinTime().getTime();
					long db = mb.getMaxTime().getTime() - mb.getMinTime().getTime();
					duration += Math.max(da, db);
				}
			}
		}
		return overlap == 0 ? 0 : overlap / duration;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
		new TrailUtil().generateMbbByTrail(this);
	}
	public List<Mbb> getMbbs() {
		return mbbs;
	}
	public void setMbbs(List<Mbb> mbbs) {
		this.mbbs = mbbs;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trail other = (Trail) obj;
		if (Double.doubleToLongBits(distance) != Double.doubleToLongBits(other.distance))
			return false;
		if (id != other.id)
			return false;
		if (mbbs == null) {
			if (other.mbbs != null)
				return false;
		} else if (!mbbs.equals(other.mbbs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Trail [id=" + id + ", name=" + name + ", distance=" + distance + ", points=" + points + ", mbbs=" + mbbs
				+ "]";
	}
}