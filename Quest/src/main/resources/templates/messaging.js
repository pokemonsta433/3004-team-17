var userName = null;
var stompClient = null;
var submittedStartGameForm = false;

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
                // enable start button
                document.getElementById("start").disabled = false;
            }
            else  if(JSON.parse(message.body).messagetype === "Start"){
                //alert("press start game to begin");
                if(!submittedStartGameForm){
                    submittedStartGameForm = true;
                    var theform = document.getElementById("startbutton-form");
                    theform.requestSubmit();
                }
            }
            else if(JSON.parse(message.body).messagetype === "Validity"){
                alert("Valid cards played: " + JSON.parse(message.body).content)
            }
        });
        stompClient.subscribe("/user/" + userName + "/reply", function(message) {
            if(JSON.parse(message.body).messagetype === "Prompt") {
                if(JSON.parse(message.body).content === "Sponsor"){
                    document.getElementById("SponsorPrompt").style.display = 'block';
                }
                else if(JSON.parse(message.body).content === "No Sponsor"){
                    document.getElementById("NoSponsorPrompt").style.display = 'block';
                }
            }
        });
        stompClient.send("/app/gameStart",{},JSON.stringify({'name': userName, msg: "Start"})); //idk where to put this
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



function sendName() {
    alert("we fucking got 'em going")
    stompClient.send("/app/hello", {}, JSON.stringify({'name': userName, msg: "testing"}));
}


function playCards(){
    var cards = document.querySelectorAll('#played-list li .card img')
    var message = ""
    cards.forEach(card => {
        message += (card.id + ",");
    })
    stompClient.send("/app/playCards", {}, JSON.stringify({'name': userName, msg: message}))
}

function moveCard(e){
    var list1 = document.getElementById("hand-list");
    var list2 = document.getElementById("played-list");
    var moveTo = e.parentElement.parentElement.parentElement === list1 ? list2 : list1;
    moveTo.appendChild(e.parentElement.parentElement);
}

function submitPrompt(e){
    document.getElementById("SponsorPrompt").style.display = 'none';
    document.getElementById("NoSponsorPrompt").style.display = 'none';
    stompClient.send("/app/prompt", {}, JSON.stringify({'name': userName, msg: e.className}));
}
function highlightCards() {
    const handCards = document.querySelectorAll('#hand-list li .card img')
    handCards.forEach(card => {
        card.style.border = '.2em solid greenyellow';
        card.style.borderRadius = '10%';
        card.onclick = function(){moveCard(card)};
    })
}

function unhighlightCards() {
    const handCards = document.querySelectorAll('#hand-list li .card img')
    handCards.forEach(card => {
        card.style.border = 'none';
    })
}
/*
* --------------------------------------
*        Rough Draggable Code
* --------------------------------------
* */
// document.addEventListener('DOMContentLoaded', function() {
//     document.onselectstart = () => { return false; }
//
//     let x = 0;
//     let y = 0;
//
//     document.addEventListener('mousemove', (e) => {
//         x = e.clientX;
//         y = e.clientY;
//     });
//
//     let cards = document.querySelectorAll('#player-hand #hand-list li .card');
//
//     cards.forEach(card => {
//         card.firstElementChild.ondragstart = () => { return false; }
//
//         let mousedown = false;
//         let interval = null;
//         let init = false;
//
//         function clearDrag() {
//             clearInterval(interval);
//             card.style = '';
//             card.className = 'card'
//             init = false;
//         }
//
//         let initPos = null;
//         card.addEventListener('mousedown', () => {
//             mousedown = true;
//             card.className = 'selected-card'
//             interval = setInterval(() => {
//                 if (!mousedown) {
//                     clearDrag(); // for some reason not clearing interval, could lag if not fixed
//                     return;
//                 }
//
//                 if (!init) {
//                     init = true;
//                     initPos = card.getBoundingClientRect();
//                 }
//
//                 card.style = `transform: translate(${-(initPos.x - (x - card.clientWidth / 2))}px, ${-(initPos.y - (y - card.clientHeight / 2))}px);`;
//                 // card.style.top = (y - card.clientHeight / 2) + 'px';
//                 // card.style.left = (x - card.clientWidth / 2) + 'px';
//             }, 100);
//         });
//
//         card.addEventListener('mouseup', () => {
//             mousedown = false;
//             clearDrag();
//         });
//     });
// });

$(function () {
/*    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });*/
});

