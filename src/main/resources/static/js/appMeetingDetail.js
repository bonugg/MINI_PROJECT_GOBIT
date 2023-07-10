$(function () {

    $("#btnUpdate").on("click", () => {
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