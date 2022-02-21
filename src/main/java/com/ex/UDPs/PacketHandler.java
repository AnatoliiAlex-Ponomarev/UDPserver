package com.ex.UDPs;

import io.vertx.core.buffer.Buffer;

public class PacketHandler {
    static private final short ptSend = 2;

    static PacketData makeInstance(Buffer buffer) {
        if (buffer.length() != 35)
            return null;
        short mac[] = new short[6];
        for(int i = 0; i < mac.length; i++)
            mac[i] = buffer.getUnsignedByte(i);

        return new PacketData(mac, buffer.getUnsignedByte(6), buffer.getUnsignedInt(7),
            buffer.getLong(11), buffer.getDouble(19), buffer.getDouble(27));
    }

    static PacketData validate(PacketData packetData) {
        if (packetData.getPt() != 1) {
            packetData.setValid(false);
            System.out.println("Wrong pt " + packetData.getPt());
        }

        return packetData;
    }

    static Buffer handle(PacketData packetData) {
        var buffer = Buffer.buffer();
        for(var x : packetData.getMac()) {
            buffer.appendUnsignedByte(x);
        }
        buffer
            .appendUnsignedByte(ptSend)
            .appendUnsignedInt(packetData.getId());
//      .appendLong(packetData.getTimestamp())
//      .appendDouble(packetData.getLatitude())
//      .appendDouble(packetData.getLongitude());
        return buffer;
        //return Buffer.buffer().appendBytes(packetData.getMac()).appendByte(ptSend).appendInt((int)packetData.getId());
    }

}

