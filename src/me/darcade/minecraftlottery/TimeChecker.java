package me.darcade.minecraftlottery;

import java.util.Calendar;

public class TimeChecker {
	public int getMinuteofYear(){
		Calendar cal = Calendar.getInstance();
		int MinuteofDay = (cal.get(Calendar.HOUR) * 60) + cal.get(Calendar.MINUTE);
		int MinuteofYear = MinuteofDay * cal.get(Calendar.DAY_OF_MONTH) * cal.get(Calendar.MONTH);
		
		//System.out.println(MinuteofYear);
		
		return MinuteofYear;
	}
	
	//TODO at the end of the year the user is allowed to do 2 lotteries between 23:59 and 0:00
	public boolean candolottery(int lastlottery, int lastlotteryyear, int distance){
		Calendar cal = Calendar.getInstance();
		boolean output = false;
		if (lastlotteryyear == 0 || lastlotteryyear == cal.get(Calendar.YEAR) && lastlottery + distance <= this.getMinuteofYear())
			output = true;
		
		//System.out.println("lastlottery: " + lastlottery);
		//System.out.println("candolottery: " + output);
		
		return output;
	}
}
