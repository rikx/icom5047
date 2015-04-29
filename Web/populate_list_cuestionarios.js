  /* Populate list with first 20 cuestionarios, organized alphabetically */
  function populate_cuestionarios(){
    $.getJSON('http://localhost:3000/list_cuestionarios', function(data) {
      cuestionarios_array = data.cuestionarios;

      populate_list(data.cuestionarios);
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
      table_content += "<td><button class='btn_flujo_cuestionario btn btn-sm btn-success btn-block' type='button' data-id='"+this.flowchart_id+"'>Método con Flujo</button></td>";
      table_content += "<td><button class='btn_abierto_cuestionario btn btn-sm btn-success btn-block' type='button' data-id='"+this.flowchart_id+"' disabled>Método Abierto</button></td>";
      table_content += '</tr>';
    });  

    // inject content string into html
    $cuestionarios_list.html(table_content);
  };