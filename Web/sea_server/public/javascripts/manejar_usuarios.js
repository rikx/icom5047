$(document).ready(function(){
  // store data for 10 usuarios
  var usuarios_array;
  var locations_array;
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
    var table_content = '';
    //AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous and add active to current clicked ganadero
    $('#usuarios_list tr td a.active').removeClass('active');
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    var myVar = $this.attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(myVar);
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
    if(myVar == this.person_id){
      table_content += '<tr>';
      table_content += "<td> ";
      table_content += "" +this.location_name+"</td>";
      table_content += '</tr>';
    }
  });  


   $('#usuario_locations').html(table_content);
    // ajax call for info
  });

/* Open edit panel */
$('#usuarios_list').on('click', 'tr td button.btn_edit_usuario', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();


    // contains ganadero id
    
    var myVar = $(this).attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(myVar);
    var thisUserObject = usuarios_array[arrayPosition];
    

    $('#usuario_name').attr("value", thisUserObject.first_name);
    $('#usuario_lastname_paternal').attr("value", thisUserObject.last_name1);
    $('#usuario_lastname_maternal').attr("value", thisUserObject.last_name2);
    $('#usuario_email').attr("value", thisUserObject.email);
    $('#usuario_telefono').attr("value", thisUserObject.phone_number);
    $('#usuario_password').attr("value", "somepassword");

    // ajax call for info

  });

function populate_usuarios(){
  $.getJSON('http://localhost:3000/users/admin/list_usuarios', function(data) {
    usuarios_array = data.usuarios;
    usuarios_locations = data.locations;
    var firstElement = usuarios_array[0];
    //locations_array = data.locations;
    
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


      $('#info_panel_heading').text(this.first_name + " " + this.last_name1 + " " + this.last_name2);
      if(this.middle_initial == null)
      {
        $('#usuario_info_name').text(this.first_name);
      }
      else
      {
       $('#usuario_info_name').text(this.first_name + " " + this.middle_initial);
     }

     $('#usuario_info_lastname_paternal').text(this.last_name1);
     $('#usuario_info_lastname_maternal').text(this.last_name2);
     $('#usuario_info_contact').text(firstElement.email + " " + firstElement.phone_number);
     table_content = '';


     alert("here 1");

     $.each(locations_array, function(i){
      if(firstElement.person_id == this.person_id){
        console.log(firstElement.person_id);
        console.log(this.person_id);
        table_content += '<tr>';
        table_content += "<td> ";
        table_content += "" +this.location_name+"</td>";
        table_content += '</tr>';
      }
    });  

     alert("here 2");
     $('#usuario_locations').html(table_content);

   });
}
});