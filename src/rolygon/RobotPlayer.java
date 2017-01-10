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
            runLumberjack();
            break;
        case SCOUT:
            runScout();
            break;
        case SOLDIER:
            break;
        case TANK:
            break;
        }
    }
	
	private static void runArchon() throws GameActionException {
		System.out.println("Running Zee Archon!!!");
		while (true) {
			Direction dir = randomDirection();
			if (rc.canBuildRobot(RobotType.GARDENER, dir)) {
				rc.buildRobot(RobotType.GARDENER, dir);
			}
			Clock.yield();
		}
	}
	
	private static void runGardener() throws GameActionException  {
		while (true) {
			Direction dir = randomDirection();
			if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
				rc.buildRobot(RobotType.LUMBERJACK, dir);
			} else if (rc.canBuildRobot(RobotType.SCOUT, dir)) {
				rc.buildRobot(RobotType.SCOUT, dir);
			}
			Clock.yield();
		}
	}
	
    static void runLumberjack() throws GameActionException {
        while (true) {
            Clock.yield();
        }
    }
	
	static void runScout() throws GameActionException {
		while (true) {
		    Clock.yield();
		}
    }
	
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
}
