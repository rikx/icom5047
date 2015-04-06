$(document).ready(function(){
  // store data for 10 dispositivos
  var dispositivos_array;
  // initial population of dispositivos list
  populate_dispositivos();

  /* Return home */
  $('#btn_home').on('click', function(){
  	window.location.href = '/users/admin'
  });

  /* Add dispositivo */
  $('#btn_add_dispositivo').on('click', function(){
  	$('#btn_edit, #heading_edit').hide();
  	$('#btn_submit, #heading_create').show();
  	$('#edit_panel').show();
  	$('#info_panel').hide();
  });


  $dispositivos_list.on('click', 'tr td a.show_info_dispositivoS', function(e){

  	alert("hello");
    // prevents link from firing
    e.preventDefault();
    var table_content = '';

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($usuarios_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }

    // contains ganadero id
    var myVar = $this.attr('data-id');
    console.log("hello " + myVar);
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(myVar);
    var thisUserObject = usuarios_array[arrayPosition];
    

    //#info_panel_heading


    $('#info_panel_heading').text(thisUserObject.first_name + " " + thisUserObject.last_name1 + " " + thisUserObject.last_name2);
    if(thisUserObject.middle_initial == null)
    {
    	$('#usuario_info_name').text(thisUserObject.first_name);
    }
    else
    {
    	$('#usuario_info_name').text(thisUserObject.first_name + " " + thisUserObject.middle_initial);
    }

    $('#usuario_info_lastname_paternal').text(thisUserObject.last_name1);
    $('#usuario_info_lastname_maternal').text(thisUserObject.last_name2);
    $('#usuario_info_contact').text(thisUserObject.email + " " + thisUserObject.phone_number);


    $.each(locations_array, function(i){
    	if(myVar == this.user_id){
    		console.log(myVar == this.user_id);
    		table_content += '<tr>';
    		table_content += "<td> ";
    		table_content += "" +this.location_name+"</td>";
    		table_content += '</tr>';
    	}
    });  


    $('#usuario_locations').html(table_content);
});

function populate_dispositivos() {
	$.getJSON('http://localhost:3000/users/admin/list_dispositivos', function(data) {
		dispositivos_array = data.dispositivos;

			// contents of dispositivos list
			var table_content = '';

			// device_id, devices.name as device_name, latest_sync, devices.user_id as assigned_user, username
			// for each item in JSON, add table row and cells
			$.each(data.dispositivos, function(i){
				table_content += '<tr>';
				table_content += "<td><a class='list-group-item ";
			  // if initial list item, set to active
			  if(i==0) {
			  	table_content +=  'active ';
			  }
			  table_content += "show_info_dispositivo' href='#', data-id='"+this.device_id+"'>"+this.device_name+"</a></td>";
			  table_content += '<td><center>'+this.latest_sync+'</center></td>';
			  table_content += "<td><button class='btn_edit_dispositivo btn btn-sm btn-success btn-block' type='button' data-id='"+this.device_id+"'>Editar</button></td>";
			  table_content += "<td><a class='btn_delete_dispositivo btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.device_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
			  table_content += '</tr>';
			});  

			// inject content string into html
			$('#dispositivos_list').html(table_content);
		});
};
});