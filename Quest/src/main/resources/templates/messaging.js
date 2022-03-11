var userName = 'Isaac';

var socket = new SockJS('/gs-guide-websocket');
stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', function (greeting) {
        showGreeting(JSON.parse(greeting.body).content);
    });
});

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function playCard(card){
   alert ("played card" + card)
    return true;
}

function highlightCards() {
    const handCards = document.querySelectorAll('#hand-list li .card img')
    handCards.forEach(card => {
        card.style.border = '.2em solid greenyellow';
        card.style.borderRadius = '10%';
        card.onclick = function(){playCard(card)};
    })
}

function unhighlightCards() {
    alert("it fuckin' worked btw :)");
    const handCards = document.querySelectorAll('#hand-list li .card img')
    handCards.forEach(card => {
        card.style.border = 'none';
    })
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

