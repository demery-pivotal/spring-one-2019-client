/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.pivotal.metrics;

import java.util.Random;
import java.util.stream.IntStream;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

// TODO: Parameters for…
//   - region
//   - batch size
//   - batch count
//   - first
//   - sleep max

public class GenerateMetrics {
  public static void main(String[] args) throws InterruptedException {
    ClientCache cache = new ClientCacheFactory()
        .addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    Region<Integer, String> replicateRegion =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("my-partitioned-region");

    Random random = new Random();
    int first = 10_000;
    int batchSize = 1000;
    int batchCount = 100;

    for(int batch = 0; batch < batchCount; batch++) {
      System.out.println("Starting batch " + batch);
      int start = first + batch * batchSize;
      int end = start + batchSize;

      IntStream.rangeClosed(start, end)
          .forEach(i -> replicateRegion.put(i, "value" + i));

      System.out.println("Sleeping after batch " + batch);
      Thread.sleep(random.nextInt(5_000)); // sleep for up to 5 seconds
      System.out.println("Waking after batch " + batch);
    }

    cache.close();
  }
}
