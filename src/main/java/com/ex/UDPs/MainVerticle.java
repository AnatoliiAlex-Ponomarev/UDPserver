package com.ex.UDPs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.datagram.*;

public class MainVerticle extends AbstractVerticle {


  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
    socket.listen(7000, "127.0.0.1", asyncResult -> {
      if (asyncResult.succeeded()) {
        socket.handler(packet -> {

          PacketData pd = PacketHandler.makeInstance(packet.data());
          if(pd != null){
          pd = PacketHandler.validate(pd);
          if (pd.isValid()){
            System.out.println("norm");
            socket.send(PacketHandler.handle(pd), packet.sender().port(), packet.sender().hostAddress(),
            asyncResult1 -> System.out.println("Send succeeded? " + asyncResult1.succeeded()));
          }
        }
          else
            System.out.println("wrong size " + packet.data().length());
        }

        );
        System.out.println("Listen succeed" + asyncResult.cause());
      } else {
        System.out.println("Listen failed" + asyncResult.cause());
      }
    });

  }
}
