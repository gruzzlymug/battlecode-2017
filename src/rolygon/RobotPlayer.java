package rolygon;
import battlecode.common.*;

public strictfp class RobotPlayer {
	static RobotController rc;
	
	public static void run(RobotController rc) throws GameActionException {
		RobotPlayer.rc = rc;
		
		RobotType rtype = rc.getType();
		switch (rtype) {
		case ARCHON:
			runArchon();
			break;
		case GARDENER:
			runGardener();
			break;
		case LUMBERJACK:
			break;
		case SCOUT:
			break;
		case SOLDIER:
			break;
		case TANK:
			break;
		}
	}
	
	private static void runArchon() throws GameActionException {
		while (true) {
			Clock.yield();
		}
	}
	
	private static void runGardener() throws GameActionException  {
		while (true) {
			Clock.yield();
		}
	}
}
