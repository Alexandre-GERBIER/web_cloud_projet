 # Projet webandcloud

Projet réalisé par Alexandre Gerbier, commencé en groupe avec Allan Comisso, mais malgré l'approche du rendu il n'a fourni aucun travail sur le projet.

repo originel:

https://github.com/AllanC56/webandcloud.git

repo du rendu, que j'ai réalisé seul:

https://github.com/Alexandre-GERBIER/web_cloud_projet.git

## Interface

L'interface rendue n'est pas fonctionnelle.

adresse de l'interface:
https://myfirstproject-290312.ew.r.appspot.com/glogin-mithril.html#!/profile

## API REST
Le dossier exemples_requetes contient un export de 3 requètes faites avec Postman sur l'API.
Chaque requète doit contenir dans son Header un champ googleToken contenant le token d'authentification google. 
Pour les utilisateurs que j'ai créés sans compte google, le token est remplacé pa
Prénom-Nom1-Nom2 .

Liste des routes de l'API:
* GET  /api/user (fonctionne): 
Header : userName 

le nom ou le prénom de l'utilisateur recherché.

* POST /api/unlike (fonctionne):



## Parties non fonctionnelles du projet
* La gestion des images

Je n'ai pas réussi à me servir des Blob et blobstore pour stocker les images. Je n'ai pas réussi à gérer correctement le formulaire html pour qu'il y ait à la fois les paramètre nécessaires au BlobStore et à mon API.

* La récupération des posts récent d'un utilisateur

La requète s'exécute mais en renvoie aucun post

## Données présentes
* utilisateurs

Le Datastore contient 600 utilisateurs, ayant chacun un post. 

L'utilisateur Jake Smith-Peralta a 599 followers et suit 599 personnes.

L'utilisateur Jake Smith-Holt a 100 followers et suit 100 personnes.

L'utilisateur Jake Smith-Santiago a 10 followers et suit 10 personnes.