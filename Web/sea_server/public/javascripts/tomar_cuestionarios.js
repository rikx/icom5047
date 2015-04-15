$(document).ready(function(){
  // cuestionarios list
  $cuestionarios_list = $('#cuestionarios_list');
  // store data for initial 20 cuestionarios
  var cuestionarios_data= $cuestionarios_list.attr('data-cuestionarios');
  var cuestionarios_array = JSON.parse(cuestionarios_data);

  //initial population of info panel
  populate_info_panel(cuestionarios_array[0]);

 	/* Button: Return home */
 	$('#btn_home').on('click', function(){
  	window.location.href = '/users'; 
	});

  // show info panel click event
 	$cuestionarios_list.on('click', 'tr td a.show_info_cuestionario', function(e){
    // prevents link from firing
    e.preventDefault();
    var table_content = '';

    // remove active from previous list item 
    remove_active_class($cuestionarios_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }
    // contains cuestionario id
    var this_id = $this.attr('data-id');
    var arrayPosition = cuestionarios_array.map(function(arrayItem) { return arrayItem.flowchart_id; }).indexOf(this_id);
    var this_list_element = cuestionarios_array[arrayPosition];
    populate_info_panel(this_list_element);
  });

  // take survey with flow method click event
  $cuestionarios_list.on('click', 'tr td button.btn_flujo_cuestionario', function(){
    // contains cuestionario id
    var this_cuestionario_id = $(this).attr('data-id');
    window.location.href = '/users/cuestionarios/flow/'+this_cuestionario_id;
  });

  // take survey with open method click event
  $cuestionarios_list.on('click', 'tr td button.btn_abierto_cuestionario', function(){
    // contains cuestionario id
    var this_cuestionario_id = $(this).attr('data-id');
    window.location.href = '/users/cuestionarios/open/'+this_cuestionario_id;
  });

 	// Populates info panel with list element's information
  function populate_info_panel(element){
    $('#cuestionario_info_name').html("<a href='/users/admin/cuestionarios/" + element.flowchart_id + "'>" +element.flowchart_name+ "</a>");
    $('#cuestionario_info_version').text(element.version);
    $('#cuestionario_info_creator').text(element.username);
  };
});