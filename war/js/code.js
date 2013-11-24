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
		alert("Mots de passe diff�rents");
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
			if(data == "failed") {
				$('#lblErrorPassword').text("Bad connection parameters. Please try again");
			} else if(data == "firstConn") {
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
					if(data == "fail") {
						$('#lblErrorPassword').text("Bad connection parameters. Please try again");
					} else {
						window.location.href = 'flights.html';
					}
				});
	}
}

function funcInitializeHeader() {
	$.post("flight",
			{
				cmd:"PostHeaderInfo"
			},
			function(data,status){
				if(data != null) {
					var tab = data.split(";");
					
					//Nombre d'utilisateurs connect�s
					if(tab[1] == 1) {
						$("#numUserConnected").text("You are the only user connected");
					} else {
						$("#numUserConnected").text(tab[1]+" users connected");
					}
					
					//login pour le lien vers la page profil
					$("#linkToProfile").text(tab[0]);
				} else {
					alert("Une erreur est survenue pendant le chargement de la page. Merci de recharger la page.");
				}
			});
}

function funcLogOutUser() {
	alert("Coucou");
	$.post("login",
			{
				cmd:"PostLogOutUser"
			},
			function(data,status){
				if(data == "success") {
					alert("Vous avez �t� d�connect�.")
				} else {
					alert("Erreur lors de la d�connexion.");
				}
			});
}

function funcGetFlights() {
	alert("coucou");
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
				  var cell9=row.insertCell(8);
				  
				  cell1.innerHTML = data[x].login;
				  cell2.innerHTML = data[x].nom;
				  cell3.innerHTML = data[x].prenom;
				  cell4.innerHTML = data[x].age;
				  cell5.innerHTML = data[x].email;
				  cell6.innerHTML = data[x].creationAccount;
				  cell7.innerHTML = data[x].lastConnexionDate;
				  cell8.innerHTML = data[x].lastConnexionTime;
				  cell9.innerHTML = "<button id=\"btnEdit\" onclick=\"funcEditUser('".concat(data[x].id).concat("');\">Edit</button>").concat("<button id=\"btnDelete\" onclick=\"funcDeleteUser('").concat(data[x].id).concat("');\">Delete</button>");
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
				 var cell9=row.insertCell(8);
				  
				 cell1.innerHTML = "<b>Login</b>";
				 cell2.innerHTML = "<b>Last Name</b>";
				 cell3.innerHTML = "<b>First Name</b>";
				 cell4.innerHTML = "<b>Age</b>";
				 cell5.innerHTML = "<b>E-mail</b>";
				 cell6.innerHTML = "<b>Creation Date</b>";
				 cell7.innerHTML = "<b>Last Connexion Date</b>";
				 cell8.innerHTML = "<b>Last Connexion Time</b>";
				 cell9.innerHTML = "<b>Action</b>";


			}
		});  
}


function funcUpdateUser() {
	$.post("ManageUsers",
		{
			cmd:"UpdateUser",
			id:$("#usr_id").text(),
			nom:$("#usr_nom").val(),
  		    prenom: $("#usr_prenom").val(),
  		    age: $("#usr_age").val(),
  			email: $("#usr_email").val(),
  		    password: $("#usr_password").val()
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error updating user")
			} else {
				alert("User updated successfully")
				window.location.href = 'manageusers.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime);
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
				window.location.href = 'edituser.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&age=").concat(data.age).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime).concat("&creationAccount=").concat(data.creationAccount).concat("&email=").concat(data.email).concat("&id=").concat(data.id);
			}
		});  
}

function funcDeleteUser(param) {
	$.post("ManageUsers",
		{
			cmd:"DeleteUser",
			id:param
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error deleting user")
			} else {
				alert("User deleted successfully")
				window.location.href = 'manageusers.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime);
			}
		});  
}


function funcAdduser(param) {
	window.location.href = 'adduser.html'
}

function funcCreateUser() {
	
	if($("#usr_admin").is(":checked")) {
        var adminvalue = 'Y'
    } else {
        var adminvalue = 'N'
    }

	
	$.post("ManageUsers",
		{
			cmd:"CreateUser",
			nom:$("#usr_nom").val(),
  		    prenom: $("#usr_prenom").val(),
  		    age: $("#usr_age").val(),
  		    login: $("#usr_login").val(),
  			email: $("#usr_email").val(),
  		    password: $("#usr_password").val(),
  		    admin: adminvalue
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error creating user")
			} else {
				alert("User created successfully")
				window.location.href = 'manageusers.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime);
			}
		});  
	

}

