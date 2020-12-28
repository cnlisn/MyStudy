package com.lisn.mystudy.thread;

public class Resource{
		private int id;//一辆车对应一个id
		private String name;//车名
		private int whellNumber;//车的轮子数
		private boolean flag = false;//标记
		public void setId(int id){
			this.id = id;
		}
		public void setName(String name){
			this.name = name;
		}
		public void setWhell(int whellNumber){
			this.whellNumber = whellNumber;
		}
		public void setFlag(boolean flag){
			this.flag = flag;
		}

		public int getId(){
			return id;
		}
		public String getName(){
			return name;
		}
		
		public int getWhellNumber(){
			return whellNumber;
		}
		public boolean isFlag(){
			return flag;
		}
	}

