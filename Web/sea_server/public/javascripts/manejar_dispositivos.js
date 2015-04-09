$(document).ready(function(){
  // store data for 10 dispositivos
  var dispositivos_array;
  // initial population of dispositivos list
  populate_dispositivos();
  // dispositivos list
  $dispositivos_list = $('#dispositivos_list');

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


  $dispositivos_list.on('click', 'tr td a.show_info_dispositivo', function(e){

    // prevents link from firing
    
    e.preventDefault();
    var table_content = '';

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($dispositivos_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    var myVar = $this.attr('data-id');
    var arrayPosition = dispositivos_array.map(function(arrayItem) { return arrayItem.device_id; }).indexOf(myVar);
    var thisUserObject = dispositivos_array[arrayPosition];
    

    //#info_panel_heading
    $('#info_panel_heading').text(thisUserObject.device_name);
    $('#dispositivo_info_name').text(thisUserObject.device_name);
    $('#dispositivo_info_id_num').text(thisUserObject.device_id);
    $('#dispositivo_info_usuario').text(thisUserObject.username);
    $('#dispositivo_info_last_sync').text(thisUserObject.latest_sync);
    var date_time = get_date_time(thisUserObject.latest_sync, true);
    
    $('#dispositivo_info_last_sync').text(date_time.date + " at " + date_time.time);



  });


$dispositivos_list.on('click', 'tr td button.btn_edit_dispositivo', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();

  var myVar = $(this).attr('data-id');
  var arrayPosition = dispositivos_array.map(function(arrayItem) { return arrayItem.device_id; }).indexOf(myVar);
  var thisUserObject = dispositivos_array[arrayPosition];


  $('#dispositivo_name').attr("value", thisUserObject.device_name);
  $('#dispositivo_id_num').attr("value", thisUserObject.device_id);
  $('#dispositivo_usuario').attr("value", thisUserObject.username);
  var date_time = get_date_time(thisUserObject.latest_sync, true);
  $('#dispositivo_last_sync').attr("value", thisUserObject.phone_number);
});

// Populate asign agente dropdown menu when clicked
$('#btn_dropdown_agentes').on('click', function(){
  var list_content = '';

  // ajax call to GET all usuarios
  $.getJSON('http://localhost:3000/usuarios', function(data) {
    $.each(data.usuarios, function(i){
      list_content += "<li><a role='menuitem' tabindex='-1' href='#' data-id='"+this.user_id+"'>"+this.username+'</a></li>';
    });

    // popuplate dropdown list with ganaderos
    $('#list_dropdown_agentes').html(list_content);
  });
});

/* Change asigned agente dropdown selected value */
$('#list_dropdown_agentes').on('click', 'li a', function(e){
  // prevents link from firing
  e.preventDefault();

  $('#btn_dropdown_agentes_text').text($(this).text()+' ');
  $('#btn_dropdown_agentes').val($(this).attr('data-id'));
});


$('#btn_close_info_panel').on('click', function(){
  $('#info_panel').hide();
  remove_active_class($dispositivos_list);
});

$('#btn_close_edit_panel').on('click', function(){
  $('#edit_panel').hide();
  remove_active_class($dispositivos_list);
});

$('#btn_add_dispositivo').on('click', function(){
  $('#btn_edit, #heading_edit').hide();
  $('#btn_submit, #heading_create').show();
  $('#edit_panel').show();
  $('#info_panel').hide();


 //#info_panel_heading
    $('#info_panel_heading').text(" ");
    $('#dispositivo_info_name').text(" ");
    $('#dispositivo_info_id_num').text(" ");
    $('#dispositivo_info_usuario').text(" ");
    $('#dispositivo_info_last_sync').text(" ");
    $('#dispositivo_info_last_sync').text(" ");

});

function populate_dispositivos() {
	$.getJSON('http://localhost:3000/users/admin/list_dispositivos', function(data) {
		dispositivos_array = data.dispositivos;

			// contents of dispositivos list
			var table_content = '';
      var firstElement = [];
      firstElement = dispositivos_array[0];

			// device_id, devices.name as device_name, latest_sync, devices.user_id as assigned_user, username
			// for each item in JSON, add table row and cells
			$.each(data.dispositivos, function(i){
				// get {date, time} object of latest_ sync for this dispositivo
				var date_time = get_date_time(this.latest_sync, true);

       table_content += '<tr>';
       table_content += "<td><a class='list-group-item ";

			  // if initial list item, set to active
			  if(i==0) {
			  	table_content +=  'active ';
			  }
			  table_content += "show_info_dispositivo' href='#', data-id='"+this.device_id+"'>"+this.device_name+"</a></td>";
			  table_content += '<td><center>'+date_time.date+' at '+date_time.time+'</center></td>';
			  table_content += "<td><button class='btn_edit_dispositivo btn btn-sm btn-success btn-block' type='button' data-id='"+this.device_id+"'>Editar</button></td>";
			  table_content += "<td><a class='btn_delete_dispositivo btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.device_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
			  table_content += '</tr>';
			});  

			// inject content string into html
			$dispositivos_list.html(table_content);

      $('#info_panel_heading').text(firstElement.device_name);
      $('#dispositivo_info_name').text(firstElement.device_name);
      $('#dispositivo_info_id_num').text(firstElement.device_id);
      $('#dispositivo_info_usuario').text(firstElement.username);
      date_time = get_date_time(firstElement.latest_sync, true);
      $('#dispositivo_info_last_sync').text(date_time.date + " at " + date_time.time);





    });
};
});