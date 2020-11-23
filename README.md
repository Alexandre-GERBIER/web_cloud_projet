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

## Format des données
Listes des différentes entités et de leurs propriétés:
* User : Key(email), fistName, lastName, avatarUrl, email, bio, googletoken, lastTimelineRetrieval

Le champ lastTimelineRetrieval devait permettre de ne récupérer que les nouveaux post

* Post : Key(userKey+timestamp), User (datastore.Key), description, timestamp, image

Le timestamp devait permettre de les ordonner, mais la date de création est gérée par le datastore, on pourrait donc le supprimer.
Pour l'instant l'image est une chaîne de caractères mais devrait être remplacée par l'url fournie par le blobStore

* Like:  Key (postKey+userKey), Post (datastore.Key), User (datastore.Key)

Cette implémentation permet des écritures concurrentes et garanti qu'aucun like ne soit perdu, par contre la taille des données explose
et le compte des likes se fait en O5nombre de like), ce qui pose problème lorsqu'on veut charger une timeline contenant 
beaucoup de post ayant beaucoup de likes.

* Follow :  key (followingUserKey + followedUserKey), follower (datastore.Key), following(datastore.Key)

On retrouve les incovénients de l'implémentation du like, les écritures sont garanties mais la lecture des données est compliquées.
De plus pour chercher les derniers posts des utilisateurs suivi, la complexité est mauvaise, on se retrouve a faire des
boucles de requètes imbriquées.


* Conclusion

En voulant garantir l'écriture des données jai trop désavantagé leur lecture. BIen qu'une grande partie du projet ne soit pas fonctionnelle, 
je pense que si elle l'étais avec cette structure de données, les temps d'accès seraient largement supérieurs aux 500 ms
du sujet.



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

## Benchmarks

Le projet n'étant pas fonctionnel, je n'ai pas pu réaliser les benchmark demandés. MAis je pense que vu les choix d'implémentation
que j'ai fait, je serais bien au dessus des 500ms du sujet, par exemple pour récupérer les derniers posts.  

### Poster un message
Le post est réalisé en temps constant car il est indépendant du nombre de followers

### Récupérer les derniers posts
Ne fonctionne pas

### Like per second
Fonctionne en temps constant car indépendant du Post. Mais je pense que ca peux poser un problème de 
taille des données et de taille des réponses lorsqu'on les compte.