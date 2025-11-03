package com.gl.testngfw.email;
import com.gl.testngfw.report.model.PropertyModel;
import com.gl.testngfw.utility.FileUtil;

import java.util.Map;

public class EmailConfig {
    private String mailToRecipients;
    private String mailCCRecipients;
    private String mailBCCRecipients;
    private String mailLogoImage;
    private String mailSubject;
    private String mailWelcomeMessage;
    private String mailHeader;
    private String startTimeLabel;
    private String endTimeLabel;
    private String showEnvironmentDetails;
    private String environmentDetailsLabel;
    private Boolean showExecutionSummary;
    private String executionSummaryLabel;
    private Boolean showFeatureWiseBreakUp;
    private String featureWiseBreakUpLabel;
    private String footerText;
    private String passBgColor;
    private String failBgColor;
    private String neBgColor;
    private String passPerBgColor;

    public EmailConfig() {
        Map<String, PropertyModel> mailConfigProperties = FileUtil.getConfigMAP("MailConfig.properties");
        if (mailConfigProperties != null) {
            this.mailToRecipients = mailConfigProperties.containsKey("mailToRecipients") ? mailConfigProperties.get("mailToRecipients").getValue() : "";
            this.mailCCRecipients = mailConfigProperties.containsKey("mailCCRecipients") ? mailConfigProperties.get("mailCCRecipients").getValue() : "";
            this.mailBCCRecipients = mailConfigProperties.containsKey("mailBCCRecipients") ? mailConfigProperties.get("mailBCCRecipients").getValue() : "";
            this.mailLogoImage = mailConfigProperties.containsKey("mailLogoImage") ? mailConfigProperties.get("mailLogoImage").getValue() : "";
            this.mailSubject = mailConfigProperties.containsKey("mailSubject") ? mailConfigProperties.get("mailSubject").getValue() : "";
            this.mailWelcomeMessage = mailConfigProperties.containsKey("mailWelcomeMessage") ? mailConfigProperties.get("mailWelcomeMessage").getValue() : "";
            this.mailHeader = mailConfigProperties.containsKey("mailHeader") ? mailConfigProperties.get("mailHeader").getValue() : "";
            this.startTimeLabel = mailConfigProperties.containsKey("startTimeLabel") ? mailConfigProperties.get("startTimeLabel").getValue() : "";
            this.endTimeLabel = mailConfigProperties.containsKey("endTimeLabel") ? mailConfigProperties.get("endTimeLabel").getValue() : "";
            this.showEnvironmentDetails = mailConfigProperties.containsKey("showEnvironmentDetails") ? mailConfigProperties.get("showEnvironmentDetails").getValue() : "";
            this.environmentDetailsLabel = mailConfigProperties.containsKey("environmentDetailsLabel") ? mailConfigProperties.get("environmentDetailsLabel").getValue() : "";
            this.showExecutionSummary = mailConfigProperties.containsKey("showExecutionSummary") && Boolean.parseBoolean(mailConfigProperties.get("showExecutionSummary").getValue());
            this.executionSummaryLabel = mailConfigProperties.containsKey("executionSummaryLabel") ? mailConfigProperties.get("executionSummaryLabel").getValue() : "";
            this.showFeatureWiseBreakUp = mailConfigProperties.containsKey("showFeatureWiseBreakUp") && Boolean.parseBoolean(mailConfigProperties.get("showFeatureWiseBreakUp").getValue());
            this.featureWiseBreakUpLabel = mailConfigProperties.containsKey("featureWiseBreakUpLabel") ? mailConfigProperties.get("featureWiseBreakUpLabel").getValue() : "";
            this.footerText = mailConfigProperties.containsKey("footerText") ? mailConfigProperties.get("footerText").getValue() : "";
            this.passBgColor = mailConfigProperties.containsKey("passBgColor") ? mailConfigProperties.get("passBgColor").getValue() : "";
            this.failBgColor = mailConfigProperties.containsKey("failBgColor") ? mailConfigProperties.get("failBgColor").getValue() : "";
            this.neBgColor = mailConfigProperties.containsKey("neBgColor") ? mailConfigProperties.get("neBgColor").getValue() : "";
            this.passPerBgColor = mailConfigProperties.containsKey("passPerBgColor") ? mailConfigProperties.get("passPerBgColor").getValue() : "";
        }
    }

