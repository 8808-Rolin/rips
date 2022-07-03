var currentPDF = "";
$("#choose-pdf").bind('click',function (){
    $("#pdf-input").click();
});

$('#pdf-none').bind('click',function (){
    $("#pdf-input").click();
})

$("#pdf-input").bind("change", function (e) {
    console.log(e.target.files);
    const file = e.target.files[0];
    const fd = new FormData();
    fd.append('file', file);
    $.ajax({
        url: "/api/upload/temp-upload.do",
        dataType: "JSON",
        async: false,
        processData: false,
        contentType: false,
        data: fd,
        method: "POST",
        success(res) {
            currentPDF = res.message;
            let PdfPath = "/api/pdf/"+currentPDF;
            $('#pdf-show').attr('href',PdfPath);
            $('#pdf-show').media(({width:'100%',height:'100%'}));
            $('#pdf-none').addClass('d-none');
            $('#pdf-none').removeClass('d-flex');
            $('#pdf-panel').removeClass('d-none');
            $('#pdf-show').css('height',"100%")
            $('#submit').removeAttr('disabled');
        },
        error(err) {
            console.log("upload pdf failure")
        }
    });
});

$('#submit').bind('click',function (e) {
    let loading = $('#loading');
    loading.removeClass('d-none');
    loading.addClass('d-flex');

    let params = {
        'pdfName':currentPDF,
        'size':$('#size').val(),
        'direction':$('#direction').val(),
        'zoom':$('#zoom').val(),
        'num':$('#num').val(),
        'printer':$('#printer').val()
    };

    $.ajax({
        url: "/api/print/pdf",
        async: true,
        contentType: "application/json",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(params),
        success: function (c) {
            // 成功 回执
            if (c.code == 0) {
                alert("加入到打印队列成功！");
                location.href = "/"
            } else {
                console.log(c)
                alert("出错啦！错误代码：" + c.code);
            }
            $('#loading').removeClass('d-flex');
            $('#loading').addClass('d-none');
        },
        error: function (e) {
            // 失败 回执
            console.log(e)
            alert("出错啦！错误：" + e);
            $('#loading').removeClass('d-flex');
            $('#loading').addClass('d-none');
        }
    });

})

