$(document).ready(function(){
  // store data for 10 citas
  var citas_array;
  // initial population of citas list
  populate_citas();
  // citas list
  $citas_list = $('#citas_list');

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});

	/* Open edit panel */
  $citas_list.on('click', 'tr td button.btn_edit_cita', function(){
    $('#edit_panel').show();

    // contains cita id
    var cita_id = $(this).attr('data-id');

    // ramon work

  });

	function populate_citas(){
    $.getJSON('http://localhost:3000/users/admin/list_citas', function(data) {
      citas_array = data.citas;

      // contents of localizaciones list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.citas, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_cita' href='#', data-id='"+this.appointment_id+"'>"+this.location_name+"</a></td>";
        table_content += '<td><center>'+get_date_time(this.date, false)+' at '+this.time+'</center></td>';
        table_content += "<td><button class='btn_edit_cita btn btn-sm btn-success btn-block' type='button' data-id='"+this.appointment_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_cita btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.appointment_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $citas_list.html(table_content);
    });
  };
});