$(function () {
    console.log($("#sign_data").val());
    if($("#sign_data").val() != null){
        $("#sign_img").attr('src',$("#sign_data").val());
        $("#sign_img").show();
    }

    if($("#appState").val() != "미승인"){
        $('#btnUpdate').prop("disabled", true);
        $('#btnDelete').prop("disabled", true);
        $('#input-appStart').attr("readonly", true);
        $('#input-appEnd').attr("readonly", true);
        $('#input-Participant').attr("readonly", true);
        $('#input-appLocation').attr("readonly", true);
        $('#input-appContent').attr("readonly", true);
    }

    $("#btnUpdate").on("click", () => {
        $("#btnUpdate").prop("disabled", true);
        $.ajax({
            url: "/appDetail/meeting",
            type: "post",
            data: $("#updateForm").serialize(),
            processData: false,
            dataType: "json",
            success: (obj) => {
                console.log(obj);
                alert(obj.item.msg);
                if(obj.item.result == "success"){
                    window.location.href = obj.item.redirectUrl;
                }
            },
            error: (error) => {
                console.log(error);
            }
        });

        $("#btnUpdate").off("click");
    });

    $('#btnDelete').on("click", () => {
        const appNum = $("#appNum").val();
        $.ajax({
            url: "/appDetail/meeting/" + appNum,
            type: 'post',
            success: (obj) => {
                alert("회의 결재가 삭제되었습니다.");
                console.log(obj);
                location.href = '/appDetail';
            },
            error: (error) => {
                console.log(error);
            }
        });
    });

});


function printPlace() {
    const place = document.getElementById('input-appLocation').value;
    document.getElementById('appData-place').innerText = place;
}

function printPeople() {
    const people = document.getElementById('input-Participant').value;
    document.getElementById('appData-people').innerText = people;
}

function printContent() {
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}