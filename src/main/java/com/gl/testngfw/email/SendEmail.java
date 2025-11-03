package com.gl.testngfw.email;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.openqa.selenium.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class SendEmail {

    private static final Logger LOGGER = Logger.getLogger(SendEmail.class.getName());
    private static ListMultimap<String, ArrayList<String>> componentsList = ArrayListMultimap.create();
    private static ListMultimap<String, ArrayList<String>> platformList = ArrayListMultimap.create();
    private static Map<String, ListMultimap<String, ArrayList<String>>> executionList = new HashMap<>();
    private static String startTime = "";
    private static Map<String, PropertyModel> projectConfig = FileUtil.getConfigMAP("ProjectConfig.properties");
    private static Map<String, PropertyModel> project = FileUtil.getConfigMAP("Project.properties");
    private static Map<String, PropertyModel> mailConfig = FileUtil.getConfigMAP("MailConfig.properties");

    private SendEmail() {
    }

    static String createMailContents() {
        getConfigMap();

        StringBuilder mainContents = new StringBuilder();
        try {
            mainContents.append("<body bgcolor='#FFFFFF'><font align='justify' color='black' face='Garamond' background-color='gray'>").
                    append("<table>").
                    append("<tr>").
                    append("<td style=\"width:85%\" >").
                    append(mailConfig.get("mailWelcomeMessage").getValue()).
                    append("</td>").
                    append("<td rowspan=\"2\">").
                    append("<img src=\"cid:image\" height=\"120\" style=\"margin-top:-63px;\">").
                    append("</td>").
                    append("</tr>").
                    append("<tr>").
                    append("<td style=\"width:85%\" >").
                    append("<h3>").
                    append(mailConfig.get("mailHeader").getValue()).
                    append("</h3></font>").
                    append("</td>").
                    append("</tr>").
                    append("</table>").
                    append("<hr><font align='justify' face='Garamond'>").
                    append("<table>").
                    append("<tr><td><h4><u>").
                    append(mailConfig.get("executionTimeHeader").getValue()).
                    append("</u></h4></td></tr><tr><td><b>").
                    append(mailConfig.get("startTimeLabel").getValue()).
                    append("</b></td><td>").
                    append(startTime).
                    append("</td></tr>");
            mainContents.append("<tr><td><b>").
                    append(mailConfig.get("endTimeLabel").getValue()).
                    append("</b> </td><td>").
                    append(Util.getEndTime()).
                    append("</td></tr></table>");
            showEnvDetails(projectConfig, project, mainContents);
            showExecutionSummary(mainContents);
            showFeatureWiseBreakUp(mainContents);
            mainContents.append(mailConfig.get("footerText").getValue());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return mainContents.toString();
    }

    private static void showEnvDetails(Map<String, PropertyModel> projectConfig, Map<String, PropertyModel> project, StringBuilder mainContents) {
        if ("true".equalsIgnoreCase(mailConfig.get("showEnvironmentDetails").getValue())) {
            mainContents.append(" <h4><u> ").
                    append(mailConfig.get("environmentDetailsLabel").getValue()).
                    append("</u></h4>  ").
                    append("<table style=\"width:70%\" border='1'><tr><th >PLATFORMS</th><th >BUILD</th><th >DEVICE OS</th><th >ENVIRONMENT</th><th >URL</th></tr>\n");
            int s;
            for (String key : platformList.keySet()) {
                s = platformList.get(key).get(0).size() - 1;
                if (key.contains("iPhone") || key.contains("iPad")) {
                    mainContents.append("<tr><td>").
                            append("Mobile_iOS").
                            append("</td><td>").
                            append(projectConfig != null ? projectConfig.get("Env").getValue() : null).
                            append("</td><td>").
                            append(componentsList.get(key).get(0).get(s)).
                            append("</td><td>").
                            append(key).
                            append("</td><td>").
                            append(project != null ? project.get("bundleID").getValue() : null).
                            append("</td></tr>");
                } else if (isWeb(key)) {
                    mainContents.append("<tr><td>").
                            append("Web").
                            append("</td><td>").
                            append(projectConfig.get("Env").getValue()).
                            append("</td><td>").append("NA").
                            append("</td><td>").
                            append(key).append("</td><td>").
                            append(projectConfig.get("BaseURL").getValue()).
                            append("</td></tr>");
                } else if (key.equalsIgnoreCase(Constants.API)) {
                    mainContents.append("<tr><td>").
                            append("REST_API").
                            append("</td><td>").
                            append(projectConfig.get("Env").getValue()).
                            append("</td><td>").
                            append("NA").
                            append("</td><td>").
                            append(key).
                            append("</td><td>").
                            append(projectConfig.get("API_BaseURL").getValue()).
                            append("</td></tr>");
                } else if (key.equalsIgnoreCase(Constants.CONTRACT)) {
                    mainContents.append("<tr><td>").
                            append("Contract").
                            append("</td><td>").
                            append(projectConfig.get("Env").getValue()).
                            append("</td><td>").
                            append("NA").
                            append("</td><td>").
                            append(key).
                            append("</td><td>").
                            append(projectConfig.get("API_BaseURL").getValue()).
                            append("</td></tr>");
                } else {
                    mainContents.append("<tr><td>").
                            append("Mobile_Android").
                            append("</td><td>").
                            append(projectConfig.get("Env").getValue()).
                            append("</td><td>").
                            append(platformList.get(key).get(0).get(s)).
                            append("</td><td>").
                            append(key).
                            append("</td><td>").
                            append(project.get("packageName").getValue()).
                            append("</td></tr>");
                }
            }
            mainContents.append("</table>");
        }
    }

    private static boolean isWeb(String key) {
        return Constants.CHROME.equalsIgnoreCase(key) || Constants.SAFARI.equalsIgnoreCase(key) ||
                Constants.FIREFOX.equalsIgnoreCase(key) || Constants.INTERNET_EXPLORER.equalsIgnoreCase(key) || Constants.MICROSOFT_EDGE.equalsIgnoreCase(key);
    }

    private static void showFeatureWiseBreakUp(StringBuilder mainContents) {
        if ("true".equalsIgnoreCase(mailConfig.get("showFeatureWiseBreakUp").getValue())) {
            mainContents.append("<h4><u>").append(mailConfig.get("featureWiseBreakUpLabel").getValue()).append("</u> </h4>");
            for (String platform : executionList.keySet()) {
                mainContents.append(createComponentWiseTable(platform));
            }
        }
    }

    private static void showExecutionSummary(StringBuilder mainContents) {
        if ("true".equalsIgnoreCase(mailConfig.get("showExecutionSummary").getValue())) {
            mainContents.append("<h4><u>").append(mailConfig.get("executionSummaryLabel").getValue()).append("</u> </h4>");

            mainContents.append(createExecutionSummary());
        }
    }

    private static String createComponentWiseTable(String platform) {
        StringBuilder htmlComponentContent = new StringBuilder();
        Set<String> browser = new TreeSet<>();
        for (String key : executionList.get(platform).keySet()) {
            String str = key.split("@")[0];
            browser.add(str);
        }
        htmlComponentContent.append("<table style=\"width:70%\" border='1'><tr><th align=\"center\" style=\"font-size: 16px;\" rowspan=2>Functionality</th>");
        htmlComponentContent.append("<th align=\"center\" style=\"font-size: 16px;\" colspan=5>").append(platform).append("</th>");
        htmlComponentContent.append("</tr><tr>");
        htmlComponentContent.append("<th align=\"center\">TOTAL</th><th align=\"center\">PASS</th><th align=\"center\">FAIL</th><th align=\"center\">SKIP</th><th align=\"center\">PASS %</th>");
        StringBuilder htmlComponentContentBuilder = new StringBuilder(htmlComponentContent + "</tr>");
        for (String br : browser) {
            LOGGER.log(Level.WARNING, "browser11" + br);

            for (String key : executionList.get(platform).keySet()) {
                if (key.contains(br)) {
                    List<ArrayList<String>> value = executionList.get(platform).get(key);
                    for (ArrayList<String> temp : value) {
                        int passPer;
                        int total = Integer.parseInt(temp.get(1)) + Integer.parseInt(temp.get(2)) + Integer.parseInt(temp.get(3));
                        if (total > 0) {
                            LOGGER.log(Level.WARNING, "Pass ::" + Integer.parseInt(temp.get(1)) + " Fail :: " +
                                    Integer.parseInt(temp.get(2)) + " Skip :: " + Integer.parseInt(temp.get(3)));
                            int passCount = Integer.parseInt(temp.get(1));
                            int failCount = Integer.parseInt(temp.get(2));
                            int skipCount = Integer.parseInt(temp.get(3));
                            passPer = (passCount * 100) / (passCount + failCount + skipCount);
                        } else
                            passPer = 0;
                        LOGGER.log(Level.WARNING, "Pass Percentage :: " + passPer);
                        String result1 = temp.get(0).substring(temp.get(0).lastIndexOf('.') + 1).trim();
                        htmlComponentContentBuilder.append("<tr> <td align=\"center\">").append(result1).append("</td>");
                        htmlComponentContentBuilder.append("<td align=\"center\"'>").append(total).append("</td>");
                        htmlComponentContentBuilder.append("<td align=\"center\" bgcolor='").append(mailConfig.get("passBgColor").getValue()).
                                append("'>").append(temp.get(1)).append("</td>").append("<td align=\"center\" bgcolor='").
                                append(mailConfig.get("failBgColor").getValue()).append("'>").append(temp.get(2)).append("</td>").
                                append("<td align=\"center\" bgcolor='").append(mailConfig.get("neBgColor").getValue()).append("'>").
                                append(temp.get(3)).append("</td>").append("<td align=\"center\" bgcolor='").
                                append(mailConfig.get("passPerBgColor").getValue()).append("'>").append(passPer).append(" %</td></tr>");
                        LOGGER.log(Level.WARNING, temp.get(0) + "-" + temp.get(1) + "-" + temp.get(2) + "-" + temp.get(3));
                    }
                }
            }
            htmlComponentContent = new StringBuilder(htmlComponentContentBuilder.toString());
            htmlComponentContent.append("</table>");
            htmlComponentContent.append("<br />");
        }

        return htmlComponentContent.toString();
    }

    private static String createExecutionSummary() {
        String htmlComponentContent = "";
        htmlComponentContent = htmlComponentContent + "<table style=\"width:70%\" border='1'><tr><th align=\"center\">PLATFORM</th>";
        htmlComponentContent = htmlComponentContent + "<th align=\"center\">TOTAL</th>";
        htmlComponentContent = htmlComponentContent + "<th align=\"center\">PASS</th><th align=\"center\">FAIL</th>" +
                "<th align=\"center\">SKIP</th><th align=\"center\">PASS %</th>";
        StringBuilder htmlComponentContentBuilder = new StringBuilder(htmlComponentContent + "</tr>");
        for (String key : platformList.keySet()) {
            List<ArrayList<String>> pList = platformList.get(key);
            for (ArrayList<String> temp : pList) {
                int passPer;
                int total = Integer.parseInt(temp.get(1)) + Integer.parseInt(temp.get(2)) + Integer.parseInt(temp.get(3));
                if (total > 0) {
                    LOGGER.log(Level.INFO, "Pass ::" + Integer.parseInt(temp.get(1)) + " Fail :: " +
                            Integer.parseInt(temp.get(2)) + " Skip :: " + Integer.parseInt(temp.get(3)));
                    int passCount = Integer.parseInt(temp.get(1));
                    int failCount = Integer.parseInt(temp.get(2));
                    int skipCount = Integer.parseInt(temp.get(3));
                    passPer = (passCount * 100) / (passCount + failCount + skipCount);
                } else {
                    passPer = 0;
                }
                LOGGER.log(Level.INFO, " Execution Pass Percentage :: " + passPer);
                htmlComponentContentBuilder.append("<tr> <td align=\"center\">").append(key).append("</td>");
                htmlComponentContentBuilder.append("<td align=\"center\">").append(total).append("</td>");
                htmlComponentContentBuilder.append("<td align=\"center\" bgcolor='").append(mailConfig.get("passBgColor").getValue()).
                        append("'>").append(temp.get(1)).append("</td>").append("<td align=\"center\" bgcolor='").
                        append(mailConfig.get("failBgColor").getValue()).append("'>").append(temp.get(2)).append("</td>").
                        append("<td align=\"center\" bgcolor='").append(mailConfig.get("neBgColor").getValue()).append("'>").
                        append(temp.get(3)).append("</td>").append("<td align=\"center\" bgcolor='").
                        append(mailConfig.get("passPerBgColor").getValue()).append("'>").append(passPer).
                        append(" %</td></tr>");
                LOGGER.log(Level.INFO, temp.get(0) + "-" + temp.get(1) + "-" + temp.get(2));
            }
        }
        htmlComponentContent = htmlComponentContentBuilder.toString();
        htmlComponentContent = htmlComponentContent + "</table>";
        htmlComponentContent = htmlComponentContent + "</br>";
        return htmlComponentContent;
    }

    public static void main(String[] args) {
        String mailContents = createMailContents();
        try {
            File newHtmlFile = new File("Reports\\Template.html");
            FileUtils.writeStringToFile(newHtmlFile, mailContents, String.valueOf(Charset.defaultCharset()));

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    private static void getConfigMap() {
        boolean isStartTimeSet = false;
        ResultResponseJson result = getResultResponseJson();
        if (result != null) {
            for (PlatformJson platform : result.getPlatformJsonList()) {
                ArrayList<String> classCount = getClassCount(platform);
                if (!isStartTimeSet) {
                    SendEmail.startTime = result.getStartTime();
                    isStartTimeSet = true;
                }
                platformList.put(platform.getPlatformName(), classCount);
                getFeatureJson(platform);
                executionList.put(platform.getPlatformName(), componentsList);
            }
        }
    }

    @NotNull
    private static ArrayList<String> getClassCount(PlatformJson platform) {
        String pcount = platform.getPassTestCase() == null ? "0" : platform.getPassTestCase();
        String fcount = platform.getFailTestCase() == null ? "0" : platform.getFailTestCase();
        String scount = platform.getSkipTestCase() == null ? "0" : platform.getSkipTestCase();
        return new ArrayList<>(Arrays.asList(platform.getPlatformName(), pcount, fcount,
                scount, ""));
    }

    private static void getFeatureJson(PlatformJson platform) {
        componentsList = ArrayListMultimap.create();
        String pcount;
        String fcount;
        String scount;
        for (FeatureJson featureJson : platform.getFeatureJsons()) {
            pcount = featureJson.getPassTestCase() == null ? "0" : featureJson.getPassTestCase();
            fcount = featureJson.getFailTestCase() == null ? "0" : featureJson.getFailTestCase();
            scount = featureJson.getSkipTestCase() == null ? "0" : featureJson.getSkipTestCase();
            ArrayList<String> featureCount = new ArrayList<>(Arrays.asList(featureJson.getFeatureName(),
                    pcount, fcount, scount, platform.getPlatformName()));
            componentsList.put(featureJson.getFeatureName(), featureCount);
        }
    }

    private static ResultResponseJson getResultResponseJson() {
        ResultResponseJson result = null;
        boolean status = Boolean.parseBoolean(project.get("useDB").getValue());
        if (status) {
            DBUpdater dbUpdater = new DBUpdater();
            result = dbUpdater.getResultStatus(Constants.getExecutionId());
        } else {
            try {
                result = getLocalResultData();
            } catch (NotFoundException e) {
                LOGGER.log(Level.WARNING, "", e);
            }

        }
        return result;
    }

    private static ResultResponseJson getLocalResultData() throws NotFoundException {

        List<Result> resultList = new ArrayList<>();
        Execution execution = null;
        try {
            JSONArray finalArray = Util.getExecutionResultList();

            for (Object obj : finalArray) {
                Gson gson = new Gson();
                execution = gson.fromJson(obj.toString(), Execution.class);
                resultList.addAll(execution.getResultsList());
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }

        if (execution != null) {
            ResultResponseJson responseData = new ResultResponseJson();
            responseData.setExecutionId(execution.getExecutionId());
            responseData.setExecutionDate(execution.getExecutionDate());
            responseData.setStartTime(execution.getStartTime());
            responseData.setEndTime(execution.getEndTime());
            responseData.setSuiteName(execution.getSuiteName());

            PlatformJson platformJson = null;
            List<PlatformJson> platformJsonList = new ArrayList<PlatformJson>();

            List<List<Result>> collections = resultList.stream()
                    .collect(Collectors.groupingBy(x -> x.getPlatformName()))
                    .entrySet().stream()
                    .map(e -> {
                        List<Result> c = new ArrayList<Result>();
                        c.addAll(e.getValue());
                        return c;
                    })
                    .collect(Collectors.toList());
            for (List<Result> list : collections) {
                platformJson = new PlatformJson();
                platformJson.setPassTestCase(String.valueOf(list.stream().filter(x -> "Pass".equals(x.getStatus())).count()));
                platformJson.setFailTestCase(String.valueOf(list.stream().filter(x -> "Fail".equals(x.getStatus())).count()));
                platformJson.setSkipTestCase(String.valueOf(list.stream().filter(x -> "Skip".equals(x.getStatus())).count()));
                platformJson.setTotalTestCase(String.valueOf(list.size()));
                platformJson.setPlatformName(list.get(0).getPlatformName());

                List<List<Result>> featureCollection = list.stream()
                        .collect(Collectors.groupingBy(x -> x.getFeatureName()))
                        .entrySet().stream()
                        .map(e -> {
                            List<Result> c = new ArrayList<Result>();
                            c.addAll(e.getValue());
                            return c;
                        })
                        .collect(Collectors.toList());
                List<FeatureJson> featureJsonList = new ArrayList<>();
                for (List<Result> feature : featureCollection) {
                    FeatureJson featureJson = new FeatureJson();
                    featureJson.setPassTestCase(String.valueOf(feature.stream().filter(x -> "Pass".equals(x.getStatus())).count()));
                    featureJson.setFailTestCase(String.valueOf(feature.stream().filter(x -> "Fail".equals(x.getStatus())).count()));
                    featureJson.setSkipTestCase(String.valueOf(feature.stream().filter(x -> "Skip".equals(x.getStatus())).count()));
                    featureJson.setTotalTestCase(String.valueOf(feature.size()));
                    featureJson.setFeatureName(feature.get(0).getFeatureName());
                    featureJsonList.add(featureJson);
                }
                platformJson.setFeatureJsons(featureJsonList);
                platformJsonList.add(platformJson);
            }
            responseData.setPlatformJsonList(platformJsonList);
            return responseData;
        } else {
            throw new NotFoundException("No execution found");
        }
    }
}
