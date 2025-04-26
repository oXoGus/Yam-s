# 🧩 Document de Travail – Développement d’un Yams Solo

**Noms des étudiants :** GIGLIONI Maëllys DINTRAT Mathis  
**Date :** 26/04/2025

---

## ✨ Tâche a. – Analyse du Code Existant

### 1. Démarche suivie  
_Décrivez brièvement comment vous avez exploré le code existant : ce que vous avez testé, observé, etc._

> Exemple : J’ai commencé par lire la classe `Yams`, puis j’ai repéré les interactions principales entre les objets. Ensuite, j’ai testé le jeu pour observer son comportement.

Dans un premier temps, j’ai parcouru les différentes classes afin de trouver le main responsable de la partie. Une fois fait, j’ai parcouru les différentes fonctions et classes appelées au sein du main pour comprendre le fonctionnement global. A partir du main j’ai pu remarquer qu’on pouvait uniquement relancer un dé à la fois. J’ai aussi remarqué qu’il n’y avait que 2 round pour une partie et donc qu’il manquait l'implémentation de 5 combinaisons (Chance, FourOfAKind, SmallStraight, LargeStraight, Yahtzee), j’ai aussi remarqué qu’il manquait la possibilité de sacrifier une combinaison si aucune n’était valide et ni la possibilité vérifier si la combinaison était valide pour ce board. En testant le programme j’ai remarqué que pour chaque interaction avec le joueur le programme ne faisait pas de vérification sur ce que le joueur a entré et donc le joueur obtient un message d'erreur s'il rentre quelque chose qui n’est pas attendue. Toujours en testant, j’ai aussi remarqué qu’il n’y avait aucune information sur les combinaisons valides ou sacrifiées ainsi que leurs points ou leurs conditions de validation. J’ai aussi remarqué qu’on n'obtient jamais de 6. ThreeOfAKind.score() devrait renvoyer la somme de tous les dés.


---

### 2. Liste des fonctionnalités déjà implémentées

> Listez ici ce qui fonctionne déjà dans le programme.

- [ ] Affichage des dés  
- [ ] Relance d’un dé (board.reroll)
- [ ] Le score du FullHouse
- [ ] Le score total de la partie


---

### 3. Liste des manques

> Identifiez ce qui manque pour que le Yams soit complet et jouable correctement en solo.

- 🔲   gestion des erreurs possibles sur les entrés du joueur
- 🔲   implémentation des 5 autres combinaisons avec leur méthodes de vérification et leur calcul de points   
- 🔲   modifier Dice pour qu’il puisse tomber sur un 6
- 🔲   Être obligé de sacrifier une combinaison si à la fin des reroll on ne peut valider aucune combinaison 
- 🔲   pouvoir relancer plusieurs dés à la fois 

---

## 🛠️ Tâche b. – Proposition de Solution

### 1. Cahier des charges simplifié

> Listez ici les fonctionnalités que vous comptez ajouter ou améliorer.
 
- [ ] Affichage des dés  
- [ ] Relance d’un dé (board.reroll)
- [ ] le score du FullHouse
- [ ] le score total de la partie
- [ ] gestion des erreurs possibles sur les entrées du joueur
- [ ] implémentation des 5 autres combinaisons avec leurs méthode de vérification et leur calcule de points   
- [ ]  modifier Dice pour qu’il puisse tomber sur un 6
- [ ] faire en sorte qu’on soit obligé de sacrifier une combinaison si à la fin des reroll on ne peut valider aucune combinaison 
- [ ] pouvoir relancer plusieurs dés à la fois 
- [ ] affichages des dés à l'horizontale 
- [ ] toString du ScoreSheet qui montre pour chaque combinaison si elle est validée, sacrifiée, les conditions de validation, ses points, son code 
- [ ] pouvoir relancer une nouvelle partie  

---

### 2. Choix techniques importants

> Expliquez ici brièvement comment vous comptez vous y prendre techniquement (nouvelle classe, refactorisation, etc.)

- stockage des Combinaisons sacrifiées dans un HashSet  
- scanner, board et scoreSheet en champ de la classe Yams  
- regroupement des demandes et vérification des entrées du joueur dans des fonctions pour réduire le nombre d’appel dans le main

---

### 3. Schéma simple de l’organisation du programme

> Ajoutez ici un schéma type UML ou une structure en texte brut pour montrer les classes et leurs relations.

```
Exemple :
Yams (main)
 ├── Board
 │	└── Dice x5
 ├── ScoreSheet
  |         └── HashMap
  |         └── HashSet
 └── Combination (interface)
   	├── FullHouse
   	├── ThreeOfAKind
   	├── Chance
	├── FourOfAKind
	├── SmallStraight
	├── LargeStraight
	└── Yahtzee

```

---

## 💻 Tâche c. – Programmation

> Listez ici les classes ou méthodes que vous avez créées ou modifiées pour répondre au cahier des charges.

- Création : méthodes pour la gestion des entrées du joueur, la vérification des combinaisons validé/sacrifié, valide pour un certain board, nombre d'occurrences des dés pour la vérification des combinaisons, sacrifier une combinaison, isCombinationPossible, affichage score dynamique dans le toString du scoreSheet.
- Modification : parseCombinaition, askCombination, tous les toString, main, reroll
- Tests réalisés : jouer des parties entières pour bien tester les conditions d’erreurs  

---

## 📦 Tâche d. – Livraison

> Vérifiez que tout est prêt pour la livraison.

- [x] Code fonctionnel  
- [x] Partie ligne de commande jouable sur 7 tours  
- [x] Combinaisons jouables au choix (et pas deux fois !)  
- [x] Affichage du score total  
- [x] Ce document rempli  

---

## ✍️ Commentaires personnels

> Vous pouvez expliquer ici ce que vous avez appris, aimé ou trouvé difficile dans l’exercice.
