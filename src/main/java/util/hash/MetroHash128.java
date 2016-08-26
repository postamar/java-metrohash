/*
 * Copyright (c) 2016 Marius Posta
 *
 * Licensed under the Apache 2 license:
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package util.hash;

import java.nio.ByteBuffer;

public class MetroHash128 extends MetroHashImpl implements MetroHash<MetroHash128> {

  final public long seed;
  private long v0, v1, v2, v3;
  private long nChunks;

  /**
   * Initializes a MetroHash128 state with the given seed.
   */
  public MetroHash128(long seed) {
    this.seed = seed;
    reset();
  }

  /**
   * First 64 bits of the current hash value.
   */
  public long getHigh() {
    return v0;
  }

  /**
   * Last 64 bits of the current hash value.
   */
  public long getLow() {
    return v1;
  }

  @Override
  public MetroHash128 writeLittleEndian(ByteBuffer output) {
    writeLittleEndian(v0, output);
    writeLittleEndian(v1, output);
    return this;
  }

  @Override
  public MetroHash128 writeBigEndian(ByteBuffer output) {
    output.asLongBuffer().put(v1).put(v0);
    return this;
  }

  @Override
  public MetroHash128 reset() {
    v0 = (seed - K0) * K3;
    v1 = (seed + K1) * K2;
    v2 = (seed + K0) * K2;
    v3 = (seed - K1) * K3;
    nChunks = 0;
    return this;
  }

  @Override
  public MetroHash128 partialApply32ByteChunk(ByteBuffer partialInput) {
    assert partialInput.remaining() >= 32;
    v0 += grab8(partialInput) * K0; v0 = rotr64(v0, 29) + v2;
    v1 += grab8(partialInput) * K1; v1 = rotr64(v1, 29) + v3;
    v2 += grab8(partialInput) * K2; v2 = rotr64(v2, 29) + v0;
    v3 += grab8(partialInput) * K3; v3 = rotr64(v3, 29) + v1;
    ++nChunks;
    return this;
  }

  @Override
  public MetroHash128 partialApplyRemaining(ByteBuffer partialInput) {
    assert partialInput.remaining() < 32;
    if (nChunks > 0) {
      metroHash128_32();
    }
    if (partialInput.remaining() >= 16) {
      metroHash128_16(partialInput);
    }
    if (partialInput.remaining() >= 8) {
      metroHash128_8(partialInput);
    }
    if (partialInput.remaining() >= 4) {
      metroHash128_4(partialInput);
    }
    if (partialInput.remaining() >= 2) {
      metroHash128_2(partialInput);
    }
    if (partialInput.remaining() >= 1) {
      metroHash128_1(partialInput);
    }
    v0 += rotr64(v0 * K0 + v1, 13);
    v1 += rotr64(v1 * K1 + v0, 37);
    v0 += rotr64(v0 * K2 + v1, 13);
    v1 += rotr64(v1 * K3 + v0, 37);
    return this;
  }

  static final private long K0 = 0xC83A91E1L;
  static final private long K1 = 0x8648DBDBL;
  static final private long K2 = 0x7BDEC03BL;
  static final private long K3 = 0x2F5870A5L;

  private void metroHash128_32() {
    v2 ^= rotr64((v0 + v3) * K0 + v1, 21) * K1;
    v3 ^= rotr64((v1 + v2) * K1 + v0, 21) * K0;
    v0 ^= rotr64((v0 + v2) * K0 + v3, 21) * K1;
    v1 ^= rotr64((v1 + v3) * K1 + v2, 21) * K0;
  }

  private void metroHash128_16(ByteBuffer bb) {
    v0 += grab8(bb) * K2; v0 = rotr64(v0, 33) * K3;
    v1 += grab8(bb) * K2; v1 = rotr64(v1, 33) * K3;
    v0 ^= rotr64(v0 * K2 + v1, 45) * K1;
    v1 ^= rotr64(v1 * K3 + v0, 45) * K0;
  }

  private void metroHash128_8(ByteBuffer bb) {
    v0 += grab8(bb) * K2; v0 = rotr64(v0, 33) * K3;
    v0 ^= rotr64(v0 * K2 + v1, 27) * K1;
  }

  private void metroHash128_4(ByteBuffer bb) {
    v1 += grab4(bb) * K2; v1 = rotr64(v1, 33) * K3;
    v1 ^= rotr64(v1 * K3 + v0, 46) * K0;
  }

  private void metroHash128_2(ByteBuffer bb) {
    v0 += grab2(bb) * K2; v0 = rotr64(v0, 33) * K3;
    v0 ^= rotr64(v0 * K2 + v1, 22) * K1;
  }

  private void metroHash128_1(ByteBuffer bb) {
    v1 += grab1(bb) * K2; v1 = rotr64(v1, 33) * K3;
    v1 ^= rotr64(v1 * K3 + v0, 58) * K0;
  }
}
