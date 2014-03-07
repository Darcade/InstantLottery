package me.darcade.minecraftlottery;

public class TimeChecker {
	public int getTimestamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public boolean candolottery(int lastlottery, int distance) {
		boolean output = false;
		if (lastlottery + distance <= this.getTimestamp())
			output = true;

		// System.out.println("lastlottery: " + lastlottery);
		// System.out.println("candolottery: " + output);

		return output;
	}
}
