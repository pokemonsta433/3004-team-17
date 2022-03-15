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
    user = atob(user); //decode the data
    console.log(user);
    user = JSON.parse(user); //parse it
    userName = user;
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
    //window.location = window.location; // can't do this because it uses get requests :(
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/serverMessages', function (message) {
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
        if(userName === null){loadUsername();}
        stompClient.subscribe("/user/" + userName + "/reply", function(message) {
            if(JSON.parse(message.body).messagetype === "Prompt") {
                if(JSON.parse(message.body).content === "Sponsor"){
                    document.getElementById("SponsorPrompt").style.display = 'block';
                }
                else if(JSON.parse(message.body).content === "No Sponsor"){
                    document.getElementById("NoSponsorPrompt").style.display = 'block';
                }
            }
            else if(JSON.parse(message.body).messagetype === "Quest"){
                if(JSON.parse(message.body).content == "First"){
                    alert("You are the sponsor");
                    document.getElementById("makeStage").style.display = 'block';
                    document.getElementById("playCards").style.display = 'block';
                    highlightCards();
                }
                else if(JSON.parse(message.body).content == "Next"){
                    var stagecount = document.getElementById("stageText");
                    var oldText = stagecount.innerHTML;
                    newtext = oldText .replace(/(\d)/g, function(match, number) {
                        return parseInt(number)+1;
                    });
                    stagecount.innerHTML = newtext
                    var cards = document.querySelectorAll('#played-list .card')
                    cards.forEach(card => {
                        card.parentElement.removeChild(card);
                    })
                    //clear playable cards
                }
                else if(JSON.parse(message.body).content == "Invalid"){
                    //do nothing?
                    alert("Invalid stage");
                }
                else if(JSON.parse(message.body).content == "Complete"){
                    var cards = document.querySelectorAll('#played-list .card')
                    cards.forEach(card => {
                        card.parentElement.removeChild(card);
                    })
                    document.getElementById("makeStage").style.display = 'none';
                    document.getElementById("playCards").style.display = 'none';
                    unhighlightCards();
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
    stompClient.send("/app/hello", {}, JSON.stringify({'name': userName, msg: "testing"}));
}


function playCards(){
    var cards = document.querySelectorAll('#played-list .card img')
    var message = ""
    cards.forEach(card => {
        message += (card.id + ",");
    })
    console.log(message)
    stompClient.send("/app/playCards", {}, JSON.stringify({'name': userName, msg: message}))
}

function moveCard(e){
    let list1 = document.getElementById("hand");
    // let list2 = document.getElementById("played-list");
    let list2 = document.getElementById("discard");
    let moveTo = e.parentElement === list1 ? list2 : list1;

    if (moveTo == list1 && list2.className === 'discard') return;

    moveTo.appendChild(e);
}

function submitPrompt(e){
    document.getElementById("SponsorPrompt").style.display = 'none';
    document.getElementById("NoSponsorPrompt").style.display = 'none';
    stompClient.send("/app/prompt", {}, JSON.stringify({'name': userName, msg: e.className}));
}

function highlightCards() {
     const handCards = document.querySelectorAll('#hand .card')
     handCards.forEach(card => {
         card.style.border = '.2em solid greenyellow';
         card.style.borderRadius = '10%';
         card.onclick = function(){moveCard(card)};
     })
 }

 function unhighlightCards() {
    const handCards = document.querySelectorAll('#hand li .card img')
     handCards.forEach(card => {
         card.style.border = 'none';
         card.onclick = "";
     })
}

document.addEventListener('DOMContentLoaded', function() {
    let discard = document.querySelector('#discard');

    function openDiscard(e) {
        let selection = document.createElement('div');
        selection.id = 'selection';

        let button = document.createElement('button');
        button.onclick = function() {
            discard.className = 'discard'
            discard.addEventListener('click', openDiscard);
            document.querySelector('body').appendChild(discard);
            selection.remove();
        }
        selection.appendChild(button);

        discard.className = '';
        discard.removeEventListener('click', openDiscard);
        selection.appendChild(discard);

        document.querySelector('body').appendChild(selection);
    }

    discard.addEventListener('click', openDiscard);
});

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

