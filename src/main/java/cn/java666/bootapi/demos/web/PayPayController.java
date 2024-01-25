package cn.java666.bootapi.demos.web;

import java.util.HashMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author geekyouth
 * @date 2024-01-25 11:07:30
 */
@RestController
@RequestMapping("/paypal")
public class PayPayController {
    // TODO
    String cookie = "<relpace yourself cookies>";
    // TODO
    String X_CSRF_TOKEN = "<replace yourself x-csrf-token>";

    /**
     * 第一步，请求列表
     */
    @RequestMapping("/list")
    public Object list() {
        String listUrl = "https://www.paypal.com/myaccount/transfer/peers?attemptId=";

        HashMap<String, String> header = new HashMap<>();
        header.put("authority", "www.paypal.com");
        header.put("accept", " text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("cache-control", "no-cache");
        header.put("dnt", "1");
        header.put("pragma", "no-cache");
        header.put("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\"");
        header.put("sec-ch-ua-mobile", " ?0");
        header.put("sec-ch-ua-platform", "\"Windows\"");
        header.put("sec-fetch-dest", "document");
        header.put("sec-fetch-mode", "navigate");
        header.put("sec-fetch-site", "none");
        header.put("sec-fetch-user", "?1");
        header.put("cookie", cookie);
        header.put("referer", "https://www.paypal.com/authflow/done");
        header.put("upgrade-insecure-requests", "1");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");

        HttpResponse response = HttpUtil.createGet(listUrl)
            .addHeaders(header)
            .execute();
        int status = response.getStatus();
        String body = response.body();
        System.out.println("status = " + status);
        System.out.println("body = " + body);

        JSONArray arraryList = new JSONArray();
        JSONObject parseObj = JSONUtil.parseObj(body);
        parseObj.getJSONArray("otherContacts").forEach(item -> {
            JSONObject item2 = JSONUtil.parseObj(item);
            String id = (item2.getStr("id"));
            arraryList.add(id);
        });
        return arraryList;
    }

    /**
     * 第二步，执行一次 delete 请求
     */
    @RequestMapping("/delete/{id}")
    public Object delete(@PathVariable String id) {
        String URL = "https://www.paypal.com/myaccount/transfer/peers/" + id + "?attemptId=";

        HashMap<String, String> header = new HashMap<>();
        header.put("authority", "www.paypal.com");
        header.put("accept", "application/json, text/javascript, */*; q=0.01");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("cache-control", "no-cache");
        header.put("cookie", cookie);
        header.put("dnt", "1");
        header.put("origin", "https://www.paypal.com");
        header.put("pragma", "no-cache");
        header.put("referer", "https://www.paypal.com/myaccount/transfer/homepage/contact-list");
        header.put("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\"");
        header.put("sec-ch-ua-arch", "\"x86\"");
        header.put("sec-ch-ua-bitness", "\"64\"");
        header.put("sec-ch-ua-full-version", "\"102.0.5005.167\"");
        header.put("sec-ch-ua-full-version-list", "\" Not A;Brand\";v=\"99.0.0.0\", \"Chromium\";v=\"102.0.5005.167\"");
        header.put("sec-ch-ua-mobile", "?0");
        header.put("sec-ch-ua-model", "");
        header.put("sec-ch-ua-platform", "\"Windows\"");
        header.put("sec-ch-ua-platform-version", "\"10.0.0\"");
        header.put("sec-ch-ua-wow64", "?0");
        header.put("sec-fetch-dest", "empty");
        header.put("sec-fetch-mode", "cors");
        header.put("sec-fetch-site", "same-origin");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        header.put("x-csrf-token", X_CSRF_TOKEN);
        header.put("x-requested-with", "XMLHttpRequest");

        HttpResponse response = HttpUtil.createRequest(Method.DELETE, URL)
            .addHeaders(header)
            .execute();
        int status = response.getStatus();
        String body = response.body();
        System.out.println("body = " + body);
        System.out.println("status = " + status);
        return status;
    }

    /**
     * 第三步，遍历列表，执行 delete 请求
     */
    @RequestMapping("/deleteAll")
    public Object deleteAll() {
        JSONArray arraryRes = new JSONArray();
        JSONArray arraryList = (JSONArray) list();
        arraryList.forEach(item -> {
            delete(item.toString());
            arraryRes.add(item);
        });
        return arraryRes;
    }
}
