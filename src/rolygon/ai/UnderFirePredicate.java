package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/13/2017.
 */
public class UnderFirePredicate implements Predicate {
    boolean underFire;

    @Override
    // this is a simple implementation and could probably be much improved
    public Predicate test(RobotController rc, Context context) throws GameActionException {
        underFire = false;
        MapLocation location = rc.getLocation();
        BulletInfo[] bullets = rc.senseNearbyBullets();
        float bodyRadius = rc.getType().bodyRadius;
        for (int idxBullet = 0; idxBullet < bullets.length; idxBullet++) {
            if (willCollideWith(bullets[idxBullet], location, bodyRadius)) {
                underFire = true;
                context.memorize("bullet", bullets[idxBullet]);
                break;
            }
        }
        return this;
    }

    @Override
    public boolean isTrue() {
        return underFire;
    }

    @Override
    public boolean isFalse() {
        return !underFire;
    }

    static boolean willCollideWith(BulletInfo bullet, MapLocation myLocation, float radius) {
        // Get relevant bullet information
        Direction propagationDirection = bullet.dir;
        MapLocation bulletLocation = bullet.location;

        // Calculate bullet relations to this robot
        Direction directionToRobot = bulletLocation.directionTo(myLocation);
        float distToRobot = bulletLocation.distanceTo(myLocation);
        float theta = propagationDirection.radiansBetween(directionToRobot);

        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
        if (Math.abs(theta) > Math.PI/2) {
            return false;
        }

        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
        // This corresponds to the smallest radius circle centered at our location that would intersect with the
        // line that is the path of the bullet.
        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

        return (perpendicularDist <= radius);
    }
}
