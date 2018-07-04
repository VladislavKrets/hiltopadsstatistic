package online.omnia.statistic;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 09.08.2017.
 */
public class Utils {
    private static FileWriter postbackURLWriter;

    static {
        try {
            File file = new File("error_urls.log");
            if (!file.exists()) file.createNewFile();

            postbackURLWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static FileWriter writer;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");

    public static void writeLog(String text) {
        try { if (writer == null) writer = new FileWriter("MGIDLog.log", true);
            writer.write(dateFormat.format(new Date()) + "\n" + text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized void write(String url){
        try {
            postbackURLWriter.write(url + "\n");
            postbackURLWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized Map<String, String> iniFileReader() {
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader iniFileReader = new BufferedReader(new FileReader("sources_stat.ini"))) {
            String property;
            String[] propertyArray;
            while ((property = iniFileReader.readLine()) != null) {
                propertyArray = property.split("=");
                if (property.contains("=")) {
                    properties.put(propertyArray[0], propertyArray[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Map<String, String> getUrlParameters(String url) {

        System.out.println(url);
        Map<String, String> parametersMap = new HashMap<>();
        if (url == null || url.isEmpty()) return parametersMap;

        String[] urlParts = url.split("\\?");

        if (urlParts.length != 2) {
            System.out.println("No ?");
            System.out.println(Arrays.asList(urlParts));
            return parametersMap;
        }

        String parameters = urlParts[1];
        if (!parameters.contains("&")) {
            System.out.println("Not found &");
            String[] pair = parameters.split("=");
            if (pair.length == 0) return parametersMap;
            if (pair.length == 2) {
                parametersMap.put(pair[0], pair[1]);
            } else if (pair.length == 1) {
                parametersMap.put(pair[0], "");
            }
            return parametersMap;
        }
        String[] keyValuePairs = parameters.split("&");
        String[] pairs;

        for (String keyValuePair : keyValuePairs) {
            pairs = keyValuePair.split("=");
            if (pairs.length == 2) {
                parametersMap.put(pairs[0], pairs[1]);
            } else if (pairs.length == 1) {
                parametersMap.put(pairs[0], "");
            }
        }
        System.out.println("Parameters have been got");
        return parametersMap;
    }

    public static AdsetEntity getAdset(SourceStatisticsEntity abstractAdsetEntity) {
        AdsetEntity adsetEntity = new AdsetEntity();
        adsetEntity.setDate(abstractAdsetEntity.getDate());
        adsetEntity.setCampaignName(abstractAdsetEntity.getCampaignName());
        adsetEntity.setSpent(abstractAdsetEntity.getSpent());
        adsetEntity.setClicks(abstractAdsetEntity.getClicks());
        adsetEntity.setConversions(abstractAdsetEntity.getConversions());
        adsetEntity.setReceiver(abstractAdsetEntity.getReceiver());
        adsetEntity.setAccountId(abstractAdsetEntity.getAccount_id());
        adsetEntity.setCpc(abstractAdsetEntity.getCpc());
        adsetEntity.setAdsetId(abstractAdsetEntity.getAdsetId());
        adsetEntity.setAfid(abstractAdsetEntity.getAfid());
        adsetEntity.setBuyerId(abstractAdsetEntity.getBuyerId());
        return adsetEntity;
    }
}
