$(function () {
    //작성자와 로그인 유저가 다르면 수정 삭제 버튼 사라지게 하고 폼 데이터 입력 읽기모드로
    // const loginUser = /*[[${#authentication.getName()}]]*/;
    // const appWriter = /*[[${approval.userNum.USERENO}]]*/;

    // if (loginUser != appWriter) {
    //     $("#btnUpdate").hide();
    //     $("#btnDelete").hide();
    //     $("#inputDate-start").attr("readonly", true);
    //     $("#inputDate-end").attr("readonly", true);
    //     $("#input-appLocation").attr("readonly", true);
    //     $("#input-appContent").attr("readonly", true);
    // }

    function printDate() {
        const dateStart = document.getElementById('inputDate-start').value;
        const dateEnd = document.getElementById('inputDate-end').value;
        document.getElementById('appData-date').innerText = `${dateStart} ~ ${dateEnd}`;
    }

    function printPlace() {
        const place = document.getElementById('input-appLocation').value;
        document.getElementById('appData-Place').innerText = place;
    }

    function printContent() {
        const content = document.getElementById('input-appContent').value;
        document.getElementById('appData-content').innerText = content;
    }

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
        // const appNum = parseInt(document.getElementById("appNum").value, 10);  // radix 10으로 지정하여 10진수로 변환
        // const appNum = document.getElementById("appNum").value;
        const appNum = $("#appNum").val();
        $.ajax({
            url: "/appDetail/approval/" + appNum,
            // url: `/appDetail/approval/${appNum}`,
            type: 'post',
            success: (obj) => {
                alert("삭제되었습니다.");
                console.log(obj);
                location.href = '/appDetail';
                // location.href = `/app/meet/{metNum}`;
                // location.href = '/appDetail';
            },
            error: (error) => {
                console.log(error);
            }
        });
    });
});
