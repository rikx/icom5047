<?php
session_start();
// connection
$host = "localhost";
$dbname = "SEA";
$username = "postgres";
$password = "RENeR2015DB";



$uName = $_POST['username'];
$uPass = $_POST['password'];
$uDevice = $_POST['android_id'];

$db = new PDO("pgsql:dbname=$dbname;host=$host", $username, $password );

$statement = $db->prepare("SELECT * FROM devices WHERE android_id = :android_id");
$statement->execute(array(":android_id" => $uDevice));
$devices = $statement->fetch(PDO::FETCH_ASSOC);
$device = $devices['android_id'];

	$statement = $db->prepare("SELECT * FROM users WHERE username = :username");
	$statement->execute(array(":username" => $uName));
	$result = $statement->fetch(PDO::FETCH_ASSOC);
	$user_id = $result['user_id'];

		$statement = $db->prepare("SELECT (passhash = crypt(:password, passhash)) AS match, passhash FROM users WHERE user_id = :user_id");
		$statement->bindParam(":password" , $uPass);
		$statement->bindParam(":user_id" , $result['user_id']);
		$statement->execute();
		$result = $statement->fetch(PDO::FETCH_ASSOC);
		$hs = $result['passhash'];
		$auth = $result['match'];

$respose = array('device' => false, 'user' => false, 'hash' => 0, 'id' => $device, 'seq' => -1, 'flag' => false);

if(!$devices){// device not found in the db
	if(!$auth){// new device wrong credentials
		$user_id = 0;
	}
	else{// the user has the rigth credentials
		$respose['user'] = true;
		$respose['hash'] = $hs;
	}
	$db->prepare("INSERT INTO devices (android_id,last_user_id,latest_sync) VALUES(:android_id,:last_user_id,:latest_sync)")->execute(array(":android_id" => $uDevice, ":last_user_id" => $user_id, ":latest_sync" => "2000-01-01 00:00:00.0"));
	$statement = $db->prepare("SELECT * FROM devices WHERE android_id = :android_id");
	$statement->execute(array(":android_id" => $uDevice));
	$result = $statement->fetch(PDO::FETCH_ASSOC);
	$respose['seq'] = $result['base_sequence'];

}else{ // the divice has be register
	$last_user_id = $devices['last_user_id'];
	$respose['device'] = true;
	if(!$auth){// existing device wrong credentials
		
	}else{// existing device rigth credentials
		$respose['user'] = true;
		$respose['hash'] = $hs;
		if($last_user_id != $user_id){// the user has ben changed 
			$statement = $db->prepare("UPDATE devices SET last_user_id = :user_id, latest_sync = '2000-01-01 00:00:00.0' WHERE android_id = :android_id");
			$statement->bindParam(':user_id', $user_id);
			$statement->bindParam(':android_id', $device);
			$statement->execute();
			$respose['flag'] = true;
		}
		// else // the user password has changed
			


	}

	
}









echo json_encode($respose);



//SELECT (passhash = crypt($1, passhash)) AS pswmatch FROM users WHERE user_id = $2
// 1 = user password 2 = user id



// $status = 0;




// // queries 

 
// $statement = $db->prepare("SELECT * FROM devices WHERE id_number = :id_number");
// $statement->execute(array(":id_number" => $device));

// $result = $statement->fetch(PDO::FETCH_ASSOC);
// $last_sync = $result['latest_sync'];
// $base_sequence = $result['base_sequence'];
// $assigned_user = $result['user_id'];

// $new_data = array();
// $deleted_data = array();

// $local_Data = json_decode($_POST["data"], true);
// $local = json_encode($_POST["data"]);
// $post = array('new_data' => array() , 'deleted_data' => array() ,'sync_info' => array('sync_status' => 0, 'local_data' => array('in' => $local_Data, 'pq' => "")));
// $debug1 = "";

