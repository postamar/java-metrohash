/*
 * Copyright (c) 2016 Marius Posta
 *
 * Licensed under the Apache 2 license:
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package util.hash;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class TestUtils {

  static final byte[] ENDIAN =
      utf8Bytes("012345678901234567890123456789012345678901234567890123456789012");

  static String hex(long hash) {
    return String.format("%016X", hash);
  }

  static String hex(byte[] bytes) {
    String hex = "";
    for (byte b : bytes) {
      hex += String.format("%02X", b);
    }
    return hex;
  }

  static byte[] utf8Bytes(String string) {
    try {
      return string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static String h64(String input) {
    MetroHash64 mh = new MetroHash64(0).apply(ByteBuffer.wrap(utf8Bytes(input)));
    return hex(mh.get());
  }

  static String h128(String input) {
    MetroHash128 mh = new MetroHash128(0).apply(ByteBuffer.wrap(utf8Bytes(input)));
    return hex(mh.getHigh()) + hex(mh.getLow());
  }


}
