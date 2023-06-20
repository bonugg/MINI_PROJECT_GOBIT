let currentPage = 0;
let currentPage2 = 0;
let currentPage3 = 0;

const showvacationview = (VNO) => {
    location.href = "/vacationAcessView?VNO=" + VNO;
}
const showmeetingview = (VNO) => {
    location.href = "/meetingAcessView?VNO=" + VNO;
}
const showbusinessiew = (VNO) => {
    location.href = "/businessAcessView?VNO=" + VNO;
}

// 초기 페이지 로드
loadPageData(currentPage);

// 다음 페이지 이동 이벤트 등록
$("#next").click(function (e) {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    e.preventDefault();
    currentPage += 1;
    loadPageData(currentPage);
});

// 이전 페이지 이동 이벤트 등록
$("#prev").click(function (e) {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    e.preventDefault();
    currentPage -= 1;
    if (currentPage < 0) {
        currentPage = 0;
    }
    loadPageData(currentPage);
});

$("#next2").click(function (e) {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    e.preventDefault();
    currentPage2 += 1;
    loadPageDataMeeting(currentPage2);
});

// 이전 페이지 이동 이벤트 등록
$("#prev2").click(function (e) {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    e.preventDefault();
    currentPage2 -= 1;
    if (currentPage2 < 0) {
        currentPage2 = 0;
    }
    loadPageDataMeeting(currentPage2);
});

$("#next3").click(function (e) {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    e.preventDefault();
    currentPage3 += 1;
    loadPageDataBusiness(currentPage3);
});

// 이전 페이지 이동 이벤트 등록
$("#prev3").click(function (e) {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    e.preventDefault();
    currentPage3 -= 1;
    if (currentPage3 < 0) {
        currentPage3 = 0;
    }
    loadPageDataBusiness(currentPage3);
});

