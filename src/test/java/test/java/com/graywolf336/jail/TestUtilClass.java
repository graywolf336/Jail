package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.graywolf336.jail.Util;

public class TestUtilClass {
    private static List<String> list;
    private static String[] array;
    private static Vector bottomCorner;
    private static Vector topCorner;

    @BeforeClass
    public static void setUp() throws Exception {
        list = new ArrayList<String>();
        list.add(Material.WHEAT_SEEDS.toString());
        list.add("coal_ore");
        list.add("torch");
        array = new String[] { Material.WHEAT_SEEDS.toString(), "coal_ore", "torch" };
        bottomCorner = new Vector(-10.50, 50.25, 100.00);
        topCorner = new Vector(50, 100, 250);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        bottomCorner = null;
        topCorner = null;
        list = null;
    }

    @Test
    public void testIsInsideAB() {
        Vector inside = new Vector(35, 64, 110);
        assertTrue(Util.isInsideAB(inside, bottomCorner, topCorner));
    }

    @Test
    public void testIsOutsideAB() {
        Vector outside = new Vector(350, 15, 350);
        assertFalse(Util.isInsideAB(outside, bottomCorner, topCorner));
    }

    @Test
    public void testHalfInHalfOutsideAB() {
        Vector halfAndHalf = new Vector(25, 75, 99);
        assertFalse(Util.isInsideAB(halfAndHalf, bottomCorner, topCorner));
    }

    @Test
    public void testFirstGreaterThanSecond() {
        Vector greaterFirst = new Vector(10, 86, -104);
        assertFalse(Util.isInsideAB(greaterFirst, topCorner, bottomCorner));
    }

    @Test
    public void testInArray() {
        assertTrue(Util.isStringInsideArray("wheat_seeds", array));
        assertTrue(Util.isStringInsideArray(Material.COAL_ORE.toString(), array));
        assertTrue(Util.isStringInsideArray("tOrCh", array));
    }

    @Test
    public void testNotInArray() {
        assertFalse(Util.isStringInsideArray("dirt", array));
        assertFalse(Util.isStringInsideArray("SAND", array));
        assertFalse(Util.isStringInsideArray(Material.BEDROCK.toString(), array));
    }

    @Test
    public void testInList() {
        assertTrue(Util.isStringInsideList("wheat_seeds", list));
        assertTrue(Util.isStringInsideList(Material.COAL_ORE.toString(), list));
        assertTrue(Util.isStringInsideList("tOrCh", list));
    }

    @Test
    public void testNotInList() {
        assertFalse(Util.isStringInsideList("dirt", list));
        assertFalse(Util.isStringInsideList("SAND", list));
        assertFalse(Util.isStringInsideList(Material.BEDROCK.toString(), list));
    }

    @Test
    public void testCorrectStringFromArray() {
        assertEquals("WHEAT_SEEDS,coal_ore,torch", Util.getStringFromArray(",", array));
    }

    @Test
    public void testCorrectStringFromList() {
        assertEquals("WHEAT_SEEDS,coal_ore,torch", Util.getStringFromList(",", list));
    }

    @Test
    public void testColorfulMessage() {
        assertEquals("§4Col§lor§fful §1messages", Util.getColorfulMessage("&4Col&lor&fful &1messages"));
    }

    @Test
    public void testNoFormat() throws Exception {
        assertEquals(60000L, Util.getTime("1"), 0);
        assertEquals(360000L, Util.getTime("6"), 0);
    }

    @Test
    public void testTimeSeconds() throws Exception {
        assertEquals(2000L, Util.getTime("2s"), 0);
        assertEquals(2000L, Util.getTime("2second"), 0);
        assertEquals(2000L, Util.getTime("2seconds"), 0);
    }

    @Test
    public void testTimeMinutes() throws Exception {
        assertEquals(60000L, Util.getTime("1m"), 0);
        assertEquals(60000L, Util.getTime("1minute"), 0);
        assertEquals(60000L, Util.getTime("1minutes"), 0);
    }

    @Test
    public void testTimeHours() throws Exception {
        assertEquals(3600000L, Util.getTime("1h"), 0);
        assertEquals(3600000L, Util.getTime("1hours"), 0);
    }

    @Test
    public void testTimeDays() throws Exception {
        assertEquals(86400000L, Util.getTime("1d"), 0);
        assertEquals(86400000L, Util.getTime("1days"), 0);
    }

    @Test
    public void testTimeSecondsPassed() throws Exception {
        assertEquals(60L, Util.getTime("60", TimeUnit.MINUTES), 0);
        assertEquals(1L, Util.getTime("60s", TimeUnit.MINUTES), 0);
        assertEquals(1L, Util.getTime("60m", TimeUnit.HOURS), 0);
        assertEquals(6L, Util.getTime("6d", TimeUnit.DAYS), 0);
    }

    @Test
    public void testDurationBreakdown() {
        assertEquals("1s", Util.getDurationBreakdown(1000));
        assertEquals("1m0s", Util.getDurationBreakdown(60000));
        assertEquals("1h0m0s", Util.getDurationBreakdown(3600000));
        assertEquals("1d0h0m0s", Util.getDurationBreakdown(86400000));
        assertEquals("1d1h1m1s", Util.getDurationBreakdown(90061000));
    }

    @Test(expected = Exception.class)
    public void testInvalidDateFormat() throws Exception {
        Util.getTime("abcdefg");
    }

    @Test(expected = Exception.class)
    public void testInvalidUpdateSigns() throws Exception {
        Util.updateSignLinesCache(new String[] {});
    }

    @Test
    public void testUpdateSignsCache() throws Exception {
        String[] lines = new String[] { "test1", "test2", "test3", "test4" };
        Util.updateSignLinesCache(lines);
        assertArrayEquals(lines, Util.getSignLines());
    }
}
