$(document).ready(function(){
	// reportes list
	$reportes_list = $('#reportes_list');
	
	// store data for initial 20 reports
	var reportes_array = JSON.parse($reportes_list.attr('data-reports'));

  // initial info panel population
  populate_info_panel(reportes_array[0]);

  /* Button: Return home */
  $('#btn_home').on('click', function(){
      window.location.href = '/users';
  });

  /* Open info panel */
  $reportes_list.on('click', 'tr td a.show_info_reporte', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($reportes_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }

    // contains report id
    var report_id = $this.attr('data-id');
    var arrayPosition = reportes_array.map(function(arrayItem) { return arrayItem.report_id; }).indexOf(report_id);
    var this_report = reportes_array[arrayPosition];

    // populate info panel with this_report info
    populate_info_panel(this_report);
  });

  /* Redirect to report page to edit it */
  $reportes_list.on('click', 'tr td button.btn_edit_reporte', function(){
    // contains cuestionario id
    var this_reporte_id = $(this).attr('data-id');
    window.location.href = '/users/reportes/'+this_reporte_id;
  });

  /* Populates info panel with $this_report information */
  function populate_info_panel($this_report){
    $('#reporte_info_id').html("<a href='/users/reportes/" +$this_report.report_id+ "'> Reporte " +$this_report.report_id+ "</a>");
    $('#reporte_info_location').text($this_report.location_name);
    $('#reporte_info_creator').text($this_report.username);
    $('#report_info_date').text($this_report.date_filed);
    $('#reporte_info_flowchart').text($this_report.flowchart_name);
  }

  /* Populates reportes list */
  function populate_reportes(){
    $.getJSON('http://localhost:3000/list_reportes', function(data) {
      reportes_array = data.reports;

      // contents of reports list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.reports, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_ganadero' href='#', data-id='"+this.report_id+"'>Reporte "+this.report_id+"</a></td>";
        table_content += "<td><center data-id='"+this.location_id+"'>"+this.location_name+"</center></td>"
        table_content += "<td><button class='btn_edit_ganadero btn btn-sm btn-success btn-block' type='button' data-id='"+this.report_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_reporte btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.report_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $reportes_list.html(table_content);
    });
  };
});