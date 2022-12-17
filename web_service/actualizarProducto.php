<?php

    require_once('datosDB.php');
    
    $conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
    if(!$conexion){
        echo "error de conexion";
    }


    $id = $_POST['id'];
    $url_imagen = $_POST['url_imagen'];
    $nombre = $_POST['nombre'];
    $precio = $_POST['precio'];
    $codigo_barras = $_POST['codigo_barras'];
    $descripcion = $_POST['descripcion'];

    $query = "UPDATE productos SET url_imagen = '$url_imagen', nombre = '$nombre', precio = '$precio', 
                codigo_barras = '$codigo_barras', descripcion = '$descripcion' WHERE id = '$id' ";

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
}

?>