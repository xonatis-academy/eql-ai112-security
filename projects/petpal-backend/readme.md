# Bienvenue sur PetPal !

## Wrapper Maven

Je vous ai mis un wrapper pour maven dans le projet pour éviter les problèmes de compatibilité.

## Confugiration de Tomcat et de JPA

Bien entendu, avant tout démarrage du projet, je vous invite à vérifier que la configuration est en adéquation avec votre poste de travail ! Comment faire ? Rien de plus simple, car il faut juste consulter dans le fichier `application.properties`.

Vous allez trouver ces élements importants. Votre base de données doit être servi par MySQL et :

1. Ca (enfin, 8181), c'est le port du serveur HTTP, n'oubliez pas, il doit être libre !
```
server.port=8181
```

2. ca c'est la connection string vers votre base de données. Si c'est pas correct, ben ca marchera pas ^^ Dommage, il ne pourra pas se connecter à la base de données ! Si la base de données, n'existe pas, ne vous en faites pas, il la créera automatiquement avec des fixtures =)
```
spring.datasource.url=jdbc:mysql://localhost:3306/petpal_spring_db?createDatabaseIfNotExist=true&serverTimezone=UTC
```

3. ca (enfin, root), c'est pour l'authentification de la base de données, il le faut aussi !
```
spring.datasource.username=root
```

4. ca (enfin, la c'est rien, mais vous pourrez avoir quelque chose ici), c'est le mot de passe pour que notre serveur HTTP puisse 
accéder à la base de données
```
spring.datasource.password=
```

## Execution de l'application

Pour démarrer votre projet :

```
./mvnw spring-boot:run
```

## Troubleshoot ?

Si vous avez des problèmes de dépendances (et faites-moi signe) :

```
./mvnw depedency:resolve
```