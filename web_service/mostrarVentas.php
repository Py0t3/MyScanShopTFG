<?php

require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}
$result = array();
$result['datos'] = array();
$query = "SELECT * FROM ventas ORDER BY ventas.id DESC";
$response = mysqli_query($conexion, $query);

while($row = mysqli_fetch_array($response))
{
    $index['id'] = $row['0'];
    $index['fecha'] = $row['1'];
    $index['id_vendedor'] = $row['2'];
    $index['id_cliente'] = $row['3'];
    $index['nombre_cliente'] = $row['4'];
    $index['factura'] = $row['5'];
  
    array_push($result['datos'],$index);
}

$result['exito']= "1";
echo json_encode($result);
