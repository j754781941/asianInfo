package com.asiainfo.monitor.controller;

import com.asiainfo.monitor.entity.ACheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Test1 {

    private static final Logger LOG = LoggerFactory.getLogger(Test1.class);

    public static void main(String[] args) {
        List<ACheckResult> aCheckResults = new ArrayList<>();
        ACheckResult aCheckResult1 = new ACheckResult();
        ACheckResult aCheckResult2 = new ACheckResult();
        ACheckResult aCheckResult3 = new ACheckResult();
        ACheckResult aCheckResult4 = new ACheckResult();
        ACheckResult aCheckResult5 = new ACheckResult();
        aCheckResult1.setCheckCycle("20171103000000");
        aCheckResult1.setCorrectSumRecord("张三");
        aCheckResult2.setCheckCycle("20170803000000");
        aCheckResult2.setCorrectSumRecord("李四");
        aCheckResult3.setCheckCycle("20170903000000");
        aCheckResult3.setCorrectSumRecord("王五");
        aCheckResult4.setCheckCycle("20170903000000");
        aCheckResult4.setCorrectSumRecord("赵六");
        aCheckResult5.setCheckCycle("20171103000000");
        aCheckResult5.setCorrectSumRecord("宋七");
        aCheckResults.add(aCheckResult1);
        aCheckResults.add(aCheckResult2);
        aCheckResults.add(aCheckResult3);
        aCheckResults.add(aCheckResult4);
        aCheckResults.add(aCheckResult5);
        Collections.sort(aCheckResults,(p1,p2)->p2.getCheckCycle().compareTo(p1.getCheckCycle()));
        for (int i=0;i<aCheckResults.size();i++){
            System.out.println("name: "+ aCheckResults.get(i).getCorrectSumRecord()+"--date: "+ aCheckResults.get(i).getCheckCycle());
        }
    }
}
