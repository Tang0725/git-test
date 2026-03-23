package top.tqx.workweek03.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qwen")
public class QwenController {

    @Value("${dashscope.api.key:}")
    private String apiKey;

    @Value("${dashscope.model:qwen-plus}")
    private String model;

    // API 地址（国内）
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    /**
     * 简单对话接口
     * @param request 包含 text 字段的请求
     * @return AI响应
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, Object> request) {
        // 获取用户输入
        String text = (String) request.get("text");
        Map<String, Object> result = new HashMap<>();



        try {
            // 构建 messages 数组
            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.set("role", "user");
            userMessage.set("content", text);
            messages.add(userMessage);

            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("model", model);
            params.put("messages", messages);
            params.put("temperature", 0.7);
            params.put("max_tokens", 1024);

            // 转换为 JSON 字符串
            String jsonBody = JSONUtil.toJsonStr(params);

            // 发送 HTTP 请求
            HttpResponse response = HttpRequest.post(API_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .timeout(30000)  // 30秒超时
                    .execute();

            // 解析响应
            String responseBody = response.body();
            if (response.isOk()) {
                JSONObject jsonResponse = JSONUtil.parseObj(responseBody);

                // 提取返回内容
                JSONArray choices = jsonResponse.getJSONArray("choices");
                if (choices != null && choices.size() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    String content = message.getStr("content");

                    result.put("success", true);
                    result.put("content", content);
                    result.put("model", model);

                    // 提取使用情况
                    JSONObject usage = jsonResponse.getJSONObject("usage");
                    if (usage != null) {
                        result.put("promptTokens", usage.getInt("prompt_tokens"));
                        result.put("completionTokens", usage.getInt("completion_tokens"));
                        result.put("totalTokens", usage.getInt("total_tokens"));
                    }
                } else {
                    result.put("success", false);
                    result.put("error", "响应格式异常");
                }
            } else {
                result.put("success", false);
                result.put("error", "API调用失败: " + response.getStatus());
                result.put("errorMessage", responseBody);
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 带系统提示词的对话接口
     * @param request 包含 text 和 systemPrompt 的请求
     * @return AI响应
     */
    @PostMapping("/chat-with-system")
    public Map<String, Object> chatWithSystem(@RequestBody Map<String, Object> request) {
        String text = (String) request.get("text");
        String systemPrompt = (String) request.getOrDefault("systemPrompt", "你是一个乐于助人的助手");
        Map<String, Object> result = new HashMap<>();

        try {
            // 构建 messages 数组
            JSONArray messages = new JSONArray();

            // 添加系统消息
            JSONObject systemMessage = new JSONObject();
            systemMessage.set("role", "system");
            systemMessage.set("content", systemPrompt);
            messages.add(systemMessage);

            // 添加用户消息
            JSONObject userMessage = new JSONObject();
            userMessage.set("role", "user");
            userMessage.set("content", text);
            messages.add(userMessage);

            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("model", model);
            params.put("messages", messages);

            // 可选参数
            if (request.containsKey("temperature")) {
                params.put("temperature", request.get("temperature"));
            } else {
                params.put("temperature", 0.7);
            }

            if (request.containsKey("maxTokens")) {
                params.put("max_tokens", request.get("maxTokens"));
            } else {
                params.put("max_tokens", 1024);
            }

            // 发送请求
            String jsonBody = JSONUtil.toJsonStr(params);
            HttpResponse response = HttpRequest.post(API_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .timeout(30000)
                    .execute();

            // 解析响应
            String responseBody = response.body();
            if (response.isOk()) {
                JSONObject jsonResponse = JSONUtil.parseObj(responseBody);
                JSONArray choices = jsonResponse.getJSONArray("choices");

                if (choices != null && choices.size() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    String content = message.getStr("content");

                    result.put("success", true);
                    result.put("content", content);
                    result.put("model", model);

                    // 返回 token 使用情况
                    JSONObject usage = jsonResponse.getJSONObject("usage");
                    if (usage != null) {
                        result.put("usage", usage);
                    }
                } else {
                    result.put("success", false);
                    result.put("error", "响应格式异常");
                }
            } else {
                result.put("success", false);
                result.put("error", "API调用失败");
                result.put("statusCode", response.getStatus());
                result.put("errorBody", responseBody);
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 多轮对话接口（支持历史消息）
     * @param request 包含 messages 和 currentMessage 的请求
     * @return AI响应
     */
    @PostMapping("/conversation")
    public Map<String, Object> conversation(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取历史消息和当前消息
            Object messagesObj = request.get("messages");
            String currentMessage = (String) request.get("currentMessage");

            JSONArray messages;
            if (messagesObj != null) {
                // 使用传入的历史消息
                messages = JSONUtil.parseArray(messagesObj);
            } else {
                messages = new JSONArray();
            }

            // 添加当前用户消息
            JSONObject userMessage = new JSONObject();
            userMessage.set("role", "user");
            userMessage.set("content", currentMessage);
            messages.add(userMessage);

            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("model", model);
            params.put("messages", messages);
            params.put("temperature", request.getOrDefault("temperature", 0.7));

            // 发送请求
            String jsonBody = JSONUtil.toJsonStr(params);
            HttpResponse response = HttpRequest.post(API_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .timeout(30000)
                    .execute();

            // 解析响应
            String responseBody = response.body();
            if (response.isOk()) {
                JSONObject jsonResponse = JSONUtil.parseObj(responseBody);
                JSONArray choices = jsonResponse.getJSONArray("choices");

                if (choices != null && choices.size() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    String content = message.getStr("content");

                    // 添加 AI 回复到历史消息
                    JSONObject assistantMessage = new JSONObject();
                    assistantMessage.set("role", "assistant");
                    assistantMessage.set("content", content);
                    messages.add(assistantMessage);

                    result.put("success", true);
                    result.put("content", content);
                    result.put("messages", messages);  // 返回更新后的完整历史
                    result.put("model", model);
                } else {
                    result.put("success", false);
                    result.put("error", "响应格式异常");
                }
            } else {
                result.put("success", false);
                result.put("error", "API调用失败");
                result.put("statusCode", response.getStatus());
                result.put("errorBody", responseBody);
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "ok");
        health.put("model", model);
        health.put("apiKeyConfigured", apiKey != null && !apiKey.isEmpty());
        health.put("apiUrl", API_URL);
        return health;
    }
}