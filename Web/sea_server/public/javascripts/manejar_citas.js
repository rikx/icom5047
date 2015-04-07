$(document).ready(function(){

  // format dates in initial cita list population
  format_dates($('.show_date_cita'));
  // citas list
  $citas_list = $('#citas_list');

// store data for 10 citas

var citas_data = $citas_list.attr('data-citas');
var citas_array = JSON.parse(citas_data);
populate_info_panel();

function populate_info_panel(){
 var firstElement = [];
 firstElement = citas_array[0]; 
 $('#info_panel_heading').text("Cita");
 $('#cita_info_location').text(firstElement.location_name);
 $('#cita_info_date').text(firstElement.date);   
 $('#cita_info_hour').text(firstElement.time);
 $('#cita_info_purpose').text(firstElement.purpose);
 $('#cita_info_agent').text(firstElement.username);   
 $('#cita_info_report').text(firstElement.report_id);
}

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

$('#btn_close_edit_panel').on('click', function(){
  $('#edit_panel').hide();
  remove_active_class($citas_list);
});

$('#btn_close_info_panel').on('click', function(){
  $('#info_panel').hide();
  remove_active_class($citas_list);
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
    var arrayPosition = citas_array.map(function(arrayItem) { return arrayItem.appointment_id; }).indexOf(myVar);
    var thisUserObject = citas_array[arrayPosition];
    console.log(thisUserObject);

    //#info_panel_heading
    $('#info_panel_heading').text("Cita");
    $('#cita_info_location').text(thisUserObject.location_name);
    $('#cita_info_date').text(thisUserObject.date);   
    $('#cita_info_hour').text(thisUserObject.time);
    $('#cita_info_purpose').text(thisUserObject.purpose);
    $('#cita_info_agent').text(thisUserObject.username);   
    $('#cita_info_report').text(thisUserObject.report_id);

  });

$citas_list.on('click', 'tr td button.btn_edit_cita', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();


  var myVar = $(this).attr('data-id');
  var arrayPosition = citas_array.map(function(arrayItem) { return arrayItem.appointment_id; }).indexOf(myVar);
  var thisUserObject = citas_array[arrayPosition];


  //date and time logic
  var dateTimeObject = new Date(thisUserObject.date + " " + thisUserObject.time);
  var day = ("0" + dateTimeObject.getDate()).slice(-2);
  var month = ("0" + (dateTimeObject.getMonth() + 1)).slice(-2);
  var hours = dateTimeObject.getHours();
  var minutes = dateTimeObject.getMinutes();
  var seconds = dateTimeObject.getSeconds();
  var theDay = dateTimeObject.getFullYear()+"-"+(month)+"-"+(day);
  var theTime = ('0' + hours).slice(-2) + ":" + ('0' + minutes).slice(-2) + ":" + ('0' + seconds).slice(-2);
  $('#cita_proposito').attr("value", thisUserObject.purpose);
  $('#cita_date').attr("value", theDay);
  $('#cita_time').attr("value", theTime);
});

function populate_citas(){
  $.getJSON('http://localhost:3000/users/admin/list_citas', function(data) {
    citas_array = data.citas;
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