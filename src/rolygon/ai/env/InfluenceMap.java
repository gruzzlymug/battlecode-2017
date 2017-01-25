package rolygon.ai.env;

/**
 * Created by nobody on 1/16/2017.
 *
 * Grid-based map of the environment...who controls which parts of the map?
 */
public class InfluenceMap {
    // top left corner of map is the origin
    // maps range from 30x30 to 100x100
    // positions will be offset by a random amount
    protected int leftOffset;
    protected int topOffset;

    protected int actualWidth = 100;
    protected int actualHeight = 100;

    // TODO avoid allocating more space than needed
    // TODO find best (better) way to store info
    protected float[][] grid = new float[100][100];

    public void addThing() {
    }

    public void reset() {
        for (int row = 0; row < actualHeight; row++) {
            for (int col = 0; col < actualWidth; col++) {
                grid[row][col] = 0;
            }
        }
    }

    public float[][] getGrid() {
        return grid;
    }
}
