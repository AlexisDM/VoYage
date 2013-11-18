function funcEmptyForm() {
	$("#usr_nom").val("");
	$("#usr_prenom").val("");
	$("#usr_age").val("");
	$("#usr_email").val("");
	$("#usr_login").val("");
	$("#usr_password").val("");
	$("#usr_rePassword").val("");
}

function funcSendInfo() {
	if($("#usr_password").val() == $("#usr_rePassword").val()) {
		$.post("formulaire",
	  		  {
	  		    cmd:"PostInfo",
	  		    nom:$("#usr_nom").val(),
	  		    prenom: $("#usr_prenom").val(),
	  		    age: $("#usr_age").val(),
	  			email: $("#usr_email").val(),
	  		    login: $("#usr_login").val(),
	  		    password: $("#usr_password").val()
	  		  },
	  		  function(){
	  			window.location.href = 'login.html';
	  	});
	} else {
		alert("Mots de passe différents");
	}
}

function funcLog() {
	$.post("login",
		{
			cmd:"PostLogin",
			login: $("#usr_login").val(),
			password: $("#usr_password").val()
		},
		function(data,status){
			if(data == "Failed") {
				$('#lblErrorPassword').text("Bad connection parameters. Please try again");
			} else if(data == "FirstConn") {
				window.location.href = 'changepassword.html'
			} else {
				var tab = new Array();
				tab = data.split(";");
				window.location.href = 'flights.html;
			}
		});  
}

function funcChangePass() {
	if($("#usr_newPassword").val() === $("#usr_repeatPassword").val()) {
		$.post("login",
				{
					cmd:"PostChangePass",
					oldPassword: $("#usr_oldPassword").val(),
					newPassword: $("#usr_newPassword").val()
				},
				function(data,status){
					alert(data);
					if(data == "fail") {
						$('#lblErrorPassword').text("Bad connection parameters. Please try again");
					} else {
						window.location.href = 'flights.html';
					}
				});
	}
}

function funcLogAdmin() {
	$.post("logAdmin",
		{
			cmd:"PostInfo",
			login: $("#adm_login").val(),
			password: $("#adm_password").val()
		},
		function(data,status){
			if(data == "Failed") {
				$('#lblErrorPassword').text("Bad connection parameters. Please try again");
			} else {
				var tab = new Array();
				tab = data.split(";");
				window.location.href = 'admin.html?login='.concat(tab[0]).concat("&nom=").concat(tab[1]).concat("&prenom=").concat(tab[2]).concat("&lastConnexionDate=").concat(tab[3]).concat("&lastConnexionTime=").concat(tab[4]);
			}
		});  
}

function funcDisconnect() {
	$.post("Admin",
		{
			cmd:"Disconnect"
		},
		function(data,status){
			if(data == "Failed") {
				alert("You couldn't be disconnected")
			} else {
				window.location.href = 'loginadmin.html';
			}
		});  
}

function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
    );
}

function funcFillData() {
	$("#userName").text(getURLParameter("login"));
	$("#fieldUserFirstName").text(getURLParameter("prenom"));
	$("#fieldUserLastName").text(getURLParameter("nom"));
	$("#fieldUserAge").text(getURLParameter("age"));
	$("#fieldUserEmail").text(getURLParameter("email"));
	$("#fieldUserLogin").text(getURLParameter("login"));
}

function funcFillDataAdmin() {
	$("#userName").text(getURLParameter("login"));
	$("#fieldAdminFirstName").text(getURLParameter("prenom"));
	$("#fieldAdminLastName").text(getURLParameter("nom"));
	$("#fieldAdminLogin").text(getURLParameter("login"));
	$("#fieldAdminLastConnexionDate").text(getURLParameter("lastConnexionDate"));
	$("#fieldAdminLastConnexionTime").text(getURLParameter("lastConnexionTime"));
}