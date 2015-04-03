$(document).ready(function(){
  // store data for 10 dispositivos
  var dispositivos_array;
  // initial population of dispositivos list
  populate_dispositivos();

  /* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin'
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