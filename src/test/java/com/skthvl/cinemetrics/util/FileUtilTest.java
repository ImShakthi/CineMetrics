package com.skthvl.cinemetrics.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileUtilTest {
  @Test
  void getChecksum() {
    final var checksum = FileUtil.calculateChecksum("test-data-academy-awards.csv", "SHA-256");

    assertNotNull(checksum);
    assertEquals("20d8c106f350a5dc44f92b716243047e0b4f73f02257a769e2a16c9cae518273", checksum);
  }
}
