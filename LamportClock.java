import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

public class LamportClock{
    private int time;
    private final String lamportFile;
    private ReentrantLock lock;
    private String stationID;

    public LamportClock(String stationID) {
        lamportFile = "lamport_events.txt";
        this.time = 0;
        this.lock = new ReentrantLock();
        this.stationID = stationID;
    }

    public LamportClock() {
        lamportFile = "lamport_events.txt";
        this.time = 0;
        this.lock = new ReentrantLock();
    }
    
    public void setStationID(String stationID){
        this.stationID = stationID;
    }
    // public void tick() {
    //     lock.lock();
    //     try {
    //         time++;
    //         recordEvent(time);
    //     } finally {
    //         lock.unlock();
    //     }
    // }

    public void tick() {
        time++;
    }

    public int getTime() {
        return time;
    }

    public void updateTime() {
        lock.lock();
        int remoteTime = UtilTool.findMaxRemoteTime(lamportFile);
        try {
            time = Math.max(time, remoteTime);
            recordEvent(time);
        } finally {
            lock.unlock();
        }
    }

    public int getClockId() {
        return hashCode();
    }

    private void recordEvent(int eventTime) {
        try {
            FileWriter fileWriter = new FileWriter(lamportFile, true);
            PrintWriter out = new PrintWriter(fileWriter, true);
            out.println(toString());

            out.close();
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStationID(){
        return this.stationID;
    }

    public String toString(){
        return getClockId()+":"+getStationID()+":"+getTime();
    }

    public static void main(String[] args) {
        LamportClock lamportClock = new LamportClock("1");
        LamportClock lamportClock2 = new LamportClock("2");
        LamportClock lamportClock3 = new LamportClock("3");
        LamportClock lamportClock4 = new LamportClock("4");

        lamportClock.tick();
        lamportClock2.tick();
        lamportClock3.tick();
        lamportClock4.tick();

    }
}
