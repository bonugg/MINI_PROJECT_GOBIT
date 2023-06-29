$(function() {
    $("#file").on('change',function(){
        const fileName = $("#file").val();
        $(".upload-name").val(fileName);
    });

    $("#submit_btn").on("click", () => {
        const file = $("#file")[0].files[0]; // 파일 객체
        const formData = new FormData($("#file_form")[0]); // form 데이터
        formData.append("file", file); // FormData에 파일 객체를 append
        $('#status_text').removeClass("shake-animation");
        $.ajax({
            url: "/memberSigns",
            type: "post",
            enctype: 'multipart/form-data',
            data: formData,
            contentType: false,
            processData: false,
            success: function(obj) {
                if(obj == "회원가입 완료"){
                    $('#status_text').text(obj).css("color","cornflowerblue").hide().fadeIn(300);
                }else {
                    $('#status_text').text(obj).css("color","indianred").addClass("shake-animation");
                }
            },
            error: (error) => {
                $('#status_text').text("회원가입 실패").css("color","indianred").addClass("shake-animation");
            }
        });
    });
});