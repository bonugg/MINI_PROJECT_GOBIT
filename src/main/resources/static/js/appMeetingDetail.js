$(function () {

    $("#btnUpdate").on("click", () => {
        $.ajax({
            url: "/appDetail/approval",
            type: "post",
            data: $("#updateForm").serialize(),
            success: (obj) => {
                alert("수정되었습니다.");
                // location.href = `/app/meet/{metNum}`;
                location.href = '/appDetail';
                console.log(obj);
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