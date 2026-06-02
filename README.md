# Chatbot Project - SAE BUT Computer Science

A Java chatbot that answers general knowledge questions. It relies on a response index, a thesaurus, and a stop-words list to find and adapt answers, and can learn new responses.

## Prerequisites

- **JDK** 8 or higher (Java installed with `java` / `javac` in PATH)

## Project Structure

```
.
├── src/
│   ├── Chatbot.java      # Entry point, dialog loop
│   ├── Index.java        # Index for fast response lookup
│   ├── Thesaurus.java    # Thesaurus (canonical word forms)
│   └── Utilitaire.java   # File reading and index building
├── mots-outils.txt       # Stop words (ignored during analysis)
├── reponses.txt          # Response database
├── questions-reponses.txt # Question/answer pairs for response forms
├── thesaurus.txt         # Word → canonical form mappings
├── mini_reponses.txt     # (optional) For debugging
└── mini_questions-reponses.txt  # (optional) For debugging
```

## Compilation and Execution

From the project root (where the `.txt` files are located):

```bash
# Compilation
javac -d out -sourcepath src src/Chatbot.java

# Execution
java -cp out Chatbot
```

In **IntelliJ IDEA**: open the project, set `src` as sources, then run the `Chatbot` class.

## Usage

- Ask general knowledge questions; the chatbot will try to answer from its database.
- To quit: type **Goodbye**.
- To teach it a new answer: reply **I'll teach you.** when it says *I don't know*, then enter the answer when prompted.

## Data Files

| File | Purpose |
|------|---------|
| `mots-outils.txt` | Words ignored during analysis (articles, pronouns, etc.) |
| `reponses.txt` | List of responses used for answering |
| `questions-reponses.txt` | Question/answer pairs for choosing the response form |
| `thesaurus.txt` | Lines formatted as `variant_form:canonical_form` for word normalization |

To test with less data, you can modify `Chatbot.java` to use `mini_reponses.txt` and `mini_questions-reponses.txt` instead of the full files.

## Author

Project completed as part of a SAE (Situation d'Apprentissage et d'Évaluation) in the BUT Computer Science program.

## Link

[Replit](https://replit.com/@Haiko333/chatbot-culture)
