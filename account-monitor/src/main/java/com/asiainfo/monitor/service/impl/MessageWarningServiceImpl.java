package com.asiainfo.monitor.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.monitor.config.DS;
import com.asiainfo.monitor.constant.Constants;
import com.asiainfo.monitor.entity.ACheckResult;
import com.asiainfo.monitor.entity.SendRequest;
import com.asiainfo.monitor.entity.SendResponse;
import com.asiainfo.monitor.mapper.AccountMonitorMapper;
import com.asiainfo.monitor.mapper.MonitorMapper;
import com.asiainfo.monitor.service.AccountAndCRMService;
import com.asiainfo.monitor.service.MessageWarningService;
import com.asiainfo.monitor.util.SendMessage;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MessageWarningServiceImpl implements MessageWarningService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageWarningServiceImpl.class);

    @Autowired
    private MonitorMapper monitorMapper;

    @Autowired
    private AccountAndCRMService accountAndCRMService;

    @Autowired
    private AccountMonitorMapper accountMonitorMapper;


    /**
     * 账单查询告警
     */
    @Override
    @DS
    public void queryCentralWarning() {
        OutputStreamWriter out =null;
        BufferedReader reader = null;
        String response = "";
        long num = 0;//告警数量
        long x = 6000;//告警阈值
        String beginTime = "";
        String endTime = "";
        //获取endTime
        Date currentTime = new Date(new Date().getTime() - 3600 * 8 *1000);
        //2020-02-02T10:41:25.052Z
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        endTime = formatter.format(currentTime);
        LOG.info("endTime:"+endTime);
        //获取beginTime
        currentTime = new Date(currentTime.getTime() - 2 * 60 * 1000);
        beginTime = formatter.format(currentTime);
        LOG.info("beginTime:" + beginTime);
        String url = "http://137.0.251.118:15601/elasticsearch/_msearch?rest_total_hits_as_int=true&ignore_throttled=true";
        String param = "{\"index\":\"acct-service-*\",\"ignore_unavailable\":true,\"preference\":1580697746591}\n" +
                "{\"version\":true,\"size\":3000,\"sort\":[{\"@timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"_source\":{\"excludes\":[]},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"@timestamp\",\"interval\":\"30s\",\"time_zone\":\"Asia/Shanghai\",\"min_doc_count\":1}}},\"stored_fields\":[\"*\"],\"script_fields\":{},\"docvalue_fields\":[{\"field\":\"@timestamp\",\"format\":\"date_time\"}],\"query\":{\"bool\":{\"must\":[{\"range\":{\"@timestamp\":{\"format\":\"strict_date_optional_time\",\"gte\":\""+ beginTime +"\",\"lte\":\""+  endTime +"\"}}}],\"filter\":[{\"query_string\":{\"query\":\"*调用查询中心的客户化帐单服务*\"}}],\"should\":[],\"must_not\":[]}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"fragment_size\":2147483647},\"timeout\":\"30000ms\"}\n";
        //创建连接
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            conn.setRequestProperty("Content-Type", "application/x-ndjson;charset=utf-8"); // 设置发送数据的格式
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Length", "858");
            conn.setRequestProperty("Host", "137.0.251.118:15601");
            conn.setRequestProperty("kbn-version", "7.2.0");
            conn.setRequestProperty("Origin", "http://137.0.251.118:15601");
            conn.setRequestProperty("Referer", "http://137.0.251.118:15601/app/kibana");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream(),"UTF-8");
            out.append(param);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"UTF-8"));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(),"UTF-8");
//                LOG.info(lines);
                response+=lines;
            }

            reader.close();
            // 断开连接
            conn.disconnect();
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONArray responses = jsonObject.getJSONArray("responses");
            JSONArray res = responses.getJSONObject(0).getJSONObject("hits").getJSONArray("hits");
            for(int i=0;i<res.size();i++){
                String result = res.getJSONObject(i).getJSONObject("_source").getString("message");
                long time = Long.parseLong(result.substring(18,result.length()-3));
//                String time = result.substring(18,result.length());
                if(time > x) {
                    num++;
                }
            }
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String msg = "账务中心场景三近2分钟，客户化账单查询6秒超时数量为"+ num +"笔\n" +
                    "时间：" + nowDate;
            LOG.info(msg);
            if(num > 20){
                SendMessage.sendMsg("2",msg);
                Long waringId = Constants.BILL_QUERY_WARING_FLAG;
                Integer waringFlag = accountMonitorMapper.queryWarningFlag(waringId);
                if(waringFlag == 1){
                    int flag = monitorMapper.insertWarnInfo(11,num,"127.0.0.1");
                    if(flag > 0){
                        LOG.info("外呼告警呼叫成功");
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
//      *  LOG.info(response);//
    }

    /**
     * 集团直冲、省内卡充、省内直充、集团卡充告警
     */
    @Override
    @DS
    public void queryGroupWarning() {
        LOG.info("cookies=====>"+getCookie());
        OutputStreamWriter out =null;
        BufferedReader reader = null;
        String response = "";
        long x = 90;//告警阈值
        String url = "http://137.0.31.223:8071/ete_monitor/operationPortal.do?action=getSdBusiScene";
        //创建连接
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"); // 设置接收数据的格式
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Host", "137.0.31.223:8071");
            conn.setRequestProperty("Cookie", "JSESSIONID=" + getCookie() + "; HttpOnly; boSysNotice=true");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //GET请求
            out = new OutputStreamWriter(
                    conn.getOutputStream(),"UTF-8");
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"UTF-8"));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(),"UTF-8");
//                LOG.info(lines);
                response+=lines;
            }

            reader.close();
            // 断开连接
            conn.disconnect();
            JSONArray responses = JSONObject.parseArray(response);
            Float onTimeRate;
            String comments;