    public String getMailToRecipients() {
        return mailToRecipients;
    }

    public void setMailToRecipients(String mailToRecipients) {
        this.mailToRecipients = mailToRecipients;
    }

    public String getMailCCRecipients() {
        return mailCCRecipients;
    }

    public void setMailCCRecipients(String mailCCRecipients) {
        this.mailCCRecipients = mailCCRecipients;
    }

    public String getMailBCCRecipients() {
        return mailBCCRecipients;
    }

    public void setMailBCCRecipients(String mailBCCRecipients) {
        this.mailBCCRecipients = mailBCCRecipients;
    }

    public String getMailLogoImage() {
        return mailLogoImage;
    }

    public void setMailLogoImage(String mailLogoImage) {
        this.mailLogoImage = mailLogoImage;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailWelcomeMessage() {
        return mailWelcomeMessage;
    }

    public void setMailWelcomeMessage(String mailWelcomeMessage) {
        this.mailWelcomeMessage = mailWelcomeMessage;
    }

    public String getMailHeader() {
        return mailHeader;
    }

    public void setMailHeader(String mailHeader) {
        this.mailHeader = mailHeader;
    }

    public String getStartTimeLabel() {
        return startTimeLabel;
    }

    public void setStartTimeLabel(String startTimeLabel) {
        this.startTimeLabel = startTimeLabel;
    }

    public String getEndTimeLabel() {
        return endTimeLabel;
    }

    public void setEndTimeLabel(String endTimeLabel) {
        this.endTimeLabel = endTimeLabel;
    }

    public String getShowEnvironmentDetails() {
        return showEnvironmentDetails;
    }

    public void setShowEnvironmentDetails(String showEnvironmentDetails) {
        this.showEnvironmentDetails = showEnvironmentDetails;
    }

    public String getEnvironmentDetailsLabel() {
        return environmentDetailsLabel;
    }

    public void setEnvironmentDetailsLabel(String environmentDetailsLabel) {
        this.environmentDetailsLabel = environmentDetailsLabel;
    }

    public Boolean getShowExecutionSummary() {
        return showExecutionSummary;
    }

    public void setShowExecutionSummary(Boolean showExecutionSummary) {
        this.showExecutionSummary = showExecutionSummary;
    }

    public String getExecutionSummaryLabel() {
        return executionSummaryLabel;
    }

    public void setExecutionSummaryLabel(String executionSummaryLabel) {
        this.executionSummaryLabel = executionSummaryLabel;
    }

    public Boolean getShowFeatureWiseBreakUp() {
        return showFeatureWiseBreakUp;
    }

    public void setShowFeatureWiseBreakUp(Boolean showFeatureWiseBreakUp) {
        this.showFeatureWiseBreakUp = showFeatureWiseBreakUp;
    }

    public String getFeatureWiseBreakUpLabel() {
        return featureWiseBreakUpLabel;
    }

    public void setFeatureWiseBreakUpLabel(String featureWiseBreakUpLabel) {
        this.featureWiseBreakUpLabel = featureWiseBreakUpLabel;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public String getPassBgColor() {
        return passBgColor;
    }

    public void setPassBgColor(String passBgColor) {
        this.passBgColor = passBgColor;
    }

    public String getFailBgColor() {
        return failBgColor;
    }

    public void setFailBgColor(String failBgColor) {
        this.failBgColor = failBgColor;
    }

    public String getNeBgColor() {
        return neBgColor;
    }

    public void setNeBgColor(String neBgColor) {
        this.neBgColor = neBgColor;
    }

    public String getPassPerBgColor() {
        return passPerBgColor;
    }

    public void setPassPerBgColor(String passPerBgColor) {
        this.passPerBgColor = passPerBgColor;
    }
}
