<?php

    $imagen= $_POST['imagen'];
    $nombre= $_POST['nombre'];

   //https://vaticinal-center.000webhostapp.com/images
    $url =$_SERVER['DOCUMENT_ROOT'] . '/images/' . $nombre . '.jpeg';
   
    
    file_put_contents($url,base64_decode($imagen));

  


    require_once('datosDB.php');
    
    $conexion = mysqli_connect( $DBhostname, $DBusername, $DBpassword, $DBname);
    if(!$conexion){
    echo "error de conexion";
    }
    
    $url_imagen ='https://vaticinal-center.000webhostapp.com/images/' . $nombre . '.jpeg';
    $precio = $_POST['precio'];
    $codigo_barras = $_POST['codigo_barras'];
    $descripcion = $_POST['descripcion'];

    $query = "INSERT INTO productos (url_imagen, nombre, precio, codigo_barras, descripcion) 
                        VALUES ('$url_imagen', '$nombre', ' $precio', '$codigo_barras', '$descripcion' )";
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