$('#vacation').on('click', function () {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#table-body').empty();
    $('#vacation').css("color","cornflowerblue");
    $('#meeting').css("color","");
    $('#business').css("color","");
    $('#bar2-1').css("backgroundColor","cornflowerblue");
    $('#bar2-2').css("backgroundColor","gray");
    $('#bar2-3').css("backgroundColor","gray");
    loadPageData(currentPage);
});
$('#meeting').on('click', function () {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#meeting').css("color","cornflowerblue");
    $('#vacation').css("color","");
    $('#business').css("color","");
    $('#bar2-2').css("backgroundColor","cornflowerblue");
    $('#bar2-1').css("backgroundColor","gray");
    $('#bar2-3').css("backgroundColor","gray");
    $('#table-body').empty();
    loadPageDataMeeting(currentPage2);
});
$('#business').on('click', function () {
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('pagination2').style.display = 'none';
    document.getElementById('pagination3').style.display = 'none';
    $('#business').css("color","cornflowerblue");
    $('#meeting').css("color","");
    $('#vacation').css("color","");
    $('#bar2-3').css("backgroundColor","cornflowerblue");
    $('#bar2-2').css("backgroundColor","gray");
    $('#bar2-1').css("backgroundColor","gray");
    $('#table-body').empty();
    loadPageDataBusiness(currentPage3);
});
function loadPageData(page) {
    const request = $.ajax({
        url: "/managerAcessAjax?page=" + page,
        method: "GET",
        dataType: "json"
    });
    request.done(function (data) {
        console.log(data);
        for (let i = 0; i < data.content.length; i++) {
            if (i == 0) {
                $("#table-body").append("<tr id='th_tr'><th id='th_vno' style='font-size: 15px'>글 번호</th><th id='th_cno' style='font-size: 15px'>사번</th><th id='th_name' style='font-size: 15px'>이름</th><th id='th_date' style='font-size: 15px'>휴가 시작 날짜</th><th id='th_date' style='font-size: 15px'>휴가 종료 날짜</th><th id='th_access' style='font-size: 15px'>결재여부</th></tr>")
                $("#table-body").append("<tr class='th_td' onclick=showvacationview(" + data.content[i].VNO + ")><td id='td_vno'>" + data.content[i].VNO + "</td><td id='td_cno'>" + data.content[i].CNO + "</td><td class='title_td' id='td_name'><a>" + data.content[i].TITLE + "</a></td><td class='td_date'>" + data.content[i].start + "</td><td class='td_date'>" + data.content[i].end + "</td><td class='Accessva_cls' id='td_access'>" + data.content[i].Accessva + "</td></tr>");
            } else
                $("#table-body").append("<tr class='th_td' onclick=showvacationview(" + data.content[i].VNO + ")><td id='td_vno'>" + data.content[i].VNO + "</td><td id='td_cno'>" + data.content[i].CNO + "</td><td class='title_td' id='td_name'><a>" + data.content[i].TITLE + "</a></td><td class='td_date'>" + data.content[i].start + "</td><td class='td_date'>" + data.content[i].end + "</td><td class='Accessva_cls' id='td_access'>" + data.content[i].Accessva + "</td></tr>");
        }

        $('.Accessva_cls').each(function () {
            if ($(this).text() === "승인") {
                $(this).css("color", "cornflowerblue").css("font-weight", "bold");
            } else if ($(this).text() === "반려") {
                $(this).css("color", "indianred").css("font-weight", "bold");
            }
        });

        let pageNumbers = [];
        for (let i = 0; i < data.totalPages; i++) {
            pageNumbers[i] = i;
        }
        let pageNumbersHtml = '';
        pageNumbers.forEach((pageNumber) => {
            if (pageNumber == currentPage) {
                pageNumbersHtml += `<button class="cur_page">${pageNumber + 1}</button>`;
            } else
                pageNumbersHtml += `<button class="list_page">${pageNumber + 1}</button>`;
        });
        document.getElementById('pageNumbersContainer').innerHTML = pageNumbersHtml;
        document.getElementById('pagination').style.display = 'flex';

        $(".list_page").click(function (e) {
            document.getElementById('pagination').style.display = 'none';
            document.getElementById('pagination2').style.display = 'none';
            document.getElementById('pagination3').style.display = 'none';
            $('#table-body').empty();
            const pageNumberText = $(this).text();
            const pageNumber = parseInt(pageNumberText, 10);
            currentPage = pageNumber - 1;
            e.preventDefault();
            loadPageData(pageNumber - 1);
        });

        if(currentPage == 0 && data.totalPages - 1 == 0) {
            $("#next2").attr("disabled", true);
            $("#prev2").attr("disabled", true);
        }else if (currentPage == 0) {
            $("#prev").attr("disabled", true);
            $("#next").removeAttr("disabled");
        } else if (currentPage == data.totalPages - 1) {
            $("#next").attr("disabled", true);
            $("#prev").removeAttr("disabled");
        }
    });
}
function loadPageDataMeeting(page) {
    const request = $.ajax({
        url: "/managerMeetingAcessAjax?page=" + page,
        method: "GET",
        dataType: "json"
    });
    request.done(function (data) {
        console.log(data);
        for (let i = 0; i < data.content.length; i++) {
            if (i == 0) {
                $("#table-body").append("<tr id='th_tr'><th id='th_vno'>글 번호</th><th id='th_cno'>이름</th><th id='th_name'>제목</th><th id='th_date'>회의 시작 시간</th><th id='th_date'>회의 종료 시간</th><th id='th_access'>결재여부</th></tr>")
                $("#table-body").append("<tr class='th_td' onclick=showmeetingview(" + data.content[i].VNO + ")><td id='td_vno'>" + data.content[i].VNO + "</td><td id='td_cno'>" + data.content[i].NAME + "</td><td class='title_td' id='td_name'><a>" + data.content[i].TITLE + "</a></td><td class='td_date'>" + data.content[i].start + "</td><td class='td_date'>" + data.content[i].end + "</td><td class='Accessva_cls' id='td_access'>" + data.content[i].Accessva + "</td></tr>");
            } else
                $("#table-body").append("<tr class='th_td' onclick=showmeetingview(" + data.content[i].VNO + ")><td id='td_vno'>" + data.content[i].VNO + "</td><td id='td_cno'>" + data.content[i].NAME + "</td><td class='title_td' id='td_name'><a>" + data.content[i].TITLE + "</a></td><td class='td_date'>" + data.content[i].start + "</td><td class='td_date'>" + data.content[i].end + "</td><td class='Accessva_cls' id='td_access'>" + data.content[i].Accessva + "</td></tr>");
        }

        $('.Accessva_cls').each(function () {
            if ($(this).text() === "승인") {
                $(this).css("color", "cornflowerblue").css("font-weight", "bold");
            } else if ($(this).text() === "반려") {
                $(this).css("color", "indianred").css("font-weight", "bold");
            }
        });

        let pageNumbers = [];
        for (let i = 0; i < data.totalPages; i++) {
            pageNumbers[i] = i;
        }
        let pageNumbersHtml = '';
        pageNumbers.forEach((pageNumber) => {
            if (pageNumber == currentPage2) {
                pageNumbersHtml += `<button class="cur_page">${pageNumber + 1}</button>`;
            } else
                pageNumbersHtml += `<button class="list_page">${pageNumber + 1}</button>`;
        });
        document.getElementById('pageNumbersContainer2').innerHTML = pageNumbersHtml;
        document.getElementById('pagination2').style.display = 'flex';

        $(".list_page").click(function (e) {
            document.getElementById('pagination').style.display = 'none';
            document.getElementById('pagination2').style.display = 'none';
            document.getElementById('pagination3').style.display = 'none';
            $('#table-body').empty();
            const pageNumberText = $(this).text();
            const pageNumber = parseInt(pageNumberText, 10);
            currentPage2 = pageNumber - 1;
            e.preventDefault();
            loadPageDataMeeting(pageNumber - 1);
        });

        if(currentPage2 == 0 && data.totalPages - 1 == 0) {
            $("#next2").attr("disabled", true);
            $("#prev2").attr("disabled", true);
        }else if (currentPage2 == 0) {
            $("#prev2").attr("disabled", true);
            $("#next2").removeAttr("disabled");
        } else if (currentPage2 == data.totalPages - 1) {
            $("#next2").attr("disabled", true);
            $("#prev2").removeAttr("disabled");
        }
    });
}

