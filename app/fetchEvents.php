<?php 
  
    //database constants
    define('DB_HOST','mysql.hostinger.com');
    define('DB_USER','u392533552_oscar');
    define('DB_PASS','Oscar191097');
    define('DB_NAME','u392533552_users');

    //connecting to database and getting the connection object
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

    //Checking if any error occured while connecting
    if (mysqli_connect_errno()) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        die();
    }

    //creating a query
    $stmt = $conn->prepare("SELECT * FROM events;");

    //executing the query 
    $stmt->execute();

    //binding results to the query 
    $stmt->bind_result($id, $event_name, $description, $capacity, $start_time, $end_time, $is_free, $venue_name, $venue_lattitude, $venue_longitude, $localized_multi_line_address_display);

    $users = array(); 

    //traversing through all the result 
    while($stmt->fetch()){
        $temp = array();
        $temp['id'] = $id;
        $temp['event_name'] = $event_name;
        $temp['description'] = $description;
        $temp['capacity'] = $capacity;
        $temp['start_time'] = $start_time;
        $temp['end_time'] = $end_time;
        $temp['is_free'] = $is_free;
        $temp['venue_name'] = $venue_name;
        $temp['venue_lattitude'] = $venue_lattitude;
        $temp['venue_longitude'] = $venue_longitude;
        $temp['localized_multi_line_address_display'] = $localized_multi_line_address_display;
        
        array_push($users, $temp);
    }

    //displaying the result in json format 
    echo json_encode($users);