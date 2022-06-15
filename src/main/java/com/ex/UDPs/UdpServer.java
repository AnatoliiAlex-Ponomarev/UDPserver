package com.ex.UDPs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.datagram.*;

public class UdpServer extends AbstractVerticle {
    static final String SERVER_HOST = "127.0.0.1";
    static final int SERVER_PORT = 7000;


    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
        socket.listen(SERVER_PORT, SERVER_HOST, asyncResult -> {
            if(asyncResult.succeeded()) {
                socket.handler(packet -> {
                        PacketData pd = PacketHandler.makeInstance(packet.data());
                        if(pd != null) {
                            PacketHandler.validate(pd);
                            if(pd.isValid()) {
                                socket.send(PacketHandler.handle(pd), packet.sender().port(), packet.sender().hostAddress(),
                                    asyncResult1 -> System.out.println("Send succeeded?" + asyncResult1.succeeded()));
                            }
                        } else
                            System.out.println("bad size: " + packet.data().length());
                    }
                );
                System.out.printf("[UDP] Server is listening on %s:%d\n", SERVER_HOST, SERVER_PORT);
                startPromise.complete();
                //System.out.println("Listen succeed");
            }
            else {
                System.out.printf("[UDP] Server listen failed on %s:%d - %s\n", SERVER_HOST, SERVER_PORT, asyncResult.cause());
                startPromise.fail(asyncResult.cause());
                //System.out.println("Listen failed" + asyncResult.cause());
            }
        });
    }
}
// ????
