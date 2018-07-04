package online.omnia.statistic;

/**
 * Created by lollipop on 10.04.2018.
 */
public class JsonAd {
    private String adId;
    private String campaignId;
    private String url;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "JsonAd{" +
                "adId='" + adId + '\'' +
                ", campaignId='" + campaignId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
