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
				window.location.href = 'flights.html';				
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

function funcGetFlights() {
	/*$.post("Flight",
			{
				cmd:"LoadUsers"
			},
			function(data,status){
				if(data == "Failed") {
					alert("Error loading users list")
				} else {
					
			});  */
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

function funcManageUsers() {
	$.post("Admin",
		{
			cmd:"ManageUsers"
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error accessing other page")
			} else {
				var tab = new Array();
				tab = data.split(";");
				window.location.href = 'manageusers.html?login='.concat(tab[0]).concat("&nom=").concat(tab[1]).concat("&prenom=").concat(tab[2]).concat("&lastConnexionDate=").concat(tab[3]).concat("&lastConnexionTime=").concat(tab[4]);
			}
		});  
}

function funcLoadUsers() {
	$.post("ManageUsers",
		{
			cmd:"LoadUsers"
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error loading users list")
			} else {
				
				var table=document.getElementById("myUsers");

				
				
				for (x in data)
				  {
				  var row=table.insertRow(x);
				  
				  var cell1=row.insertCell(0);
				  var cell2=row.insertCell(1);
				  var cell3=row.insertCell(2);
				  var cell4=row.insertCell(3);
				  var cell5=row.insertCell(4);
				  var cell6=row.insertCell(5);
				  var cell7=row.insertCell(6);
				  var cell8=row.insertCell(7);
				  
				  cell1.innerHTML = data[x].login;
				  cell2.innerHTML = data[x].nom;
				  cell3.innerHTML = data[x].prenom;
				  cell4.innerHTML = data[x].age;
				  cell5.innerHTML = data[x].creationAccount;
				  cell6.innerHTML = data[x].lastConnexionDate;
				  cell7.innerHTML = data[x].lastConnexionTime;
				  cell8.innerHTML = "<button id=\"btnEdit\" onclick=\"funcEditUser('".concat(data[x].id).concat("');\">Edit</button>").concat("<button id=\"btnDelete\" onclick=\"funcDeleteUser('").concat(data[x].id).concat("');\">Delete</button>");
				  }
				
				 var row=table.insertRow(0);
				 var cell1=row.insertCell(0);
				 var cell2=row.insertCell(1);
				 var cell3=row.insertCell(2);
				 var cell4=row.insertCell(3);
				 var cell5=row.insertCell(4);
				 var cell6=row.insertCell(5);
				 var cell7=row.insertCell(6);
				 var cell8=row.insertCell(7);
				  
				 cell1.innerHTML = "<b>Login</b>";
				 cell2.innerHTML = "<b>Last Name</b>";
				 cell3.innerHTML = "<b>First Name</b>";
				 cell4.innerHTML = "<b>Age</b>";
				 cell5.innerHTML = "<b>Creation Date</b>";
				 cell6.innerHTML = "<b>Last Connexion Date</b>";
				 cell7.innerHTML = "<b>Last Connexion Time</b>";
				 cell8.innerHTML = "<b>Action</b>";


			}
		});  
}

function funcEditUser(param) {
	$.post("ManageUsers",
		{
			cmd:"EditUser",
			id:param
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error accessing other page")
			} else {
				var tab = new Array();
				tab = data.split(";");
				window.location.href = 'manageusers.html?login='.concat(tab[0]).concat("&nom=").concat(tab[1]).concat("&prenom=").concat(tab[2]).concat("&lastConnexionDate=").concat(tab[3]).concat("&lastConnexionTime=").concat(tab[4]);
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