package com.cetc.pacong.serviceImpl;

import java.util.HashMap;
import java.util.Map;

public class HeadersParm {
    public static String Cookie = "deviceId=4c1932cf; mh_access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzM2NzM5MDksImxvZ2luTm8iOiIxYTNlN2E4MjY1MTk0MWY4YjA1YjljNzlmYzk3NjhmNSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiZTI2NGNlODItNGNkYi00Y2Y0LTk1NDUtZDYyMjViZWIwMTAyIiwiY2xpZW50X2lkIjoibWhXZWIifQ.Awe-ndhU95Uf8Lfv2UKwO9WJXq92okJXfUcJgQpLcuE; mh_refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJhdGkiOiJlMjY0Y2U4Mi00Y2RiLTRjZjQtOTU0NS1kNjIyNWJlYjAxMDIiLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzM3NDU0NTQsImxvZ2luTm8iOiIxYTNlN2E4MjY1MTk0MWY4YjA1YjljNzlmYzk3NjhmNSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiMTM0YTYxNmYtNTM3Mi00NzFhLTk3NGQtOWUyYzAxZTJlN2Y4IiwiY2xpZW50X2lkIjoibWhXZWIifQ.EXVLgQp7BgK0PbwGXbSkN3pSvKgAgQblcKS0YG2tTTE; mh_expires_in=1633673910.679";
    public static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
    public static String Authorization="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzM3MDEwNDcsImxvZ2luTm8iOiIxYTNlN2E4MjY1MTk0MWY4YjA1YjljNzlmYzk3NjhmNSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiYzUxNTY1NWUtZmQ3Yy00YjJjLWE1NmMtZjNjOWU3N2ViMTc2IiwiY2xpZW50X2lkIjoibWhXZWIifQ.nllYatcbHOnrEdANF1NPCME4h8zZlRxnlIgn660XNzk";
    public static  String Host = "mdcloud.joinchain.cn";
    public static String user_id="1433665628848357378";

    public static  Map<String,String> getHeaders(){
        Map<String,String> headers=new HashMap<>();
        new HashMap<>();
        headers.put("User-Agent",userAgent);
        headers.put("Cookie",Cookie);
        headers.put("Authorization",Authorization);
        headers.put("Host",Host);
        headers.put("user_id",user_id);
        return headers;
    }

}
