<?php

require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}


$result = array();
$result['datos'] = array();
$query = "SELECT * FROM clientes";
$response = mysqli_query($conexion, $query);

while($row = mysqli_fetch_array($response))
{
    $index['id'] = $row['0'];
    $index['nombre'] = $row['1'];
    $index['email'] = $row['2'];
    $index['telefono'] = $row['3'];
    $index['direccion'] = $row['4'];
    $index['dni'] = $row['5'];
  
    array_push($result['datos'],$index);
}

$result['exito']= "1";
echo json_encode($result);
