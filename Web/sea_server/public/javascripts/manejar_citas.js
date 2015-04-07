$(document).ready(function(){
  // store data for 10 citas
  var citas_array;
  // format dates in initial cita list population
  format_dates($('.show_date_cita'));
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

  $citas_list.on('click', 'tr td a.show_info_cita', function(e){
   
    // preveLnts link from firing
    e.preventDefault();
    var table_content = '';

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($citas_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    var myVar = $this.attr('data-id');
    console.log(myVar);
    var arrayPosition = citas_array.map(function(arrayItem) { return arrayItem.appointment_id; }).indexOf(myVar);
    var thisUserObject = citas_array[arrayPosition];
    

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
      table_content += '<tr>';
      table_content += "<td> ";
      table_content += "" +this.location_name+"</td>";
      table_content += '</tr>';
    }
  });  


   $('#usuario_locations').html(table_content);
});

	function populate_citas(){
    $.getJSON('http://localhost:3000/users/admin/list_citas', function(data) {
      citas_array = data.citas;
      alert("lolking");
      console.log(citas_array);

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