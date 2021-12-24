package com.interview.welld.points.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.interview.welld.points.data.Line;
import com.interview.welld.points.data.Point;
import com.interview.welld.points.data.Vector;

@RestController
public class PointsController{
	
	List<Point> points = new ArrayList<Point>();
	
	@RequestMapping(method = RequestMethod.POST, path = "/space")
	public Point createPoint(@RequestBody Point point) {
		if(point.getX()==null || point.getY()==null)
		{
			throw new IllegalArgumentException("Point's coordinates cannot be empty!");
		}
		
		if(!points.contains(point))
		{
			points.add(point);
		}
		return point;
	} 

	@RequestMapping(method = RequestMethod.GET, path = "/space")
	public List<Point> getAllPoints() {
		return this.points;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/space")
	public List<Point> deleteAllPoints() {
		this.points = new ArrayList<Point>();
		return this.points;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/line/{n}")
	public List<Line> getLine(@PathVariable("n") int n) {
		
		if(n<2) {
			throw new IllegalArgumentException("Cannot determine lines because they are infinite!");
		}

		List<Line> lines = new ArrayList<Line>();
		
		//Not enough points
		if(points.size()<n)
		{
			return lines;
		}
		
		List<Vector> vectors = new ArrayList<Vector>();
		//Find all normalized vectors
		for(int i=0;i<points.size();i++) {
			for(int j=i+1;j<points.size();j++) {
				Vector v = new Vector();
				v.setI(points.get(j).getX()-points.get(i).getX());
				v.setJ(points.get(j).getY()-points.get(i).getY());
				v.normalize();

				//Opposite vectors represent the same line
				if(v.getI()<0)
				{
					v.setI(-v.getI());
					v.setJ(-v.getJ());
				} else if(v.getI()==0 && v.getJ()<0) {
					v.setJ(-v.getJ());
				}
				
				if(!vectors.contains(v))
				{
					vectors.add(v);
				}
			}
		}
		
		//For each vector pick a point and count how many of the other points belong to its line
		//if a point matches remove it in order to avoid test on parallel lines (same vector)
		for(Vector v:vectors)
		{
			List<Point> vectorPoints = points.stream().collect(Collectors.toList());

			Map<Double, List<Point>> parallelLines = new HashMap<Double, List<Point>>();
			Map<Double, List<Point>> verticalLines = new HashMap<Double, List<Point>>();
			
			while(vectorPoints.size()>0)
			{
				Point basePoint = vectorPoints.remove(0);
				if(v.getI()==0) {
					Double x = basePoint.getX();
			
					if(!verticalLines.containsKey(x))
					{
						verticalLines.put(x, new ArrayList<Point>());
					}
					
					List<Point> currentLine = verticalLines.get(x);
					currentLine.add(basePoint);
					verticalLines.put(x, currentLine);					
				} else {
					
					Double q = -Double.valueOf(Math.round(1000*(basePoint.getX()*(basePoint.getY()+v.getJ())-
							(basePoint.getX()+v.getI())*basePoint.getY())
						/v.getI()))/1000;
			
					if(!parallelLines.containsKey(q))
					{
						parallelLines.put(q, new ArrayList<Point>());
					}
					
					List<Point> currentLine = parallelLines.get(q);
					currentLine.add(basePoint);
					parallelLines.put(q, currentLine);
				}
			}
			
			for(Double q:parallelLines.keySet())
			{
				List<Point> parallelLine = parallelLines.get(q);
				Line line = new Line();
				line.setOffset(q);
				line.setPoints(parallelLine.stream().collect(Collectors.toSet()));
				line.setVector(v);
				line.setEquation("y="+(v.getJ()/v.getI())+"x+"+q);
				lines.add(line);
			}
			
			for(Double x:verticalLines.keySet())
			{
				List<Point> verticalLine = verticalLines.get(x);
				Line line = new Line();
				line.setOffset(null);
				line.setPoints(verticalLine.stream().collect(Collectors.toSet()));
				line.setVector(v);
				line.setEquation("x="+verticalLine.get(0).getX());
				lines.add(line);
			}
		}
		
		//Filter by line points >= n
		lines = lines.stream().filter(x->{return x.getPoints().size()>=n;}).collect(Collectors.toList());
		
		//Return lines
		return lines;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public RestError handleIllegalArgumentException(IllegalArgumentException iae) {
		return new RestError(HttpStatus.BAD_REQUEST.value(), iae.getMessage());
	}
}