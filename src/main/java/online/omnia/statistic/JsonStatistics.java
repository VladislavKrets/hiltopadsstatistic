package online.omnia.statistic;

/**
 * Created by lollipop on 10.04.2018.
 */
public class JsonStatistics {
    private String revenue;
    private String currency;
    private String impressions;
    private String clicks;
    private String conversions;
    private String cpm;
    private String campaignTitle;
    private String subId;

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getConversions() {
        return conversions;
    }

    public void setConversions(String conversions) {
        this.conversions = conversions;
    }

    public String getCpm() {
        return cpm;
    }

    public void setCpm(String cpm) {
        this.cpm = cpm;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    @Override
    public String toString() {
        return "JsonStatistics{" +
                "revenue='" + revenue + '\'' +
                ", currency='" + currency + '\'' +
                ", impressions='" + impressions + '\'' +
                ", clicks='" + clicks + '\'' +
                ", conversions='" + conversions + '\'' +
                ", cpm='" + cpm + '\'' +
                ", campaignTitle='" + campaignTitle + '\'' +
                ", subId='" + subId + '\'' +
                '}';
    }
}
