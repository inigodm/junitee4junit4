function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}

async function relaunch() {
	  while(!finished){
	      await sleep(2000);
	      loadDoc();//location = location;");
	    }
}

var loading = "data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="

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
		    var node = document.getElementById(json.res[i].name + 'tbody');
		    while (node.hasChildNodes()) {
		      node.removeChild(node.lastChild);
		    }
		    for (j = 0; j < json.res[i].results.length; j++){
		    	if (!json.res[i].results[j].testFinished){
		    		drawUnfinished(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "818117", json.res[i].name);
		    	}else if (!json.res[i].results[j].isAvaliable){
					drawUnavaliable(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "8181F7", json.res[i].name);
				}
				else if (json.res[i].results[j].isCorrect){
					drawOK(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "4caf50", json.res[i].name);
				}
				else{
					drawError(table.getElementsByTagName('tbody')[0], json.res[i].results[j], "ca6059", json.res[i].name);
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
	tbdy.id = name + 'tbody';
	tbl.appendChild(thead);
	tbl.appendChild(tbdy);
	body.appendChild(tbl);
	return tbl;
}

function drawUnfinished(table, result, color, tableName){
	var row = getNewRow(tableName + result.name + "temp", table);
	var cell1 = row.insertCell(0); 
    var cell2 = row.insertCell(1); 
    cell1.setAttribute("colspan", '2');
    cell1.setAttribute("style", "background-color:#" + color);//4caf50
    cell2.setAttribute("style", "background-color:#" + color);
    cell1.innerHTML = result.name;
    cell2.innerHTML = result.time+".0 milisecs";
}

function drawOK(table, result, color, tableName){
	var row = getNewRow(tableName + result.name, table);
    var cell1 = row.insertCell(0); 
    var cell2 = row.insertCell(1); 
    cell1.setAttribute("colspan", '2');
    cell1.setAttribute("style", "background-color:#" + color);//4caf50
    cell2.setAttribute("style", "background-color:#" + color);
    cell1.innerHTML = result.name;
    cell2.innerHTML = result.time+".0 milisecs";
}

function getNewRow(rowId, table){
	var row = document.getElementById(rowId);
	if (row != null) {
		table.deleteRow(row.rowIndex)
	}
	row = table.insertRow(-1);
	row.id = rowId;
	return row;
}
var index = 0;

function drawError(table, result, color, tableName){
	drawAccordion(table, 
			result, 
			color, 
			result.msg.replace(new RegExp("<", 'g'), "(").replace(new RegExp(">", 'g'), ")"),
			"danger", 
			'err', 
			tableName)
}

function drawUnavaliable(table, result, color, tableName){
	drawAccordion(table, 
			result, 
			color, 
			result.reason,
			"info", 
			'info',
			tableName)
}

function drawAccordion(table, result, color, firstText, infoOrDanger, btnLabel, tableName){
	var row = getNewRow(tableName + result.name + 1, table);
    var div = document.createElement('div');
	var span = document.createElement('span');
	span.innerHTML = result.name;
	var btn = document.createElement('button');
	btn.setAttribute("class", 'btn btn-' + infoOrDanger);
	btn.setAttribute("type", 'button');
	btn.setAttribute("data-toggle", 'collapse');
	btn.setAttribute("data-target", '#demo' + result.name + index);
	btn.innerHTML = btnLabel;
	var cell1 = row.insertCell(0); 
	cell1.appendChild(div);
    cell1.appendChild(span);
    div.appendChild(btn);
    var cell2 = row.insertCell(1); 
    var cell3 = row.insertCell(2); 
    cell1.setAttribute("style", "background-color:#" + color);//ca6059
    cell2.setAttribute("style", "background-color:#" + color);
    cell3.setAttribute("style", "background-color:#" + color);
    cell2.innerHTML = firstText;
    cell3.innerHTML = result.msg;
    row = getNewRow(tableName + result.name + 2, table);
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