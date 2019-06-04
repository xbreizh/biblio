# biblio


 1- Le code source de l’application -> https://github.com/xbreizh/bibliotheque

    2- Les scripts SQL de création de la base de données avec un jeu de données de démo
    Les fichiers de configuration exemple

    	troparo_dump.sql

    3- Le WSDL du web service ainsi qu’un projet SoapUI contenant un plan de test du web-service

    	dossier soap  (vous devez au prealable demarrer le webservice -> cf 5-)

    Les ressources nécessaires à la création des différents composants du système (base de données, web service, application web, batch) ainsi que les fichiers de configuration permettant de déployer :
        le webservice (base de données + web-service)
        l’application web (base de données + web-service + application web)
        le batch (base de données + web-service + batch)
    La documentation générale du projet au format PDF
    Une documentation succincte (un fichier  README.md  suffit) expliquant comment déployer l'application

    	dossier app

    	Pour lancer l'application:
    		1- demarrer Docker
    		2- depuis une invite de commande, taper la commande docker-compose up puis valider

    		une fois le script termine, vous pouvez vous connecter avec:
    		Webservice:
    		 http://localhost:8080/troparo_app/services

    		Webapp:
    		 http://localhost:8090/library-webapp/login

    		 Credentiels: lokii / 123

    		Mail-app
    		 pour recevoir le mail de test, vous pouvez modifier l'adresse de test au niveau du fichier  
    		 bibliotheque\mail_app\src\main\java\org\mail\implEmailManagerImpl
    		 ligne 42