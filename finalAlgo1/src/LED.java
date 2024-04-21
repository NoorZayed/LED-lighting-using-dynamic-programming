
public class LED {
	private final int led;
	private final int source;

	public LED(int led, int source) {
		this.led = led;
		this.source = source;
	}

	public int getLed() {
		return led;
	}

	public int getSource() {
		return source;
	}
	@Override
    public String toString() {
        return "LED " + led + " connected to Switch " + source;
    }
}
