$(document).ready(function(){
  // store data for 10 localizaciones
  var localizaciones_array;
  // initial population of localizaciones list
  populate_localizaciones();
  // localizaciones list
  $localizaciones_list = $('#localizaciones_list');

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});

		/* Add ganadero */
	$('#btn_add_localizacion').on('click', function(){
	  $('#btn_edit, #heading_edit').hide();
	  $('#btn_submit, #heading_create').show();
	  $('#edit_panel').show();
	  $('#info_panel').hide();
	});

	function populate_localizaciones(){
    $.getJSON('http://localhost:3000/users/admin/list_localizaciones', function(data) {
      localizaciones_array = data.localizaciones;

      // contents of localizaciones list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.localizaciones, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_localizacion' href='#', data-id='"+this.location_id+"'>"+this.location_name+"</a></td>";
        table_content += "<td><button class='btn_add_associates btn btn-sm btn-success btn-block' type='button', data-id='"+this.location_id+"'>Agregar Ganadero/Agente</button></td>";
        table_content += "<td><button class='btn_edit_localizacion btn btn-sm btn-success btn-block' type='button' data-id='"+this.location_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_localizacion btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.location_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $localizaciones_list.html(table_content);
    });
  };
});