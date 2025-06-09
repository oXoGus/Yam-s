# Rapport Final – Projet Java BUT1

## Page de garde

**Titre du projet** :  Yam's
**Nom(s) des étudiant·e·s** : GIGLIONI Maëllys - DINTRAT Mathis
**Groupe / TD / TP** :  TD B / TP Gamma
**Date de rendu** : 09/06/2025

---

## Introduction

> Présentez en quelques lignes le projet et le scénario suivi :
- Contexte du projet
Concevoir à partir d’une base de code en java, un yams proposant plusieurs modes de jeu conformes au règles du jeu classique 

- Objectifs principaux
    - Réorganiser le code selon le modèle MVC
    - Création d'une interface graphique avec JavaFX
    - Ajouter la possibilité de jouer avec des cartes (avec leurs combinaison correspondantes)

- Organisation et répartition du travail
    - Réorganiser le code selon le modèle MVC => Maëllys/Mathis
    - Interface graphique => Maëllys
    - Cartes => Mathis
    - Intégration et verificaiton => Mathis

## 1.  Bilan Phase 1

- Scénario : B/C (approfondissement)
- Priorités de cette dernière phase
    - MVC
    - Version graphique
    - Cartes


## 2. Phase 2

> Pour les scénarios B/C : précisez les corrections apportées avant d'ajouter des nouveautés.

- Fonctionnalités de base réalisées
    - Menu
    - fenêtre d'aide pour réaliser les combinaisons
    - demander le nombre de joueur/IA pour la partie
    - fenêtre de séléction du type d'IA
    - fenêtre pour entrer le pseudo de l'utilisateur 

- Problèmes ou limites rencontrées
    - difficulté a bien comprendre comment mettre en place le MVC avec JavaFX, le plus perturbant etant qu'on ne puissent pas instancier les controleurs et donc pas de polymorphisme avec les controlleurs
    - difficulté pour montrer les actions de l'IA, utiliser une animation ne fonctionnait pas donc on a dû faire un thread parallèle pour pouvoir diretement faire des pause pour a la fois laisser le temps a l'utilisateu de voir les actions de L'IA tout en ne bloquant pas les actions de l'utilisateur pendant les pause (retour au menu)
    - problèmes pour que les tableaux aient la bonne taille
- Points retravaillés dans cette phase
    - Le Board : on a crée une interface et utilisé le polymorphisme pour les cartes et les dés
    - on peut maintenant intancier la ScoreSheet avec une liste de combination pour pouvoir utiliser des combinaison qu'on veut



#### Réorganisation du code selon le modèle MVC

> Présentez :
- Le découpage logique de votre application (modèle, vue, contrôleur)
    - modèle : tout la logique interne de l'applicaiton
    - vue : l'interface de l'application
    - contrôleur : fait le lien entre le modêle et la vue, gères les actions de l'utilisateur 
- Les classes principales et leur rôle
    - Yams : lance l'application en chargeant le menu
    - MenuController : est directement instancié, en fonction du bouton cliqué, instancie les différentes parties avec les options choisis pas l'utilisateur, il charge la partie 
    - GameController : est instancié du menu, il gère le déroulement de la partie, les roulements des utilisateurs, et l'affiche de fin de partie, pour chaque tour il charge l'interface de l'utilisateur 
    - UserController (PlayerController et AIController) : s'occupe de toutes les actions de l'utilisateur sur son tour.

- Un schéma représentant la structure de votre code.
Yams (main)
 ├── views 
 │  ├── AIView.fxml
 │  ├── GameView.fxml
 │  ├── menu.fxml
 │  ├── PlayerView.fxml
 │	└── Yams.java
 |
 ├── controller
 │     ├── GameController
 │     ├── ManuController
 │     └── UserController
 │              ├── AIController
 │              └── PlayerController
 |
 └── model
        ├── User
        │    ├── Player
        │    └── AI
        │        ├── RiskyAI
        |        ├── SafeAI
        |        └── RandomAI
        ├── Board  
        │     ├── BoardDice
        |     └── BoardCard
        ├── GameElement
        │     ├── Dice
        |     └── Card    
        ├── Suit
        ├── ScoreSheet
        └── Combination
                ├── FullHouse
                ├── ThreeOfAKind
                ├── Chance
                ├── FourOfAKind
                ├── SmallStraight
                ├── LargeStraight
                ├── Yahtzee
                └── CardCombination
                        ├── Flush
                        ├── FourOfAKindCard
                        ├── FullHouseCard
                        ├── HightCard
                        ├── OnePair
                        ├── RoyalFlush
                        ├── Straight
                        ├── StraightFlush
                        ├── ThreeOfAKindCard
                        └── TwoPair
                


## 4. Fonctionnalités finales

> Listez ici toutes les fonctionnalités réalisées à la fin du projet.
> Pour chaque amélioration, expliquez brièvement son intérêt et sa difficulté.

- [x] Fonctionnalités de base (lancer de dés, score…) 
    - intérêt : essenciel au bon fonctionnement du jeu
    - difficulté : modéré

- [x] Corrections apportées : 
    - intérêt : moins de bugs
    - difficulté : modéré

- [x] Version graphique (JavaFX) : 
    - intérêt : meilleurs interaction avec l'utilisateur
    - difficulté : haute 
    
- [x] Version avec cartes 
    - intérêt : diversifier le gameplay
    - difficulté : modéré


## 5. Tests et validation

> Montrez que vous avez vérifié le bon fonctionnement :
- Cas testés (exemples)
    - on verifit le nombre de joueur entré par l'utilisateur si il est positif (ou nul pour une partie custom mais avec forcément au moins une IA)

## 6. Difficultés rencontrées

> Soyez honnêtes : c’est ici que vous racontez ce qui a été compliqué.
> Cela peut être technique, organisationnel, ou lié à la compréhension du projet.

- difficulté à séparer le modèle et la vue.
- difficulté pour s'organiser avec tout les autres projets en parallèle 
- difficulté a bien comprendre comment mettre en place le MVC avec JavaFX, le plus perturbant etant qu'on ne puissent pas instancier les controleurs et donc pas de polymorphisme avec les controlleurs
- difficulté pour montrer les actions de l'IA, utiliser une animation ne fonctionnait pas donc on a dû faire un thread parallèle pour pouvoir diretement faire des pause pour a la fois laisser le temps a l'utilisateu de voir les actions de L'IA tout en ne bloquant pas les actions de l'utilisateur pendant les pause (retour au menu)
- problèmes pour que les tableaux aient la bonne taille


## 7. Pistes d’amélioration ou d’évolution

> Proposez des pistes même si vous ne les avez pas réalisées.
> Objectif : montrer que vous avez du recul sur votre travail.
- amélorer les algo de détection des éléments de jeux manquant
- rajouter des modificateurs pour avoir plsu de dynamisme 
- un multijoueur en ligne :p

## ✍️ Commentaire personnel 

> Vous pouvez écrire ici ce que vous avez appris, aimé ou trouvé difficile dans cette SaÉ.
- la partie algo pour trouver les élément de jeux manquant était intéressant
- On a bien pu mettre en pratique et concrétiser ce qu'on nous avais apris pendant le cours d'IHM avec le MVC et JavaFX
- le plus intéressant était de refléchir à l'organisations des classe pour utiliser le polymorphisme 