// 	$flag = 0;
// 	$queries = array(
// 		'address' => "SELECT * FROM address WHERE last_update > '".$last_sync."' AND status != -1",
// 		'category' => "SELECT * FROM category WHERE last_update > '".$last_sync."'",//no status
// 		'person' => "SELECT * FROM person WHERE last_update > '".$last_sync."' AND status != -1",
// 		'specialization' => "SELECT * FROM specialization WHERE last_update > '".$last_sync."'",//no status
// 		'users' => "SELECT * FROM users WHERE last_update > '".$last_sync."' AND status != -1",
// 		'flowchart' => "SELECT * FROM flowchart WHERE last_update > '".$last_sync."' AND status = 1",
// 		'item' => "SELECT * FROM item WHERE flowchart_id = ALL (SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status = 1) ",
// 		'option' => "SELECT option_id,parent_id,next_id,label FROM (SELECT item_id FROM item WHERE flowchart_id = ALL (SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status = 1)) AS questions INNER JOIN option AS answers ON questions.item_id=answers.parent_id",
// 		'location' => "SELECT * FROM location WHERE last_update > '".$last_sync."' AND status != -1",
// 		'location_category' => "SELECT * FROM location_category WHERE last_update > '".$last_sync."'",//no status
// 		'report' => "SELECT * FROM report WHERE last_update > ".$last_sync."' AND status != -1",
// 		'path' => "SELECT * FROM path WHERE last_update > '".$last_sync."' AND report_id = ALL (SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status != -1)",
// 		'appointments' => "SELECT * FROM appointments WHERE last_update > '".$last_sync."' AND status = 1",
// 		'users_specialization' => "SELECT * FROM users_specialization WHERE last_update > '".$last_sync."'",//no status
// 		);
// 	$queries_deleted = array(
// 		'address' => "SELECT address_id FROM address WHERE last_update > '".$last_sync."' AND status = -1",
// 		'category' => "SELECT category_id FROM category WHERE last_update > '".$last_sync."'",
// 		'person' => "SELECT person_id FROM person WHERE last_update > '".$last_sync."' AND status = -1",
// 		'specialization' => "SELECT spec_id FROM specialization WHERE last_update > '".$last_sync."'",
// 		'users' => "SELECT users_id FROM users WHERE last_update > '".$last_sync."' AND status = -1", // TODO: only get de autorized users
// 		'flowchart' => "SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status = -1",
// 		'item' => "SELECT item_id FROM item WHERE flowchart_id = ALL (SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status = -1) ",
// 		'option' => "SELECT option_id FROM (SELECT item_id FROM item WHERE flowchart_id = ALL (SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status = -1)) AS questions INNER JOIN option AS answers ON questions.item_id=answers.parent_id",
// 		'location' => "SELECT location_id FROM location WHERE last_update > '".$last_sync."' AND status = -1",
// 		'location_category' => "SELECT location_id,category_id FROM location_category WHERE last_update > '".$last_sync."'",
// 		'report' => "SELECT report_id FROM report WHERE last_update > ".$last_sync."' AND status = -1",
// 		'path' => "SELECT  FROM report_id,option_id,sequence WHERE last_update > '".$last_sync."' AND report_id = ALL (SELECT flowchart_id FROM flowchart WHERE last_update > '".$last_sync."' AND status = -1)",
// 		'appointments' => "SELECT appointments_id FROM appointments WHERE last_update > '".$last_sync."' AND status = -1",
// 		'users_specialization' => "SELECT users_id,spec_id FROM users_specialization WHERE last_update > '".$last_sync."'",
// 		);
// 	$insert = array(
// 		'address' => "INSERT INTO address (address_id,address_line1,address_line2,city,zipcode,status) VALUES(:address_id,:address_line1,:address_line2,:city,:zipcode,1)",
// 		'person' => "INSERT INTO person (person_id,last_name1,first_name,email,last_name2,middle_initial,phone_number,status) VALUES(:person_id,:last_name1,:first_name,:email,:last_name2,:middle_initial,:phone_number,1)",
// 		'location' => "INSERT INTO location (location_id,name,address_id,owner_id,manager_id,license,agent_id,status) VALUES(:location_id,:name,:address_id,:owner_id,:manager_id,:license,:agent_id,1)",
// 		'report' => "INSERT INTO report (report_id,creator_id,location_id,flowchart_id,note,date_filed,name,status) VALUES(:report_id,:creator_id,:location_id,:flowchart_id,:note,:date_filed,:name,:status)",
// 		'path' => "INSERT INTO path (report_id,option_id,data,sequence) VALUES(:report_id,:option_id,:data,:sequence)",
// 		'appointments' => "INSERT INTO appointments (appointment_id,date,time,report_id,purpose,maker_id,status) VALUES(:appointment_id,:date,:time,:report_id,:purpose,:maker_id,1)",
// 		);


