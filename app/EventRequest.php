
<?php

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    

    $response = array();
    $response["success"] = false;  
    

    $event = $_POST["event"];


    $statement = mysqli_prepare($con, "SELECT * FROM events WHERE event_name = ?");

    mysqli_stmt_bind_param($statement, "s", $event);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);

    mysqli_stmt_bind_result($statement, $id, $event_name, $description, $capacity, $start_time, $end_time, $is_free, $venue_name, $venue_lattitude, $venue_longitude, $localized_multi_line_address_display);

    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true; 

        $response['id'] = $id;
        $response['event_name'] = $event_name;
        $response['description'] = $description;
        $response['capacity'] = $capacity;
        $response['start_time'] = $start_time;
        $response['end_time'] = $end_time;
        $response['is_free'] = $is_free;
        $response['venue_name'] = $venue_name;
        $response['venue_lattitude'] = $venue_lattitude;
        $response['venue_longitude'] = $venue_longitude;
        $response['localized_multi_line_address_display'] = $localized_multi_line_address_display;

    }     
    

    echo json_encode($response);
?>