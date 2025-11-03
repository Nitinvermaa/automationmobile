package com.gl.testngfw.api;

import com.gl.testngfw.logging.FrameworkLogger;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAPIExtension {
    private static List<Filter> filterList = new ArrayList<>();

    public RestAPIExtension(boolean enableConsoleLog) {
        ignoreSSLValidation();
        filterList.clear();
        if (enableConsoleLog) {
            filterList.add(new RequestLoggingFilter(LogDetail.ALL));
            filterList.add(new ResponseLoggingFilter(LogDetail.ALL));
        }
        filterList.add(new RequestLoggingFilter(FrameworkLogger.getPrintStream()));
        filterList.add(new ResponseLoggingFilter(FrameworkLogger.getPrintStream()));
        filterList.add(new ErrorLoggingFilter(FrameworkLogger.getPrintStream()));
    }

    /**
     * Method to update JSON values for respective key
     *
     * @param path:       JSON path for the Key
     * @param value:      Value to be change
     * @param jsonString: JSON Body
     * @return Updated JSON Body
     */
    public static String setJsonData(String path, Object value, String jsonString) {
        return JsonPath.with(jsonString).param(path, value).toString();
    }

    /**
     * Method to generate JSON Object from the JSON File
     *
     * @param path: File Path
     * @return JSON Object
     * @throws IOException IOException
     */
    public static JSONObject getJsonObjectFromFile(String path) throws IOException {
        File file = new File(path);
        return JsonPath.from(file).getJsonObject(".");
    }

    /**
     * Method for API Schema Validation
     *
     * @param response:       API ValidatableResponse
     * @param schemaFilePath: Schema file for API. Schema can be generated from http://jsonschema.net/
     *                        Copy the API JSON response to generate schema file
     */
    public static void schemaValidation(ValidatableResponse response, String schemaFilePath) {
        response.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFilePath));
    }

    /**
     * Post Request with body in JSON format
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @param body:   RequestBody in JSON Format
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse postRequest(String url, Map<String, ?> header, String body) {
        return given().body(body).headers(header).filters(filterList)
                .when()
                .post(url)
                .then();
    }

    /**
     * Post Request with body Map
     *
     * @param url    :    Request URL
     * @param header : Header Data for the request
     * @param body   :   RequestBody in Map
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse postRequest(String url, Map<String, ?> header, Map<String, ?> body) {
        return given().body(body).headers(header).filters(filterList)
                .when()
                .post(url)
                .then();
    }

    /**
     * Post Request with Param Map
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @return RestAssured Validatable response in JSON Format
     */

    public ValidatableResponse postRequestWithParameter(String url, Map<String, ?> header, Map<String, ?> parameter) {
        return given().headers(header).params(parameter).filters(filterList)
                .when()
                .post(url)
                .then();
    }

    /**
     * Post Request with Param Map
     *
     * @param url:       Request URL
     * @param parameter: Parameter Data for the request
     * @return RestAssured Validatable response in JSON Format
     */

    public ValidatableResponse postRequestWithParameter(String url, Map<String, ?> parameter) {
        return given().params(parameter).filters(filterList)
                .when()
                .post(url)
                .then();
    }

    /**
     * Post Request with Parameters
     *
     * @param url:        Request URL
     * @param header:     Header Data for the request
     * @param body:       RequestBody in Map
     * @param parameters: Request Parameters
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse postRequest(String url, Map<String, ?> header, Map<String, ?> body, Map<String, ?> parameters) {
        return given().body(body).headers(header).params(parameters).filters(filterList)
                .when()
                .post(url)
                .then();
    }

    /**
     * Post Request
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse postRequest(String url, Map<String, ?> header) {
        return given().headers(header).filters(filterList)
                .when()
                .post(url)
                .then();
    }

    /**
     * Put Request with body
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @param body:   RequestBody in JSON String format
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse putRequest(String url, Map<String, ?> header, String body) {
        return given().body(body).headers(header).filters(filterList)
                .when()
                .put(url)
                .then();
    }

    /**
     * Put Request with body
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @param body:   RequestBody in Map
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse putRequest(String url, Map<String, ?> header, Map<String, ?> body) {
        return given().body(body).headers(header).filters(filterList)
                .when()
                .put(url)
                .then();
    }

    /**
     * Put Request with Parameters
     *
     * @param url:        Request URL
     * @param header:     Header Data for the request
     * @param body:       RequestBody in Map
     * @param parameters: Request Parameters
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse putRequest(String url, Map<String, ?> header, Map<String, ?> body, Map<String, ?> parameters) {
        return given().body(body).headers(header).params(parameters).filters(filterList)
                .when()
                .put(url)
                .then();
    }

    /**
     * Put Request
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse putRequest(String url, Map<String, ?> header) {
        return given().headers(header).filters(filterList)
                .when()
                .put(url)
                .then();
    }

    /**
     * Put Request
     *
     * @param url:        Request URL
     * @param parameters: Parameter Data for the request
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse putRequestWithParameters(String url, Map<String, ?> parameters) {
        return given().params(parameters).filters(filterList)
                .when()
                .put(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse getRequest(String url, Map<String, ?> header) {
        return given().headers(header).filters(filterList)
                .when()
                .get(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url:        Request URL
     * @param parameters: request Parameters
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse getRequestWithPathVariable(String url, Map<String, ?> headers, Map<String, ?> parameters) {
        return given().pathParams(parameters).headers(headers).filters(filterList)
                .when()
                .get(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url:        Request URL
     * @param parameters: request Parameters
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse getRequestWithParameters(String url, Map<String, ?> parameters) {
        return given().params(parameters).filters(filterList)
                .when()
                .get(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url:        Request URL
     * @param parameters: request Parameters
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse getRequestWithParameters(String url, Map<String, ?> header, Map<String, ?> parameters) {
        return given().params(parameters).headers(header).filters(filterList)
                .when()
                .get(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url: Request URL
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse getRequest(String url) {
        return given().filters(filterList)
                .when()
                .get(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url: Request URL
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse deleteRequest(String url) {
        return given().filters(filterList)
                .when()
                .delete(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse deleteRequest(String url, Map<String, ?> header) {
        return given().headers(header).filters(filterList)
                .when()
                .delete(url)
                .then();
    }

    /**
     * Get Request
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @param body:   RequestBody in Map
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse deleteRequest(String url, Map<String, ?> header, String body) {
        return given().headers(header).body(body).filters(filterList)
                .when()
                .delete(url)
                .then();
    }

    public ValidatableResponse deleteRequest(String url, Map<String, ?> header, Map<String, ?> parameter) {
        return given().params(parameter).headers(header).filters(filterList)
                .when()
                .delete(url)
                .then();
    }

    /**
     * Post Request with multipart for File upload
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @param body:   RequestBody in Map
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse uploadFile(String url, Map<String, ?> header, String body, File file) {
        return given().multiPart("metadata", body, "application/json").multiPart(file)
                .headers(header).filters(filterList)//"Content-Type", "multipart/related;boundary=*****")
                .when()
                .post(url)
                .then();
    }

    /**
     * Post Request with multipart for File upload
     *
     * @param url:    Request URL
     * @param header: Header Data for the request
     * @return RestAssured Validatable response in JSON Format
     */
    public ValidatableResponse uploadFile(String url, Map<String, ?> header, Map<String, ?> parameters, File file) {
        return given().multiPart(file).headers(header)
                .params(parameters).filters(filterList)//"Content-Type", "multipart/related;boundary=*****")
                .when()
                .post(url)
                .then();
    }

    /**
     * Method to generate JSON Object from the JSON File
     *
     * @param path: File Path
     * @return JSON Object
     * @throws IOException IOException
     */
    public String getJsonStringFromFile(String path) throws IOException {
        File file = new File(path);
        return JsonPath.from(file).prettify();
    }

    /**
     * Method for Ignore SSL Validation
     */
    private void ignoreSSLValidation() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    /**
     * Method to API status code
     *
     * @param response: Validatable Response object
     * @return : status code
     */
    public int getResponseStatusCode(ValidatableResponse response) {
        return response.extract().statusCode();
    }

    /**
     * Method to get specific Key value from JSON
     *
     * @param response: Validatable Response object
     * @param key:      Key for which value need to be extracted
     * @return : String value for provided key
     */
    public String getJSONAttribute(ValidatableResponse response, String key) {
        return response.extract().path(key).toString();
    }
}
