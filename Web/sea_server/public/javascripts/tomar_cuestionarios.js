$(document).ready(function(){
  // cuestionarios list
  $cuestionarios_list = $('#cuestionarios_list');
  
  // store data for initial 20 cuestionarios
  var cuestionarios_array = []; 
  var data_username = $cuestionarios_list.attr('data-username');
  var data_user_type = $cuestionarios_list.attr('data-type');

  var data_cuestionarios = $cuestionarios_list.attr('data-cuestionarios');
  if(data_cuestionarios.length >2){
    cuestionarios_array = JSON.parse($cuestionarios_list.attr('data-cuestionarios'));

    // initial info panel population
    populate_info_panel(cuestionarios_array[0]);
  } else {
    $('#info_panel').hide();
  }

  if(data_user_type == 'admin')
  {
      $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Administrador'+ " - " +data_username+"</small> </h2>");
  }
  else if(data_user_type == 'agent')
  {
    $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Agente'+ " - " +data_username+"</small> </h2>");
  }
  else
  {
    $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Especialista'+ " - " +data_username+"</small> </h2>");
  }
  /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compared with flowchart_name
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('flowchart_name'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.flowchart_id === localMatch.flowchart_id;
    },
    local: cuestionarios_array,
    remote: {
      url: '/cuestionarios/take/%QUERY',
      filter: function(list) {
        // populate global arrays with matching results
        cuestionarios_array = list.cuestionarios;
        // populate list with matching results
        populate_list(list.cuestionarios);
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
      suggestion: function(cuestionario){
        return '<p><strong>Nombre: </strong>'+cuestionario.flowchart_name+'</p>'+ '<p><strong>Versión: </strong>'+cuestionario.version+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').bind('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    cuestionarios_array = [datum];
    populate_list([datum]);
  });

  $("#search_bar").keypress(function (event) {
    if (event.which == 13) {
      var user_input = $('#search_bar').val();
      if(user_input == ''){
        populate_cuestionarios();
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
    populate_list(cuestionarios_array);
  });
  /* Search Code End */

 	/* Button: Return home */
 	$('#btn_home').on('click', function(){
  	window.location.href = '/users'; 
	});

  /* Open info panel */
  $cuestionarios_list.on('click', 'tr td a.show_info_cuestionario', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#info_panel').show();

    // remove active from previous clicked list item
    remove_active_class($cuestionarios_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains cuestionario id
    var cuestionario_id = $this.attr('data-id');
    var arrayPosition = cuestionarios_array.map(function(arrayItem) { return arrayItem.flowchart_id; }).indexOf(cuestionario_id);
    var this_cuestionario = cuestionarios_array[arrayPosition];

    // populate info panel with this_cuestionario info
    populate_info_panel(this_cuestionario);

    // set id values of info panel buttons
    $('#btn_flujo_cuestionario').attr('data-id', this_cuestionario.flowchart_id);
    $('#btn_abierto_cuestionario').attr('data-id', this_cuestionario.flowchart_id);
  });

  // take survey with flow method click event
  $('#btn_flujo_cuestionario').on('click', function(){
    // contains cuestionario id
    var this_cuestionario_id = $(this).attr('data-id');
    window.location.href = '/users/cuestionarios/flow/'+this_cuestionario_id;
  });

  // take survey with open method click event
  $('#btn_abierto_cuestionario').on('click', function(){
    // contains cuestionario id
    var this_cuestionario_id = $(this).attr('data-id');
    window.location.href = '/users/cuestionarios/open/'+this_cuestionario_id;
  });

 	// Populates info panel with list element's information
  function populate_info_panel(element){
    $('#info_panel_heading').html(element.flowchart_name);
    $('#cuestionario_info_name').html(element.flowchart_name);
    $('#cuestionario_info_version').text(element.version);
    $('#cuestionario_info_creator').text(element.username);

    // set id values of info panel buttons
    $('#btn_flujo_cuestionario').attr('data-id', element.flowchart_id);
    $('#btn_abierto_cuestionario').attr('data-id', element.flowchart_id);
  };

    /* Populate list with first 20 cuestionarios, organized alphabetically */
  function populate_cuestionarios(){
    $.getJSON('/list_tomar_cuestionarios', function(data) {
      cuestionarios_array = data.cuestionarios;

      populate_list(data.cuestionarios);
      populate_info_panel(data.cuestionarios[0]);
    });
  };

  /* Populate list with cuestionarios_set information */
  function populate_list(cuestionarios_set){
    // contents of cuestionarios list
    var table_content = '';

    // for each item in JSON, add table row and cells
    $.each(cuestionarios_set, function(i){
      table_content += '<tr>';
      table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
        table_content +=  'active ';
      }
      table_content += "show_info_cuestionario' href='#', data-id='"+this.flowchart_id+"'>"+this.flowchart_name+"</a></td>";
      //table_content += "<td><button class='btn_flujo_cuestionario btn btn-sm btn-success btn-block' type='button' data-id='"+this.flowchart_id+"'>Método con Flujo</button></td>";
      //table_content += "<td><button class='btn_abierto_cuestionario btn btn-sm btn-success btn-block' type='button' data-id='"+this.flowchart_id+"' disabled>Método Abierto</button></td>";
      table_content += '</tr>';
    });  

    // inject content string into html
    $cuestionarios_list.html(table_content);

    if(cuestionarios_set.length > 0)
      populate_info_panel(cuestionarios_set[0]);
    // close current info panel 
    //$('#info_panel').hide();
  };
});