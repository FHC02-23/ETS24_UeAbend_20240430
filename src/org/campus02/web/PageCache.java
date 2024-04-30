package org.campus02.web;

import java.io.*;
import java.util.HashMap;

public class PageCache {

    private HashMap<String, WebPage> cache = new HashMap<>();

//    public PageCache() {
//        this.cache = new HashMap<>();
//    }


    public HashMap<String, WebPage> getCache() {
        return cache;
    }

    public WebPage readFromCache(String url) throws CacheMissException{
        if (cache.containsKey(url)) {
            return cache.get(url);
        }
        throw new CacheMissException("url not cached: " + url);
    }

    public void writeToCache(WebPage webPage) {
        cache.put(webPage.getUrl(), webPage);
    }

    public void warmUp(String pathToUrls) {
        try (BufferedReader br = new BufferedReader(
                new FileReader(pathToUrls)
        )) {
            String url;
            while ((url = br.readLine()) != null) {
                try {
                    WebPage webPage = UrlLoader.loadWebPage(url);
                    writeToCache(webPage);
                } catch (UrlLoaderException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
