var userName = null;
var stompClient = null;
var submittedStartGameForm = false;

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

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
}

function loadGameOver(){
    fetch('http://localhost:8080/gameOver', {
        method: "GET"
    }).then (response => response.text())
        .then(result => {
            let parser = new DOMParser();
            doc = parser.parseFromString(result, 'text/html');
            document.replaceChild( doc.documentElement, document.documentElement);
        })
}

async function showSPrompt(){
    await sleep(200);
    document.getElementById("SponsorPrompt").style.display = 'block';
}

async function showPrompt(){
    await sleep(200);
    document.getElementById("NoSponsorPrompt").style.display = 'block';
}

async function showTPrompt(){
    await sleep(200);
    document.getElementById("TournamentPrompt").style.display = 'block';
}

async function showEventPrompt(){
    await sleep(200);
    document.getElementById("EventPrompt").style.display = 'block';
}
async function questcontinue(){
    clearPlayArea();
    alert("You have made it to the next stage!");
    //update stage count
    //enable challenge button
    await sleep(200);
    document.getElementById("challenge").style = 'display: block';
    document.getElementById("submitChallenge").style = 'display:  block';
    //reset card highlighting
    unhighlightCards();
    highlightCards();
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
                let oldText = playercount.innerHTML;
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
            }
        });
        if(userName === null){loadUsername();}
        stompClient.subscribe("/user/" + userName + "/reply", function(message) {
            if(JSON.parse(message.body).messagetype === "Win"){
                console.log("Game Over");
                loadGameOver();
            }
            if(JSON.parse(message.body).messagetype === "Prompt") {
                if(JSON.parse(message.body).content === "Sponsor"){
                    showSPrompt();
                }
                else if(JSON.parse(message.body).content === "No Sponsor"){
                    showPrompt();
                }
                else if(JSON.parse(message.body).content === "Foe Issue"){
                    alert("Too few foes");
                }
                else if(JSON.parse(message.body).content === "Tournament"){
                    showTPrompt();
                }
                else if(JSON.parse(message.body).content === "EventCard"){
                    showEventPrompt();
                }
            }
            else if(JSON.parse(message.body).messagetype === "Tournament"){
                if(JSON.parse(message.body).content === "Participate"){

                    document.getElementById("tournament").style.display = 'block';
                    document.getElementById("tournamentText").style.display = 'block';
                    highlightCards();
                }
                else if(JSON.parse(message.body).content === "Wait") {
                    clearPlayArea();
                    alert("Waiting for other players");
                    document.getElementById("tournament").style.display = 'none';
                    document.getElementById("tournamentText").style.display = 'none';
                    unhighlightCards();
                }
            }
            else if(JSON.parse(message.body).messagetype === "Quest"){
                if(JSON.parse(message.body).content === "First"){
                    alert("You are the sponsor");
                    document.getElementById("makeStage").style.display = 'block';
                    document.getElementById("playCards").style.display = 'block';
                    highlightCards();
                }
                else if(JSON.parse(message.body).content === "Next"){
                    //increase stage count
                    let stagecount = document.getElementById("stageText");
                    let oldText = stagecount.innerHTML;
                    newtext = oldText .replace(/(\d)/g, function(match, number) {
                        return parseInt(number)+1;
                    });
                    stagecount.innerHTML = newtext

                    clearPlayArea();
                    unhighlightCards(); //TODO: we should make a re-highlight function that does all this
                    highlightCards();
                }

                else if(JSON.parse(message.body).content === "Bid Won"){
                    alert("you win the bid and continue the quest alone")
                    //increase stage count
                    let stagecount = document.getElementById("stageText");
                    let oldText = stagecount.innerHTML;
                    newtext = oldText .replace(/(\d)/g, function(match, number) {
                        return parseInt(number)+1;
                    });
                    stagecount.innerHTML = newtext

                    clearPlayArea();
                    unhighlightCards();
                    //no need to highlight cards again, we will get a "next quest" to do that
                }

                else if(JSON.parse(message.body).content === "Invalid"){
                    //do nothing?
                    alert("Invalid stage");
                }
                else if(JSON.parse(message.body).content === "Complete"){
                    clearPlayArea();
                    document.getElementById("makeStage").style.display = 'none';
                    document.getElementById("playCards").style.display = 'none';
                    unhighlightCards();
                }
                else if(JSON.parse(message.body).content === "Stage"){
                    clearPlayArea();

                    //increase stage count
                    let stagecount = document.getElementById("challengeText");
                    let oldText = stagecount.innerHTML;
                    newtext = oldText .replace(/(\d)/g, function(match, number) {
                        return parseInt(number)+1;
                    });
                    stagecount.innerHTML = newtext

                    document.getElementById("challenge").style.display = 'block';
                    document.getElementById("submitChallenge").style.display = 'block';
                    unhighlightCards();
                    highlightCards();
                }
                else if(JSON.parse(message.body).content === "Continue"){
                    questcontinue();
                }
                else if(JSON.parse(message.body).content.split(" ")[0] === "BidRequest"){
                    alert("A test has been started! Please bid a minimum of " + JSON.parse(message.body).content.split(" ")[1] + " cards!")
                    unhighlightCards();
                    biddingHighlight();
                    document.getElementById("Bid").style.display = 'block';
                    document.getElementById("submitBid").style.display = 'block';
                }
                else if(JSON.parse(message.body).content === "Lose"){
                    alert("You lost the quest!");
                    clearPlayArea();
                    unhighlightCards();
                    document.getElementById("challenge").style.display = 'none';
                    document.getElementById("submitChallenge").style.display = 'none';
                }
                else if(JSON.parse(message.body).content === "Bid Lost"){
                    alert("You lost the bid!");
                    clearPlayArea();
                    unhighlightCards();
                    document.getElementById("challenge").style.display = 'none';
                    document.getElementById("submitChallenge").style.display = 'none';
                }
                else if(JSON.parse(message.body).content === "Wait") {
                    clearPlayArea();
                    alert("Waiting for other players");
                    document.getElementById("challenge").style.display = 'none';
                    document.getElementById("submitChallenge").style.display = 'none';
                    unhighlightCards();
                }
            }
            else if(JSON.parse(message.body).messagetype === "Update"){
                refreshPage();
            }
        });
        stompClient.send("/app/gameStart",{},JSON.stringify({'name': userName, msg: "Start"}));
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

