package com.cetc.pacong.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception{

        List<String> urls=Arrays.asList(
                "http://www.086mrw.com/channels/275.html"

                );
        for (String url_0 : urls) {




        Integer count =0;

        Connection connection= Jsoup.connect(url_0).ignoreContentType(true);
        System.out.println(url_0);

        Document rootdocument =connection.ignoreContentType(true).get();
        Elements elements =rootdocument.getElementsByTag("li");
        for (Element element : elements) {
            element =element.getElementsByTag("p").first();
            if(element==null)continue;
                String name = element.text();
                if(name.length()==0)continue;

            if(name.contains("..."))name=name.replace("...","");
            if(name.contains("--"))name=name.replace("--","");
            if(name.contains("推荐名人："))name=name.replace("推荐名人：","");
            System.out.println(name);

        }

    }
    }


}
