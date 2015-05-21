$(document).ready(function(){

	$categories_list = $('#categories');
	$specialties_list = $('#specialties');

  var all_categories = [];
  var all_specialties = [];

	var data_categories = $('#categoria_panel').attr('data-all-categorias');
	var data_specialties = $('#specialty_panel').attr('data-all-specialties');
  var data_username = $('#categoria_panel').attr('data-username');
  var data_user_type = $('#categoria_panel').attr('data-type');

  if(data_categories.length >2){
    all_categories = JSON.parse($('#categoria_panel').attr('data-all-categorias'));

    // initial info panel population
    populate_categories_info_panel(all_categories[0]);
  } else {
    $('#info_panel_category').hide();
  }

  if(data_specialties.length >2){
    all_specialties = JSON.parse($('#specialty_panel').attr('data-all-specialties'));

    // initial info panel population
    populate_specialties_info_panel(all_specialties[0]);
  } else {
    $('#specialty_info_panel').hide();
  }

	$('#edit_category_panel').hide();
	$('#add_category_panel').hide();
	$('#edit_specialty_panel').hide();
	$('#add_specialty_panel').hide();
  $('#specialty_row').hide();




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


	populate_list_categories(all_categories);
	populate_list_specialties(all_specialties);

  /* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users'
  });

  /* Close edit category panel */
  $('#btn_close_add_category').on('click', function(){
    $('#add_category_panel').hide();
    $('#info_panel_category').show();
  });

  /* Close edit category panel */
  $('#btn_close_edit_category').on('click', function(){
    $('#edit_category_panel').hide();
    $('#info_panel_category').show();
  });

  /* Close add specialty panel */
  $('#btn_close_add_specialty').on('click', function(){
    $('#add_specialty_panel').hide();
    $('#specialty_info_panel').show();
  });

  /* Close edit specialty panel */
  $('#btn_close_edit_specialty').on('click', function(){
    $('#edit_specialty_panel').hide();
    $('#specialty_info_panel').show();
  });

	/* Change Selected Category */
	$categories_list.on('click', 'tr td a.the_category', function(e){
		e.preventDefault();
    $('#info_panel_category').show();
    $('#add_category_panel').hide();
    $('#edit_category_panel').hide();

    remove_active_class($categories_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

		var category_id = $(this).attr('data-id');
		var category_name = $(this).attr('data-category-name');
    var arrayPosition = all_categories.map(function(arrayItem) { return arrayItem.category_id; }).indexOf(category_id);
    var this_category = all_categories[arrayPosition];
    // populate info panel with this_category
    populate_categories_info_panel(this_category);

    // set id values of info panel buttons
    $('#btn_edit_categories').attr('data-id', category_id);
    $('#btn_edit_categories').attr('data-category-name', category_name); 
    $('#btn_delete_category').attr('data-id', category_id);
  });

	/* Change Selected Specialty */
	$specialties_list.on('click', 'tr td a.the_specialty', function(e){
		e.preventDefault();
    $('#specialty_info_panel').show();
    $('#edit_specialty_panel').hide();
    $('#add_specialty_panel').hide();

    // remove active from previous list item 
    remove_active_class($specialties_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

		var specialty_id = $(this).attr('data-id');
		var specialty_name = $(this).attr('data-specialty-name');
    var arrayPosition = all_specialties.map(function(arrayItem) { return arrayItem.spec_id; }).indexOf(specialty_id);
    var this_specialty = all_specialties[arrayPosition];
    // populate info panel with this_specialty
    populate_specialties_info_panel(this_specialty);

    // set id values of info panel buttons
    $('#btn_edit_specialties').attr('data-id', specialty_id);
    $('#btn_edit_specialties').attr('data-specialty-name', specialty_name); 
    $('#btn_delete_specialty').attr('data-id', specialty_id);
  });


  $('#categories_tab').on('click', function(){
     $('#specialty_row').hide();
     $('#category_row').show();
     $('#categories_tab').addClass('active');
     $('#specialties_tab').removeClass('active');
     if(all_categories.length > 0){
      $('#info_panel_category').show();
     }
     $('#edit_category_panel').hide();
     $('#add_category_panel').hide();
  });

  $('#specialties_tab').on('click', function(){
    $('#specialty_row').show();
     $('#category_row').hide();
     $('#specialties_tab').addClass('active');
     $('#categories_tab').removeClass('active');
    if(all_specialties.length > 0){
      $('#specialty_info_panel').show();
    }
     $('#edit_specialty_panel').hide();
     $('#add_specialty_panel').hide();
  });


	$('#btn_edit_categories').on('click', function(){
		$('#add_category_panel').hide();
		$('#edit_category_panel').show();
		var category_name = $('#btn_edit_categories').attr("data-category-name");
		$('#category_name').val(category_name);
     $('#info_panel_category').hide();
	});

	$('#btn_add_categories').on('click', function(){
		$('#add_category_panel').show();
		$('#edit_category_panel').hide();
    $('#info_panel_category').hide();
    //remove_active_class($('#specialties'));


	});

	$('#btn_edit_specialties').on('click', function(){
		$('#add_specialty_panel').hide();
		$('#edit_specialty_panel').show();
		var specialty_name = $('#btn_edit_specialties').attr("data-specialty-name");
		$('#specialty_name').val(specialty_name);
    $('#specialty_info_panel').hide();

	});

	$('#btn_add_specialties').on('click', function(){
		$('#add_specialty_panel').show();
		$('#edit_specialty_panel').hide();
    $('#specialty_info_panel').hide();

	});

	/* POSTs new category information */
	$('#btn_post_new_category').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_category');
  var form_data = $the_form.serializeArray();
  var new_category = ConverToJSON(form_data);
  console.log(new_category.new_category);
  if(!new_category.new_category.trim().length > 0)
  {
    alert("Por favor escriba el nombre de una categoría.");
  }
  else{
  // ajax call to post new category
  $.ajax({
  	url: "/users/admin/new_category",
  	method: "POST",
  	data: JSON.stringify(new_category),
  	contentType: "application/json",
  	dataType: "json",
  	success: function(data) {
  		if(data.exists){
  			alert("Categoría con este nombre ya fue agregada");
  		} else {
  			alert("Categoría fue agregada exitosamente.");
          // clear add form
          $the_form[0].reset();
      }
        // update ganadero list after posting 
        populate_categories();
        $('#info_panel_category').show();
        $('#edit_category_panel').hide();
        $('#add_category_panel').hide();

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

	/* POSTs new specialty information */
	$('#btn_post_new_specialty').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_specialty');
  var form_data = $the_form.serializeArray();
  var new_specialty = ConverToJSON(form_data);
  if(!new_specialty.new_specialty.trim().length > 0)
  {
    alert("Por favor escriba el nombre de una especialidad.");
  }
  else
  {
  // ajax call to post new category
  $.ajax({
  	url: "/users/admin/new_specialty",
  	method: "POST",
  	data: JSON.stringify(new_specialty),
  	contentType: "application/json",
  	dataType: "json",
  	success: function(data) {
  		if(data.exists){
  			alert("Especialidad con este nombre ya fue agregada");
  		} else {
  			alert("Especialidad fue agregada exitosamente.");
          // clear add form
          $the_form[0].reset();
      }
        // update ganadero list after posting 
        populate_specialties();
        $('#add_specialty_panel').hide();
        $('#edit_specialty_panel').hide();
        $('#specialty_info_panel').show();
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

	/* POSTs new category information */
	$('#btn_put_new_category').on('click', function(){
  // get form data and conver to json format
  var category_name = $('#btn_edit_categories').attr("data-category-name");
  var category_id = $('#btn_edit_categories').attr("data-id");
  // ajax call to post new category
  var $the_form = $('#form_edit_category');
  var form_data = $the_form.serializeArray();
  var new_category = ConverToJSON(form_data);
  $.ajax({
  	url: "/users/admin/edit_category/" + category_id,
  	method: "PUT",
  	data: JSON.stringify(new_category),
  	contentType: "application/json",
  	dataType: "json",
  	success: function(data) {
      if(data.exists){
        alert("Categoría con este nombre ya existe");
      } else {
        alert("Categoría fue modificada exitosamente.");
        $the_form[0].reset();
        populate_categories();
        $('#info_panel_category').show();
        $('#edit_category_panel').hide();
      }
},
error: function( xhr, status, errorThrown ) {
	alert( "Sorry, there was a problem!" );
	console.log( "Error: " + errorThrown );
	console.log( "Status: " + status );
	console.dir( xhr );
}
});
});


/* DELETEs new category information */
$('#btn_delete_category').on('click', function(){
   var category_id = $('#btn_edit_categories').attr("data-id");
  $.ajax({
    url: "/users/admin/delete_category/" + category_id,
    method: "DELETE",
    contentType: "application/json",
    dataType: "json",
    success: function(data) {
      alert("Categoría fue eliminada exitosamente");
        //var the_categories = data.categories;
       $('#info_panel_category').show();
       $('#edit_category_panel').hide();
      populate_categories();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
});

/* DELETEs new category information */
$('#btn_delete_specialty').on('click', function(){
 var specialty_id = $('#btn_edit_specialties').attr("data-id");
 $.ajax({
  url: "/users/admin/delete_specialty/" + specialty_id,
  method: "DELETE",
  contentType: "application/json",
  dataType: "json",
  success: function(data) {
    alert("Especialidad fue eliminada exitosamente");
        populate_specialties();
        $('#add_specialty_panel').hide();
        $('#edit_specialty_panel').hide();
        $('#specialty_info_panel').show();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
});

/* POSTs new category information */
$('#btn_put_new_specialty').on('click', function(){
  // get form data and conver to json format
  var specialty_name = $('#btn_edit_specialties').attr("data-specialty-name");
  var specialty_id = $('#btn_edit_specialties').attr("data-id");
  // ajax call to post new category
  var $the_form = $('#form_edit_specialty');
  var form_data = $the_form.serializeArray();
  var new_specialty = ConverToJSON(form_data);
  //url: "/users/admin/usuarios/" + usuario_id,
  $.ajax({
  	url: "/users/admin/edit_specialty/" + specialty_id,
  	method: "PUT",
  	data: JSON.stringify(new_specialty),
  	contentType: "application/json",
  	dataType: "json",
  	success: function(data) {
        if(data.exists){
        alert("Especialidad con este nombre ya existe");
      } else {
        alert("Especialidad fue modificada exitosamente.");
        $the_form[0].reset();
        populate_specialties();
        $('#add_specialty_panel').hide();
        $('#edit_specialty_panel').hide();
        $('#specialty_info_panel').show();
      }
  		


},
error: function( xhr, status, errorThrown ) {
	alert( "Sorry, there was a problem!" );
	console.log( "Error: " + errorThrown );
	console.log( "Status: " + status );
	console.dir( xhr );
}
});
});

/* Populate info panel with $this_category information */
function populate_categories_info_panel($this_category){
  $('#category_info_name').text($this_category.name);

  // set id values of info panel buttons
  $('#btn_edit_categories').attr('data-id', $this_category.category_id);
  $('#btn_edit_categories').attr('data-category-name', $this_category.name); 
  $('#btn_delete_category').attr('data-id', $this_category.category_id);
}

/* Populate info panel with $this_specialty information */
function populate_specialties_info_panel($this_specialty){
  $('#specialty_info_name').text($this_specialty.name);

  // set id values of info panel buttons
  $('#btn_edit_specialties').attr('data-id', $this_specialty.spec_id);
  $('#btn_edit_specialties').attr('data-specialty-name', $this_specialty.name); 
  $('#btn_delete_specialty').attr('data-id', $this_specialty.spec_id);
}

/* Populate list with first 20 usuarios, organized alphabetically */
function populate_categories(){
	$.getJSON('/list_categories', function(data) {
		all_categories = data.categories;
		populate_list_categories(data.categories);
	});
};

/* Populate list with first 20 usuarios, organized alphabetically */
function populate_specialties(){
	$.getJSON('/list_specialties', function(data) {
		all_specialties = data.specialties;
		populate_list_specialties(data.specialties);
	});
};

/* Populate list with usuarios_set information */
function populate_list_categories(the_categories){
	table_content = '';
	$.each(the_categories, function(i){
		table_content += '<tr>';
		table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
      	table_content +=  'active ';
        populate_categories_info_panel(this);
      }
      table_content += "the_category' href='#', data-id='"+this.category_id+"',  data-category-name='"+this.name+"'>"+this.name+"</a></td>";
      table_content += '</tr>';
  });
	$('#categories').html(table_content);
};



/* Populate list with usuarios_set information */
function populate_list_specialties(the_specialties){
	table_content = '';
	$.each(the_specialties, function(i){
		table_content += '<tr>';
		table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
      	table_content +=  'active ';
        populate_specialties_info_panel(this);
      }
      table_content += "the_specialty' href='#', data-id='"+this.spec_id+"',  data-specialty-name='"+this.name+"'>"+this.name+"</a></td>";
      table_content += '</tr>';
  });
	$('#specialties').html(table_content);
};


});
