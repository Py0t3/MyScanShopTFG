<?php

    try{

        $base= new PDO("mysql:host=localhost; dbname=web_service", "root", "");

        $base->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $base->exec("SET CHARACTER SET UTF8");

    }catch(Exception $e){

        echo "Línea del error" . $e->getLine();
        die("Error" . $e->getMessage());
    }

?>