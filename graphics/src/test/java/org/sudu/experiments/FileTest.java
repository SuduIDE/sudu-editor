package org.sudu.experiments;

import org.sudu.experiments.math.XorShiftRandom;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileTest {
  public static void main(String[] args) throws IOException {
    Path path = Path.of("temp", "filename");
    System.out.println("path.toAbsolutePath() = " + path.toAbsolutePath());
    Files.createDirectories(path.getParent());
    XorShiftRandom xr = new XorShiftRandom(1,2);
    byte[] data = new byte[1024*1024*64];
    xr.fill(data);
    Files.write(path, data);

    byte[] kb16 = new byte[16*1024];
    int[] hash = new int[data.length / kb16.length];
    for (int i = 0; i < hash.length; i++) {
      System.arraycopy(data, kb16.length * i, kb16, 0, kb16.length);
      hash[i] = Arrays.hashCode(kb16);
    }

    StringBuilder sb = new StringBuilder(16 * hash.length);
    for (int i = 0; i < hash.length; i++) {
      int ip8 = i % 8;
      if (ip8 == 0) {
        sb.append('[').append(i).append("] ");
      }
      sb.append("0x").append(Integer.toHexString(hash[i]));
      if (ip8 == 7) {
        sb.append(", \n");
      } else {
        sb.append(", ");
      }
    }

    System.out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    System.out.println();
//    Files.write(path)
  }
}
