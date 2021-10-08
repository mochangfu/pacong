package com.cetc.pacong.serviceImpl;

import java.util.HashMap;
import java.util.Map;

public class HeadersParm {
    public static String Cookie = "deviceId=4c1932cf; mh_access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOiJpbWFnZV9jb2RlIiwidXNlcl9uYW1lIjoieHgxNDMzNjY1NjI4NDM3MzE1NTg1Iiwic2NvcGUiOlsiMTAwIl0sImlkIjoiMTQzMzY2NTYyODg0ODM1NzM3OCIsImV4cCI6MTYzMzY2NjI1NCwibG9naW5ObyI6IjFhM2U3YTgyNjUxOTQxZjhiMDViOWM3OWZjOTc2OGY1IiwiYXV0aG9yaXRpZXMiOlsiMTAwMTAwMSJdLCJqdGkiOiI4NmEwM2YwOS1jNzJkLTQxMTctYTY5Ni05MDU4YWYzNmVhNzkiLCJjbGllbnRfaWQiOiJtaFdlYiJ9.V53tSsud2MQ64266756Q0RK9XUddBfznqsAnAQ7qdWE; mh_refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOiJpbWFnZV9jb2RlIiwidXNlcl9uYW1lIjoieHgxNDMzNjY1NjI4NDM3MzE1NTg1Iiwic2NvcGUiOlsiMTAwIl0sImF0aSI6Ijg2YTAzZjA5LWM3MmQtNDExNy1hNjk2LTkwNThhZjM2ZWE3OSIsImlkIjoiMTQzMzY2NTYyODg0ODM1NzM3OCIsImV4cCI6MTYzMzc0NTQ1NCwibG9naW5ObyI6IjFhM2U3YTgyNjUxOTQxZjhiMDViOWM3OWZjOTc2OGY1IiwiYXV0aG9yaXRpZXMiOlsiMTAwMTAwMSJdLCJqdGkiOiIxMzRhNjE2Zi01MzcyLTQ3MWEtOTc0ZC05ZTJjMDFlMmU3ZjgiLCJjbGllbnRfaWQiOiJtaFdlYiJ9.fhXG9KT_Us9B3mejV7DJXuAn3vQSy2qvi1iAGxDDZ1Q; mh_expires_in=1633666255.217";
    public static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
    public static String Authorization="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOiJpbWFnZV9jb2RlIiwidXNlcl9uYW1lIjoieHgxNDMzNjY1NjI4NDM3MzE1NTg1Iiwic2NvcGUiOlsiMTAwIl0sImlkIjoiMTQzMzY2NTYyODg0ODM1NzM3OCIsImV4cCI6MTYzMzY2NjI1NCwibG9naW5ObyI6IjFhM2U3YTgyNjUxOTQxZjhiMDViOWM3OWZjOTc2OGY1IiwiYXV0aG9yaXRpZXMiOlsiMTAwMTAwMSJdLCJqdGkiOiI4NmEwM2YwOS1jNzJkLTQxMTctYTY5Ni05MDU4YWYzNmVhNzkiLCJjbGllbnRfaWQiOiJtaFdlYiJ9.V53tSsud2MQ64266756Q0RK9XUddBfznqsAnAQ7qdWE";
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
