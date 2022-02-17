package com.ex.UDPs;

import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PacketHandlerTest {
  private static final int[] field_size= {6, 1, 4, 8, 8, 8};
  private static byte [] result1 = new byte[35];
  private static byte [] result2;
  private static byte [] result3;
  private static byte[] result4;

  @BeforeAll
  static void initResults(){

    int k = 0;
    for(var x : field_size){
      for(byte i = 0; i < x; i++) {
        result1[k++]=i;
      }
    }
    result1[6] = 1;
    result2 = Arrays.copyOf(result1, 35);
    result2[6] = 3;
    result3 = Arrays.copyOf(result1, 22);
    result4 = Arrays.copyOf(result1, 11);
    result4[6] = 2;
  }
  @Test
  void makeInstanceFalseSize() {
    var pd = PacketHandler.makeInstance(Buffer.buffer().appendBytes(result3));
    assertNull(pd);
  }

  @Test
  void makeInstance() {
    var pd = PacketHandler.makeInstance(Buffer.buffer().appendBytes(result1));
    PacketData pdActual = new PacketData(new byte[]{0,1,2,3,4,5},
      (byte) 1, 66051L, 283686952306183L, 283686952306183L, 283686952306183L);
    assertNotNull(pd);
    assertEquals(pd.getTimestamp(), pdActual.getTimestamp());
    assertEquals(pd.getId(), pdActual.getId());
    assertEquals(pd.getPt(), pdActual.getPt());
    assertEquals(pd.getLatitude(), pdActual.getLatitude());
    assertEquals(pd.getLongitude(), pdActual.getLongitude());
    assertArrayEquals(pdActual.getMac(), pd.getMac());
  }

  @Test
  void validate() {
    var pd = PacketHandler.makeInstance(Buffer.buffer().appendBytes(result1));
    assertTrue(pd.isValid());
  }

  @Test
  void validateFalsePT() {
    var pd = PacketHandler.makeInstance(Buffer.buffer().appendBytes(result2));
    pd = PacketHandler.validate(pd);
    assertFalse(pd.isValid());
  }

  @Test
  void handle() {
    var actual = PacketHandler.handle(PacketHandler.makeInstance(Buffer.buffer().appendBytes(result1)));
    assertArrayEquals(actual.getBytes(), result4);
  }
}
