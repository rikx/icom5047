$(document).ready(function(){
  // store data for 10 ganaderos
  var ganaderos_array, locations_array;
  // initial population of ganaderos list
  populate_ganaderos();
  //ganaderos list
  $ganaderos_list = $('#ganaderos_list');

  /* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin'
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
    var table_content = '';
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
    var myVar = $this.attr('data-id');
    var arrayPosition = ganaderos_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(myVar);
    var thisUserObject = ganaderos_array[arrayPosition];

    $('#info_panel_heading').text(thisUserObject.first_name + " " + thisUserObject.last_name1 + " " + thisUserObject.last_name2);
    if(thisUserObject.middle_initial == null)
    {
      $('#ganadero_info_name').text(thisUserObject.first_name);
    }
    else
    {
     $('#ganadero_info_name').text(thisUserObject.first_name + " " + thisUserObject.middle_initial);
   }

   $('#ganadero_info_apellidos').text(thisUserObject.last_name1 + " " + thisUserObject.last_name2);
   $('#ganadero_info_contact').text(thisUserObject.email + " " + thisUserObject.phone_number);
   

   $.each(locations_array, function(i){
    if(myVar == this.person_id){
      table_content += '<tr>';
      table_content += "<td> ";
      table_content += "" +this.location_name+"</td>";
      table_content += '</tr>';
    }
  });  


   $('#ganadero_locations').html(table_content);
 });

/* Add ganadero */
$('#btn_add_ganadero').on('click', function(){
  $('#btn_edit, #heading_edit').hide();
  $('#btn_submit, #heading_create').show();
  $('#edit_panel').show();
  $('#info_panel').hide();
});

/* POSTs new ganadero information */
$('#btn_submit').on('click', function(){
  // get form data and conver to json format
  var form_data = $('#form_manage_ganadero').serializeArray();
  var new_ganadero = ConverToJSON(form_data);
  console.log("New ganadero: " + JSON.stringify(new_ganadero));

  // ajax call to post new ganadero
  $.ajax({
    url: "http://localhost:3000/users/admin/ganaderos",
    method: "POST",
    data: JSON.stringify(new_ganadero),
    contentType: "application/json",
    dataType: "json",

    success: function() {
      alert("Ganadero ha sido añadido al systema.");
      // clear add forms
    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });

  // update ganadero list after posting 
  populate_ganaderos();
});

/* Open edit panel */
$ganaderos_list.on('click', 'tr td button.btn_edit_ganadero', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();


// contains ganadero id
var myVar =  $(this).attr('data-id');
var arrayPosition = ganaderos_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(myVar);
var thisUserObject = ganaderos_array[arrayPosition];



$('#ganadero_name').attr("value", thisUserObject.first_name);
$('#ganadero_apellido1').attr("value", thisUserObject.last_name1);
$('#ganadero_apellido2').attr("value", thisUserObject.last_name2);
$('#ganadero_email').attr("value", thisUserObject.email);
$('#ganadero_telefono').attr("value", thisUserObject.phone_number);
  //thisUserObject.first_name + " " + thisUserObject.last_name1 + " " + thisUserObject.last_name2);

});

/* PUTs edited ganadero information */
$('#btn_edit').on('click', function(){
    //TODO: collect data to submit from edit form

    $.ajax({
      url: "http://localhost:3000/users/admin/ganadero",
      method: "PUT",

      success: function( data ) {

      },

      // Code to run if the request fails; the raw request and
      // status codes are passed to the function
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      },

      // Code to run regardless of success or failure
      complete: function( xhr, status ) {
        alert( "The request is complete!" );
      }
    });
  });

/* DELETEs ganadero information */
$ganaderos_list.on('click', 'tr td a.btn_delete_ganadero', function(e){
    // prevents link from firing
    e.preventDefault();

    // contains ganadero id
    $(this).attr('data-id');

    $.ajax({
      url: "http://localhost:3000/users/admin/ganadero",
      method: "DELETE",

      success: function( data ) {

      },

      // Code to run if the request fails; the raw request and
      // status codes are passed to the function
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      },

      // Code to run regardless of success or failure
      complete: function( xhr, status ) {
        alert( "The request is complete!" );
      }
    });

  });

  function populate_ganaderos(){
    $.getJSON('http://localhost:3000/users/admin/list_ganaderos', function(data) {
      ganaderos_array = data.ganaderos;
      locations_array = data.locations;
      var firstElement = ganaderos_array[0];

      // contents of ganaderos list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.ganaderos, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_ganadero' href='#', data-id='"+this.person_id+"'>"+this.first_name+' '+this.last_name1+' '+this.last_name2+"</a></td>";
        table_content += "<td><button class='btn_edit_ganadero btn btn-sm btn-success btn-block' type='button' data-id='"+this.person_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $ganaderos_list.html(table_content);

      //populate info panel with information regarding the first person on the list
      $('#info_panel_heading').text(firstElement.first_name + " " + firstElement.last_name1 + " " + firstElement.last_name2);
      if(firstElement.middle_initial == null) {
        $('#ganadero_info_name').text(firstElement.first_name);
      } else {
        $('#ganadero_info_name').text(firstElement.first_name + " " + firstElement.middle_initial);
      }
      $('#ganadero_info_apellidos').text(firstElement.last_name1 + " " + firstElement.last_name2);
      $('#ganadero_info_contact').text(firstElement.email + " " + firstElement.phone_number);

      table_content = '';

      $.each(locations_array, function(i){
        if(firstElement.person_id == this.person_id){
          table_content += '<tr>';
          table_content += "<td> ";
          table_content += "" +this.location_name+"</td>";
          table_content += '</tr>';
        }
      });  

      $('#ganadero_locations').html(table_content);
    });
  };
});