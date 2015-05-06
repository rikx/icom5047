$(document).ready(function(){

	$categories_list = $('#categories');
	var all_categories = JSON.parse($('#categoria_panel').attr('data-all-categorias'));

	$('#edit_category_panel').hide();
	$('#add_category_panel').hide();

	populate_list(all_categories);

	/* Change Selected Category */
	$categories_list.on('click', 'tr td a.the_category', function(e){
		e.preventDefault();
		var category_id = $(this).attr('data-id');
		$('#btn_edit_categories').attr("data-id", category_id);
		var category_name = $(this).attr('category_name');
		$('#btn_edit_categories').attr("category_name", category_name);
		console.log(category_id);
		console.log(category_name);
    // remove active from previous list item 
    remove_active_class($categories_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }
});

	$('#btn_edit_categories').on('click', function(){
		$('#add_category_panel').hide();
		$('#edit_category_panel').show();
		var category_name = $('#btn_edit_categories').attr("category_name");
		$('#category_name').val(category_name);


	});

	$('#btn_add_categories').on('click', function(){
		$('#add_category_panel').show();
		$('#edit_category_panel').hide();

	});

	/* POSTs new category information */
	$('#btn_post_new_category').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_category');
  var form_data = $the_form.serializeArray();
  var new_category = ConverToJSON(form_data);

  console.log("Posting New Category.New category is ");
  console.log(new_category);


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
  var category_name = $('#btn_edit_categories').attr("category_name");
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
  		// if(data.exists){
  		// 	alert("Localización con este número de licensia ya existe");
  		// } else {
  		// 	alert("Localización ha sido añadido al sistema.");
    //     // clear add form
    //     $the_form[0].reset();
    //     // update locations list after posting 
    //     populate_localizaciones();
    // }


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
      populate_list(data.categories);
    });
  };

/* Populate list with usuarios_set information */
function populate_list(all_categories){
	table_content = '';
	$.each(all_categories, function(i){
		table_content += '<tr>';
		table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
      	table_content +=  'active ';
      }
      table_content += "the_category' href='#', data-id='"+all_categories[i].category_id+"',  category_name='"+all_categories[i].name+"'>"+all_categories[i].name+"</a></td>";
      table_content += '</tr>';
  });
	$('#categories').html(table_content);
};


});
