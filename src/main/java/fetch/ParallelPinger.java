package fetch;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//Perhaps this you go into a separate file
class HTTPFetcher implements Callable<String> {

    String url;

    HTTPFetcher(String url) {
        this.url = url;
    }

    public String getSwappiData() throws MalformedURLException, IOException {
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("User-Agent", "server");
        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext()) {
            jsonStr = scan.nextLine();
        }
        scan.close();
        return jsonStr;
    }

    @Override
    public String call() throws Exception {
        return getSwappiData();
    }
}

public class ParallelPinger {

    public static String getJsonFromAllServers(String param) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();

        List<Future<String>> futures = new ArrayList<>();


            Callable<String> pingUrlCallable = new HTTPFetcher("https://magnusklitmose.com/Flights-1.0/api/flight/" + param);
            System.out.println("https://magnusklitmose.com/Flights-1.0/api/flight/" + param);
            Future<String> fut = executor.submit(pingUrlCallable);
            futures.add(fut);

        /* Callable<String> pingUrlCallable = new HTTPFetcher("https://magnusklitmose.com/flights-1.0/api/flight/%22);
        Future<String> future = executor.submit(pingUrlCallable);
        futures.add(future);*/

        List<String> results = new ArrayList();
        for (Future<String> future : futures) {
            String result = future.get(2, TimeUnit.MINUTES);
            results.add(result);
        }
        executor.shutdown();

        String jsonString = "";
        for (String r : results) {
            jsonString += r;
        }
        return jsonString;
    }

    public static void main(String[] args) throws Exception {
        /* long timeStart = System.nanoTime();
        List<String> results = getJsonFromAllServers("people");
        String jsonString = "[";
        for (String r : results) {
            jsonString += r;
        }
        jsonString += "]";

        long timeEnd = System.nanoTime();
        long total = (timeEnd - timeStart) / 1_000_000;
        System.out.println("Time to check URLS: " + total + "ms.");*/
    }
}