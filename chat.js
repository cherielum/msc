// $(function() {    //only open function when document is ready - document.ready
// 	$("#btn-send").click(function(data) {
//         e.preventDefault(); 
        
//         $.ajax({
//             type: "POST", 
//             data: {
//                 id: $(this).val()
//             },
//         })
        
// 	});

// })

jQuery.support.cors = true;
$(document).ready(function(){
    console.log($("#btn-send"));
    $("#btn-send").click(function(e){
        e.preventDefault();
        $.post({url: "http://big-hack.us-east-1.elasticbeanstalk.com/",
         type: "POST", 
         data: JSON.stringify({ "request": "hey" }),
         dataType: "json",
         contentType: "application/json; charset=utf-8",
         failure: function(result) {
            console.log(result);
         },
        success: function(result){
            console.log(result);
        }});
    });
});