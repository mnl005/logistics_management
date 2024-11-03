
let running = false;


let json_file =  (async () => { json_file = await $.getJSON("../static/main.json"); })();
// let json_file =  (async () => { json_file = await $.getJSON("/static/main.json"); })();

// main.json 에서 정보 받아 담는 객체
let main = {
    url : null,
    type : null,
    event : null,
    error : null,

    id_data : null,
    msg : null,
    req_data: null,
    res_data: null,

    req_define : null,
    res_define : null,

    data_json : null,
    event_target : null,

    redirect : null
}


// 초기화면 설정
// pop(["#logistics"]);
// pop(["#group",".group_infos",".group_user_info",".group_create",".group_list",".group_invite",".group_invite_list"]);
pop(["#login",".login_form",".join_form"]);
// pop(["#logistics",".inventory_view"]);

// 클릭시 요청을 보내거나 이벤트를 처리
$(document).on('click', '.button', function(event) {
    if(running){
        return null;
    }
    else{
        running = true;
    }

    start(event)
        .then(event => {
            // html에서 요소 추출
            main.data_json = $(this).data('json');
            console.log(main.data_json);
            main.id_data = $(this).data('id');
            main.event_target = event;
            return main;
        })
        .then(main => {
            // 추출한 요소중 data_json 참고해서 지시사항 가져온다
                let get_json_test = (typeof main.data_json === "string") ? Object.assign({}, json_file[main.data_json]) : main.data_json;
                main.url = get_json_test.url;
                main.type = get_json_test.type;
                main.event = get_json_test.event;
                main.error = get_json_test.error;

                main.msg = null;
                main.req_data = null;
                main.res_data = null;

                main.req_define = get_json_test.req_define;
                main.res_define = get_json_test.res_define;
                // 다른 페이지로 바로 이동
                if (main.type === "get") window.location.href = main.url;
            return main;
        })
        .then(main => {
            // 클라이언트 이벤트 실행
            client_event(main.event, main.event_target);
            return main;
        })
        .then( main => {
            // 폼에서 필요한 정보 추출
            const form_data =  form(main);
            // 폼에서 이미지 추출시 시간이 걸림으로 딜레이 시간을 준다
            return delay(0,form_data).then(main => {return main;});
        })
        .then(async main => {
            console.log("req----------",main);
            // 요청을 보내야 하는 경우 요청을 보낸다
            if(main.url !== "none"){
                return send(main);
            }
            else{
                return main;
            }
        })
        .then(main => {
            // 리다이렉트시 처리
            if(typeof main.redirect === "string"){
                window.location.href = main.redirect
            }
            // 요청을 보낸경우 응답을 확인하고 받은 응답을 클라이언트에 보여준다
            if(main.url !== "none"){
                console.log("res----------",main);
                if("err_msg" in main){
                    msg(main.err_msg,"red");
                }
                else{
                    msg(main.msg,"cornflowerblue");
                    if(main.res_data !== null){
                        data_spread(main.res_define,main.res_data);
                    }
                }
            }
            else{
                console.log("클라이언트이벤트종료");
            }
            running = false;
            return main;
        })
        .catch(result => {
            console.log(result);
            running = false;
            // msg(result,"red");
        });

});

// 딜레이 부여
function delay(delayTime,obj) {
    return new Promise(resolve => {
        let count = 0;
        const interval = setInterval(() => {
            count++;
            if (count * 1000 >= delayTime) {
                clearInterval(interval);
                resolve(obj);
            }
        }, 1000);
    });
}

// 딜레이 적용
function start(event) {
    return new Promise((resolve) => {
        resolve(event);
    })
}


// 폼에서 데이터 추출
function form(result) {
    if(result.req_define && result.req_define.includes("form_data")){
        let req_data = {};
        let form = $(result.event_target.target.closest('form')).find(':input').not(':button');
        form.each(function() {
            let inputType = $(this).attr('type');
            let inputValue = $(this).val();
            let inputName = $(this).attr('name');


            if (inputType === 'checkbox') {req_data[inputName] = $(this).is(':checked');}
            else if (inputType === 'number') {req_data[inputName] = parseFloat(inputValue);}
            else if (inputType === 'file') {
                let boxs = $(this).siblings(".image_box").children(".image");
                let base64 = "";
                boxs.each((index, element) => {
                    base64 += $(element).css('background-image');
                    base64 += "~~~~";
                });
                req_data[inputName] = base64;
            }
            else{req_data[inputName] = inputValue;}
        });
        result.req_data = req_data;
    }
    return result;
}

// 명시된 url로 json 요청을 보낸다
function send(req) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: req.url,
            type: req.type,
            contentType: "application/json",
            dataType : "json",
            data: JSON.stringify(req),
            success: function(res) {
                resolve(res);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                reject(errorThrown);
            }
        });
    });
}


