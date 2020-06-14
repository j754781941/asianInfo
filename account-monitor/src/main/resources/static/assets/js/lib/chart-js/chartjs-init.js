$(function () {
    "use strict";

    //生成日期函数
    function getBeforeDate(n){//n为你要传入的参数，当前为0，前一天为-1，后一天为1
        var date = new Date() ;
        var month,day ;
        date.setDate(date.getDate()+n);
        month = date.getMonth()+1;
        day = date.getDate() ;
        var s = ( month < 10 ? ( '0' + month ) : month ) + '-' + ( day < 10 ? ( '0' + day ) : day) ;
        return s ;
    }

    function getDate(){
        var dateArray = new Array();
        for(var i=6;i>=0;i--){
            dateArray[6-i] = getBeforeDate(-i)
        }
        return dateArray
    }

    Chart.defaults.global.defaultFontSize = 10;
    /*与上周环比趋势
    --------------------*/
    $.ajax({
        type:"post",
        url:"/queryChain",
        contentType: "application/json;charset=utf-8",
        async:true,
        success:function(data){
            //删除样式
            $("#invoiceTrend").removeClass("color-primary");
            $("#prepaidTrend").removeClass("color-primary");
            $("#negativeTrend").removeClass("color-primary");
            $("#canalTrend").removeClass("color-primary");
            $("#format8Trend").removeClass("color-primary");
            $("#paymentTrend").removeClass("color-primary");
            //修改样式
            if(parseFloat(data.invoiceTrend) < 0){
                $("#invoiceTrend").html("↓"+data.invoiceTrend+"%");
                $("#invoiceTrend").addClass("color-success");
            }else if(parseFloat(data.invoiceTrend) > 0 ){
                $("#invoiceTrend").html("↑"+data.invoiceTrend+"%");
                $("#invoiceTrend").addClass("color-danger");
            }else{
                $("#invoiceTrend").addClass("color-primary");
                $("#invoiceTrend").html("0.00%");
            }
            if(parseFloat(data.negativeTrend) < 0){
                $("#negativeTrend").html("↓"+data.negativeTrend + "%");
                $("#negativeTrend").addClass("color-success");
            }else if(parseFloat(data.negativeTrend) > 0 ){
                $("#negativeTrend").html("↑"+data.negativeTrend + "%");
                $("#negativeTrend").addClass("color-danger");
            }else{
                $("#negativeTrend").addClass("color-primary");
                $("#negativeTrend").html("0.00%");
            }
            if(parseFloat(data.prepaidTrend) < 0){
                $("#prepaidTrend").html("↓"+data.prepaidTrend + "%");
                $("#prepaidTrend").addClass("color-success");
            }else if(parseFloat(data.prepaidTrend) > 0 ){
                $("#prepaidTrend").html("↑"+data.prepaidTrend + "%");
                $("#prepaidTrend").addClass("color-danger");
            }else{
                $("#prepaidTrend").addClass("color-primary");
                $("#prepaidTrend").html("0.00%");
            }
            if(parseFloat(data.canalTrend) < 0){
                $("#canalTrend").html("↓"+data.canalTrend + "%");
                $("#canalTrend").addClass("color-success");
            }else if(parseFloat(data.canalTrend) > 0 ){
                $("#canalTrend").html("↑"+data.canalTrend + "%");
                $("#canalTrend").addClass("color-danger");
            }else{
                $("#canalTrend").addClass("color-primary");
                $("#canalTrend").html("0.00%");
            }
            if(parseFloat(data.format8Trend) < 0){
                $("#format8Trend").html("↓"+data.format8Trend + "%");
                $("#format8Trend").addClass("color-success");
            }else if(parseFloat(data.format8Trend) > 0 ){
                $("#format8Trend").html("↑"+data.format8Trend + "%");
                $("#format8Trend").addClass("color-danger");
            }else{
                $("#format8Trend").addClass("color-primary");
                $("#format8Trend").html("0.00%");
            }
            if(parseFloat(data.paymentTrend) < 0){
                $("#paymentTrend").html("↓"+data.paymentTrend + "%");
                $("#paymentTrend").addClass("color-success");
            }else if(parseFloat(data.paymentTrend) > 0 ){
                $("#paymentTrend").html("↑"+data.paymentTrend + "%");
                $("#paymentTrend").addClass("color-danger");
            }else{
                $("#paymentTrend").addClass("color-primary");
                $("#paymentTrend").html("0.00%");
            }
        }
    });

    /*专票发票推送失败异常
    --------------------*/
    var invoice = {
        type: 'line',
        data: {
            labels: [""],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            datasets: [{
                label: "专票推送失败",
                data: [0, 0, 0, 0, 0, 498, 28800],
                backgroundColor: 'transparent',
                borderColor: '#ed7f7e',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#ed7f7e',
            }]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 8,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                labels: {
                    usePointStyle: true,
                    fontFamily: 'Montserrat',
                },
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Month'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '错误数'
                    }
                }]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    };
    //给发票推送异常的赋值
    var x = getDate();
    var y = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryInvoice",
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x[i] == data[j].invoiceDate){
                        y[i] = data[j].invoiceCount
                    }
                }
            }
            invoice.data.labels = x;
            invoice.data.datasets[0].data = y;
            var ctx = $("#invoice-error");
            window.myLine = new Chart(ctx, invoice);
        }
    });


    /*格式八上传异常
    --------------------*/
    var format8 = {
        type: 'line',
        data: {
            labels: [""],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            datasets: [{
                label: "成功数据量",
                data: [0, 0, 0, 0, 0, 498, 28800],
                backgroundColor: 'transparent',
                borderColor: '#9724BE',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#9724BE',
            }]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 8,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                labels: {
                    usePointStyle: true,
                    fontFamily: 'Montserrat',
                },
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Month'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '数量'
                    }
                }]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    };
    //给格式八上传异常的赋值
    var x1 = getDate();
    var y1 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryFormat8",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x1.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x1[i] == data[j].formatDate){
                        y1[i] = data[j].formatCount
                    }
                }
            }
            format8.data.labels = x1;
            format8.data.datasets[0].data = y1;
            var ctx = $("#format8");
            window.myLine = new Chart(ctx, format8);
        }
    });


    /*负余额账本异常
    --------------------*/
    var negative_balance = {
        type: 'line',
        data: {
            labels: [""],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            datasets: [{
                label: "负余额账本",
                data: [0, 0, 0, 0, 0, 498, 28800],
                backgroundColor: 'transparent',
                borderColor: '#31B2C9',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#31B2C9',
            }]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 8,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                labels: {
                    usePointStyle: true,
                    fontFamily: 'Montserrat',
                },
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Month'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '异常数'
                    }
                }]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    };
    //给负余额账本异常的赋值
    var x2 = getDate();
    var y2 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryNegativeBalance",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x2.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x2[i] == data[j].negativeBalanceDate){
                        y2[i] = data[j].negativeBalanceCount
                    }
                }
            }
            negative_balance.data.labels = x2;
            negative_balance.data.datasets[0].data = y2;
            var ctx = $("#negative_balance");
            window.myLine = new Chart(ctx, negative_balance);
        }
    });

    /*预存支付计划监控
    --------------------*/
    var prepaid_payment = {
        type: 'line',
        data: {
            labels: [""],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            datasets: [{
                label: "未处理",
                data: [0, 0, 0, 0, 0, 498, 28800],
                backgroundColor: 'transparent',
                borderColor: '#ffd219',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#ffd219',
            },{
                label: "处理失败",
                data: [0, 0, 0, 0, 0, 498, 28800],
                backgroundColor: 'transparent',
                borderColor: '#ff2e2f',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#ff2e2f',
            }]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 8,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                labels: {
                    usePointStyle: true,
                    fontFamily: 'Montserrat',
                },
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Month'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '异常数'
                    }
                }]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    };
    //预存支付计划未处理监控
    var no_flag = false;
    var faild_flag = false;
    //未处理
    var x3 = getDate();
    var y3 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryPrepaidPayment",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x3.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x3[i] == data[j].prepaidDate){
                        y3[i] = data[j].prepaidCount
                    }
                }
            }
            prepaid_payment.data.labels = x3;
            prepaid_payment.data.datasets[0].data = y3;
            no_flag = true;
            if(faild_flag == true){
                var ctx = $("#prepaid_payment");
                window.myLine = new Chart(ctx, prepaid_payment);
            }
        }
    });
    //处理失败
    var x15 = getDate();
    var y15 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryPrepaidPaymentError",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x15.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x15[i] == data[j].prepaidErrorDate){
                        y15[i] = data[j].prepaidErrorCount
                    }
                }
            }
            prepaid_payment.data.labels = x15;
            prepaid_payment.data.datasets[1].data = y15;
            faild_flag = true;
            if(no_flag == true){
                var ctx = $("#prepaid_payment");
                window.myLine = new Chart(ctx, prepaid_payment);
            }
        }
    });
    /*缴费复机异常监控
    --------------------*/
    var payment_complex = {
        type: 'line',
        data: {
            labels: [""],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            datasets: [{
                label: "上传成功",
                data: [0, 0, 0, 0, 0, 0, 0],
                backgroundColor: 'transparent',
                borderColor: '#ff952c',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#ff952c',
            },{
                label: "上传失败",
                data: [0, 0, 0, 0, 0, 0, 0],
                backgroundColor: 'transparent',
                borderColor: '#ff0667',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#ff0667',
            }]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 8,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                labels: {
                    usePointStyle: true,
                    fontFamily: 'Montserrat',
                },
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Month'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '数量'
                    }
                }]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    };
    //账务直采上传成功记录监控
    var x4 = getDate();
    var y4 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryPaymentComplex",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x4.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x4[i] == data[j].complexDate){
                        y4[i] = data[j].complexCount
                    }
                }
            }
            payment_complex.data.labels = x4;
            payment_complex.data.datasets[0].data = y4;
            var ctx = $("#payment_complex");
            window.myLine = new Chart(ctx, payment_complex);
        }
    });
    //账务直采上传失败记录监控
    var x30 = getDate();
    var y30 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryPaymentComplexError",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x30.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x30[i] == data[j].complexDate){
                        y30[i] = data[j].complexCount
                    }
                }
            }
            payment_complex.data.labels = x30;
            payment_complex.data.datasets[1].data = y30;
        }
    });
    /*各渠道缴费量与入表数据异常
   --------------------*/
    var each_trench = {
        type: 'line',
        data: {
            labels: [],
            type: 'line',
            defaultFontFamily: 'Montserrat',
            datasets: [{
                label: "CRM",
                data: [0, 0, 0, 0, 0, 0, 0],
                backgroundColor: 'transparent',
                borderColor: '#e6a1f2',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#e6a1f2',
            },{
                label: "网厅",
                data: [0, 0, 0, 0, 0, 0, 0],
                backgroundColor: 'transparent',
                borderColor: '#ed7f7e',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#ed7f7e',
            },{
                label: "IPTV",
                data: [0, 0, 0, 0, 0, 0, 0],
                backgroundColor: 'transparent',
                borderColor: '#87de75',
                borderWidth: 2,
                pointStyle: 'circle',
                pointRadius: 4,
                pointBorderColor: 'transparent',
                pointBackgroundColor: '#87de75',
            }]
        },
        options: {
            responsive: true,

            tooltips: {
                mode: 'index',
                titleFontSize: 8,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            legend: {
                labels: {
                    usePointStyle: true,
                    fontFamily: 'Montserrat',
                },
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Month'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        display: true,
                        drawBorder: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '异常数'
                    }
                }]
            },
            title: {
                display: false,
                text: 'Normal Legend'
            }
        }
    };
    //CRM
    var x5 = getDate();
    var y5 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryCRMInfos",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x5.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x5[i] == data[j].crmDate){
                        y5[i] = data[j].crmCount
                    }
                }
            }
            // alert(x5)
            // each_trench.data.labels = x5;
            // alert(each_trench.data.labels)
            each_trench.data.datasets[0].data = y5;
        }
    });
    //网厅
    var x6 = getDate();
    var y6 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryNetworkLobbies",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x6.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x6[i] == data[j].networkDate){
                        y6[i] = data[j].networkCount
                    }
                }
            }
            // alert(x6)
            // each_trench.data.labels = x6;
            // alert(each_trench.data.labels)
            each_trench.data.datasets[1].data = y6;
        }
    });
    //IPTV
    var x7 = getDate();
    var y7 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryIPTVInfos",
        contentType:"application/json;charset=utf-8",
        async:true,
        success:function(data){
            for(var i=0;i<x7.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x7[i] == data[j].iptvDate){
                        y7[i] = data[j].iptvCount
                    }
                }
            }
            // alert(x7)
            // each_trench.data.labels = x7;
            // alert(each_trench.data.labels)
            each_trench.data.datasets[2].data = y7;
        }
    });
    each_trench.data.labels = getDate();
    var ctx = $("#each_trench");
    window.myLine = new Chart(ctx, each_trench);

    //统计信息
    var flag1 = false;
    var flag2 = false;
    var flag3 = false;
    var flag4 = false;
    var flag5 = false;
    var barChart = {
        type: 'bar',
        data: {
            labels: getDate(),
            datasets: [
                {
                    label: "专票发票推送失败异常",
                    data: [0, 0, 0, 0, 0, 0, 0],
                    borderColor: "#ed7f7e",
                    borderWidth: "0",
                    backgroundColor: "#ed7f7e"
                },
                {
                    label: "预存支付计划处理失败",
                    data: [0, 0, 0, 0, 0, 0, 0],
                    borderColor: "#31B2C9",
                    borderWidth: "0",
                    backgroundColor: "#31B2C9"
                },
                {
                    label: "账务直采上传成功",
                    data: [0, 0, 0, 0, 0, 0, 0],
                    borderColor: "#ffd219",
                    borderWidth: "0",
                    backgroundColor: "#ffd219"
                },
                {
                    label: "格式八上传记录",
                    data: [0, 0, 0, 0, 0, 0, 0],
                    borderColor: "#aa61be",
                    borderWidth: "0",
                    backgroundColor: "#aa61be"
                },
                {
                    label: "负余额账本异常",
                    data: [0, 0, 0, 0, 0, 0, 0],
                    borderColor: "#32ff35",
                    borderWidth: "0",
                    backgroundColor: "#32ff35"
                }
            ]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    };
    //专票
    var x10 = getDate();
    var y10 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryInvoice",
        dataType:"json",
        data: {
            flag : 2
        },
        async:true,
        success:function(data){
            for(var i=0;i<x10.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x10[i] == data[j].invoiceDate){
                        y10[i] = data[j].invoiceCount
                    }
                }
            }
            barChart.data.datasets[0].data = y10;
            flag1 = true;
            if(flag2 == true && flag3 == true && flag4 == true && flag5 == true){
                var ctx = document.getElementById("barChart");
                window.myLine = new Chart(ctx, barChart);
            }
        }
    });
    //预存支付计划
    var x11 = getDate();
    var y11 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryPrepaidPaymentError",
        dataType:"json",
        data: {
            flag : 2
        },
        async:true,
        success:function(data){
            for(var i=0;i<x11.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x11[i] == data[j].prepaidErrorDate){
                        y11[i] = data[j].prepaidErrorCount
                    }
                }
            }
            barChart.data.datasets[1].data = y11;
            flag2 = true;
            if(flag1 == true && flag3 == true && flag4 == true && flag5 == true){
                var ctx = document.getElementById("barChart");
                window.myLine = new Chart(ctx, barChart);
            }
        }
    });
    //缴费复机异常
    var x12 = getDate();
    var y12 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryPaymentComplex",
        dataType:"json",
        data: {
            flag : 2
        },
        async:true,
        success:function(data){
            for(var i=0;i<x12.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x12[i] == data[j].complexDate){
                        y12[i] = data[j].complexCount
                    }
                }
            }
            barChart.data.datasets[2].data = y12;
            flag3 = true;
            if(flag2 == true && flag1 == true && flag4 == true && flag5 == true){
                var ctx = document.getElementById("barChart");
                window.myLine = new Chart(ctx, barChart);
            }
        }
    });
    //格式八上传异常
    var x13 = getDate();
    var y13 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryFormat8",
        dataType:"json",
        data: {
            flag : 2
        },
        async:true,
        success:function(data){
            for(var i=0;i<x13.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x13[i] == data[j].formatDate){
                        y13[i] = data[j].formatCount
                    }
                }
            }
            barChart.data.datasets[3].data = y13;
            flag4 = true;
            if(flag2 == true && flag3 == true && flag1 == true && flag5 == true){
                var ctx = document.getElementById("barChart");
                window.myLine = new Chart(ctx, barChart);
            }
        }
    });
    //负余额账本异常
    var x14 = getDate();
    var y14 = [0, 0, 0, 0, 0, 0, 0];
    $.ajax({
        type:"post",
        url:"/queryNegativeBalance",
        dataType:"json",
        data: {
            flag : 2
        },
        async:true,
        success:function(data){
            for(var i=0;i<x14.length;i++){
                for(var j=0;j<data.length;j++){
                    if(x14[i] == data[j].negativeBalanceDate){
                        y14[i] = data[j].negativeBalanceCount
                    }
                }
            }
            barChart.data.datasets[4].data = y14;
            flag5 = true;
            if(flag2 == true && flag3 == true && flag4 == true && flag1 == true){
                var ctx = document.getElementById("barChart");
                window.myLine = new Chart(ctx, barChart);
            }
        }
    });
})
