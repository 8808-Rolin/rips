// 传入一个FormData,上传到服务器获取一个临时的图片URL连接
function upload_temp_image (data){
    $.ajax({
        url:"/upload/temp/img",
        dataType:"JSON",
        async:false,
        processData:false,
        contentType:false,
        data:data,
        method:"POST",
        success(res){
            //上传成功，返回url
            console.log(res);
            if(res.code == 0){
                return res.message;
            }else{
                return "-1";
            }
        },
        error(err){
            console.log("upload image failure")
            return "-1";
        }
    });
}