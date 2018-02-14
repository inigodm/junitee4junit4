function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}

async function relaunch() {
	  while(!finished){
	      await sleep(2000);
	      loadDoc();//location = location;");
	    }
}

var finished = false;
		
function loadDoc() {
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	      var json = JSON.parse(this.responseText);
	      console.log(this.responseText);
	      finished = json.finished;
	      for (i = 0; i < json.res.length; i++){
	    	var table = document.getElementById(json.res[i].name);
		    if (table == null){
		    	table = createTable(json.res[i].name);
			}
		    for (j = 0; j < json.res[i].results.length; j++){
				if (json.res[i].results[j].isCorrect == 2){
					drawOK(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "8181F7");
				}
				else if (json.res[i].results[j].isCorrect == 1){
					drawOK(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "4caf50");
				}
				else{
					drawError(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "ca6059");
					table.setAttribute("style", "background-color:red");
				}
				
		    }
			
		}
	  }
	};
	xhttp.open("POST", "testAsync", true);
	xhttp.send();
}

function createTable(name){
	var body = document.getElementById('tables');
	var tbl = document.createElement('table');
	tbl.id = name;
	tbl.setAttribute("class", 'table table-striped');
	tbl.setAttribute("style", "background-color:green");
	var thead = document.createElement('thead');
	var tr = document.createElement('tr');
	var th = document.createElement('th');
	th.innerHTML = "Class " + name;
	tr.appendChild(th);
	tr.appendChild(document.createElement('th'));
	thead.appendChild(tr);
	var tbdy = document.createElement('tbody');
	tbl.appendChild(thead);
	tbl.appendChild(tbdy);
	body.appendChild(tbl);
	return tbl;
}

function drawOK(table, result, color){
	var row = table.insertRow(-1);
    var cell1 = row.insertCell(0); 
    var cell2 = row.insertCell(1); 
    cell1.setAttribute("colspan", '2');
    cell1.setAttribute("style", "background-color:#" + color);//4caf50
    cell2.setAttribute("style", "background-color:#" + color);
    cell1.innerHTML = result.name;
    cell2.innerHTML = result.time+".0 milisecs";
}
var index = 0;

function drawError(table, result, color){
	var row = table.insertRow(-1);
	var div = document.createElement('div');
	var span = document.createElement('span');
	span.innerHTML = result.name;
	var btn = document.createElement('button');
	btn.setAttribute("class", 'btn btn-danger');
	btn.setAttribute("type", 'button');
	btn.setAttribute("data-toggle", 'collapse');
	btn.setAttribute("data-target", '#demo' + result.name + index);
	btn.innerHTML = "err";
	var cell1 = row.insertCell(0); 
    cell1.appendChild(div);
    cell1.appendChild(span);
    div.appendChild(btn);
    var cell2 = row.insertCell(1); 
    var cell3 = row.insertCell(2); 
    cell1.setAttribute("style", "background-color:#" + color);//ca6059
    cell2.setAttribute("style", "background-color:#" + color);
    cell3.setAttribute("style", "background-color:#" + color);
    cell2.innerHTML = result.msg.replace(new RegExp("<", 'g'), "(").replace(new RegExp(">", 'g'), ")");
    cell3.innerHTML = result.msg;
    row = table.insertRow(-1);
    row.setAttribute("style", "background-color:#" + color);
    cell1 = row.insertCell(0); 
    cell1.setAttribute("colspan", '2');
    cell2 = row.insertCell(1);
    div = document.createElement('div');
    div.setAttribute("id", 'demo' + result.name + index);
    div.setAttribute("class", 'collapse');
    //div.setAttribute("aria-expanded", 'true');
    div.innerHTML = result.exception;
    cell1.appendChild(div);
    index += 1;
	
}