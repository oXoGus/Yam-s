# Rapport d'avancement – Projet Yams

# 

1. ## Rappel des objectifs

- Objectif 1 : Yams solo jouable sur ligne de commande  
- Objectif 2 : Yams duo avec adversaire IA ou humain  
- Ce rapport fait le point sur l'avancement à la fin de la première période.

2. ## Tâches effectuées

### Objectif 1 – Yams solo

Analyse du code fourni :

\[Décrivez les manques identifiés\]

- implémentation des 5 combinaisons manquantes (calcul du score et vérification de validité pour un Board)  
- pouvoir relancer plusieurs dés (qui compte pour une relance)  
- Le joueur doit pouvoir sacrifier une combinaison si aucune combinaison n’est valide pour son board.  
- Ajouter un HashSet\<Combination\>  dans le dans ScoreSheet qui stocke les Combination qui ont été sacrifiées  
- Écran de fin où le joueur peut rejouer une nouvelle partie  
- Méthodes isSarcrified() et isValidate() pour savoir l’état d’une combinaison   
- Toute la gestion des erreurs, dans chaque fonction demandant une entrée du joueur, tant que les données ne sont pas valides on redemande à l’utilisateur.

\- Cahier des charges :  
  \- \[Fonctionnalités prévues, décisions techniques\]

* relancer plusieurs dés par relance (2 relances max par tour)  
* Cocher 7 combinaisons   
* Sacrifier 1 combinaisons si aucune combinaison n’est possible   
* Affichage du score total en fin de partie  
* relancer une partie


    
\- Programmation :  
  \- \[Indiquez les classes ou modules développés\]

* toutes les Combination manquantes (Chance, FourOfAKind, SmallStraight, LargeStraight, Yahtzee)  
* toString() du ScoreSheet permettant d’expliquer les règles d’obtention de chaque Combinaison, leur score, leur code à entrer pour les sélectionner ainsi que leur état : validé, sacrifié ou ni l’un ni l’autre pour l’instant.  
* affichage des dés à l'horizontale pour une meilleur lisibilité   
* Création de méthodes pour la gestion des erreurs dans la class Yams mais aussi dans la class ScoreSheet

\- Livrable :  
  \- \[État du projet : jouable ? complet ?\]

* État du projet : jouable en solo

### Objectif 2 – Yams duo

\- Analyse :  
  \- \[Quels manques ont été identifiés pour passer à deux joueurs ?\]

* ajouter un nouveau ScoreSheet et Board pour le deuxième joueur   
* modification du main pour gérer l’alternance des tours entre le joueur 1 et 2  
* afficher qui a gagné à la fin de la partie   
* écran d'accueil pour que le joueur puisse choisir enter partie solo, duo ou duo avec IA  
* création d’une IA  
* menu pour choisir le mode de jeu

\- Cahier des charges et architecture envisagée :

* La classe Yams aura le choix de la partie avec le menu, son lancement et si le joueur veut rejouer  
* Création d’une interface Game avec comme méthodes abstraites toutes les méthodes qui seront communes aux trois modes de jeu : demander les pseudo, jouer les rounds, affichage de fin de partie  
* Création de classe NormalGame implémentant l’interface Game qui contient les méthodes pour la déroulement d’une partie avec les règles du jeu classiques  
* Création de la classe Player qui aura comme champs username, board et scoreSheet ainsi que toutes les méthodes qui étaient nécessaire pour le déroulement d’une partie en solo.  
* Création d’une interface User pour gérer les classes IAs et Players.  
* Création d’une interface IA implémentée par les classes RandomAI, SafeAI et RiskyAI.  
* La classe NormalGame a un champ users qui est une ArrayList qui contient tous les User de la partie. Grâce à NormalGame et Player on peut jouer avec autant de joueurs et d’AI que l’on souhaite.  
* Affichage de fin de partie avec le classement des users avec leurs score.

\- Programmation :  
  \- classe Yams avec main et menu : terminé  
  \- classe NormalGame : terminé  
  \- interface IA :  terminé  
 \- Trois types d’IA en classes : terminé  
 \- Création de l’interface user : terminé 

\- Livrable :  
  \- \[État actuel du mode duo\]

* État actuel du mode duo : terminé 

3. ## Organisation du groupe

  **\- \[Qui a fait quoi ?\]**  
**\- classe NormalGame \=\> Mathis**    
**\- modification de la classe Yams (utilisation du polymorphisme avec l’interface Game) \=\> Mathis**   
**\- création du menu \=\> Maëllys**  
**\- classe DuoIA et IA \=\> Maëllys**

**\- Outils utilisés :**  
  **\- Versionning : git**  
  **\- Communication :  discord**  
  **\- Plannification :  discord / ce rapport** 

4. ## Difficultés rencontrées

\- Techniques :    
  \- probleme de fonction récusive pour la gestion des erreurs avec les entré de l’utilisateur   
\- Organisationnelles :    
  \- N/A

5. ## Pistes d’amélioration du jeu

Listez ici les idées d’évolution que vous souhaiteriez intégrer dans les objectifs 3+.

\- Idée 1 : interface graphique en JavaFX  
\- Idée 2 : sauvegarde du meilleur score dans chaque mode de jeu dans un fichier   
\- Idée 3 : mode équipe  
\- Idée 4 : ajouter des nouvelles combinaisons  
\- Idée 5 : améliorer l’IA  
 

6. ## Objectifs 3+ envisagés

\- Propositions d’objectifs à développer (techniquement réalistes dans le temps imparti) :  
\- Idée 2 : sauvegarde du meilleur score dans chaque mode de jeu dans un fichier   
\- Idée 3 : mode équipe  
\- Idée 4 : ajouter des nouvelles combinaisons  
\- Idée 5 : améliorer l’IA  