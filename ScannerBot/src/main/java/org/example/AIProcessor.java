package org.example;
//
//import okhttp3.*;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import java.io.IOException;
//
//public class AIProcessor {
//
//    private static final String OPENAI_API_KEY = "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"; // Замініть на свій ключ OpenAI API
import okhttp3.*;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class AIProcessor {

    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY"); // Замініть на ваш API ключ

    public String getSummaryFromAI(String text) throws IOException {
        System.out.println(OPENAI_API_KEY);
        OkHttpClient client = new OkHttpClient();

        // Формуємо запит для OpenAI
        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");

        // Додаємо повідомлення користувача
        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "Это текст новости из мира киноиндустрии. Нужно сократить его так, чтобы оставшаяся основная информация связана с новостью и мнение было логически завершено,также длина редактируемого текста должна быть хотя бы 100 слов. Просьба игнорировать рекламные предложения и все, что связано с Украиной: " + text);
        messages.put(userMessage);

        json.put("messages", messages);
        json.put("max_tokens", 300);
        json.put("temperature", 0.7);

        // Відправляємо запит на кінцеву точку OpenAI /v1/chat/completions
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseData = response.body().string();
        System.out.println("Отримана відповідь: " + responseData);

        // Отримуємо текст з відповіді OpenAI
        JSONObject jsonResponse = new JSONObject(responseData);
        return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();
    }
}
