package com.thinkgem.jeesite.modules.school.entity.lessonstudent;

public class RollCallSet {
	int count;
	static String attendance="出勤,";
	static String late="迟到,";
	static String absent="缺课,";
	static String early="请假,";
	
	public static String Set(int count){
		if(((count&8)==0)){
			attendance="";
		}
		if(((count&4)==0)){
			late="";
		}
		if(((count&2)==0)){
			absent="";
		}
		if(((count&1)==0)){
			early="";
		}
		return attendance+late+absent+early;
	}
	
	
	private static int getCount(String name){
		int count=0;
		if(name.equals("出勤")){
			count=8;
		}
		if(name.equals("迟到")){
			count=4;
		}
		if(name.equals("缺课")){
			count=2;
		}
		if(name.equals("请假")){
			count=1;
		}
		return count;
	}
	
	public static int get(String result){
		int count=0;
		String[] results=result.split(",");
		for(int i=0;i<results.length;i++){
			count=count+getCount(results[i]);
		}
		return count;
	}
	
    public static void main(String[] args) { 
    	RollCallSet rollCallSet=new RollCallSet();
    	System.out.println(rollCallSet.Set(6));
    } 
}
