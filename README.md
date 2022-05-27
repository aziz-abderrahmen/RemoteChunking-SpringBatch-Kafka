# Spring Boot & Spring batch & Spring Cloud Stream & Kafa


## Lancement du projet
Téléchargez l’archive du projet et décompressez-la.

### Lancement des serveurs
#### Serveur “domain”
Exécuter la classe “AppDomain”
#### Serveur “server-master”
Exécuter la classe “JobMasterApp”
#### Serveur “worker-service”
Exécuter la classe “WorkerApp”


Si vous souhaitez changer le port des serveurs, vous pouvez modifier la config “server.port” dans le fichier “applications.properties” présent dans tous les serveurs.

#### Serveur kafka
Prenez l’archive “kafka-server” et décompressez-la
Lancer le run.bat à la racine du répertoire Kafka, cela va permettre de lancer le Zookeeper et un broker Kafka
Deux fenêtres CMD doivent s’ouvrir, sans erreur.

Si le serveur Kafka a des problèmes pour se lancer, supprimer tout le contenu du répertoire “data” dans la racine.