function challenge(){
    var cards = document.querySelectorAll('#played-list .card img')
    var message = ""
    cards.forEach(card => {
        message += (card.id + ",");
    })
    console.log(message)
    stompClient.send("/app/challenge", {}, JSON.stringify({'name': userName, msg: message}))
}

function playTournament(){
    var cards = document.querySelectorAll('#played-list .card img')
    var message = ""
    cards.forEach(card => {
        message += (card.id + ",");
    })
    console.log(message)
    stompClient.send("/app/tournament", {}, JSON.stringify({'name': userName, msg: message}))
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

function discard(e){
    let discardPile = document.getElementById("discard");
    discardPile.appendChild(e);
    e.onclick = function (){}
}

function moveCard(e){
    let list1 = document.getElementById("hand");
    // let list2 = document.getElementById("played-list");
    let list2 = document.getElementById("played-list");
    let moveTo = e.parentElement === list1 ? list2 : list1;
    //if (moveTo == list1 && list2.className === 'discard') return;
    moveTo.appendChild(e);
    unhighlightCards();
    highlightCards();
}

function addCardToBid(e){
    let list1 = document.getElementById("hand");
    // let list2 = document.getElementById("played-list");
    let list2 = document.getElementById("played-list");
    let moveTo = e.parentElement === list1 ? list2 : list1;
    //if (moveTo == list1 && list2.className === 'discard') return;
    moveTo.appendChild(e);
}

function submitPrompt(e){
    document.getElementById("SponsorPrompt").style.display = 'none';
    document.getElementById("NoSponsorPrompt").style.display = 'none';
    document.getElementById("TournamentPrompt").style.display = 'none';
    document.getElementById("EventPrompt").style.display = 'none';
    stompClient.send("/app/prompt", {}, JSON.stringify({'name': userName, msg: e.className}));
}
function highlightCards() {

    //highlight all allies no matter what
    const handAllies = document.querySelectorAll('#hand .card.ally img')
    handAllies.forEach(card => {
        card.style.border = '.2em solid DeepSkyBlue';
        card.style.borderRadius = '10%';
        card.parentElement.onclick = function(){moveCard(card.parentElement)};
    })

    let makingStage = (document.getElementById("playCards").style.display === 'block');

    if(makingStage){
        //if you have a test queued then you most certainly cannot play anything else
        const playedTest = document.querySelector('#played-list .card.test img');
        if (playedTest != null){
            return;
        }

    }

    const playedFoe = document.querySelector('#played-list .card.foe img');

    if (playedFoe === null){
        console.log("playedfoe is null")
        const handFoes = document.querySelectorAll('#hand .card.foe img')
        handFoes.forEach(card => {
            card.style.border = '.2em solid orange';
            card.style.borderRadius = '10%';
            card.parentElement.onclick = function(){moveCard(card.parentElement)};
        })
        if (makingStage){
            const handTests = document.querySelectorAll('#hand .card.test img')
            handTests.forEach(card => {
                card.style.border = '.2em solid green';
                card.style.borderRadius = '10%';
                card.parentElement.onclick = function(){moveCard(card.parentElement)};
            })
        }
        else{
            const handAmours = document.querySelectorAll('#hand .card.amour img')
            handAmours.forEach(card => {
                card.style.border = '.2em solid HotPink';
                card.style.borderRadius = '10%';
                card.parentElement.onclick = function(){moveCard(card.parentElement)};
            })

        }
    }
    else{
        const playedWeapons = document.querySelectorAll('#played-list .card.weapon img');
        const handWeapons = document.querySelectorAll('#hand .card.weapon img')
        handWeapons.forEach(card => {
            //check if a weapon of this name has been played already
            var found = false;
            playedWeapons.forEach(weap => {
                if (weap.classList.contains(card.classList)){
                    found = true;
                }
            }
             )
            if (!found){
                card.style.border = '.2em solid greenyellow';
                card.borderRadius = '10%';
                card.parentElement.onclick = function(){moveCard(card.parentElement)};
            }
        })
    }

}
function biddingHighlight() {
    //highlight all cards for bidding
    const handCards = document.querySelectorAll('#hand .card img')
    handCards.forEach(card => {
        card.style.border = '.2em solid Red';
        card.style.borderRadius = '10%';
        card.parentElement.onclick = function () {
            addCardToBid(card.parentElement)
        };
    })
}

function bidCards(){
    var cards = document.querySelectorAll('#played-list .card img')
    var message = ""
    cards.forEach(card => {
        message += (card.id + ",");
    })
    console.log(message)
    stompClient.send("/app/bidCards", {}, JSON.stringify({'name': userName, msg: message}))

    unhighlightCards();
    document.getElementById("Bid").style.display = 'none';
    document.getElementById("submitBid").style.display = 'none';
}

function addAlly(e){
    let allies = document.getElementById("allies");
    allies.appendChild(e);
    e.onclick = function (){}
}

function clearPlayArea(){
    var cards = document.querySelectorAll('#played-list .card')
    cards.forEach(card => {
        if (!card.classList.contains("ally")){
            discard(card);
        }
        else{
            addAlly(card); //TODO: Matt, when you make a div for ally cards to go in I would appreciate if you could put this card in the allies div :)
        }
        //card.parentElement.removeChild(card);
    })
}

function unhighlightCards() {
    const handCards = document.querySelectorAll('#hand .card img')
    handCards.forEach(card => {
        card.style.border = 'none';
        card.parentElement.onclick = function(){};
    })
    const discardCards = document.querySelectorAll('#discard .card img')
    discardCards.forEach(card=>{
        card.style.border= 'none';
        card.parentElement.onclick = function(){};
    })
}

function openDiscard() {
    let discard = document.querySelector('#discard');

    let selection = document.createElement('div');
    selection.id = 'selection';

    let button = document.createElement('button');
    button.onclick = function() {
        discard.className = 'discard'
        discard.onclick = openDiscard;
        document.querySelector('body').appendChild(discard);
        selection.remove();
    }
    selection.appendChild(button);

    discard.className = '';
    discard.onclick = '';
    selection.appendChild(discard);

    document.querySelector('body').appendChild(selection);
}

function openAllies(allies) {
    let selection = document.createElement('div');
    selection.id = 'selection';

    function paramlessOpen() { openAllies(allies); };

    let className = allies.className;
    let parent = allies.parentElement;
    let button = document.createElement('button');
    button.onclick = function() {
        allies.className = className;
        allies.onclick = paramlessOpen;
        parent.appendChild(allies);
        selection.remove();
    }
    selection.appendChild(button);

    allies.className = '';
    allies.onclick = '';
    selection.appendChild(allies);

    document.querySelector('body').appendChild(selection);
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

