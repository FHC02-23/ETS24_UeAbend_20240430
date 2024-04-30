package org.campus02.web;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class WebProxy {

    private PageCache cache;
    private int numCacheHits = 0;
    private int numCacheMisses = 0;

    public WebProxy() {
        this.cache = new PageCache();
    }

    public WebProxy(PageCache cache) {
        this.cache = cache;
    }

    public WebPage fetch(String url) throws UrlLoaderException {
        try {
            WebPage webPage = cache.readFromCache(url);
            numCacheHits++;
            return webPage;
        } catch (CacheMissException e) {
            numCacheMisses++;
            WebPage webPage = UrlLoader.loadWebPage(url);
            cache.writeToCache(webPage);
            return webPage;
        }
    }

    public String statsHits() {
        return "stats hits: " + numCacheHits;
    }

    public String statsMisses() {
        return "stats misses: " + numCacheMisses;
    }

    public boolean writePageCacheToFile(String pathToFile) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(pathToFile)
        )) {
            // entryset
            for (Map.Entry<String, WebPage> webPageEntry : cache.getCache().entrySet()) {
                String url = webPageEntry.getKey();
                WebPage webPage = webPageEntry.getValue();
                bw.write(url + ";" + webPage.getContent());
                bw.newLine();
            }
            // keyset
//            for (String url : cache.getCache().keySet()) {
//                WebPage webPage = cache.getCache().get(url);
//                bw.write(url + ";" + webPage.getContent());
//            }
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
