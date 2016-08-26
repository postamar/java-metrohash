/*
 * Copyright (c) 2016 Marius Posta
 *
 * Licensed under the Apache 2 license:
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package util.hash;

import java.nio.ByteBuffer;

class MetroHashImpl {

  static void writeLittleEndian(long hash, ByteBuffer output) {
    output.put((byte) hash);
    output.put((byte) (hash >>> 8));
    output.put((byte) (hash >>> 16));
    output.put((byte) (hash >>> 24));
    output.put((byte) (hash >>> 32));
    output.put((byte) (hash >>> 40));
    output.put((byte) (hash >>> 48));
    output.put((byte) (hash >>> 56));
  }

  static long rotr64(long x, int r) {
    return (x >>> r) | (x << (64 - r));
  }

  static long grab1(ByteBuffer bb) {
    return ((long) bb.get() & 0xFFL);
  }

  static long grab2(ByteBuffer bb) {
    final long v0 = bb.get();
    final long v1 = bb.get();
    return (v0 & 0xFFL) | (v1 & 0xFFL) << 8;
  }

  static long grab4(ByteBuffer bb) {
    final long v0 = bb.get();
    final long v1 = bb.get();
    final long v2 = bb.get();
    final long v3 = bb.get();
    return (v0 & 0xFFL) | (v1 & 0xFFL) << 8 | (v2 & 0xFFL) << 16 | (v3 & 0xFFL) << 24;
  }

  static long grab8(ByteBuffer bb) {
    final long v0 = bb.get();
    final long v1 = bb.get();
    final long v2 = bb.get();
    final long v3 = bb.get();
    final long v4 = bb.get();
    final long v5 = bb.get();
    final long v6 = bb.get();
    final long v7 = bb.get();
    return (v0 & 0xFFL) | (v1 & 0xFFL) << 8 | (v2 & 0xFFL) << 16 | (v3 & 0xFFL) << 24
           | (v4 & 0xFFL) << 32 | (v5 & 0xFFL) << 40 | (v6 & 0xFFL) << 48 | (v7 & 0xFFL) << 56;
  }
}
