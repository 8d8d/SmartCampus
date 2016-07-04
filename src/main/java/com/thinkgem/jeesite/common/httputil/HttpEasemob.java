package com.thinkgem.jeesite.common.httputil;
import java.io.BufferedReader; 
import java.io.DataOutputStream; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.UnsupportedEncodingException; 
import java.net.HttpURLConnection; 
import java.net.MalformedURLException; 
import java.net.URL;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.mapper.JsonMapper; 

public class HttpEasemob {
	public static String URL = "https://a1.easemob.com/junyan/inteligencecampus/users"; 

    public static String PostRequest(JSONObject obj) { 
    	String jsonString="";
        try { 
            //创建连接 
            URL url = new URL(URL); 
            HttpURLConnection connection = (HttpURLConnection) url 
                    .openConnection(); 
            connection.setDoOutput(true); 
            connection.setDoInput(true); 
            connection.setRequestMethod("POST"); 
            connection.setUseCaches(false); 
            connection.setInstanceFollowRedirects(true); 
            connection.setRequestProperty("Content-Type", 
                    "application/json"); 

            connection.connect(); 

            //POST请求 
            DataOutputStream out = new DataOutputStream( 
                    connection.getOutputStream()); 


            out.writeBytes(obj.toString()); 
            out.flush(); 
            out.close(); 

			// 读取响应
			if (connection.getResponseCode()==200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String lines;
				StringBuffer sb = new StringBuffer("");
				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sb.append(lines);
				}
				jsonString=sb.toString();
				reader.close();
			}
            // 断开连接 
            connection.disconnect(); 
        } catch (MalformedURLException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
        return jsonString;
    } 

    public static String PutRequest(JSONObject obj,String token) { 
    	String jsonString="";
        try { 
            //创建连接 
            URL url = new URL(URL); 
            HttpURLConnection connection = (HttpURLConnection) url 
                    .openConnection(); 
            connection.setDoOutput(true); 
            connection.setDoInput(true); 
            connection.setRequestMethod("PUT"); 
            connection.setUseCaches(false); 
            connection.setInstanceFollowRedirects(true); 
            connection.setRequestProperty("Content-Type", 
                    "application/json"); 
            connection.setRequestProperty("Authorization", 
                    "Bearer "+token); 

            connection.connect(); 


            //POST请求 
            DataOutputStream out = new DataOutputStream( 
                    connection.getOutputStream()); 


            out.writeBytes(obj.toString()); 
            out.flush(); 
            out.close(); 

			// 读取响应
			if (connection.getResponseCode() == 400) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String lines;
				StringBuffer sb = new StringBuffer("");
				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sb.append(lines);
				}
				jsonString=sb.toString();
				reader.close();
			}
            // 断开连接 
            connection.disconnect(); 
        } catch (MalformedURLException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
        return jsonString;
    } 
    
    public static void registerUser(String username,String password){
    	URL="https://a1.easemob.com/junyan/inteligencecampus/users"; 
        JSONObject obj = new JSONObject(); 
        obj.put("username", username); 
        obj.put("password", password); 
        System.out.println("url:"+PostRequest(obj));
    }
    
    public static String getToken(){
    	URL="https://a1.easemob.com/junyan/inteligencecampus/token"; 
        JSONObject obj = new JSONObject(); 
        obj.put("grant_type", "client_credentials"); 
        obj.put("client_id", "YXA6URCO4O8rEeW5yRMiH_Mnew"); 
        obj.put("client_secret", "YXA623cpjuhNbvN8Vm1VkDT4BQrtA6E"); 
    	return PostRequest(obj);
    }
    
    public static void changePassword(String username,String password){
    	URL="https://a1.easemob.com/junyan/inteligencecampus/users"; 
        JSONObject obj = new JSONObject(); 
        obj.put("newpassword", password);
        
        TokenEntity tokenEntity=(TokenEntity) JsonMapper.fromJsonString(getToken(),TokenEntity.class);
        System.out.println("url2:"+tokenEntity.access_token);
        
    }
    public static void main(String[] args) { 
    	System.out.println(getToken());
    	changePassword("123456","123");
    } 
}
