# 🧩 Document de Travail – Développement d’un Yams Duo

**Noms des étudiants :**  GIGLIONI Maëllys - DINTRAT Mathis
**Date :** 26/04/2025

---

## ✨ Tâche a. – Analyse du Code Produit pour l’Objectif 1

### 1. Démarche suivie  
_Décrivez comment vous avez relu, testé ou réutilisé le code produit pour l'objectif 1 pour identifier les points à adapter ou à compléter._

Nous nous sommes dits qu’il y aurait trop de répétition de code entre le mode solo et duo donc nous avons repensé toute l’architecture du programme pour utiliser un maximum de polymorphisme. Nous avons d'abord créé une interface Game pour les différents modes de jeux qui a, une classe d’utilitaires Games qui a des fonctions statiques communes aux modes de jeu, une classe NormalGame et CustomGame toutes deux implémentant l’interface Game. Nous avons transformé la classe Yams qui permettait de jouer en solo en classe Player avec comme champ username, board et scoreSheet. On a 3 classes IA qui implémentent l’interface IA : SafeAI, RandomAI, RiskyAI. Cette classe Player et interface implémentent l’interface User. On a ensuite fait une méthode statique menu qui renvoie un Game, on utilise le polymorphisme de Game dans la fonction main pour jouer les différents modes de jeu.

---

### 2. Liste des éléments réutilisables

> Indiquez ici ce qui peut rester inchangé ou être réutilisé tel quel dans la version duo.

- [ ] Classe Dice  
- [ ] Classe Board
- [ ] Méthodes pour partie en solo
- [ ] les Combinations 
- [ ] Classe ScoreSheet

---

### 3. Liste des manques par rapport à un Yams duo

> Listez ici les fonctionnalités ou éléments manquants pour avoir un jeu jouable à deux, humain ou IA.

- 🔲 Gestion de deux joueurs  
- 🔲 Interface IA avec 3 IA différentes
- 🔲 Interface Game
- 🔲 Classe Games
- 🔲 Menu renvoyant un Game
- 🔲 Classe NormalGame
- 🔲 Interface User
- 🔲 Classe Player
- 🔲 Affichage de fin avec classement 



---

## 🛠️ Tâche b. – Proposition de Solution

### 1. Cahier des charges simplifié

> Listez les fonctionnalités que vous comptez développer ou modifier.

- [ ] Permettre à deux joueurs ou plus de jouer à tour de rôle  
- [ ] Permettre au joueur de faire une partie contre autant d’IA qu’il souhaite et de joueur qu’il souhaite
- [ ] Affichage de fin personnalisé en fonction des modes
- [ ] Nombre de round peuvent être < 7 grâce au polymorphisme de Game
- [ ] Jouer contre plusieurs types d’IA (choisi en début de partie) 

---

### 2. Choix techniques importants

> Décrivez ici vos grandes orientations de conception.

- Limiter le plus possible la répétition de code
- Utiliser le plus possible le polymorphisme à l’aide des interfaces
- Utilisation de listes pour ne pas limiter le nombre maximal de joueurs à 2 mais plus à 2147483647 joueurs 
- Créer le plus de possibilités pour le joueur grâce à l’interface User et la surcharge de la méthode pour créer une NormalGame qui permet de jouer contre n’importe qui, que ce soit IA ou humain, et quel que soit leur nombre

---

### 3. Schéma simple de l’organisation du programme

```
Exemple :
Yams (main)
 ├── Games
 ├── Game (interface)
  |	├── NormalGame
 ├── User (interface)
  |	├── Player
  |	└── AI (interface)
  |		├── SafeAI
  |		├── RandomAI
  |		└── RiskyAI
 ├── Board
 │	└── Dice x5
 ├── ScoreSheet
  |         ├── HashMap
  |         └── HashSet
 └── Combination (interface)
   	├──FullHouse
   	├── ThreeOfAKind
   	├── Chance
	├── FourOfAKind
	├── SmallStraight
	├── LargeStraight
	└── Yahtzee
```

---

## 💻 Tâche c. – Programmation

> Détaillez les ajouts/modifications apportés au code.

- Ajouts : menu, écran de fin avec classement, interface Game/User/IA, classe Player/Games/SafeAI/RandomAI/RiskyAI, toutes les méthodes pour les IA.
- Modifications : regroupement d’une partie en 3 fonction initialisation (new Game), playRounds, endScreen
- Tests réalisés : jouer des parties entières pour bien tester les conditions d’erreurs  

---

## 📦 Tâche d. – Livraison

> Cochez ce qui a été fait.

tout les fonctionnalités du cahier des charges ont été faites


---

## ✍️ Commentaire personnel

> Vous pouvez écrire ici ce que vous avez appris, aimé ou trouvé difficile dans cette version duo ?

On a appris à créer des interactions pour utiliser le polymorphisme.
En terme d’algorithmique, l’IA était plus compliqué que le reste.

