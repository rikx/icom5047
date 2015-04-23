$(document).ready(function(){
	// reportes list
	$reportes_list = $('#reportes_list');
	
	// store data for initial 10 reports
	var reportes_data = $reportes_list.attr('data-reports');
	var reportes_array = JSON.parse(reportes_data);


    populate_info_panel();

    function populate_info_panel(){
     var firstElement = [];
     firstElement = reportes_array[0]; 
     $('#reporte_info_id').html("<a href='/users/reportes/" + firstElement.report_id + "'> Reporte " +  firstElement.report_id + "</a>");
     $('#reporte_info_location').text( firstElement.location_name);
     $('#reporte_info_creator').text( firstElement.username);
     $('#report_info_date').text( firstElement.date_filed);
     $('#reporte_info_ganadero').text( firstElement.ganadero_name);
     $('#reporte_info_flowchart').text( firstElement.flowchart_name);
 }

    /* Button: Return home */
    $('#btn_home').on('click', function(){
        window.location.href = '/users';
    });

 $reportes_list.on('click', 'tr td a.show_info_reporte', function(e){
    // prevents link from firing
    e.preventDefault();
    var table_content = '';

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($reportes_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }

    // contains ganadero id
   // var myVar = $(this).attr('data-id');
   var myVar = $this.attr('data-id');
   var arrayPosition = reportes_array.map(function(arrayItem) { return arrayItem.report_id; }).indexOf(myVar);
   var thisUserObject = reportes_array[arrayPosition];

    //#info_panel_heading
    $('#reporte_info_id').html("<a href='/users/reportes/" + thisUserObject.report_id + "'> Reporte " + thisUserObject.report_id + "</a>");
    $('#reporte_info_location').text(thisUserObject.location_name);
    $('#reporte_info_creator').text(thisUserObject.username);
    $('#report_info_date').text(thisUserObject.date_filed);
    $('#reporte_info_ganadero').text(thisUserObject.ganadero_name);
    $('#reporte_info_flowchart').text(thisUserObject.flowchart_name);


});


$reportes_list.on('click', 'tr td button.btn_edit_reporte', function(){

// contains cuestionario id
var this_reporte_id = $(this).attr('data-id');
window.location.href = '/users/reportes/'+this_reporte_id;

});

});