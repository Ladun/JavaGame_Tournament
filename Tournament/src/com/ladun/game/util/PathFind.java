package com.ladun.game.util;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import com.ladun.game.Point;

public class PathFind {
	
	public static class Node implements Comparable<Node>{
		public Node parent;
		public Point p;
		public int g, h;
		public Node(Node parent, Point p,int g, int h) {
			this.p = p;
			setting(parent, g, h);
		}
		@Override
		public int compareTo(Node o) {
			
			return this.g + this.h <= o.g +o.h? 1:-1;
		}
		public void setting(Node parent,int g, int h) {
			this.parent = parent;
			this.g = g;
			this.h = h;
		}
	}

	public static Stack<Point> AStar(int[][] map, Point st, Point ed){
		PriorityQueue<Node> openList = new PriorityQueue<Node>();
		PriorityQueue<Node> closeList = new PriorityQueue<Node>();
		Stack<Point> path = null;
		if(!isInMap(map,st) || !isInMap(map,ed))
			return null; 
		
		openList.offer(new Node(null,st,0,0));
		while(openList.size() > 0) {
			Node currentNode = openList.poll();
			closeList.offer(currentNode);
			if(currentNode.p.equalPos(ed)) {
				path = new Stack<>();
				Node temp = currentNode;
				while(temp.parent != null) {
					path.add(temp.p);
					temp = temp.parent;
				}
				break;
			}
			for(int y = -1; y <= 1;y++) {
				for(int x = -1; x <= 1; x++) {
					if(x == 0 && y == 0)
						continue;
					
					Point _point = new Point(currentNode.p.getX() + x, currentNode.p.getY() + y);
					if(!isInMap(map,_point))
						continue;
					
					boolean flag = false;
					for(Node n : closeList) {
						if(n.p.equalPos(_point)){
							flag = true;
							break;
						}
					}
					if(flag)
						continue;
					int g = ((Math.abs(x) == 1 && Math.abs(y) == 1)? 14 : 10);
					int h =  (Math.abs(ed.getX()- _point.getX()) + Math.abs(ed.getY() - _point.getY())) * 10;
					
					for(Node n : openList) {
						if(n.p.equalPos(_point)){
							if(n.g > g) {
								n.setting(currentNode,g,h);
							}
							flag = true;
							break;
						}
					}
					if(flag)
						continue;
					
					openList.offer(new Node(currentNode,_point,g,h));
				}
			}
			
		}
		
		
		return path;
	}
	
	private static boolean isInMap(int[][] map, Point p) {
		if(p.getY() >= 0 && p.getY() < map.length && p.getY() >= 0 && p.getY() < map[0].length)
			return true;
		return false;
	}
}