//            6:集团直充  12：省内直充
            int[] arr = {6,12};
            for ( int i: arr ){
                onTimeRate = responses.getJSONObject(i).getFloat("on_time_rate");
                comments = responses.getJSONObject(i).getString("comments");
                LOG.info( comments + "====>" + onTimeRate );
                if( onTimeRate <= 90 ){
                    Float ordersCount = responses.getJSONObject(i).getFloat("orders_count");
                    Float busiSuccess = responses.getJSONObject(i).getFloat("busi_success");
                    Float successRate = responses.getJSONObject(i).getFloat("success_rate");
                    if(!(onTimeRate == 0 && ordersCount == 0 && busiSuccess == 0 && successRate == 0)){
                        if("集团直充服务资源树".equals(comments) || "省内直充".equals(comments) || "集团卡充".equals(comments) || "省内卡充".equals(comments)){
                            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            SendMessage.sendMsg("7","BSN端到端监控:" + comments + "告警！及时率为：" + onTimeRate + "%，已经低于阈值，请及时查看！\n" +
                                    "时间为：" + nowDate + "\n" );
                            if("集团直充服务资源树".equals(comments)){
                                Long waringId = Constants.BSN_WARING_FLAG;
                                Integer waringFlag = accountMonitorMapper.queryWarningFlag(waringId);
                                if(waringFlag == 1){
                                    int flag = monitorMapper.insertWarnInfo(12,0,"127.0.0.1");
                                    if(flag > 0){
                                        LOG.info("外呼告警呼叫成功");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void queryAccountAndCRMCheck() {
        List<ACheckResult> aCheckResults = new ArrayList<>();
        aCheckResults = accountAndCRMService.queryACheckResults();
        String msg = "";
        System.out.println(aCheckResults.get(0).toString());
        if(aCheckResults.size() > 0){
            msg = String.format("各位领导，早上好！\n" +
                            "计费智能监控系统邀您查看\n计费与CRM对账差异报告:\n" +
                            "对账日期：%s\n" +
                            "CRM总金额：%s\n" +
                            "计费总金额：%s\n" +
                            "CRM总笔数：%s\n" +
                            "计费总笔数：%s\n" +
                            "一致总笔数：%s\n" +
                            "差异总笔数：%s\n" +
                            "计费-CRM差值%s分\n",
                    aCheckResults.get(0).getCheckCycle(),
                    aCheckResults.get(0).getSrcSumAmount(),
                    aCheckResults.get(0).getDstSumAmount(),
                    aCheckResults.get(0).getSrcSumRecord(),
                    aCheckResults.get(0).getDstSumRecord(),
                    aCheckResults.get(0).getCorrectSumRecord(),
                    aCheckResults.get(0).getDiffSumRecord(),
                    String.valueOf(Integer.parseInt(aCheckResults.get(0).getDstSumAmount())-Integer.parseInt(aCheckResults.get(0).getSrcSumAmount())));
        }else{
            msg = "账务与CRM对账异常：没进行对账！请到前台页面进行手工对账。";
        }
        LOG.info(msg);
        SendMessage.sendMsg("4",msg);
    }


    /**
     * httpclient模拟登陆获得cookie
     * @return
     */
    public String getCookie(){
        HashMap<String, String> cookies = new HashMap<String, String>(2);
        String url = "http://137.0.31.223:8071/ete_monitor/security/login/";
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionTimeToLive(60000, TimeUnit.MILLISECONDS).build();
        HttpPost httpPost = new HttpPost(url.trim());
        httpPost.setHeader(new BasicHeader("Content-type", "application/x-www-form-urlencoded"));
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("j_username", "100"));
        list.add(new BasicNameValuePair("j_password", "30675bfe85f7b88632dcd34cb3aff100335be9da"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            HttpResponse response = httpClient.execute(httpPost);
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header header : headers) {
                if (header.getValue().contains("JSESSIONID")) {
                    String token = header.getValue()
                            .substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(';'));
                    cookies.put("cookie", token);
                }
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return cookies.get("cookie");
    }


}
