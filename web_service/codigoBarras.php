<?php

require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}

$codigo_barras = $_POST['codigo_barras'];

$result = array();
$result['datos'] = array();
$query = "SELECT * FROM productos WHERE codigo_barras ='$codigo_barras'";
$response = mysqli_query($conexion, $query);

while($row = mysqli_fetch_array($response))
{
    $index['id'] = $row['0'];
    $index['url_imagen'] = $row['1'];
    $index['nombre'] = $row['2'];
    $index['precio'] = $row['3'];
    $index['codigo_barras'] = $row['4'];
    $index['descripcion'] = $row['5'];
  
    array_push($result['datos'],$index);
}
if(count($result['datos'])>0){
    $result['exito']= "1";
    echo json_encode($result);
}
else{
    $result['exito']= "0";
    echo json_encode($result);
}
