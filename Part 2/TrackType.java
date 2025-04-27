import java.util.ArrayList;

/**
 * Track class to keep track of lap times during run-time
 * 
 * @author Peter Bojthe
 * @version 1.0.2
 */
public class TrackType {
    private final String type;
    private final ArrayList<Double> trackTimes = new ArrayList<>();

    /** Constructor to create a type of track @param type is the name of the track */
    public TrackType(String type) { this.type = type; }

    /** @param time to be added to the array list of track times */
    public void addTime(double time) { trackTimes.add(time); }

    /** @return the type of track */
    public String getType() { return type; }

    /** Get all times for this track */
    public ArrayList<Double> getTrackTimes() { return trackTimes; }
}
