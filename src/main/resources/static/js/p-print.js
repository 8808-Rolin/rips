var image_params = [];
var image_param = {};
var currentImg = "";
var prelink = "/";

var currentX = 0;
var currentY = 0;
var currentWidth = 0;
var currentHeight = 0;
var currentRotate = 0;

var cropper = null;
var image = $("#img-cropper")

$("#choose-img").bind('click', function () {
    $("#image-input").click()
});

$("#rotate").bind("click", function () {
    cropper.rotate(-90);
});
$("#plus").bind("click", function () {
    cropper.zoom(0.1);
});
$("#minus").bind("click", function () {
    cropper.zoom(-0.1);
});
$("#reset").bind("click", function () {
    cropper.reset();
});

$('#delete').bind("click", function () {
    $('#div-crop').addClass('d-none');
    for (let i = 0, len = image_params.length; i < len; i++) {
        let o = image_params[i]
        if (currentImg === o.image_name) {
            image_params.splice(i, 1);
            $('#' + o.id).unbind("click");
            $('#' + o.id).remove();
            break;
        }
    }
    //如果列表为空 则隐藏顶部框
    if (image_params.length == 0) {
        $('#show').addClass('d-none')
    }
});

$("#image-input").bind("change", function (e) {
    console.log(e.target.files);
    var file = e.target.files[0];
    var fd = new FormData();
    fd.append('file', file);
    $.ajax({
        url: prelink + "api/upload/temp-image.do",
        dataType: "JSON",
        async: false,
        processData: false,
        contentType: false,
        data: fd,
        method: "POST",
        success(res) {
            console.log(res);
            if (res.code == 0) {
                //上传成功，进行一个的裁剪框的生成
                // 如此裁剪框已经存在，则替换裁剪框的图标
                image.attr('src', prelink + "api/image/" + res.message)
                currentImg = res.message;
                if (!cropper) {
                    image.cropper({
                        aspectRatio: 210 / 297,
                        viewMode: 0,
                        autoCropArea: 1,
                        crop: function (event) {
                            currentX = event.detail.x;
                            currentY = event.detail.y;
                            currentWidth = event.detail.width;
                            currentHeight = event.detail.height;
                            currentRotate = event.detail.rotate;
                        }
                    });
                    cropper = image.data('cropper');
                } else {
                    cropper.reset().replace(prelink + "api/image/" + res.message);
                }
            }
        },
        error(err) {
            console.log("upload image failure")
        }
    });
    $('#div-crop').removeClass('d-none')
});


function createDiv(id, num, name) {
    // var tempdiv = "<img class='p-0 show-item overflow-hidden h-75'" +
    //     "id='" + id + "' num='" + num + "' name = '" + name + "'/>";
    var tempdiv = "<div class=' h-100 show-item p-0 overflow-hidden' "+"id='" + id + "' ><img height='100%' width='auto' " +
        "num='" + num + "' name = '" + name + "'/></div>";

    // 此处有删除按钮 以上去掉了删除
    // var tempdiv = "<div class='p-0 show-item d-flex flex-column-reverse justify-content-start align-items-center overflow-hidden'" +
    //     "id='" + id + "' num='" + num + "' name = '" + name + "'><div class='item-dele d-flex justify-content-center align-items-center fs-3 text-danger'>" +
    //     "<i class='bi bi-trash3'></i></div></div>";
    return tempdiv;
}

$('#apply').bind("click", function () {
    // 判断是否为全新图片

    //设置一段唯一ID
    var newID = "STATIC_" + new Date().getTime();
    let c = true;
    for (let i = 0, len = image_params.length; i < len; i++) {
        if (image_params[i].image_name === currentImg) {
            // 此时当前图片与列表中的相等，即是老图片，执行这部分代码，替换DIV和list

            //提取之前的ID和在num中的位数
            let oid = image_params[i].id;
            let onum = i;

            // 设定新图片参数并替换对应列表的内容
            image_param = {
                id: newID,
                image_base64:cropper.getCroppedCanvas({maxWidth:4096,maxHeight:8192}).toDataURL(),
                image_name: currentImg,
                x: currentX,
                y: currentY,
                width: currentWidth,
                height: currentHeight,
                rotate: currentRotate
            };
            image_params[i] = image_param;

            // 将新图片更新到任务栏
            console.log(oid)
            let temp = cropper.getCroppedCanvas({ width: 84, height: 118.8 }).toDataURL('image/png');
            $(createDiv(newID, onum, currentImg)).replaceAll($('#' + oid));
            // $('#' + newID).css('background-image', "url(" + temp + ")")
            $( '#' + newID +' img' ).attr('src', temp )

            // 隐藏工具栏和裁剪框的隐藏
            $('#div-crop').addClass('d-none')

            // 阻止换新代码执行并跳出循环
            c = false;
            break;
        }
    }
    if (c) {
        //如果是新图片则执行这部分代码，添加到div并添加到list

        // 读取所有要发送的数据
        image_param = {
            id: newID,
            image_base64:cropper.getCroppedCanvas({maxWidth:4096,maxHeight:8192}).toDataURL(),
            image_name: currentImg,
            x: currentX,
            y: currentY,
            width: currentWidth,
            height: currentHeight,
            rotate: currentRotate
        };
        // 将这段图片参数存入列表
        image_params.push(image_param);

        // 将图片更新到展示栏
        let temp = cropper.getCroppedCanvas({ width: 84, height: 118.8 }).toDataURL('image/png');
        $('#show').append(createDiv(newID, image_params.length, currentImg));
        $( '#' + newID + ' img' ).attr('src', temp )
        $('#show').removeClass('d-none')

        // 隐藏工具栏和裁剪框的隐藏
        $('#div-crop').addClass('d-none')

    }
    // 这部分代码是更新完成之后共同会执行的部分


    // 对新的div进行click绑定（以下时间是点击了div图片后的响应函数）
    $("#" + newID).unbind("click").bind("click", newID, function (e) {
        let d = $('#' + e.data + ' img');
        // 更新当前显示的图片名称
        currentImg = d.attr("name");
        // 重置裁剪框
        cropper.reset().replace(prelink + "api/image/" + d.attr("name"));
        // 显示裁剪框以及工具栏
        $('#div-crop').removeClass('d-none')
    })
});

$('#submit').on("click", function () {
    // 判断数据是否填充完毕且正常
    if (image_params.length == 0) {
        alert('请先上传图片，否则无法加入到打印队列');
        return;
    }
    // 启动加载动画
    $('#loading').removeClass('d-none');
    $('#loading').addClass('d-flex');




    var a = {
        ip: image_params,
        pp: {
            size: $('#size').val(),
            type: $('#type').val(),
            density: $('#density').val(),
            num: $('#num').val(),
            printName: $('#printer').val()
        }
    }
    console.log({data: a})
    $.ajax({
        url: prelink + "api/print/img",
        async: true,
        contentType: "application/json",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(a),
        success: function (res) {
            // 成功 回执
            if (res.code == 0) {
                alert("加入到打印队列成功！");
                location.href = "/"
            } else {
                console.log(res)
                alert("出错啦！错误代码：" + res.code);
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
});