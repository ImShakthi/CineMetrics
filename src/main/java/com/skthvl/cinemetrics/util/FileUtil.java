package com.skthvl.cinemetrics.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/** Utility class that provides file-related operations such as calculating file checksums. */
@Slf4j
public class FileUtil {
  /**
   * Calculates the checksum of a file using the specified algorithm. This method reads the contents
   * of the file, processes it with the given checksum algorithm, and returns the calculated
   * checksum as a hexadecimal string.
   *
   * @param filePath the path to the file for which the checksum should be calculated
   * @param algorithm the name of the algorithm to use for computing the checksum (e.g., "MD5",
   *     "SHA-256")
   * @return a hexadecimal string representing the checksum of the file, or null if an error occurs
   */
  public static String calculateChecksum(final String filePath, final String algorithm) {
    try {
      final var classLoader = FileUtil.class.getClassLoader();
      Path path = Paths.get(Objects.requireNonNull(classLoader.getResource(filePath)).toURI());
      MessageDigest digest = MessageDigest.getInstance(algorithm);

      final var inputStream = Files.newInputStream(path, StandardOpenOption.READ);
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        digest.update(buffer, 0, bytesRead);
      }
      byte[] checksumBytes = digest.digest();
      return bytesToHex(checksumBytes).trim();
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
