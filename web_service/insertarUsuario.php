<?php

    require_once('datosDB.php');

    $conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
    if(!$conexion){
    echo "error de conexion";
    }

    $user= $_POST['user'];
    $password = $_POST['password'];
    $nombre = $_POST['nombre'];
    $puesto = $_POST['puesto'];
    $email = $_POST['email'];
    
    $query = "INSERT INTO usuarios (user, password, nombre, puesto, email) 
                        VALUES ('$user', '$password', '$nombre', '$puesto', '$email' )";
    $resultado = mysqli_query($conexion,$query);
    if($resultado){
        $result['exito']= "1";
        echo json_encode($result);
    }
    else{
        $result['exito']= "0";
        echo json_encode($result);
    }


    mysqli_close($conexion);
?>
