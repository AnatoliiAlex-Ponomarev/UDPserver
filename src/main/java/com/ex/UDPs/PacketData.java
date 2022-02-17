package com.ex.UDPs;

public class PacketData {
  private final byte [] mac; //6 bytes
  private final byte pt;
  private final long id;
  private final long timestamp;
  private final long latitude;
  private final long longitude;
  private boolean valid = true;

  public PacketData(byte[] mac, byte pt, long id, long timestamp, long latitude, long longitude) {
    this.mac = mac;
    this.pt = pt;
    this.id = id;
    this.timestamp = timestamp;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public  byte[] getMac(){
    return mac;
  }
  public byte getPt(){
    return pt;
  }
  public long getId(){
    return id;
  }
  public long getTimestamp(){
    return timestamp;
  }
  public long getLatitude(){
    return latitude;
  }
  public long getLongitude(){
    return longitude;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }
}