function funcManageFlights() {
	$.post("Admin",
		{
			cmd:"ManageFlights"
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error accessing other page")
			} else {
				var tab = new Array();
				tab = data.split(";");
				window.location.href = 'manageflights.html?login='.concat(tab[0]).concat("&nom=").concat(tab[1]).concat("&prenom=").concat(tab[2]).concat("&lastConnexionDate=").concat(tab[3]).concat("&lastConnexionTime=").concat(tab[4]);
			}
		});  
}

function funcLoadFlights() {
	$.post("ManageFlights",
		{
			cmd:"LoadFlights"
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error loading flight list")
			} else {
				
				var table=document.getElementById("myFlights");

				
				
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
				  
				  cell1.innerHTML = data[x].from;
				  cell2.innerHTML = data[x].to;
				  cell3.innerHTML = data[x].departure;
				  cell4.innerHTML = data[x].arrival;
				  cell5.innerHTML = data[x].time;
				  cell6.innerHTML = data[x].seats;
				  cell7.innerHTML = data[x].price;
				  cell8.innerHTML = "<button id=\"btnEdit\" onclick=\"funcEditFlight('".concat(data[x].id).concat("');\">Edit</button>").concat("<button id=\"btnDelete\" onclick=\"funcDeleteFlight('").concat(data[x].id).concat("');\">Delete</button>");
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
				  
				 cell1.innerHTML = "<b>From</b>";
				 cell2.innerHTML = "<b>To</b>";
				 cell3.innerHTML = "<b>Date of departure</b>";
				 cell4.innerHTML = "<b>Date of arrival</b>";
				 cell5.innerHTML = "<b>Duration</b>";
				 cell6.innerHTML = "<b>Number or seats</b>";
				 cell7.innerHTML = "<b>Price</b>";
				 cell8.innerHTML = "<b>Action</b>";


			}
		});  
}

function funcUpdateFlight() {
	$.post("ManageFlights",
		{
			cmd:"UpdateFlight",
			id:$("#flight_id").text(),
			from:$("#flight_from").val(),
  		    to: $("#flight_to").val(),
  		    departure: $("#flight_departure").val(),
  			arrival: $("#flight_arrival").val(),
  		    price: $("#flight_price").val(),
  		    seats: $("#flight_seats").val()
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error updating flight")
			} else {
				alert("Flight updated successfully")
				window.location.href = 'manageflights.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime);
			}
		});  
}

function funcEditFlight(param) {
	$.post("ManageFlights",
		{
			cmd:"EditFlight",
			id:param
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error accessing other page")
			} else {
				window.location.href = 'editflight.html?departure='.concat(data.departure).concat("&arrival=").concat(data.arrival).concat("&from=").concat(data.from).concat("&to=").concat(data.to).concat("&seats=").concat(data.seats).concat("&price=").concat(data.price).concat("&id=").concat(data.id);
			}
		});  
}


function funcDeleteFlight(param) {
	$.post("ManageFlights",
		{
			cmd:"DeleteFlight",
			id:param
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error deleting flight")
			} else {
				alert("Flight deleted successfully")
				window.location.href = 'manageflights.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime);
			}
		});  
}

function funcCreateFlight() {
	$.post("ManageFlights",
		{
			cmd:"CreateFlight",
			from:$("#flight_from").val(),
  		    to: $("#flight_to").val(),
  		    departure: $("#flight_departure").val(),
  			arrival: $("#flight_arrival").val(),
  		    price: $("#flight_price").val(),
  		    seats: $("#flight_seats").val()
		},
		function(data,status){
			if(data == "Failed") {
				alert("Error creating flight")
			} else {
				alert("Flight created successfully")
				window.location.href = 'manageflights.html?login='.concat(data.login).concat("&nom=").concat(data.nom).concat("&prenom=").concat(data.prenom).concat("&lastConnexionDate=").concat(data.lastConnexionDate).concat("&lastConnexionTime=").concat(data.lastConnexionTime);
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

function funcUserLoad() {
	$("#usr_login").text(getURLParameter("login"));
	$("#usr_prenom").val(getURLParameter("prenom"));
	$("#usr_nom").val(getURLParameter("nom"));
	$("#usr_age").val(getURLParameter("age"));
	$("#usr_email").val(getURLParameter("email"));
	$("#usr_creation").text(getURLParameter("creationAccount"));
	$("#usr_lastConnexionDate").text(getURLParameter("lastConnexionDate"));
	$("#usr_lastConnexionTime").text(getURLParameter("lastConnexionTime"));
	$("#usr_id").text(getURLParameter("id"));
	
}

function funcFlightLoad() {
	$("#flight_from").val(getURLParameter("from"));
	$("#flight_to").val(getURLParameter("to"));
	$("#flight_departure").val(getURLParameter("departure"));
	$("#flight_arrival").val(getURLParameter("arrival"));
	$("#flight_price").val(getURLParameter("price"));
	$("#flight_seats").val(getURLParameter("seats"));
	$("#flight_id").text(getURLParameter("id"));
	
}
