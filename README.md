# BlackJack

## Todo

1. Version avec Mise : Ajouter dans la classe Joueur un constructeur pour la somme de départ, Mise par défaut (10 % de la somme initiale)
2. Version avec Croupier : Abstraire classe Joueur et dériver de la classe Joueur les classes Humain et IA
3. Ecrire une Interface pour l'interaction Jeu/Joueur (Remplacer les interactions console par cette Interface)
4. Mettre en place un système de Logging
5. Transformer chaque version en une machine à états (Design Pattern State + FlyWeight + Command + Composite)

## Rapport de Bugs (Prioritaire)

### Version Multi

#### Crash : Out of bounds quand une carte est piochée lors d'un tour complet après plusieurs parties jouées

1. Quand un joueur obtient 21 points dès le tirage initial, il n'est pas retiré de la liste des joueurs en lice
2. Quand il reste un joueur en lide avec le meilleur score, il devrait se retirer automatiquement (Préciser quand une telle situation se présente avec un message spécifique)
3. Quand un joueur a un score inférieur à d'autres joueurs en tête, le faire jouer automatiquement (Préciser quand une telle situation se présente)
4. Quand un joueur a un score inférieur à d'autres joueurs qui se sont retirés en tête, le faire jouer automatiquement (Préciser quand une telle situation se présente)

### Version Solo

1. Quand le joueur obtient 21 points dès le tirage initial, il n'est pas retiré de la liste des joueurs en lice