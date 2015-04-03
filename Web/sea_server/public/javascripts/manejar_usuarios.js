$(document).ready(function(){
  // store data for 10 usuarios
  var usuarios_array;
  // initial population of usuarios list
  populate_usuarios();

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});
  
  /* Show info panel */
  $('#usuarios_list').on('click', 'tr td a.show_info_usuario', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous and add active to current clicked ganadero
    $('#usuarios_list tr td a.active').removeClass('active');
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    $this.attr('data-id');

    // ajax call for info
  });

	/* Open edit panel */
  $('#usuarios_list').on('click', 'tr td button.btn_edit_usuario', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains ganadero id
    $(this).attr('data-id');

    // ajax call for info

  });

  function populate_usuarios(){
    $.getJSON('http://localhost:3000/users/admin/list_usuarios', function(data) {
    usuarios_array = data.usuarios;
    //especialidades_array = data.especialidades;

    // contents of usuarios list
    var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.usuarios, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_usuario' href='#', data-id='"+this.person_id+"'>"+this.email+"</a></td>";
        table_content += "<td><button class='btn_edit_usuario btn btn-sm btn-success btn-block' type='button' data-id='"+this.person_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_usuario btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $('#usuarios_list').html(table_content);
  });
  }
});