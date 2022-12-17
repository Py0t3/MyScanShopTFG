<?php

    

    $imagen= $_POST['imagen'];
    $nombre= $_POST['nombre'];

   //https://vaticinal-center.000webhostapp.com/images
    $url =$_SERVER['DOCUMENT_ROOT'] . '/images/' . $nombre . '.jpeg';
   
    //file_put_contents($url,base64_decode($imagen));
    $resultado = file_put_contents($url,base64_decode($imagen));

    
    if($resultado){
        $result['exito']= "1";
        echo json_encode($result);
    }
    else{
        $result['exito']= "0";
        echo json_encode($result);
    }
?>