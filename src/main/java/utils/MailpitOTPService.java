package utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;


public class MailpitOTPService {private final String baseUrl;

    public MailpitOTPService(String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
    }

    private String getMessagesUrl() {
        return baseUrl + "/api/v1/messages";
    }

    private String getMessageUrl(String id) {
        return baseUrl + "/api/v1/message/" + id;
    }

    public void clearEmails() throws Exception {
        URL url = new URL(getMessagesUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.getResponseCode();
    }

    private JSONObject fetchLatestEmail() throws Exception {
        // 1. get messages list
        URL url = new URL(getMessagesUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray messages = json.getJSONArray("messages");

        if (messages.length() == 0) return null;

        JSONObject latest = messages.getJSONObject(0);
        String id = latest.getString("ID");

        // 2. fetch full email
        URL fullUrl = new URL(getMessageUrl(id));
        HttpURLConnection fullConn = (HttpURLConnection) fullUrl.openConnection();
        fullConn.setRequestMethod("GET");

        BufferedReader fullIn = new BufferedReader(new InputStreamReader(fullConn.getInputStream()));
        StringBuilder fullResponse = new StringBuilder();

        while ((line = fullIn.readLine()) != null) {
            fullResponse.append(line);
        }
        fullIn.close();

        return new JSONObject(fullResponse.toString());
    }

    private String getEmailBody(JSONObject email) {
        if (email.has("HTML")) {
            return email.getString("HTML");
        } else if (email.has("Text")) {
            return email.getString("Text");
        }
        return "";
    }

    private String extractOtp(String body) {

        // context-based
        Pattern p1 = Pattern.compile(
                "code to confirm your identity:\\s*(\\d{6})",
                Pattern.CASE_INSENSITIVE
        );
        Matcher m1 = p1.matcher(body);

        if (m1.find()) {
            return m1.group(1);
        }

        // fallback
        Pattern p2 = Pattern.compile("\\b\\d{6}\\b");
        Matcher m2 = p2.matcher(body);

        if (m2.find()) {
            return m2.group();
        }

        return null;
    }

    public String getOtp(int retries, int delaySeconds) throws Exception {

        for (int i = 0; i < retries; i++) {

            JSONObject email = fetchLatestEmail();

            if (email != null) {
                String body = getEmailBody(email);

                System.out.println("Email body: " + body);

                String otp = extractOtp(body);
                if (otp != null) {
                    return otp;
                }
            }

            Thread.sleep(delaySeconds * 1000L);
        }

        throw new Exception("OTP not found");
    }
}
