package com.asiainfo.monitor.util;

import com.alibaba.fastjson.JSON;
import com.asiainfo.monitor.entity.SendRequest;
import com.asiainfo.monitor.entity.SendResponse;
import com.asiainfo.monitor.service.impl.StaffIdMonitorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class SendMessage {

    private static final Logger LOG = LoggerFactory.getLogger(SendMessage.class);

    /**
     * 通过集团接口发送短信
     * @param groupID
     * @param logContent
     */
    public static void sendMsg(String groupID,String logContent){
        try {
            //========================================================================
            String sendGroupId = groupID;
            String[] sendGroup = sendGroupId.split(",");
            //logContent为短信内容
            String sendMsg =logContent;
            SendRequest request = new SendRequest();
            SendResponse response;
            String url = "http://137.0.250.173:8088/local/acctMonitor/sendMsg";
            for(String groupId:sendGroup) {
                request.setGroupId(Integer.valueOf(groupId));
                request.setLogContent(sendMsg);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                HttpEntity<String> httpEntity = new HttpEntity<String>(JSON.toJSONString(request), httpHeaders);
                ResponseEntity<SendResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, SendResponse.class);
                response = responseEntity.getBody();
                if(response.getCode()!=0){
                    LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>短信发送失败>>>>"+response.getMsg());
                }else{
                    LOG.info(">>>>>>>>>短信发送成功>>>>>>>>>>");
                }
            }

            //========================================================================
        } catch (ResourceAccessException c){
            LOG.info("ResourceAccessException-->" + c);
        }
        catch (Exception e) {
            LOG.info("Exception-->" + e);
        }
    }

    /**
     * 选择短信接口发送中心
     * @param hostCenter
     * @param logContent
     */
    public static void sendHostCenter(String hostCenter,String logContent){
        if(hostCenter.equals("账务中心")){
            sendMsg("11",logContent);
        }else if (hostCenter.equals("采预中心") || hostCenter.equals("消息省中心")){
            sendMsg("12",logContent);
        }else if(hostCenter.equals("充值中心") || hostCenter.equals("佣金")){
            sendMsg("13",logContent);
        }else if(hostCenter.equals("批价中心")){
            sendMsg("14",logContent);
        }
        sendMsg("10",logContent);
    }
}