// 무엇을, 어디에, 어떻게, 방식
// what, where, how, way
// 받응 응답에 있는 데이터를 사용자 화면에 뿌린다
function data_spread(res_define,data){
    res_define.forEach(obj => {

        // 받은 데이터, 데이터를 삽입할 곳, 키와 값의 쌍, 삽입하는 방식
        let what = data[obj.what];
        let where = obj.where;
        let how = obj.how;
        let way = obj.way;


        if(way === "object"){
            how.forEach(ob => {
                let parts = ob.split("-");

                // 삽입할 요소, 삽입할 값, 삽입할 값의 타입
                // 삽입할 요소는 html의 클래스 이름
                let key = parts[0];
                let value;
                try{
                    value = what[key].toString();
                }
                catch (e){
                  value = "값없음";
                }
                let type = parts[1];


                if(type === "image"){
                    let str = value.split("~~~~");
                    if(str[0].length <= 30){
                        $(where).find("." + key).css("background-image", "none");
                    }
                    else{
                        console.log("2");
                        $(where).find("." + key).css("background-image", str[0]);
                    }
                }
                else if(type === "text"){
                    $(where).find("." + key).text(value);
                }
                else if(type === "id"){
                    $(where).find(".button").attr("data-id", value);
                }
            });
        } // 받은 데이터, 데이터를 삽입할 곳, 키와 값의 쌍, 삽입하는 방식
        else if(way === "array"){
            what.forEach(ob => {
                let sample = $(where).find('.sample').eq(0).clone();
                how.forEach(obob => {
                        let parts = obob.split("-");
                        let key = parts[0];
                        let type = parts[1];
                        let value;
                    try{
                        value = ob[key].toString();
                    }
                    catch (e){
                        value = "값없음";
                    }


                        if(type === "image"){
                            let str = value.split("~~~~");
                            if(str[0].length <= 30){
                                sample.find("." + key).css("background-image", "none");                            }
                            else{
                                sample.find("." + key).css("background-image", str[0]);
                            }
                        }
                        if(type === "image_box"){
                            let image_one = value.split("~~~~");
                            sample.find(".image").not(':first').remove();
                            sample.find(".image").css("background-image","none");
                            image_one.forEach(img => {
                                let keyElement = sample.find("." + key).eq(0).clone();
                                keyElement.css({
                                    "background-image": img,
                                    "display": "none"
                                });

                                sample.find(".image_box").children('.image').last().after(keyElement);

                            })
                            sample.find(".image_box").children('.image').first().remove();
                            sample.find(".image_box").children('.image').last().remove();
                            sample.find(".image_box").children('.image').eq(0).css("display","block");

                        }
                        else if(type === "text"){
                            sample.find("." + key).text(value);
                        }
                        else if(type === "id"){
                            sample.find(".button").attr("data-id", value);
                        }
                    }
                );

                $(where).append(sample);
            });

            // 해당 요소가 같은 클래스를 가진 형제 요소가 있는지 확인
            if (!($(where).find('.sample').siblings('.sample').length === 0)) {
                $(where).find('.sample').eq(0).remove();
            }
        }
        else if(way === "html"){
            console.log("html");
        }

    });
}


let messageTimeout; // 타이머를 저장할 변수
function msg(text, color) {
    const $msg = $("#msg");
    $msg.text(text).css("background-color", color).stop(true, true).slideDown();
    if (messageTimeout) {
        clearTimeout(messageTimeout);
    }
    messageTimeout = setTimeout(function() {
        $msg.stop(true, true).slideUp(function() {
            $(this).css("background-color", "cornflowerblue");
        });
    }, 3000);
}



function client_event(arr,events){
        arr.forEach(obj => {
            window[obj.do](obj.list,events);
        });
}
function right_image(list,events){
    let selector = $(events.target).parent().find('.image');
    let all = selector.length -1;
    let now = selector.filter(":visible").index();

    if(now < all){
        selector.hide();
        selector.eq(now + 1).show();
    }
    else{
        selector.hide();
        selector.eq(0).show();
    }
}

function left_image(list,events){
    let selector = $(events.target).parent().find('.image');
    let all = selector.length -1;
    let now = selector.filter(":visible").index();

    if(now >= all){
        selector.hide();
        selector.eq(now - 1).show();
    }
    else{
        selector.hide();
        selector.eq(all).show();
    }
}


function pop(list,evnets){
    $(".pop").stop().hide();
    list.forEach(selector => {
        $(selector).stop().show();
        let sample = $(selector).children('.sample').eq(0).clone();
        if(sample.length){
            $(selector).children(".sample").remove();
            $(selector).append(sample);
        }
    }
    );
}

function toggle(list,events){
    list.forEach(selector => {
        $(selector).stop().toggle();
    });
}

function clean(list,events){
    list.forEach(selector => {

    });
}

$('input[type="file"]').change(function() {
    let input = this;
    if (input.files && input.files[0]) {
        let file = this.files[0];
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function() {
            $(input).siblings('.image_box').prepend('<div class="image" style="background-image:url(' + reader.result + ')"></div>');
        };
    }
});




