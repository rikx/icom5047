$(document).ready(function(){
  // cuestionarios list
  $cuestionarios_list = $('#cuestionarios_list');
  // store data for initial 10 cuestionarios
  var cuestionarios_data= $cuestionarios_list.attr('data-cuestionarios');
  var cuestionarios_array = JSON.parse(cuestionarios_data);

   /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compard with ganadero full names or emails
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('flowchart_name'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.flowchart_name === localMatch.flowchart_name;
    
    local: cuestionarios_array,
    remote: {
      url: 'http://localhost:3000/cuestionarios/%QUERY',
      filter: function(list) {
        // populate global arrays with matching results
        cuestionarios_array = list.cuestionarios;
        // populate list with matching results
        populate_list(cuestionarios_array);
        return $.map(list.cuestionarios, function(cuestionario) { 
          return cuestionario;
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
    name: 'cuestionarios',
    displayKey: 'flowchart_name',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(ganadero){
        return '<p><strong>Nombre: </strong>'+cuestionario.flowchart_name+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').bind('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    populate_list({datum});
  });
  /* Search Code End */

  //initial population of info panel
  populate_info_panel(cuestionarios_array[0]);

  /* Button: Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users'; 
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($usuarios_list);
  });

 /* Open edit page */
 $cuestionarios_list.on('click', 'tr td button.btn_edit_cuestionario', function(){
		// contains cuestionario id
    var this_cuestionario_id = $(this).attr('data-id');
    window.location.href = '/users/admin/cuestionarios/'+this_cuestionario_id;
  });

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

  // Populates info panel with list element's information
  function populate_info_panel(element){
    $('#cuestionario_info_name').html("<a href='/users/admin/cuestionarios/" + element.flowchart_id + "'>" +element.flowchart_name+ "</a>");
    $('#cuestionario_info_version').text(element.version);
    $('#cuestionario_info_creator').text(element.username);
  };

  function populate_cuestionarios(){
    $.getJSON('http://localhost:3000/list_cuestionarios', function(data) {
      cuestionarios_array = data.cuestionarios;

      // contents of localizaciones list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.cuestionarios, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_cuestionario' href='#', data-id='"+this.flowchart_id+"'>"+this.flowchart_name+"</a></td>";
        table_content += "<td><button class='btn_edit_cuestionario btn btn-sm btn-success btn-block' type='button' data-id='"+this.flowchart_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_cuestionario btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.flowchart_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $cuestionarios_list.html(table_content);
    });
  };
});