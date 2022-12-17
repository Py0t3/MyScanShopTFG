<?php
    require_once('datosDB.php');

    $conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
    if(!$conexion){
    echo "error de conexion";
    }

    $id= $_POST['id'];
    $nombre = $_POST['nombre'];
    $email = $_POST['email'];
    $telefono = $_POST['telefono'];
    $direccion = $_POST['direccion'];
    $dni = $_POST['dni'];

    $query = "UPDATE clientes SET nombre = '$nombre', email = '$email', telefono = '$telefono', 
            direccion = '$direccion', dni = '$dni' WHERE id = '$id' ";

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
