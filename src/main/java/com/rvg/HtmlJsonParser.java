
package com.rvg;

import org.jsoup.Jsoup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.exceptions.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class HtmlJsonParser {

    public static void main(String[] args) throws IOException, java.io.IOException {
        String oldPassword = "RVGOLD";
        String newPassword = "RVGNEW";
        String confirmPassword = "RVGNEW";
        String filePath = "/Users/rajathv/Developer/tutorialJava/tutorial/src/main/java/com/rvg/test.html";
        String htmlContent = "";
        try {
            htmlContent = readHtmlFile(filePath);

            // System.out.println(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // // Parse the HTML content to extract JSON
        // String jsonString = parseJsonFromHtml(htmlContent);

        // // Modify the JSON with new values
        // String modifiedJson = modifyJsonValues(jsonString);

        // // Display the modified JSON
        // System.out.println(modifiedJson);

        String jsonContent = extractJsonFromHtml(htmlContent);
        String updatedJson = updatePasswordValues(jsonContent, oldPassword, newPassword, confirmPassword);

        // Now you can use the jsonContent as needed
        System.out.println(updatedJson);
    }

    private static String readHtmlFile(String filePath) {
        Path path = Paths.get(filePath);

        try {
            return Files.readString(path);
        } catch (java.io.IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return filePath;
    }

    private static String extractJsonFromHtml(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);

        // Find the textarea with id "jsonInput"
        Element jsonInputTextarea = document.select("#jsonInput").first();

        // Extract the value attribute, which contains the JSON
        String jsonValue = jsonInputTextarea.val();

        // Decode HTML entities in the JSON value
        String decodedJson = org.jsoup.parser.Parser.unescapeEntities(jsonValue, true);

        return decodedJson;
    }

    private static String updatePasswordValues(String json, String oldPassword, String newPassword,
            String confirmPassword) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            // Assuming formInputs is an array
            JsonNode formInputs = rootNode.path("formInputs");
            for (JsonNode input : formInputs) {
                String key = input.path("key").asText();
                if ("oldPassword".equals(key)) {
                    ((ObjectNode) input).put("value", oldPassword);
                }
                if ("newPassword".equals(key) || "confirmPassword".equals(key)) {
                    ((ObjectNode) input).put("value", newPassword);
                }
            }

            return objectMapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            return json;
        }
    }
}
