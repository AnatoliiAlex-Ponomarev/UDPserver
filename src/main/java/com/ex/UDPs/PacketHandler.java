package com.ex.UDPs;

import io.vertx.core.buffer.Buffer;

public class PacketHandler {
  static private final byte ptSend = 2;

  static PacketData makeInstance(Buffer buffer){
    if(buffer.length() != 35)
      return null;
    return new PacketData(buffer.getBytes(0, 6), buffer.getByte(6), buffer.getUnsignedInt(7),
      buffer.getLong(11), buffer.getLong(19), buffer.getLong(27));
  }

  static PacketData validate(PacketData packetData){
    if (packetData.getPt() != 1)
    {
      packetData.setValid(false);
      System.out.println("Wrong pt " + packetData.getPt());
    }

    return packetData;
  }

  static Buffer handle(PacketData packetData){
    return Buffer.buffer().appendBytes(packetData.getMac()).appendByte(ptSend).appendInt((int)packetData.getId());
  }

}

