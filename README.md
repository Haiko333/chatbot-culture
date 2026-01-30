# Projet Chatbot — SAE BUT Info

Chatbot en Java répondant à des questions de culture générale. Il s’appuie sur un index de réponses, un thésaurus et des mots-outils pour retrouver et adapter les réponses, et peut apprendre de nouvelles réponses.

## Prérequis

- **JDK** 8 ou supérieur (Java installé et `java` / `javac` dans le PATH)

## Structure du projet

```
.
├── src/
│   ├── Chatbot.java      # Point d'entrée, boucle de dialogue
│   ├── Index.java        # Index pour accès rapide aux réponses
│   ├── Thesaurus.java    # Thésaurus (formes canoniques des mots)
│   └── Utilitaire.java   # Lecture des fichiers et construction des index
├── mots-outils.txt       # Mots-outils (à ignorer dans l’analyse)
├── reponses.txt          # Base des réponses
├── questions-reponses.txt # Paires question / réponse pour les formes
├── thesaurus.txt         # Correspondances mot → forme canonique
├── mini_reponses.txt     # (optionnel) Pour debug
└── mini_questions-reponses.txt  # (optionnel) Pour debug
```

## Compilation et exécution

À la racine du projet (là où se trouvent les fichiers `.txt`) :

```bash
# Compilation
javac -d out -sourcepath src src/Chatbot.java

# Exécution
java -cp out Chatbot
```

Sous **IntelliJ IDEA** : ouvrir le projet, définir `src` comme sources, puis lancer la classe `Chatbot`.

## Utilisation

- Posez des questions de culture générale ; le chatbot tente de répondre à partir de sa base.
- Pour quitter : tapez **Au revoir**.
- Pour lui faire apprendre une nouvelle réponse : répondez **Je vais te l'apprendre.** à une question à laquelle il a dit *Je ne sais pas*, puis entrez la réponse quand il vous le demande.

## Fichiers de données

| Fichier | Rôle |
|--------|------|
| `mots-outils.txt` | Mots ignorés dans l’analyse (articles, pronoms, etc.) |
| `reponses.txt` | Liste des réponses utilisées pour répondre |
| `questions-reponses.txt` | Paires question / réponse pour choisir la forme de réponse |
| `thesaurus.txt` | Lignes `forme_variante:forme_canonique` pour normaliser les mots |

Pour tester avec moins de données, on peut modifier `Chatbot.java` pour utiliser `mini_reponses.txt` et `mini_questions-reponses.txt` à la place des fichiers complets.

## Auteur

Projet réalisé dans le cadre d’une SAE du BUT Informatique.