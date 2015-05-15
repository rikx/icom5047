$(document).ready(function(){
	// reportes list
	$reportes_list = $('#reportes_list');
	
  // store data for initial 20 dispositivos
  var reportes_array = [];
  var user_info = JSON.parse($reportes_list.attr('data-user'));
  
  var data_reportes = $reportes_list.attr('data-reports');
  if(data_reportes.length > 2){
    reportes_array = JSON.parse($reportes_list.attr('data-reports'));

    // initial info panel population
    populate_info_panel(reportes_array[0]);
  } else {
    $('#info_panel').hide();
  }

  /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compared with agent, report_name, location_name or date
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('username', 'report_name', 'location_name', 'report_date', 'flowchart_name'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.report_id === localMatch.report_id;
    },
    local: reportes_array,
    remote: {
      url: 'http://localhost:3000/reportes/%QUERY',
      filter: function(list) {
        // populate global arrays with matching results
        reportes_array = list.reports;

        // populate list with matching results
        populate_list(list.reports);
        return $.map(list.reports, function(reporte) { 
          return reporte;
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
    name: 'reportes',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(reporte){
        var name;
        if(reporte.report_name != null)
          name = reporte.report_name;
        else
          name = 'Reporte sin título';
        return '<p><strong>Nombre: </strong>'+name+'</p><p><strong>Localizacion: </strong>'+reporte.location_name+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').on('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    reportes_array = [datum];
    populate_list([datum]);
  });

  $("#search_bar").keypress(function (event) {
    if (event.which == 13) {
  
      var user_input = $('#search_bar').val();
      if(user_input == ''){
        populate_reportes();
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
    populate_list(reportes_array);
  });
  /* Search Code End */

  //Delete Localizacion
  $('#btn_delete').on('click', function(){
    var report_id = $('#btn_delete').attr("data-id");
  $.ajax({
    url: "http://localhost:3000/users/admin/delete_report/" + report_id,
    method: "PUT",
    contentType: "application/json",
    dataType: "json",
    success: function(data) {
      alert("Reporte fue eliminado");
      populate_reportes();
    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });
});

  /* Button: Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users';
  });

  /* Open info panel */
  $reportes_list.on('click', 'tr td a.show_info_report', function(e){
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

    // set id values of info panel buttons
    $('#btn_view_report').attr('data-id', report_id);
    $('#btn_delete').attr('data-id', report_id);
  });

  /* View report (redirect to report page) */
  $('#btn_view_report').on('click', function(){
    // contains cuestionario id
    var this_reporte_id = $(this).attr('data-id');
    window.location.href = '/users/reportes/'+this_reporte_id;
  });

  /* Redirect to report page to edit it */
  $reportes_list.on('click', 'tr td button.btn_edit_report', function(){
    // contains cuestionario id
    var this_reporte_id = $(this).attr('data-id');
    window.location.href = '/users/reportes/'+this_reporte_id;
  });

  // /* Delete report */
  // $('#btn_delete').on('click', function(){
  //   var confirm_delete = confirm('Desea borrar el reporte "'+$(this).attr('data-report-name')+'"?');
  //   if(confirm_delete){
  //     // ajax call to delete report
  //     $.ajax({
  //       url: "http://localhost:3000/users/admin/reportes/"+$(this).attr('data-id'),
  //       method: "DELETE",
  //       success: function(data) {
  //         alert("Reporte ha sido borrado del sistema.");
  
  //         // update reportes list after posting 
  //         populate_reportes();
  //       },
  //       error: function( xhr, status, errorThrown ) {
  //         alert( "Sorry, there was a problem!" );
  //         console.log( "Error: " + errorThrown );
  //         console.log( "Status: " + status );
  //         console.dir( xhr );
  //       }
  //     });
  //   }
  // });

  /* Populates info panel with $this_report information */
  function populate_info_panel($this_report){
    if($this_report.report_name == undefined){
      $('#info_panel_heading').html('Reporte sin título');
    } else {
      $('#info_panel_heading').html($this_report.report_name);
    }  
    $('#reporte_info_id').html($this_report.report_id);
    $('#reporte_info_location').text($this_report.location_name);
    $('#reporte_info_creator').text($this_report.username);
    $('#report_info_date').text($this_report.report_date);
    $('#reporte_info_flowchart').html($this_report.flowchart_name);
    $('#reporte_info_flowchart_version').html($this_report.version);

    var survey_type;
    if($this_report.status == 0){
      survey_type = 'Método abierto';
    } else if($this_report.status == 1){
      survey_type = 'Método con flujo';
    }
    $('#reporte_info_flowchart_type').html(survey_type);
    
    // set id values of info panel buttons
    $('#btn_view_report').attr('data-id', $this_report.report_id);
    $('#btn_delete').attr('data-id', $this_report.report_id);
  }

  /* Populate list with first 20 reportes, organized alphabetically by report name */
  function populate_reportes(){
    $.getJSON('http://localhost:3000/list_reportes', function(data) {
      reportes_array = data.reports;

      populate_list(data.reports);
      populate_info_panel(data.reports[0]);
    });
  };

  /* Populate list with reportes_set information */
  function populate_list(reportes_set){
    // contents of reports list
    var table_content = '';

    // for each item in JSON, add table row and cells
    $.each(reportes_set, function(i){
      table_content += '<tr>';
      table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
        table_content +=  'active ';
        populate_info_panel(this);
      }
      var report_name;
      if(this.report_name != null) {
        report_name = this.report_name;
      } else {
        report_name = 'Reporte sin título';
      }
      table_content += "show_info_report' href='#', data-id='"+this.report_id+"'>"+report_name+"</a></td>";
      //table_content += "<td><center data-id='"+this.location_id+"'>"+this.location_name+"</center></td>"
      //table_content += "<td><button class='btn_edit_report btn btn-sm btn-success btn-block' type='button' data-id='"+this.report_id+"'>Editar</button></td>";
      //table_content += "<td><a class='btn_delete_report btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.report_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
      table_content += '</tr>';
    });  
    
    // inject content string into html
    $reportes_list.html(table_content);

    if(user_info.user_type != 'admin'){
      $('#btn_delete').hide();
    }
  }
});