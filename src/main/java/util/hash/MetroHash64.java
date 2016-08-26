/*
 * Copyright (c) 2016 Marius Posta
 *
 * Licensed under the Apache 2 license:
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package util.hash;

import java.nio.ByteBuffer;

public class MetroHash64 extends MetroHashImpl implements MetroHash<MetroHash64> {

  final public long seed;
  private long v0, v1, v2, v3;
  private long nChunks;
  private long hash;

  /**
   * Initializes a MetroHash64 state with the given seed.
   */
  public MetroHash64(long seed) {
    this.seed = seed;
    reset();
  }

  /**
   * Current hash value.
   */
  public long get() {
    return hash;
  }

  @Override
  public MetroHash64 writeLittleEndian(ByteBuffer output) {
    writeLittleEndian(hash, output);
    return this;
  }

  @Override
  public MetroHash64 writeBigEndian(ByteBuffer output) {
    output.asLongBuffer().put(hash);
    return this;
  }

  @Override
  public MetroHash64 reset() {
    v0 = v1 = v2 = v3 = hash = (seed + K2) * K0;
    nChunks = 0;
    return this;
  }

  @Override
  public MetroHash64 partialApply32ByteChunk(ByteBuffer partialInput) {
    assert partialInput.remaining() >= 32;
    v0 += grab8(partialInput) * K0; v0 = rotr64(v0, 29) + v2;
    v1 += grab8(partialInput) * K1; v1 = rotr64(v1, 29) + v3;
    v2 += grab8(partialInput) * K2; v2 = rotr64(v2, 29) + v0;
    v3 += grab8(partialInput) * K3; v3 = rotr64(v3, 29) + v1;
    ++nChunks;
    return this;
  }

  @Override
  public MetroHash64 partialApplyRemaining(ByteBuffer partialInput) {
    assert partialInput.remaining() < 32;
    if (nChunks > 0) {
      metroHash64_32();
    }
    if (partialInput.remaining() >= 16) {
      metroHash64_16(partialInput);
    }
    if (partialInput.remaining() >= 8) {
      metroHash64_8(partialInput);
    }
    if (partialInput.remaining() >= 4) {
      metroHash64_4(partialInput);
    }
    if (partialInput.remaining() >= 2) {
      metroHash64_2(partialInput);
    }
    if (partialInput.remaining() >= 1) {
      metroHash64_1(partialInput);
    }
    hash ^= rotr64(hash, 28);
    hash *= K0;
    hash ^= rotr64(hash, 29);
    return this;
  }

  static final private long K0 = 0xD6D018F5L;
  static final private long K1 = 0xA2AA033BL;
  static final private long K2 = 0x62992FC1L;
  static final private long K3 = 0x30BC5B29L;

  private void metroHash64_32() {
    v2 ^= rotr64(((v0 + v3) * K0) + v1, 37) * K1;
    v3 ^= rotr64(((v1 + v2) * K1) + v0, 37) * K0;
    v0 ^= rotr64(((v0 + v2) * K0) + v3, 37) * K1;
    v1 ^= rotr64(((v1 + v3) * K1) + v2, 37) * K0;
    hash += v0 ^ v1;
  }

  private void metroHash64_16(ByteBuffer bb) {
    v0 = hash + grab8(bb) * K2; v0 = rotr64(v0, 29) * K3;
    v1 = hash + grab8(bb) * K2; v1 = rotr64(v1, 29) * K3;
    v0 ^= rotr64(v0 * K0, 21) + v1;
    v1 ^= rotr64(v1 * K3, 21) + v0;
    hash += v1;
  }

  private void metroHash64_8(ByteBuffer bb) {
    hash += grab8(bb) * K3;
    hash ^= rotr64(hash, 55) * K1;
  }

  private void metroHash64_4(ByteBuffer bb) {
    hash += grab4(bb) * K3;
    hash ^= rotr64(hash, 26) * K1;
  }

  private void metroHash64_2(ByteBuffer bb) {
    hash += grab2(bb) * K3;
    hash ^= rotr64(hash, 48) * K1;
  }

  private void metroHash64_1(ByteBuffer bb) {
    hash += grab1(bb) * K3;
    hash ^= rotr64(hash, 37) * K1;
  }
}
