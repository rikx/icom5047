$(document).ready(function(){

	$categories_list = $('#categories');
	$specialties_list = $('#specialties');
	var all_categories = JSON.parse($('#categoria_panel').attr('data-all-categorias'));
	var all_specialties = JSON.parse($('#specialty_panel').attr('data-all-specialties'));

	$('#edit_category_panel').hide();
	$('#add_category_panel').hide();
	$('#edit_specialty_panel').hide();
	$('#add_specialty_panel').hide();
  $('#specialty_row').hide();

	populate_list_categories(all_categories);
	populate_list_specialties(all_specialties);

  /* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users'
  });
  
	/* Change Selected Category */
	$categories_list.on('click', 'tr td a.the_category', function(e){
		e.preventDefault();
		var category_id = $(this).attr('data-id');
		$('#btn_edit_categories').attr("data-id", category_id);
		var category_name = $(this).attr('data-category-name');
		$('#btn_edit_categories').attr("data-category-name", category_name);
    $('#btn_delete_category').attr("data-id", category_id);
		$('#category_name').val(category_name);
    $('#category_info_name').text(category_name);
    $('#info_panel_category').show();
    //$('#info_category_name').text("Categoria: " + category_name);
    //$('#info_panel_heading_category').text("Categoria: " + category_name);
    // remove active from previous list item 
    remove_active_class($categories_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }
});

	/* Change Selected Specialty */
	$specialties_list.on('click', 'tr td a.the_specialty', function(e){
		e.preventDefault();
    $('#specialty_info_panel').show();
    $('#edit_specialty_panel').hide();
    $('#add_specialty_panel').hide();
		var specialty_id = $(this).attr('data-id');
		$('#btn_edit_specialties').attr("data-id", specialty_id);
		var specialty_name = $(this).attr('data-specialty-name');
		$('#btn_edit_specialties').attr("data-specialty-name", specialty_name);
		//alert(specialty_name);
		$('#specialty_name').val(specialty_name);
    $('#specialty_info_name').text(specialty_name);

    // remove active from previous list item 
    remove_active_class($specialties_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }
});


  $('#categories_tab').on('click', function(){
     $('#specialty_row').hide();
     $('#category_row').show();
  });

  $('#specialties_tab').on('click', function(){
    $('#specialty_row').show();
     $('#category_row').hide();
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

  // ajax call to post new category
  $.ajax({
  	url: "http://localhost:3000/users/admin/new_category",
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
        $('#add_category_panel').hide();
    },
    error: function( xhr, status, errorThrown ) {
    	alert( "Sorry, there was a problem!" );
    	console.log( "Error: " + errorThrown );
    	console.log( "Status: " + status );
    	console.dir( xhr );
    }
});
});

	/* POSTs new specialty information */
	$('#btn_post_new_specialty').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_specialty');
  var form_data = $the_form.serializeArray();
  var new_specialty = ConverToJSON(form_data);

  // ajax call to post new category
  $.ajax({
  	url: "http://localhost:3000/users/admin/new_specialty",
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
	$('#btn_put_new_category').on('click', function(){

  // get form data and conver to json format
  var category_name = $('#btn_edit_categories').attr("data-category-name");
  var category_id = $('#btn_edit_categories').attr("data-id");
  // ajax call to post new category
  var $the_form = $('#form_edit_category');
  var form_data = $the_form.serializeArray();
  var new_category = ConverToJSON(form_data);
  //url: "http://localhost:3000/users/admin/usuarios/" + usuario_id,

  $.ajax({
  	url: "http://localhost:3000/users/admin/edit_category/" + category_id,
  	method: "PUT",
  	data: JSON.stringify(new_category),
  	contentType: "application/json",
  	dataType: "json",
  	success: function(data) {
  		alert("Categoria fue editada exitosamente");
        $the_form[0].reset();
        // update locations list after posting 
        populate_categories();
        $('#edit_category_panel').hide();

    


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
    url: "http://localhost:3000/users/admin/delete_category/" + category_id,
    method: "DELETE",
    contentType: "application/json",
    dataType: "json",
    success: function(data) {
      alert("Categoría fue eliminada exitosamente");
        //var the_categories = data.categories;
      $('specialty_info_panel').hide();
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
  url: "http://localhost:3000/users/admin/delete_specialty/" + specialty_id,
  method: "DELETE",
  contentType: "application/json",
  dataType: "json",
  success: function(data) {
    alert("Especialidad fue eliminada exitosamente");
        populate_specialties();
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
  //url: "http://localhost:3000/users/admin/usuarios/" + usuario_id,
  $.ajax({
  	url: "http://localhost:3000/users/admin/edit_specialty/" + specialty_id,
  	method: "PUT",
  	data: JSON.stringify(new_specialty),
  	contentType: "application/json",
  	dataType: "json",
  	success: function(data) {
  		alert("Especialidad fue editada exitosamente");
        // clear add form
        $the_form[0].reset();
        // update locations list after posting 
        populate_specialties();
        $('#edit_specialty_panel').hide();

},
error: function( xhr, status, errorThrown ) {
	alert( "Sorry, there was a problem!" );
	console.log( "Error: " + errorThrown );
	console.log( "Status: " + status );
	console.dir( xhr );
}
});
});

/* Populate list with first 20 usuarios, organized alphabetically */
function populate_categories(){
	$.getJSON('http://localhost:3000/list_categories', function(data) {
		all_categories = data.categories;
		populate_list_categories(data.categories);
	});
};

/* Populate list with first 20 usuarios, organized alphabetically */
function populate_specialties(){
	$.getJSON('http://localhost:3000/list_specialties', function(data) {
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
      	//table_content +=  'active ';
      }
      table_content += "the_category' href='#', data-id='"+this.category_id+"',  data-category-name='"+this.name+"'>"+this.name+"</a></td>";
      table_content += '</tr>';
  });
	$('#categories').html(table_content);
};



/* Populate list with usuarios_set information */
function populate_list_specialties(all_specialties){
	table_content = '';
	$.each(all_specialties, function(i){
		table_content += '<tr>';
		table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
      	//table_content +=  'active ';
      }
      table_content += "the_specialty' href='#', data-id='"+all_specialties[i].spec_id+"',  data-specialty-name='"+all_specialties[i].name+"'>"+all_specialties[i].name+"</a></td>";
      table_content += '</tr>';
  });
	$('#specialties').html(table_content);
};


});
