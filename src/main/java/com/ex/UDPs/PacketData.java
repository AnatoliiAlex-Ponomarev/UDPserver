package com.ex.UDPs;

public class PacketData {
    private final short[] mac; //6 bytes
    private final long macLong;
    private final short pt;
    private final long id;
    private final long timestamp;
    private final double latitude;
    private final double longitude;
    private boolean valid = true;

    public PacketData(short[] mac, short pt, long id, long timestamp, double latitude, double longitude) {
        this.mac = mac;
        long macLong = 0;
        for(var x : mac) {
            macLong = (macLong << 8) | x;
        }
        this.macLong = macLong;
        this.pt = pt;
        this.id = id;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public short[] getMac() {
        return mac;
    }

    public short getPt() {
        return pt;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getMacLong() {
        return macLong;
    }
}
