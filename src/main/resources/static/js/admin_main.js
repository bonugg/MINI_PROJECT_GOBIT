$(function () {
    let dept;
    let popupLayer3 = document.getElementById("popup_layer3");
    let popupLayer4 = document.getElementById("popup_layer4");

    // 'dept_btn버튼 클릭 시 dept에 부서 정보 담음
    $(".dept_btn").click(function () {
        dept = $(this).data("userdept");
        $('#btn_text').text("변경").css("color", "#4c4c4c").css("backgroundColor", "rgb(192, 193, 201)").on({
            'mouseenter': function () {
                $(this).css('background-color', "rgb(220, 221, 227)");
            },
            'mouseleave': function () {
                $(this).css('background-color', "rgb(192, 193, 201)");
            }
        });
        $('.dept_btn').css("backgroundColor", "").css("color", "").css("font-weight", "").on({
            'mouseenter': function () {
                $(this).css('backgroundColor', "").css("color", "").css("font-weight", "");
            },
            'mouseleave': function () {
                $(this).css('backgroundColor', "").css("color", "").css("font-weight", "");
            }
        });
        $(this).css("backgroundColor", "#181F42").css("color", "white").css("font-weight", "bold").on({
            'mouseenter': function () {
                $(this).css('backgroundColor', "#253170").css("color", "white").css("font-weight", "bold");
            },
            'mouseleave': function () {
                $(this).css('backgroundColor', "#181F42").css("color", "white").css("font-weight", "bold");
            }
        });
        listGetIt(dept);
    });
    // '모두 체크' 버튼이 클릭되면 실행할 함수를 정의
    $(".check-all").click(function () {
        $(".user-checkbox" + dept).prop("checked", true);
    });

    // '모두 체크 해제' 버튼이 클릭되면 실행할 함수를 정의
    $(".uncheck-all").click(function () {
        $(".user-checkbox" + dept).prop("checked", false);
    });

    $(".chk_btn_submit").click(function () {
        let userposition = $("#chk_tag").val();  // 직급을 가져옵니다.

        let usernums = [];
        $(".user-checkbox" + dept + ":checked").each(function () {
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
                        if (usernums[i] == $('#all2').val()) {
                            $('#position').text(userposition).css("color", "#4c4c4c");
                        }
                    }
                    $('#btn_text').text("변경").css("color", "#4c4c4c").css("backgroundColor", "rgb(192, 193, 201)").on({
                        'mouseenter': function () {
                            $(this).css('background-color', "rgb(220, 221, 227)");
                        },
                        'mouseleave': function () {
                            $(this).css('background-color', "rgb(192, 193, 201)");
                        }
                    });
                    $(".user-checkbox" + dept).prop("checked", false);
                } else if (response == "체크없음") {
                    $('#btn_text').text("하나 이상 체크하세요.").css("color", " white").css("font-size", "12px").css("backgroundColor", "indianred").on({
                        'mouseenter': function () {
                            $(this).css('background-color', "#c04343");
                        },
                        'mouseleave': function () {
                            $(this).css('background-color', "indianred");
                        }
                    });
                } else if (response == "직급없음") {
                    $('#btn_text').text("직급을 선택하세요.").css("color", " white").css("backgroundColor", "indianred").on({
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
            }
        });
    });

    //엔터 입력 시 동작
    $("#search").keyup(function (event) {
        if (event.which === 13) {
            $("#submit_search").click();
        }
    });

    function eventPopClose() {
        if (popupLayer3.style.display === "block") {
            document.addEventListener("click", function (event) {
                if (event.target.id == "popup_layer3"
                    && popupLayer3.contains(event.target)) {
                    popupLayer3.style.display = "none";
                }
            });
        }
    }

    function eventPopClose2() {
        if (popupLayer4.style.display === "block") {
            document.addEventListener("click", function (event) {
                if (event.target.id == "popup_layer4"
                    && popupLayer4.contains(event.target)) {
                    popupLayer4.style.display = "none";
                }
            });
        }
    }

    $("#pwd_clean").click(function () {
        popupLayer3.style.display = "block";
        $('#b1').show();
        $('#b2').hide();
        $('#alret').text($('#name').text() + "님의 패스워드를 초기화 하시겠습니까?");
        eventPopClose();
    });

    window.sub = function sub() {
        $.ajax({
            type: "GET",
            url: "/admin/changePwd",
            data: {
                id: $('#all2').val()
            },
            success: function (obj) {
                if (obj == "성공") {
                    popupLayer3.style.display = "none";
                    popupLayer4.style.display = "block";
                    $('#alret_1').text("패스워드 초기화 완료");
                    eventPopClose2();
                }
            }
        });
    };
    window.can = function can() {
        popupLayer3.style.display = "none";
    };

    $(".search_input_btn").click(function () {
        let searchText = $('#search').val();
        let searchTag = $('#tag').val();
        console.log(searchTag);
        $.ajax({
            type: "GET",
            url: "/admin/searchDept",
            data: {
                searchTag: searchTag,
                searchText: searchText,
                userdept: dept
            }, // 검색어를 서버에 전달
            success: function (response) {
                // 검색 결과 처리 로직 (예: 검색 결과를 테이블로 동적 생성 등)
                if (response.length == 0) {
                    $('#userListWrapper').html('검색 결과가 없습니다.');
                } else {
                    let userListHTML = '';
                    for (let i = 0; i < response.length; i++) {
                        let item = response[i];
                        userListHTML += `
                                    <table class="notice_table" id="${item.userNum}" onclick="getId('${item.userNum}')">
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
                               
                            </td>
                            <td rowspan="4" class="user_delete_td">
                              
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
                    $('#userListWrapper').html(userListHTML);
                }
                $('#search').val("");
                $('#tag').val("");
            }
        });
    });


    window.getId = function getId(id) {
        $.ajax({
            type: "GET",
            url: "/admin/detailuser",
            data: {
                id: id
            },
            success: function (response) {
                $('#user_div_text').hide();
                $('#user_div_btn').show();
                $('.user_po').val("");
                const userImage = document.getElementById('user_img');
                userImage.src = "/upload/" + response.userImage;
                $('#name').text(response.userName).css("color", "#4c4c4c");
                $('#eno').text(response.userEno).css("color", "#4c4c4c");
                $('#dept').text(response.userDept).css("color", "#4c4c4c");
                $('#position').text(response.userPo).css("color", "#4c4c4c");
                $('#email').text(response.userEmail).css("color", "#4c4c4c");
                $('#address_id').text(response.userAddress).css("color", "#4c4c4c");
                $('#phone').text(response.userPhone).css("color", "#4c4c4c");
                $('#joindate').text(response.userJoinDate).css("color", "#4c4c4c");
                $('.user_po').attr('id', "ups" + response.userNum);
                $('#all2').val(response.userNum);
                $('#delete_user').val(response.userNum);
            }
        })
    };

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
                        $('#userListWrapper').html(dept + '부서에 속한 사원이 없습니다.');
                    } else {
                        // userList가 있을 경우
                        $('#cnt_' + dept).text("총 인원 : " + response.length);
                        let userListHTML = '';
                        for (let i = 0; i < response.length; i++) {
                            let item = response[i];
                            userListHTML += `
                                    <table class="notice_table" id="${item.userNum}" onclick="getId('${item.userNum}')">
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
                               
                            </td>
                            <td rowspan="4" class="user_delete_td">
                            
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
                        $('#userListWrapper').html(userListHTML);

                    }
                }
            }
        );
    }

    $(document).on("click", "#delete_user", function () {
        let usernum = $('#delete_user').val();
        popupLayer3.style.display = "block";
        $('#b2').show();
        $('#b1').hide();
        $('#alret').text($('#una' + usernum).text() + "님을 정말 삭제하시겠습니까?");
        eventPopClose();
    });

    window.del = function del() {
        let usernum = $('#delete_user').val();
        $.ajax({
            type: "DELETE",
            url: "/admin/deleteUser",
            data: {
                usernum: usernum
            },
            success: function (response) {
                // Ajax 요청이 성공하면 호출되는 함수
                if (response.item.msg == "정상적으로 삭제되었습니다.") {
                    popupLayer3.style.display = "none";
                    popupLayer4.style.display = "block";
                    $('#alret_1').text($('#una' + usernum).text() + "님을 삭제했습니다.");
                    if (popupLayer4.style.display === "block") {
                        document.addEventListener("click", function (event) {
                            if (event.target.id == "popup_layer4"
                                && popupLayer4.contains(event.target)) {
                                $('#user_div_btn').hide();
                                $('#user_div_text').show();
                                const userImage = document.getElementById('user_img');
                                userImage.src = "/img/user.png";
                                $('#name').text("사원 이름").css("color", "");
                                $('#eno').text("사원 사번").css("color", "");
                                $('#dept').text("사원 부서").css("color", "");
                                $('#position').text("사원 직급").css("color", "");
                                $('#email').text("사원 이메일").css("color", "");
                                $('#address_id').text("사원 주소").css("color", "");
                                $('#phone').text("사원 번호").css("color", "");
                                $('#joindate').text("사원 입사일").css("color", "");
                                $('.user_po').attr('id', "no_val");
                                $('#all2').val("no_val");
                                $('#delete_user').val("no_val");
                                listGetIt(response.item.dept);
                                popupLayer4.style.display = "none";
                            }
                        });
                    }
                } else {
                    alert("삭제 실패");
                }
            }
        });
    };

    $(document).on("click", "#all2", function () {
        let usernum = $('#all2').val();
        let position = $("#ups" + usernum).val();

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
                    $('.user_po').val("");
                    $('#position').text(position).css("color", "#4c4c4c");
                }
            }
        });
    });
    $('#dept_btn_it').click();
});