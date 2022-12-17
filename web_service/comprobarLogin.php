<?php

require_once('datosDB.php');

$conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
if(!$conexion){
    echo "error de conexion";
}


$nombre= htmlentities(addslashes($_POST['nombre']));
$password= htmlentities(addslashes($_POST['pass']));

$query= "SELECT * FROM usuarios WHERE user ='$nombre' AND password ='$password'";

$resultado = mysqli_query($conexion,$query);

$registrados = mysqli_num_rows($resultado);

if($registrados == 0 ){

          
    $result['exito']= "0";
    echo json_encode($result);
 }
 else{

     
     $result = array();
     $result['datos'] = array();
     while($row = mysqli_fetch_array($resultado))
    {
    
          $index['id'] =$row['0'];
          $index['user'] =$row['1'];
          $index['password'] =$row['2'];
          $index['nombre'] =$row['3'];
          $index['puesto'] =$row['4'];
          
          array_push($result['datos'],$index);

     }

     }
     $result['exito']= "1";
     echo json_encode($result);




?>