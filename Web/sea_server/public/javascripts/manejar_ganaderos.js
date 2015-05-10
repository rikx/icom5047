$(document).ready(function(){
  // ganaderos list
  $ganaderos_list = $('#ganaderos_list');

  // store data for initial 20 ganaderos
  var ganaderos_array = [];
  var localizaciones_array = []; 
  var user_info = JSON.parse($ganaderos_list.attr('data-user'));

  var data_ganaderos = $ganaderos_list.attr('data-ganaderos')
  var data_locations = $ganaderos_list.attr('data-localizaciones');

  if(data_ganaderos.length >2){
    ganaderos_array = JSON.parse($ganaderos_list.attr('data-ganaderos'));

    if(data_locations.length >2){
      localizaciones_array = JSON.parse($ganaderos_list.attr('data-localizaciones'));
    }

    // initial info panel population
    populate_info_panel(ganaderos_array[0]);
  } else {
    $('#info_panel').hide();
  }


  /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compard with ganadero full names or emails
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('person_name', 'email'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.person_id === localMatch.person_id;
    },
    local: ganaderos_array,
    remote: {
      url: 'http://localhost:3000/ganaderos/%QUERY',
      filter: function(list) {
        // populate global arrays with matching results
        ganaderos_array = list.ganaderos;
        localizaciones_array = list.locations;
        // populate list with matching results

        populate_list(list.ganaderos);
        return $.map(list.ganaderos, function(ganadero) { 
          return ganadero;
        });
      }
    }
  });

  // kicks off loading and processing of 'local' and 'prefetch'
  search_source.initialize();

  // set typeahead functionality for search bar
  $('.typeahead').typeahead({
    hint: false,
    highlight: true
  },
  {
    name: 'ganaderos',
    displayKey: 'person_name',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(ganadero){
        return '<p><strong>Nombre: </strong>'+ganadero.person_name+'</p><p><strong>Email: </strong>'+ganadero.email+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').bind('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    ganaderos_array = [datum];
    populate_list([datum]);
  });

  $("#search_bar").keypress(function (event) {
    if (event.which == 13) {
      var user_input = $('#search_bar').val();
      if(user_input == ''){
        populate_ganaderos();
        return;
      } else {
        search_source.get(user_input, sync, async);
        $(this).typeahead('close');
        function sync(datums) {
          //console.log('datums from `local`, `prefetch`, and `#add`');
          //console.log(datums);
          populate_list(datums);

        }

        function async(datums) {
          //console.log('datums from `remote`');
          //console.log(datums);
          populate_list(datums);
        }
      }
    }
  });
  
  $('#search_bar').on('input', function() { 
    populate_list(ganaderos_array);
  });
  /* Search Code End */

  /* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users'
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($ganaderos_list);
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($ganaderos_list);
  });

  /* Open info panel */
  $ganaderos_list.on('click', 'tr td a.show_info_ganadero', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous clicked list item
    remove_active_class($ganaderos_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    var ganadero_id = $this.attr('data-id');
    var arrayPosition = ganaderos_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(ganadero_id);
    var this_ganadero = ganaderos_array[arrayPosition];

    // populate info panel with this_ganadero info
    populate_info_panel(this_ganadero);

    // set id values of info panel buttons
    $('#btn_edit_ganadero').attr('data-id', this_ganadero.person_id);
    $('#btn_delete').attr('data-id', this_ganadero.person_id);
    $('#btn_delete').attr('data-name', this_ganadero.person_name); 
  });

  /* Add ganadero */
  $('#btn_add_ganadero').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // clear add form
    $('#form_manage_ganadero')[0].reset();
  });

  /* POSTs new ganadero information */
  $('#btn_submit').on('click', function(){
    // get form data and conver to json format
    var $the_form = $('#form_manage_ganadero');
    var form_data = $the_form.serializeArray();
    var new_ganadero = ConverToJSON(form_data);
    console.log(new_ganadero);
    if(!new_ganadero.ganadero_name.trim().length > 0)
    {
      alert("Por favor ingrese nombre de ganadero.");
    }
    else if(!new_ganadero.ganadero_apellido1.trim().length > 0)
    {
      alert("Por favor ingrese su primer apellido.");
    }
    else if(!new_ganadero.ganadero_email.trim().length > 0)
    {
      alert("Por favor ingrese su correo electrónico");
    }
    else if(new_ganadero.ganadero_email.trim().length > 255)
    {
      alert("Correo electrónico no puede tener más de 255 cáracteres.");
    }
    else
    {
    // ajax call to post new ganadero
    $.ajax({
      url: "http://localhost:3000/users/admin/ganaderos",
      method: "POST",
      data: JSON.stringify(new_ganadero),
      contentType: "application/json",
      dataType: "json",
      success: function(data) {
        if(data.exists){
          alert("Ganadero con este correo electrónico o teléfono ya existe");
        } else {
          alert("Ganadero ha sido añadido al sistema.");
          // clear add form
          $the_form[0].reset();
        }
        // update ganadero list after posting 
        populate_ganaderos();
        $('#edit_panel').hide();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  }
  });

  /* Open edit panel */
 $('#btn_edit_ganadero').on('click', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

  // contains ganadero id
  var ganadero_id =  $(this).attr('data-id');
  var arrayPosition = ganaderos_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(ganadero_id);
  var this_ganadero = ganaderos_array[arrayPosition];

  $('#btn_edit').attr('data-id', ganadero_id);
  $('#ganadero_name').val(this_ganadero.first_name);
  $('#ganadero_m_initial').val(this_ganadero.middle_initial);
  $('#ganadero_apellido1').val(this_ganadero.last_name1);
  $('#ganadero_apellido2').val(this_ganadero.last_name2);
  $('#ganadero_email').val(this_ganadero.email);
  $('#ganadero_telefono').val(this_ganadero.phone_number);
});

  /* PUTs edited ganadero information */
  $('#btn_edit').on('click', function(){
    var ganadero_id = $(this).attr('data-id');
  // get form data and conver to json format
  var $the_form = $('#form_manage_ganadero');
  var form_data = $the_form.serializeArray();
  var new_ganadero = ConverToJSON(form_data);
  if(!new_ganadero.ganadero_name.trim().length > 0)
  {
    alert("Por favor ingrese nombre de ganadero.");
  }
  else if(!new_ganadero.ganadero_apellido1.trim().length > 0)
  {
    alert("Por favor ingrese su primer apellido.");
  }
  else if(!new_ganadero.ganadero_email.trim().length > 0)
  {
    alert("Por favor ingrese su correo electrónico");
  }
  else if(new_ganadero.ganadero_email.trim().length > 255)
  {
    alert("Correo electrónico no puede tener más de 255 cáracteres.");
  }
  else
  {
  // ajax call to update ganadero
  $.ajax({
    url: "http://localhost:3000/users/admin/ganaderos/" + ganadero_id,
    method: "PUT",
    data: JSON.stringify(new_ganadero),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      alert("Informacion de ganadero ha sido editada en el sistema.");
      // update ganadero list after posting 
      populate_ganaderos();
      $('#edit_panel').hide();
      $('#info_panel').show();
      

    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });
}
});

    //Delete Ganadero
    $('#btn_delete').on('click', function(){
      var ganadero_id = $('#btn_delete').attr("data-id");
      $.ajax({
        url: "http://localhost:3000/users/admin/delete_ganadero/" + ganadero_id,
        method: "PUT",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
          alert("Ganadero fue eliminado fue eliminado");
          //populate_usuarios();
          populate_ganaderos();
        },
        error: function( xhr, status, errorThrown ) {
          alert( "Sorry, there was a problem!" );
          console.log( "Error: " + errorThrown );
          console.log( "Status: " + status );
          console.dir( xhr );
        }
      });
    });

  /* Populates info panel with $this_ganadero information */
  function populate_info_panel($this_ganadero){
    // populate basic information panel
    $('#info_panel_heading').text($this_ganadero.first_name + " " + $this_ganadero.last_name1 + " " + $this_ganadero.last_name2);
      if($this_ganadero.middle_initial == null) {
        $('#ganadero_info_name').text($this_ganadero.first_name);
      } else {
        $('#ganadero_info_name').text($this_ganadero.first_name + " " + $this_ganadero.middle_initial);
      }
    $('#ganadero_info_apellidos').text($this_ganadero.last_name1 + " " + $this_ganadero.last_name2);
    $('#ganadero_info_contact').html('<p>'+$this_ganadero.email+'</p><p>'+$this_ganadero.phone_number+'</p>');
      
    // populate associated locations panel
    var table_content = '';
    $.each(localizaciones_array, function(i){
      if($this_ganadero.person_id == this.person_id){
        table_content += '<tr><td>'+this.location_name+'</td></tr>';
      }
    }); 

    if(table_content == '')
    {
      table_content += 'Ganadero no tiene localizaciones asignadas.';
    }
    $('#ganadero_locations').html(table_content);

    // set id values of info panel buttons
    $('#btn_edit_ganadero').attr('data-id', $this_ganadero.person_id);
    $('#btn_delete').attr('data-id', $this_ganadero.person_id);
    $('#btn_delete').attr('data-name', $this_ganadero.person_name); 
  }

  /* Populate list with first 20 ganaderos, organized alphabetically */
  function populate_ganaderos(){
    $.getJSON('http://localhost:3000/list_ganaderos', function(data) {
      ganaderos_array = data.ganaderos;
      localizaciones_array = data.locations;
      
      populate_list(data.ganaderos);
    });
  };
  
  /* Populate list with ganaderos_set information */
  function populate_list(ganaderos_set){
    // contents of ganaderos list
    var table_content = '';
    // for each item in JSON, add table row and cells

    $.each(ganaderos_set, function(i){
      table_content += '<tr>';
      table_content += "<td><a class='list-group-item ";
      table_content += "show_info_ganadero' href='#', data-id='"+this.person_id+"'>"+this.person_name+"</a></td>";
      //table_content += "<td><button class='btn_edit_ganadero btn btn-sm btn-success btn-block' type='button' data-id='"+this.person_id+"'>Editar</button></td>";
      //table_content += "<td><a class='btn_delete_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
      table_content += '</tr>';
    }); 
       
    // inject content string into html
    $ganaderos_list.html(table_content);
    // close current info panel 
    //$('#btn_close_info_panel').trigger('click');
    
    populate_info_panel(ganaderos_set[0]);
  };
});