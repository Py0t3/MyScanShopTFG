<?php

$facturaNombre= $_POST['facturaNombre'];
$factura = $_POST['factura'];


$url =$_SERVER['DOCUMENT_ROOT'] . '/facturas/' . $facturaNombre;

file_put_contents($url,base64_decode($factura));

require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}


$id_vendedor = $_POST['id_vendedor'];
$id_cliente = $_POST['id_cliente'];
$nombre_cliente = $_POST['nombre_cliente'];
$urlFactura = 'https://vaticinal-center.000webhostapp.com/facturas/' . $facturaNombre;

$query = "INSERT INTO ventas (id_vendedor, id_cliente, nombre_cliente, factura) 
                    VALUES ('$id_vendedor', '$id_cliente', '$nombre_cliente', '$urlFactura')";

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