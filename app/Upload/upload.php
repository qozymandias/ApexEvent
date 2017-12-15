<?php 

    require_once 'dbDetails.php';

    $upload_path = 'uploads/';

    $server_ip = gethostbyname(gethostname());

    $upload_url = 'http://'.$server_ip.'/Upload/'.$upload_path;

    $response = array(); 

    if($_SERVER['REQUEST_METHOD'] == 'POST'){
        if(isset($_POST['name']) and isset($_FILES['image']['name'])){

            $con = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME) or die('unable to connect');
            $name = $_POST['name'];

            $fileinfo = pathinfo($_FILES['image']['name']);

            $extension = $fileinfo['extension'];

            $file_url = $upload_url . $name . '.'.$extension; 

            $file_path = $upload_path. $name . '.'.$extension; 

            try{

                move_uploaded_file($_FILES['image']['tmp_name'], $file_path);

                $sql = "INSERT INTO images (url, name) VALUES ('$file_url','$name')";
                if(mysqli_query($con,$sql)){
                    $response['error'] = false; 
                    $response['url'] = $file_url; 
                    $response['name'] = $name; 
                }

            }catch(Exception $e){
                $response['error'] = false; 
                $response['message'] = $e->getMessage(); 
            }
            mysqli_close($con);

        }else{
            $response['error'] = true; 
            $response['message'] = 'please choose a file';
        }
        
        echo json_encode($response);
    }