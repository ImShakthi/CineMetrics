package com.skthvl.cinemetrics.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {
  public static String calculateChecksum(final String filePath, final String algorithm) {
    try {
      Path path = Path.of(ClassLoader.getSystemResource(filePath).toURI());
      MessageDigest digest = MessageDigest.getInstance(algorithm);

      final var inputStream = Files.newInputStream(path, StandardOpenOption.READ);
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        digest.update(buffer, 0, bytesRead);
      }
      byte[] checksumBytes = digest.digest();
      return bytesToHex(checksumBytes);
    } catch (Exception e) {
      log.error("Error getting checksum for file: {}", filePath, e);
    }
    return null;
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexBuilder = new StringBuilder();
    for (byte b : bytes) {
      hexBuilder.append(String.format("%02x", b));
    }
    return hexBuilder.toString();
  }
}
