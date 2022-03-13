var userName = null;
var stompClient = null;

function setConnected(connected) { //this is not required
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function saveUsername(){
    user = JSON.stringify(userName);
    //converts to base 64 ASCII
    user = btoa(user)
    //save to web storage -- I'm not teaching my team how to use cookies they can barely understand english!
    localStorage.setItem('_user', user);
    return true;
}

function loadUsername() {
    var user = localStorage.getItem('_user');
    //if (!user) alert("somehow you got here without setting your name! Please go back to the home page and try joining the game again!")
    localStorage.removeItem('_user');
    user = atob(user); //decode the data
    user = JSON.parse(user); //parse it
    userName = user;
    alert("user name is " + userName);
}

function refreshPage() {
    //setUsername();
    //location.reload();
    //window.location;
    fetch('http://localhost:8080/gameboard?playername=' + userName , {
        method: "GET"
    }).then (response => response.text())
        .then(result => {
            let parser = new DOMParser();
            doc = parser.parseFromString(result, 'text/html');
            document.replaceChild( doc.documentElement, document.documentElement);
        })
    //loadUsername();
    //window.location = window.location; // can't do this because it uses get requests :(
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/serverMessages', function (message) {
            //alert("message received: " + JSON.parse(message.body).content);
            //if(message.body.substring(12,message.body.length - 2) === "Change"){
            if (JSON.parse(message.body).messagetype === "Join") {
                // get player name
                $("#playerlist").append("<tr><td>" + JSON.parse(message.body).content + "</td></tr>");
                var playercount = document.getElementById("head")
                var oldText = playercount.innerHTML;
                newtext = oldText .replace(/(\d)+?\//g, function(match, number) {
                    return parseInt(number)+1 + "/";
                });


                playercount.innerHTML = newtext;
                console.log("boiiii new text is " + newtext);

                // enable start button
                document.getElementById("start").disabled = false;
            }
            else  if(JSON.parse(message.body).messagetype === "Start"){
                alert("press start game to begin");
                //document.getElementById("startbutton-form").submit();
            }
        });
    });
}

connect(); //somebody has to call connect

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendCard(uname, id) {
    let card = {
        name: uname,
        msg: "play " + id
    }
    let msg = JSON.stringify(card);
    stompClient.send("/app/ServerRcv", {}, msg);
}

function sendName() {
    alert("we fucking got 'em going")
    stompClient.send("/app/hello", {}, JSON.stringify({'name': userName, msg: "testing"}));
}

function playCard(id){
    alert("Card Played " + id);
    sendCard(userName, id);
}

function highlightCards() {
    const handCards = document.querySelectorAll('#hand-list li .card img')
    handCards.forEach(card => {
        card.style.border = '.2em solid greenyellow';
        card.style.borderRadius = '10%';
        card.onclick = function(){playCard(card.id)};
    })
}

function unhighlightCards() {
    const handCards = document.querySelectorAll('#hand-list li .card img')
    handCards.forEach(card => {
        card.style.border = 'none';
    })
}


$(function () {
/*    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });*/
});