// 	try
// 	{ 
// 		// $db -> beginTransaction ();

// 	if(isset($result["id_number"]) && !empty($result['id_number'])){ // the divice is allredy in the db

// 		if (isset($_POST["sync_type"]) && !empty($_POST["sync_type"])){
// 			$user_id = $result["user_id"];

// 			$statement = $db->prepare("SELECT * FROM users WHERE user_id = :user_id");
// 			$statement->execute(array(":user_id" => $user_id));
// 			$result = $statement->fetch(PDO::FETCH_ASSOC);

// 			$user_type = $result["type"];
// 			if($user_type == "agent"){
// 				if(!strcmp($_POST["sync_type"], "FULL")){// sync full

// 					foreach ($queries as $table => $query) {
// 						$statement = $db->prepare($query);
// 						$statement->execute();
// 						$results=$statement->fetchAll(PDO::FETCH_ASSOC);
// 						$new_data[$table] = $results;
// 					}
// 					$status = 1;
// 					$debug1 = "full";
// 				}else if(!strcmp($_POST["sync_type"], "INC")){// sync inc

// 						foreach ($queries as $table => $query) { // TODO: change $queries to $queries_new
// 							$statement = $db->prepare($query);
// 							$statement->execute();
// 							$results=$statement->fetchAll(PDO::FETCH_ASSOC);
// 							$new_data[$table] = $results;
// 						}
// 						$debug1 = "incremental";
// 						foreach ($queries_deleted as $table => $query) { 
// 							$statement = $db->prepare($query);
// 							$statement->execute();
// 							$results=$statement->fetchAll(PDO::FETCH_ASSOC);
// 							$deleted_data[$table] = $results;
// 						}

// 						// insert device new data to the db
// 						$statement = $db->prepare($insert['address']);
// 						foreach ($local_Data['address'] as $key => $value) {
// 							$statement->bindParam(":address_id", $value['address_id']);
// 							$statement->bindParam(":address_line1", $value['address_line1']);
// 							$statement->bindParam(":address_line2", $value['address_line2']);
// 							$statement->bindParam(":city", $value['city']);
// 							$statement->bindParam(":zipcode", $value['zipcode']);
// 							$statement->execute();
// 						}
// 						$statement = $db->prepare($insert['person']);
// 						foreach ($local_Data['person'] as $key => $value) {
// 							$statement->bindParam(":person_id", $value['person_id']);
// 							$statement->bindParam(":last_name1", $value['last_name1']);
// 							$statement->bindParam(":first_name", $value['first_name']);
// 							$statement->bindParam(":email", $value['email']);
// 							$statement->bindParam(":last_name2", $value['last_name2']);
// 							$statement->bindParam(":middle_initial", $value['middle_initial']);
// 							$statement->bindParam(":phone_number", $value['phone_number']);
// 							$statement->execute();
// 						}
// 						$statement = $db->prepare($insert['location']);
// 						foreach ($local_Data['location'] as $key => $value) {
// 							$statement->bindParam(":location_id", $value['location_id']);
// 							$statement->bindParam(":name", $value['name']);
// 							$statement->bindParam(":address_id", $value['address_id']);
// 							$statement->bindParam(":owner_id", $value['owner_id']);
// 							$statement->bindParam(":manager_id", $value['manager_id']);
// 							$statement->bindParam(":license", $value['license']);
// 							$statement->bindParam(":agent_id", $value['agent_id']);
// 							$statement->execute();
// 						}
// 						$statement = $db->prepare($insert['report']);
// 						foreach ($local_Data['report'] as $key => $value) {
// 							$statement->bindParam(":report_id", $value['report_id']);
// 							$statement->bindParam(":creator_id", $value['creator_id']);
// 							$statement->bindParam(":location_id", $value['location_id']);
// 							$statement->bindParam(":flowchart_id", $value['flowchart_id']);
// 							$statement->bindParam(":note", $value['note']);
// 							$statement->bindParam(":date_filed", $value['date_filed']);
// 							$statement->bindParam(":name", $value['name']);
// 							$statement->bindParam(":status", $value['status']);
// 							$statement->execute();
// 						}
// 						$statement = $db->prepare($insert['path']);
// 						foreach ($local_Data['path'] as $key => $value) {
// 							$statement->bindParam(":report_id", $value['report_id']);
// 							$statement->bindParam(":option_id", $value['option_id']);
// 							$statement->bindParam(":data", $value['data']);
// 							$statement->bindParam(":sequence", $value['sequence']);
// 							$statement->execute();
// 						}
// 						$statement = $db->prepare($insert['appointments']);
// 						foreach ($local_Data['appointments'] as $key => $value) {
// 							$statement->bindParam(":appointment_id", $value['appointment_id']);
// 							$statement->bindParam(":date", $value['date']);
// 							$statement->bindParam(":time", $value['time']);
// 							$statement->bindParam(":report_id", $value['report_id']);
// 							$statement->bindParam(":purpose", $value['purpose']);
// 							$statement->bindParam(":maker_id", $value['maker_id']);
// 							$statement->execute();
// 						}
// 						$status = 1;
// 					}else{ 

