$(function () {
// 현재 페이지 URL이 /main 인 경우에만 웹소켓 연결을 시작합니다.
    let charRoom = userNum + receiveUserNum;
    let currentURL = window.location.href;
    const chaturl = `ws://${location.host}/database-change?charRoom=` + charRoom;
    window.onload = () => {

        setupWebSocketAsync();
    };

    function scrollToBottom() {
        let messageDiv = document.getElementById("message_div");
        messageDiv.scrollTop = messageDiv.scrollHeight;
    }


    async function setupWebSocketAsync() {
        const socket = new WebSocket(chaturl);

        socket.onopen = () => {
            console.log(chaturl);
            console.log("Connected to WebSocket server");
        };

        socket.onmessage = (event) => {
            // if (event.data == "left") {
            //     console.log("left")
            //     $('#user_check').val("0");
            // } else if (event.data == "joined") {
            //     console.log("joined")
            //     $('#user_check').val("1");
            //     $("#send_text").removeAttr("disabled");
            //     $('#send_text').attr("placeholder", "메세지를 입력하세요");
            // } else {
            let jsonObj = JSON.parse(event.data);
            console.log(jsonObj.sendUser);
            if (jsonObj.receiveUser == userNum || jsonObj.sendid == userNum) {
                // 새로운 테이블 아이템 생성
                let newItem = document.createElement('div');
                newItem.className = 'item';

                let userProfileStyle = jsonObj.sendid == userNum ? 'position: absolute; right: 0; top: 0;' : '';
                let onOffTableStyle = jsonObj.sendid == userNum ? 'position: absolute; right: 0; top: 33px;' : '';
                let dateStyle = jsonObj.sendid == userNum ? 'position: absolute; right: 0; top: 70px;' : '';

                newItem.innerHTML = `
                <div class="te">
                    <div class="user_prof" style="${userProfileStyle}">
                   <img src="/upload/${jsonObj.sendUserImg}"
                         alt="유저 프로필 이미지"
                         class="img_list"
                         style="font-size: 0px"
                    />
                    <p  class="username_p">${jsonObj.sendUser}님</p>
                </div>
                <table class="onoff_table" style="${onOffTableStyle}">
                    <tr>
                        <td class="message_list">
                ${jsonObj.sendMsg}
            </td>
                    </tr>
                </table>
                                        <p class="date" style="${dateStyle}">${jsonObj.sendDate}</p>
                </div>
    `;

                // "onoff-grid-container" 요소에 새 아이템 추가
                let gridContainer = document.getElementById('onoff-grid-container');
                gridContainer.appendChild(newItem);
                scrollToBottom();
            }
        }
        // WebSocket이 종료될 때까지 기다리기
        await new Promise((resolve) => {
            socket.addEventListener("close", resolve);
        });
    }
});