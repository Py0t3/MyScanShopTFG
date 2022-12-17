<?php

require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}

$query2 = "SELECT max(id) FROM ventas";

$result = mysqli_query($conexion,$query2);

while($row = mysqli_fetch_array($result))
{
  
    echo $row['max(id)'];
}


mysqli_close($conexion);
?>