// 						$debug1 = "NPI>>>>>.....";
// 						$status = -1;
// 					}
// 				}else if($user_type == "specialist" || $user_type == "agent"){

// 					if(!strcmp($_POST["sync_type"], "FULL")){// sync full

// 						foreach ($queries as $table => $query) {
// 							$statement = $db->prepare($query);
// 							$statement->execute();
// 							$results=$statement->fetchAll(PDO::FETCH_ASSOC);
// 							$new_data[$table] = $results;
// 						}
// 						$status = 1;
// 						$debug1 = "full";

// 						}else if(!strcmp($_POST["sync_type"], "INC")){// sync inc

// 							foreach ($queries as $table => $query) { // TODO: change $queries to $queries_new
// 								$statement = $db->prepare($query);
// 								$statement->execute();
// 								$results=$statement->fetchAll(PDO::FETCH_ASSOC);
// 								$new_data[$table] = $results;
// 							}
// 							$debug1 = "incremental";
// 							foreach ($queries_deleted as $table => $query) { 
// 								$statement = $db->prepare($query);
// 								$statement->execute();
// 								$results=$statement->fetchAll(PDO::FETCH_ASSOC);
// 								$deleted_data[$table] = $results;
// 								$debug1 .= $table;
// 							}

// 							// insert device new data to the db
// 							$statement = $db->prepare($insert['address']);
// 							foreach ($local_Data['address'] as $key => $value) {
// 								$statement->bindParam(":address_id", $value['address_id']);
// 								$statement->bindParam(":address_line1", $value['address_line1']);
// 								$statement->bindParam(":address_line2", $value['address_line2']);
// 								$statement->bindParam(":city", $value['city']);
// 								$statement->bindParam(":zipcode", $value['zipcode']);
// 								$statement->execute();
// 							}
// 							$statement = $db->prepare($insert['person']);
// 							foreach ($local_Data['person'] as $key => $value) {
// 								$statement->bindParam(":person_id", $value['person_id']);
// 								$statement->bindParam(":last_name1", $value['last_name1']);
// 								$statement->bindParam(":first_name", $value['first_name']);
// 								$statement->bindParam(":email", $value['email']);
// 								$statement->bindParam(":last_name2", $value['last_name2']);
// 								$statement->bindParam(":middle_initial", $value['middle_initial']);
// 								$statement->bindParam(":phone_number", $value['phone_number']);
// 								$statement->execute();
// 							}
// 							$statement = $db->prepare($insert['location']);
// 							foreach ($local_Data['location'] as $key => $value) {
// 								$statement->bindParam(":location_id", $value['location_id']);
// 								$statement->bindParam(":name", $value['name']);
// 								$statement->bindParam(":address_id", $value['address_id']);
// 								$statement->bindParam(":owner_id", $value['owner_id']);
// 								$statement->bindParam(":manager_id", $value['manager_id']);
// 								$statement->bindParam(":license", $value['license']);
// 								$statement->bindParam(":agent_id", $value['agent_id']);
// 								$statement->execute();
// 							}
// 							$statement = $db->prepare($insert['report']);
// 							foreach ($local_Data['report'] as $key => $value) {
// 								$statement->bindParam(":report_id", $value['report_id']);
// 								$statement->bindParam(":creator_id", $value['creator_id']);
// 								$statement->bindParam(":location_id", $value['location_id']);
// 								$statement->bindParam(":flowchart_id", $value['flowchart_id']);
// 								$statement->bindParam(":note", $value['note']);
// 								$statement->bindParam(":date_filed", $value['date_filed']);
// 								$statement->bindParam(":name", $value['name']);
// 								$statement->bindParam(":status", $value['status']);
// 								$statement->execute();
// 							}
// 							$statement = $db->prepare($insert['path']);
// 							foreach ($local_Data['path'] as $key => $value) {
// 								$statement->bindParam(":report_id", $value['report_id']);
// 								$statement->bindParam(":option_id", $value['option_id']);
// 								$statement->bindParam(":data", $value['data']);
// 								$statement->bindParam(":sequence", $value['sequence']);
// 								$statement->execute();
// 							}
// 							$statement = $db->prepare($insert['appointments']);
// 							foreach ($local_Data['appointments'] as $key => $value) {
// 								$statement->bindParam(":appointment_id", $value['appointment_id']);
// 								$statement->bindParam(":date", $value['date']);
// 								$statement->bindParam(":time", $value['time']);
// 								$statement->bindParam(":report_id", $value['report_id']);
// 								$statement->bindParam(":purpose", $value['purpose']);
// 								$statement->bindParam(":maker_id", $value['maker_id']);
// 								$statement->execute();
// 							}
// 							$status = 1;