function loadPageDataBusiness(page) {
    const request = $.ajax({
        url: "/managerBusinessAcessAjax?page=" + page,
        method: "GET",
        dataType: "json"
    });
    request.done(function (data) {
        console.log(data);
        for (let i = 0; i < data.content.length; i++) {
            if (i == 0) {
                $("#table-body").append("<tr id='th_tr'><th id='th_vno'>글 번호</th><th id='th_cno'>사번</th><th id='th_name'>이름</th><th id='th_date'>출장 시작 날짜</th><th id='th_date'>출장 종료 날짜</th><th id='th_access'>결재여부</th></tr>")
                $("#table-body").append("<tr class='th_td' onclick=showbusinessiew(" + data.content[i].VNO + ")><td id='td_vno'>" + data.content[i].VNO + "</td><td id='td_cno'>" + data.content[i].CNO + "</td><td class='title_td' id='td_name'><a>" + data.content[i].TITLE + "</a></td><td class='td_date'>" + data.content[i].start + "</td><td class='td_date'>" + data.content[i].end + "</td><td class='Accessva_cls' id='td_access'>" + data.content[i].Accessva + "</td></tr>");
            } else
                $("#table-body").append("<tr class='th_td' onclick=showbusinessiew(" + data.content[i].VNO + ")><td id='td_vno'>" + data.content[i].VNO + "</td><td id='td_cno'>" + data.content[i].CNO + "</td><td class='title_td' id='td_name'><a>" + data.content[i].TITLE + "</a></td><td class='td_date'>" + data.content[i].start + "</td><td class='td_date'>" + data.content[i].end + "</td><td class='Accessva_cls' id='td_access'>" + data.content[i].Accessva + "</td></tr>");
        }

        $('.Accessva_cls').each(function () {
            if ($(this).text() === "승인") {
                $(this).css("color", "cornflowerblue").css("font-weight", "bold");
            } else if ($(this).text() === "반려") {
                $(this).css("color", "indianred").css("font-weight", "bold");
            }
        });

        let pageNumbers = [];
        for (let i = 0; i < data.totalPages; i++) {
            pageNumbers[i] = i;
        }
        let pageNumbersHtml = '';
        pageNumbers.forEach((pageNumber) => {
            if (pageNumber == currentPage2) {
                pageNumbersHtml += `<button class="cur_page">${pageNumber + 1}</button>`;
            } else
                pageNumbersHtml += `<button class="list_page">${pageNumber + 1}</button>`;
        });
        document.getElementById('pageNumbersContainer3').innerHTML = pageNumbersHtml;
        document.getElementById('pagination3').style.display = 'flex';

        $(".list_page").click(function (e) {
            document.getElementById('pagination').style.display = 'none';
            document.getElementById('pagination2').style.display = 'none';
            document.getElementById('pagination3').style.display = 'none';
            $('#table-body').empty();
            const pageNumberText = $(this).text();
            const pageNumber = parseInt(pageNumberText, 10);
            currentPage3 = pageNumber - 1;
            e.preventDefault();
            loadPageDataBusiness(pageNumber - 1);
        });

        if(currentPage3 == 0 && data.totalPages - 1 == 0) {
            $("#next3").attr("disabled", true);
            $("#prev3").attr("disabled", true);
        }else if (currentPage3 == 0) {
            $("#prev3").attr("disabled", true);
            $("#next3").removeAttr("disabled");
        } else if (currentPage3 == data.totalPages - 1) {
            $("#next3").attr("disabled", true);
            $("#prev3").removeAttr("disabled");
        }
    });
}
