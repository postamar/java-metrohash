/*
 * Copyright (c) 2016 Marius Posta
 *
 * Licensed under the Apache 2 license:
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package util.hash;

import java.nio.ByteBuffer;

public interface MetroHash<T extends MetroHash<T>> {

  /**
   * Applies the instance's Metro hash function to the bytes in the given buffer.
   * This updates this instance's hashing state.
   * @return this
   */
  default T apply(ByteBuffer input) {
    reset();
    while (input.remaining() >= 32) {
      partialApply32ByteChunk(input);
    }
    return partialApplyRemaining(input);
  }

  /**
   * Writes the current hash to the given byte buffer in little-endian order.
   * @return this
   */
  T writeLittleEndian(ByteBuffer output);

  /**
   * Writes the current hash to the given byte buffer in big-endian order.
   * @return this
   */
  T writeBigEndian(ByteBuffer output);

  /**
   * Re-initializes the hashing state.
   * @return this
   */
  T reset();

  /**
   * Consumes a 32-byte chunk from the byte buffer and updates the hashing state.
   *
   * @param partialInput byte buffer with at least 32 bytes remaining.
   * @return this
   */
  T partialApply32ByteChunk(ByteBuffer partialInput);

  /**
   * Consumes the remaining bytes from the byte buffer and updates the hashing state.
   * @param partialInput byte buffer with less than 32 bytes remaining.
   * @return this
   */
  T partialApplyRemaining(ByteBuffer partialInput);

  /**
   * Hashes the input using MetroHash64 with the default seed (0) and returns the resulting state.
   */
  static MetroHash64 hash64(byte[] input) {
    return hash64(ByteBuffer.wrap(input));
  }

  /**
   * Hashes the input using MetroHash64 with the default seed (0) and returns the resulting state.
   */
  static MetroHash64 hash64(ByteBuffer input) {
    return hash64(0, input);
  }

  /**
   * Hashes the input using MetroHash64 with the given seed and returns the resulting state.
   */
  static MetroHash64 hash64(long seed, byte[] input) {
    return hash64(seed, ByteBuffer.wrap(input));
  }

  /**
   * Hashes the input using MetroHash64 with the given seed and returns the resulting state.
   */
  static MetroHash64 hash64(long seed, ByteBuffer input) {
    return new MetroHash64(seed).apply(input);
  }

  /**
   * Hashes the input using MetroHash128 with the default seed (0) and returns the resulting state.
   */
  static MetroHash128 hash128(byte[] input) {
    return hash128(ByteBuffer.wrap(input));
  }

  /**
   * Hashes the input using MetroHash128 with the default seed (0) and returns the resulting state.
   */
  static MetroHash128 hash128(ByteBuffer input) {
    return hash128(0, input);
  }

  /**
   * Hashes the input using MetroHash128 with the given seed and returns the resulting state.
   */
  static MetroHash128 hash128(long seed, byte[] input) {
    return hash128(seed, ByteBuffer.wrap(input));
  }

  /**
   * Hashes the input using MetroHash128 with the given seed and returns the resulting state.
   */
  static MetroHash128 hash128(long seed, ByteBuffer input) {
    return new MetroHash128(seed).apply(input);
  }
}
