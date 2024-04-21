import java.util.Arrays;
import java.util.List;

public class LEDLightingResult {
    private final int maxLEDs;
    private final LED[] LEDs;
    private final int[][] dpTable;

    public LEDLightingResult(int maxLEDs, List<LED> LEDs, int[][] dpTable) {
        this.maxLEDs = maxLEDs;
        this.LEDs = LEDs.toArray(new LED[0]); // Convert List to array
        this.dpTable = dpTable.clone(); // Using clone to create a copy of the array
    }

    // Additional constructor accepting array of LEDs and 2D array
    public LEDLightingResult(int maxLEDs, LED[] LEDs, int[][] dpTable) {
        this.maxLEDs = maxLEDs;
		this.LEDs = Arrays.copyOf(LEDs, LEDs.length);
        this.dpTable = Arrays.copyOf(dpTable, dpTable.length);
    }

    public int getMaxLEDs() {
        return maxLEDs;
    }

    public LED[] getLEDs() {
        return Arrays.copyOf(LEDs, LEDs.length);
    }

    public int[][] getDPTable() {
        return Arrays.copyOf(dpTable, dpTable.length);
    }

    public int getNumberOfLEDs() {
        return LEDs.length;
    }
}
