package com.nottingham.cs.tung.g54idp.data;

import java.util.Date;

//This class hold data of 1 time slot
//How many computers are free at what time
public class SlotData {
	Date time;
	int free;
	
	public SlotData(Date time, int free){
		this.time = time;
		this.free = free;
	}
	
	public Date getTime() {
		return time;
	}
	
	public int getFree() {
		return free;
	}

}
