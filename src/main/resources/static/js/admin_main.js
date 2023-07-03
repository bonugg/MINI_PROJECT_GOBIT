$(function () {

    listGetIt("IT");
    listGetIt("디자인");
    listGetIt("경영");
    listGetIt("개발");

    // '모두 체크' 버튼이 클릭되면 실행할 함수를 정의
    $(".check-all").click(function () {
        let userdept = $(this).data("userdept");
        $(".user-checkbox" + userdept).prop("checked", true);
    });

    // '모두 체크 해제' 버튼이 클릭되면 실행할 함수를 정의
    $(".uncheck-all").click(function () {
        let userdept = $(this).data("userdept");
        $(".user-checkbox" + userdept).prop("checked", false);
    });

    $(".chk_btn_submit").click(function () {
        let userdept = $(this).data("userdept");
        let userposition = $("#chk_tag_" + userdept).val();  // 직급을 가져옵니다.

        let usernums = [];
        $(".user-checkbox" + userdept + ":checked").each(function () {
            usernums.push($(this).data("user-num"));
        });
        if (usernums.length == 0) {
            usernums.push("데이터없음");
        }

        $.ajax({
            url: "/admin/chkmodify",
            type: "post",
            traditional: true, // ajax 배열 넘기기 옵션
            data: {
                userposition: userposition,
                usernums: usernums
            },
            success: function (response) {
                console.log(response);
                if (response == "성공") {
                    for (let i = 0; i < usernums.length; i++) {
                        $('#uname' + usernums[i]).text(userposition);
                    }
                    $('#btn_text_' + userdept).text("변경").css("backgroundColor", "#181F42").on({
                        'mouseenter': function () {
                            $(this).css('background-color', "#253170");
                        },
                        'mouseleave': function () {
                            $(this).css('background-color', "#181F42");
                        }
                    });
                    $(".user-checkbox" + userdept).prop("checked", false);
                } else if (response == "체크없음") {
                    $('#btn_text_' + userdept).text("하나 이상 체크하세요.").css("font-size", "12px").css("backgroundColor", "indianred").on({
                        'mouseenter': function () {
                            $(this).css('background-color', "#c04343");
                        },
                        'mouseleave': function () {
                            $(this).css('background-color', "indianred");
                        }
                    });
                } else if (response == "직급없음") {
                    $('#btn_text_' + userdept).text("직급을 선택하세요.").css("backgroundColor", "indianred").on({
                        'mouseenter': function () {
                            $(this).css('background-color', "#c04343");
                        },
                        'mouseleave': function () {
                            $(this).css('background-color', "indianred");
                        }
                    });
                }
            },
            error: function (xhr, status, error) {
                alert("전송 중 오류가 발생했습니다.");
            }
        });
    });

    $(".all_list").click(function () {//리스트 전체보기
        let userdept = $(this).data("userdept");
        listGetIt(userdept);
    });

    $(".search_input_btn").click(function () {
        let userdept = $(this).data("userdept");
        let searchText = $('#search_' + userdept).val();
        let searchTag = $('#tag_' + userdept).val();
        console.log(searchTag);
        $.ajax({
            type: "GET",
            url: "/admin/searchDept",
            data: {
                searchTag: searchTag,
                searchText: searchText,
                userdept: userdept
            }, // 검색어를 서버에 전달
            success: function (response) {
                console.log(response);
                // 검색 결과 처리 로직 (예: 검색 결과를 테이블로 동적 생성 등)
                if (response.length == 0) {
                    $('#userListWrapper' + userdept).html('검색 결과가 없습니다.');
                } else {
                    let userListHTML = '';
                    for (let i = 0; i < response.length; i++) {
                        let item = response[i];
                        userListHTML += `
                                    <table class="notice_table">
                        <tr>
                            <td rowspan="4" class="user_Img_td">
                                <div class="user_Img_div">
                                    <img src="/upload/${item.userImage}" alt="프로필 사진" class="user_Img">
                                    <input type="checkbox" class="${'user-checkbox' + userdept}" data-user-num="${item.userNum}">
                                </div>
                            </td>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">이름</div>
                                </div>
                            </th>
                            <td class="user_Name" id="${'una' + item.userNum}">${item.userName}</td>
                            <td rowspan="2" class="user_position_td">
                                <div class="user_position_div">
                                    <div class="user_position_div2">
                                        <select name="user_position" id="${'ups' + item.userNum}">
                                            <option value="" selected disabled hidden>직급 변경</option>
                                            <option value="신입">신입</option>
                                            <option value="팀장">팀장</option>
                                        </select>
                                    </div>
                                </div>
                            </td>
                            <td rowspan="4" class="user_delete_td">
                                <div class="user_delete_div">
                                    <div class="user_delete_div2">${item.userNum}</div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">사번</div>
                                </div>
                            </th>
                            <td class="user_Name">${item.userEno}</td>
                        </tr>
                        <tr>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">직급</div>
                                </div>
                            </th>
                            <td class="user_Name" id="${'uname' + item.userNum}">${item.userPo}</td>
                            <td rowspan="2" class="user_position_td">
                                <div class="user_position_div">
                                    <div class="user_position_div2">
                                        <button type="button" class="up_btn" data-usernum="${item.userNum}">변경</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">퇴사여부</div>
                                </div>
                            </th>
                            <td class="user_Name_N">${item.userExitChk}</td>
                        </tr>
                    </table>
                                    `;
                    }
                    $('#userListWrapper' + userdept).html(userListHTML);
                }
            }
        });
    });

    function listGetIt(dept) {
        $.ajax({
                type: "GET",
                url: "/admin/listDept",
                data: {
                    dept: dept
                },
                success: function (response) {
                    // Ajax 요청이 성공하면 호출되는 함수
                    if (response.length == 0) {
                        $('#userListWrapper' + dept).html(dept + '부서에 속한 사원이 없습니다.');
                    } else {
                        // userList가 있을 경우
                        $('#cnt_' + dept).text("총 인원 : " + response.length);
                        let userListHTML = '';
                        for (let i = 0; i < response.length; i++) {
                            let item = response[i];
                            userListHTML += `
                                    <table class="notice_table">
                        <tr>
                            <td rowspan="4" class="user_Img_td">
                                <div class="user_Img_div">
                                    <img src="/upload/${item.userImage}" alt="프로필 사진" class="user_Img">
                                    <input type="checkbox" class="${'user-checkbox' + dept}" data-user-num="${item.userNum}">
                                </div>
                            </td>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">이름</div>
                                </div>
                            </th>
                            <td class="user_Name" id="${'una' + item.userNum}">${item.userName}</td>
                            <td rowspan="2" class="user_position_td">
                                <div class="user_position_div">
                                    <div class="user_position_div2">
                                        <select name="user_position" id="${'ups' + item.userNum}">
                                            <option value="" selected disabled hidden>직급 변경</option>
                                            <option value="신입">신입</option>
                                            <option value="팀장">팀장</option>
                                        </select>
                                    </div>
                                </div>
                            </td>
                            <td rowspan="4" class="user_delete_td">
                                <div class="user_delete_div">
                                    <div class="user_delete_div2">${item.userNum}</div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">사번</div>
                                </div>
                            </th>
                            <td class="user_Name">${item.userEno}</td>
                        </tr>
                        <tr>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">직급</div>
                                </div>
                            </th>
                            <td class="user_Name" id="${'uname' + item.userNum}">${item.userPo}</td>
                            <td rowspan="2" class="user_position_td">
                                <div class="user_position_div">
                                    <div class="user_position_div2">
                                        <button type="button" class="up_btn" data-usernum="${item.userNum}">변경</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th class="user_Name_th">
                                <div class="user_Name_div">
                                    <div class="user_Name_div2">퇴사여부</div>
                                </div>
                            </th>
                            <td class="user_Name_N">${item.userExitChk}</td>
                        </tr>
                    </table>
                                    `;
                        }
                        $('#userListWrapper' + dept).html(userListHTML);

                        // 체크박스 이벤트 핸들러 추가
                        $(".user-checkbox" + dept).on("change", function () {
                            console.log($(this).data("user-num"));
                            const checkboxes = $(".user-checkbox" + dept + ":checked");
                            if (checkboxes.length > 0) {
                                // 체크박스가 1개 이상이면 보여줍니다.
                            } else {
                                // 체크박스가 0개이면 숨깁니다.
                            }
                        });
                    }
                }
            }
        );
    }

    $(document).on("click", ".user_delete_div2", function () {
        let usernum = $(this).text();
        if (confirm($('#una' + usernum).text() + "님을 정말 삭제하시겠습니까?")) {
            $.ajax({
                type: "DELETE",
                url: "/admin/deleteUser",
                data: {
                    usernum: usernum
                },
                success: function (response) {
                    // Ajax 요청이 성공하면 호출되는 함수
                    if (response.item.msg == "정상적으로 삭제되었습니다.") {
                        alert($('#una' + usernum).text() + "님을 삭제했습니다.");
                        listGetIt(response.item.dept);
                    } else {
                        alert("삭제 실패");
                    }
                }
            });
        }
    });
    $(document).on("click", ".up_btn", function () {
        console.log(1);
        let usernum = $(this).data("usernum");
        let position = $("#ups" + usernum).val();
        console.log(usernum);

        $.ajax({
            type: "POST",
            url: "/admin/changePosition",
            data: {
                usernum: usernum,
                position: position
            },
            success: function (response) {
                // Ajax 요청이 성공하면 호출되는 함수
                if (response == "성공") {
                    $('#uname' + usernum).text(position);
                }
            }
        });
    });
});