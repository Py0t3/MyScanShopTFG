<?php
require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}

if(isset($_POST['id'])){
    $id =$_POST['id'];
}
else{
    $id=NULL;
}


$query = "DELETE FROM productos WHERE id = '$id'";

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