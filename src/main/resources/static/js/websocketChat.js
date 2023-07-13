$(function () {
// 현재 페이지 URL이 /main 인 경우에만 웹소켓 연결을 시작합니다.
    let charRoom = userNum + receiveUserNum;
    let currentURL = window.location.href;

    window.onload = () => {
            const chaturl = `ws://${location.host}/database-change?charRoom=` + charRoom;
            setupWebSocketAsync(chaturl);
    };

    function scrollToBottom() {
        let messageDiv = document.getElementById("message_div");
        messageDiv.scrollTop = messageDiv.scrollHeight;
    }


    async function setupWebSocketAsync(url) {
        const socket = new WebSocket(url);

        socket.onopen = () => {
            console.log(url);
            console.log("Connected to WebSocket server");
        };

        socket.onmessage = (event) => {
            console.log("---------------");
            console.log(event);
            console.log("---------------");
            let jsonObj = JSON.parse(event.data);

            // 새로운 테이블 아이템 생성
            let newItem = document.createElement('div');
            newItem.className = 'item';

            newItem.innerHTML = `
                    <div class="user_prof">
                       <img src="/upload/${jsonObj.sendUserImg}"
                             alt="유저 프로필 이미지"
                             class="img_list"
                             style="font-size: 0px"
                        />
                        <p  class="username_p">${jsonObj.sendUser}님</p>
                    </div>
                    <table class="onoff_table">
                        <tr>
                            <td class="message_list">
                    ${jsonObj.sendMsg}
                </td>
                        </tr>
                    </table>
    `;

            // "onoff-grid-container" 요소에 새 아이템 추가
            let gridContainer = document.getElementById('onoff-grid-container');
            gridContainer.appendChild(newItem);
            scrollToBottom();

        };

        socket.onclose = () => {
            console.log("Disconnected from WebSocket server");
        };

        // WebSocket이 종료될 때까지 기다리기
        await new Promise((resolve) => {
            socket.addEventListener("close", resolve);
        });
    }
});