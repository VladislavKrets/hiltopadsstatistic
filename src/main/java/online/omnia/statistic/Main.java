package online.omnia.statistic;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 10.04.2018.
 */
public class Main {
    public static int days;
    public static long deltaTime = 24 * 60 * 60 * 1000;

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        if (!args[0].matches("\\d+")) return;
        if (Integer.parseInt(args[0]) == 0) {
            deltaTime = 0;
        }
        days = Integer.parseInt(args[0]);

        List<AccountsEntity> accountsEntities = MySQLDaoImpl.getInstance().getAccountsEntities("hilltopads");
        Gson gson = new Gson();
        String token = "4gMHiFeskgwmd7YWKnJl3vAjZofKlnqNEJo6BtRGaFT59Miqc33ZCamwVSSDhMGD";
        Type type;
        JsonParser parser = new JsonParser();
        JsonElement element;
        Map<String, JsonStatistics> statisticsMap;
        Map<String, JsonAd> adsMap;
        String answer;
        SourceStatisticsEntity sourceStatisticsEntity;
        SourceStatisticsEntity entity;
        Map<String, String> parameters;
        int afid;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (AccountsEntity accountsEntity : accountsEntities) {
            token = accountsEntity.getApiKey();
            answer = HttpMethodUtils.getMethod("https://hilltopads.com/api/advertiser/inventory?key=" + token);
            assert answer != null;
            element = parser.parse(answer);
            type = new TypeToken<Map<String, JsonAd>>() {
            }.getType();
            answer = element.getAsJsonObject().get("result").getAsJsonObject().get("ads").toString();
            adsMap = gson.fromJson(answer, type);
            for (int i = 0; i <= days; i++) {
                type = new TypeToken<Map<String, JsonStatistics>>() {
                }.getType();
                answer = HttpMethodUtils.getMethod("https://hilltopads.com/api/advertiser/listStats?key="
                        + token +
                        "&date="
                        + dateFormat.format(new Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000))
                        + "&group=campaignId");
                assert answer != null;
                element = parser.parse(answer);
                answer = element.getAsJsonObject().get("result").toString().replaceAll("[\\[\\]]", "");
                statisticsMap = gson.fromJson(answer, type);
                for (Map.Entry<String, JsonAd> jsonAdEntry : adsMap.entrySet()) {
                    for (Map.Entry<String, JsonStatistics> jsonStatEntry : statisticsMap.entrySet()) {
                        if (jsonStatEntry.getKey().equals(jsonAdEntry.getValue().getCampaignId())) {
                            sourceStatisticsEntity = new SourceStatisticsEntity();
                            parameters = Utils.getUrlParameters(jsonAdEntry.getValue().getUrl());
                            if (parameters.containsKey("cab")) {
                                if (parameters.get("cab").matches("\\d+")
                                        && MySQLDaoImpl.getInstance().getAffiliateByAfid(Integer.parseInt(parameters.get("cab"))) != null) {
                                    afid = Integer.parseInt(parameters.get("cab"));
                                } else {
                                    afid = 0;
                                }
                            } else afid = 2;
                            sourceStatisticsEntity.setAfid(afid);
                            sourceStatisticsEntity.setAccount_id(accountsEntity.getAccountId());
                            sourceStatisticsEntity.setBuyerId(accountsEntity.getBuyerId());
                            sourceStatisticsEntity.setReceiver("API");
                            sourceStatisticsEntity.setSpent(Double.parseDouble(jsonStatEntry.getValue().getRevenue()));
                            sourceStatisticsEntity.setCampaignId(jsonStatEntry.getKey());
                            sourceStatisticsEntity.setCampaignName(jsonStatEntry.getValue().getCampaignTitle());
                            sourceStatisticsEntity.setClicks(Integer.parseInt(jsonStatEntry.getValue().getClicks()));
                            sourceStatisticsEntity.setConversions(Integer.parseInt(jsonStatEntry.getValue().getConversions()));
                            sourceStatisticsEntity.setImpressions(Integer.parseInt(jsonStatEntry.getValue().getImpressions()));
                            sourceStatisticsEntity.setDate(new java.sql.Date(System.currentTimeMillis() - deltaTime - i * 24L * 60 * 60 * 1000));
                            if (Main.days != 0) {
                                entity = MySQLDaoImpl.getInstance().getSourceStatistics(sourceStatisticsEntity.getAccount_id(),
                                        sourceStatisticsEntity.getCampaignName(), sourceStatisticsEntity.getDate(), sourceStatisticsEntity.getAdsetId());
                                if (entity != null) {
                                    sourceStatisticsEntity.setId(entity.getId());
                                    MySQLDaoImpl.getInstance().updateSourceStatistics(sourceStatisticsEntity);
                                    entity = null;
                                } else MySQLDaoImpl.getInstance().addSourceStatistics(sourceStatisticsEntity);
                            } else {
                                if (MySQLDaoImpl.getInstance().isDateInTodayAdsets(sourceStatisticsEntity.getDate(), sourceStatisticsEntity.getAccount_id(),
                                        sourceStatisticsEntity.getCampaignId(), sourceStatisticsEntity.getAdsetId())) {
                                    MySQLDaoImpl.getInstance().updateTodayAdset(Utils.getAdset(sourceStatisticsEntity));
                                } else MySQLDaoImpl.getInstance().addTodayAdset(Utils.getAdset(sourceStatisticsEntity));

                            }
                        }
                    }
                }
            }
        }
        MySQLDaoImpl.getSessionFactory().close();
    }
}
