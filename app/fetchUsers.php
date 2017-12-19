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
    $stmt = $conn->prepare("SELECT user_id, username, age, email FROM users;");

    //executing the query 
    $stmt->execute();

    //binding results to the query 
    $stmt->bind_result($id, $username, $age, $email);

    $users = array(); 

    //traversing through all the result 
    while($stmt->fetch()){
        $temp = array();
        $temp['id'] = $id;
        $temp['username'] = $username; 
        $temp['age'] = $age;
        $temp['email'] = $email; 
        
        array_push($users, $temp);
    }

    //displaying the result in json format 
    echo json_encode($users);