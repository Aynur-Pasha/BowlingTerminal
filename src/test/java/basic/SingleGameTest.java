package basic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SingleGameTest {
    private SingleGame game;

    @Before
    public void setUp() {
        game = new SingleGame();
    }

    @Test
    public void testAddShot() {
        game.addShot(5);
        assertEquals(5, game.getTotalPoints());
    }

    @Test
    public void testAddMultipleShots() {
        game.addShot(3);
        game.addShot(7);
        assertEquals(10, game.getTotalPoints());
    }

    @Test
    public void testGetPointsAfterStrike() {
        game.addShot(10);
        game.addShot(5);
        game.addShot(3);
        assertEquals(26, game.getTotalPoints());
    }

    @Test
    public void testGetPointsAfterSpare() {
        game.addShot(5);
        game.addShot(5);
        game.addShot(3);
        game.addShot(4);
        assertEquals(20, game.getTotalPoints());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddShotAfterGameOver() {
        for (int i = 0; i < 13; i++) {
            game.addShot(10);
        }
    }

    @Test
    public void testConvertShotsToString() {
        game.addShot(5);
        game.addShot(3);
        assertEquals("| 5 3 |     |     |     |     |     |     |     |     |     |", game.getPointsInFrame());
    }

    @Test
    public void testConvertShotsToStringWithStrikesAndSpares() {
        for (int i = 0; i < 10; i++) {
            game.addShot(10);
        }
        game.addShot(5);
        game.addShot(5);
        game.addShot(3);
        assertEquals("|  X  |  X  |  X  |  X  |  X  |  X  |  X  |  X  |  X  | 5 / 3 |", game.getPointsInFrame());
    }

    @Test
    public void testResetGame() {
        game.addShot(5);
        game.addShot(3);
        game.resetGame();
        assertEquals(0, game.getTotalPoints());
        assertEquals(-1, game.getCurrentFrame());
        assertEquals(-1, game.getShots()[0][0]);
        assertEquals(-1, game.getShots()[0][1]);
    }

//    @Test
//    public void testSetShots() {
//        List<List<Integer>> shotsList = new ArrayList<>();
//        shotsList.add(Arrays.asList(5, 3));
//        shotsList.add(Arrays.asList(8, 1));
//        game.setShots(shotsList);
//        assertEquals(5, game.getShots()[0][0]);
//        assertEquals(3, game.getShots()[0][1]);
//        assertEquals(8, game.getShots()[1][0]);
//        assertEquals(1, game.getShots()[1][1]);
//    }

    @Test(expected = InvalidPinCountException.class)
    public void testInvalidPinCount() {
        game.addShot(11);
    }

    @Test(expected = InvalidPinCountException.class)
    public void testInvalidPinCountAfterStrike() {
        game.addShot(10);
        game.addShot(2);
    }

    @Test
    public void testSetCurrentPoints() {
        int[] points = {10, 20, 30};
        game.setCurrentPoints(points);
        assertArrayEquals(points, game.getCurrentPoints());
    }

    @Test
    public void testSetCurrentBonus() {
        int[] bonus = {0, 1, 2};
        game.setCurrentBonus(bonus);
        assertArrayEquals(bonus, game.getCurrentBonus());
    }

    @Test
    public void testIsGameover() {
        assertFalse(game.isGameover());
        game.setGameover(true);
        assertTrue(game.isGameover());
    }
}
