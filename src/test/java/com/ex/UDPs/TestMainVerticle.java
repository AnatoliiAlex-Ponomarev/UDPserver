package com.ex.UDPs;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

@RunWith(VertxUnitRunner.class)
public class TestMainVerticle {
    Vertx vertx;
    private int port;
    private static final String SERVER_HOST = "127.0.0.1";
    private DatagramSocket test_socket;

    private static final int[] field_size = {6, 1, 4, 8, 8, 8};
    private static final byte[] result1 = new byte[35];
    private static byte[] result2 = new byte[11];
    private static byte[] result11 = new byte[35];
    private static byte[] result22 = new byte[11];


    private PacketData packetData;

    {
        int k = 0;
        for(var x : field_size) {
            for(byte i = 0; i < x; i++) {
                result1[k++] = i;
            }
        }
        result1[6] = 1;

        k = 0;
        for(var x : field_size) {
            for(byte i = 0; i < x; i++) {
                result11[k++] = (byte) (10 - i);
            }
        }
        result11[6] = 1;
        result2 = Arrays.copyOf(result1, 11);
        result2[6] = 2;
        result22 = Arrays.copyOf(result11, 11);
        result22[6] = 2;
    }

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Before
  public void deploy_verticle(TestContext testContext) {

      vertx = rule.vertx();

      try {
          ServerSocket socket = new ServerSocket(0);
          port = socket.getLocalPort();
          socket.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
      test_socket = vertx.createDatagramSocket(new DatagramSocketOptions());

      //io.vertx.core.datagram.DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
//      test_socket.listen(port, SERVER_HOST);
//      test_socket.handler(result -> {
//
//      })

    vertx.deployVerticle(new UdpServer(), testContext.asyncAssertSuccess());
  }

  @Test
  public void verticle_deployed(TestContext testContext) throws Throwable {
    Async async = testContext.async();
    async.complete();
  }

  @After
    public void close(TestContext testContext) {

      test_socket.close();
      System.out.println("test_socket is closed");
  }
  @Test
  public void test_send(TestContext testContext) throws Throwable{
      Async async = testContext.async();

      test_socket.listen(port, SERVER_HOST, asyncResult -> {
          if (asyncResult.succeeded()) {
              test_socket.handler(packet -> {


                  for(var x : packet.data().getBytes()){
                      System.out.printf("%02d ", x);
                  }
                  System.out.println();
                  testContext.assertTrue(Arrays.equals(packet.data().getBytes(), result2));
                  async.complete();
              });
          } else {
              System.out.println("Listen failed" + asyncResult.cause());
          }
      });

      test_socket.send(Buffer.buffer(result1), UdpServer.SERVER_PORT, UdpServer.SERVER_HOST, asyncResult -> {
          System.out.println("Send succeeded?? " + asyncResult.succeeded());
      });
      //async.wait();

  }


    @Test
    public void test_send1(TestContext testContext) throws Throwable {
        Async async = testContext.async();

        test_socket.listen(port, SERVER_HOST, asyncResult -> {
            if (asyncResult.succeeded()) {
                test_socket.handler(packet -> {


                    for (var x : packet.data().getBytes()) {
                        System.out.printf("%02d ", x);
                    }
                    System.out.println();
                    testContext.assertTrue(Arrays.equals(packet.data().getBytes(), result22));
                    async.complete();
                });
            } else {
                System.out.println("Listen failed" + asyncResult.cause());
            }
        });

        test_socket.send(Buffer.buffer(result11), UdpServer.SERVER_PORT, UdpServer.SERVER_HOST, asyncResult -> {
            System.out.println("Send succeeded??? " + asyncResult.succeeded());
        });
    }
}