// 						}else{ 

// 							$debug1 = "NPI>>>>>.....";
// 							$status = -1;
// 						}
// 					}
// 				}

// 	}else{ // add the divice to the db

// 		$db->prepare("INSERT INTO devices (id_number,latest_sync) VALUES(:id_number,:latest_sync)")->execute(array(":id_number" => $device, ":latest_sync"=> "2000-01-01 00:00:00.0"));
// 		$statement = $db->prepare("SELECT * FROM devices WHERE id_number = :id_number");
// 		$statement->execute(array(":id_number" => $device));
// 		$result = $statement->fetch(PDO::FETCH_ASSOC);
// 		$base_sequence = $result['base_sequence'];
// 		$debug1 = "NEW DEVICE test 4";
// 		$flag = 1;

// 	}
// 	// $transaction = $db -> commit (); 

// 	$post['new_data'] = $new_data; 
// 	$post['deleted_data'] = $deleted_data;
// 	$post['sync_info'] = array('sync_status' => $status, 'flag' => $flag, 'base_sequence' => $base_sequence , 'transaction' => $transaction, 'local_data' => array('in' => $local, 'pq' => $debug1, 'papi'=>$local_Data['report']));
// 	echo json_encode($post);
// 	if($status){
// 		$db->exec("UPDATE devices SET latest_sync = '2000-01-01 00:00:00.0'"); //now()
// 	}

// 	}
// 	catch ( Exception $e )
// 	{ 
// 		$debug1 = "transaction fail";
// 		// $db -> rollBack (); 
// 		$post['new_data'] = $new_data; 
// 		$post['deleted_data'] = $deleted_data;
// 		$post['sync_info'] = array('sync_status' => -1, 'local_data' => array('in' => $local_Data, 'pq' => $debug1));
// 		echo json_encode($post);
// 	} 



?>