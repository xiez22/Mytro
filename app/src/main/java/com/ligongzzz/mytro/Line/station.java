package com.ligongzzz.mytro.Line;

public class station
{
	public int num;          // 第〇位用于存储数量
	public double sx, sy;
	public String name=null;;
	public int[] sl;
	public int[] ss;
	public int[] newsta;
	public station(){
		name="";
		sl=new int[20];
		ss=new int[20];
		newsta=new int[20];
	}
}
