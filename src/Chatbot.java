import java.util.ArrayList;
import java.util.Scanner;

public class Chatbot {

    private static final String MESSAGE_IGNORANCE = "Je ne sais pas.";
    private static final String MESSAGE_APPRENTISSAGE = "Je vais te l'apprendre.";
    private static final String MESSAGE_BIENVENUE = "J'attends tes questions de culture générale.";
    private static final String MESSAGE_QUITTER = "Au revoir.";
    private static final String MESSAGE_INVITATION = "Je t'écoute.";
    private static final String MESSAGE_CONFIRMATION = "Très bien, c'est noté.";

    private static Index indexThemes; // index pour trouver rapidement les réponses à partir des mots NON outils de la question
    private static Index indexFormes; // index pour trouver rapidement les formes de réponse possibles à partir des mots-outils de la question

    static private ArrayList<String> motsOutils; // vecteur trié des mots outils
    static private ArrayList<String> reponses; // vecteur des réponses
    private static ArrayList<String> formesReponses; //vecteur des formes de réponses
    private static Thesaurus thesaurus; //thésaurus

    public static void main(String[] args) {

        // initialisation du vecteur des mots outils
        motsOutils = Utilitaire.lireMotsOutils("mots-outils.txt");

        // tri du vecteur des mots outils
        Utilitaire.trierChaines(motsOutils);

        // initialisation du vecteur des réponses
        reponses = Utilitaire.lireReponses("reponses.txt");
        // reponses = Utilitaire.lireReponses("mini_reponses.txt");

        // initialisation du thésaurus (partie 2)
        thesaurus = new Thesaurus("thesaurus.txt");

        // construction de l'index pour retrouver rapidement les réponses sur leurs thématiques
        indexThemes = Utilitaire.constructionIndexReponses(reponses, motsOutils, thesaurus);
        //indexThemes.afficher();

        // construction de la table des formes de réponses
        formesReponses = Utilitaire.constructionTableFormes(reponses, motsOutils, thesaurus);
        //System.out.println(formesReponses);

        // initialisation du vecteur des questions/réponses idéales
        ArrayList<String> questionsReponses = Utilitaire.lireQuestionsReponses("questions-reponses.txt");
        // ArrayList<String> questionsReponses = Utilitaire.lireQuestionsReponses("mini_questions-reponses.txt");

        // construction de l'index pour retrouver rapidement les formes possibles de réponses à partir des mots outils de la question
        indexFormes = Utilitaire.constructionIndexFormes(questionsReponses, formesReponses, motsOutils, thesaurus);
        // indexFormes.afficher();

        String reponse = "";
        String entreeUtilisateur = ""; // la dernière entrée de l'utilisateur
        String derniereQuestion = ""; // La derniere question posé par l'utilisateur


        Scanner lecteur = new Scanner(System.in);

        System.out.println("  _____ _           _   _           _   ");
        System.out.println(" / ____| |         | | | |         | |  ");
        System.out.println("| |    | |__   __ _| |_| |__   ___ | |_ ");
        System.out.println("| |    | '_ \\ / _` | __| '_ \\ / _ \\| __|");
        System.out.println("| |____| | | | (_| | |_| |_) | (_) | |_ ");
        System.out.println(" \\_____|_| |_|\\__,_|\\__|_.__/ \\___/ \\__|");
        System.out.println("__________________________________________");
        
        System.out.println();
        System.out.print("> ");
        System.out.println(MESSAGE_BIENVENUE);

        boolean peutApprendre = false;

        do {
            System.out.print("> ");
            entreeUtilisateur = lecteur.nextLine();

            if (!entreeUtilisateur.equalsIgnoreCase(MESSAGE_QUITTER)) {
                // si user dit "Je vais te l'apprendre." et peutApprendre = false
                if (entreeUtilisateur.equalsIgnoreCase(MESSAGE_APPRENTISSAGE) && peutApprendre) {
                    System.out.println("> " + MESSAGE_INVITATION);
                    System.out.print("> ");
                    String nouvelleReponse = lecteur.nextLine();

                    // si la réponse existe pas
                    if (!Utilitaire.reponseExiste(nouvelleReponse, indexThemes, reponses, motsOutils, thesaurus)) {
                        // ajouter la reponse
                        Utilitaire.IntegrerNouvelleReponse(nouvelleReponse, reponses, indexThemes, motsOutils, thesaurus);
                        // ecrire la réponse dans le fichier "reponses.txt"
                        Utilitaire.ecrireFichier("reponses.txt", nouvelleReponse);
                        // Utilitaire.ecrireFichier("mini_reponses.txt", nouvelleReponse);
                    }

                    // si la forme de la question existe pas
                    if (!Utilitaire.formeQuestionReponseExiste(derniereQuestion, nouvelleReponse, indexFormes, formesReponses, motsOutils, thesaurus)) {
                        // ajouter la nouvelle question
                        Utilitaire.integrerNouvelleQuestionReponse(derniereQuestion, nouvelleReponse, formesReponses, indexFormes, motsOutils, thesaurus);

                        // écrire la question et reponse dans "questions-reponses.txt"
                        Utilitaire.ecrireFichier("questions-reponses.txt", derniereQuestion + " " + nouvelleReponse);
                        // Utilitaire.ecrireFichier("mini_questions-reponses.txt", derniereQuestion + " " + nouvelleReponse);
                    }

                    System.out.println("> " + MESSAGE_CONFIRMATION);
                    peutApprendre = false;

                } else {
                       boolean seulementMotsOutils = Utilitaire.entierementInclus(motsOutils, entreeUtilisateur, thesaurus);

                    // question contexte = que avec des mots outils
                    if (seulementMotsOutils) {
                        reponse = repondreEnContexte(entreeUtilisateur, derniereQuestion); // répondre avec dernierQuestion
                    } else {
                        derniereQuestion = entreeUtilisateur; // ajouter la question a derniereQuestion
                        reponse = repondre(entreeUtilisateur); // repondre
                    }

                    System.out.println("> " + reponse);

                    // si reponse bot = "Je ne sais pas." => peutApprendre = true
                    peutApprendre = reponse.equals(MESSAGE_IGNORANCE);
                }
            }
        } while (!entreeUtilisateur.equalsIgnoreCase(MESSAGE_QUITTER));
    }


    static private String repondre(String question) {
        //int choix = (int) (Math.random() * reponses.size());
        //return (reponses.get(choix));
        ArrayList<Integer> reponsesCandidates = Utilitaire.constructionReponsesCandidates(question, indexThemes, motsOutils, thesaurus);

        if (reponsesCandidates.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }

        ArrayList<Integer> reponsesSelectionnees = Utilitaire.selectionReponsesCandidates(
                question,
                reponsesCandidates,
                indexFormes,
                reponses,
                formesReponses,
                motsOutils,
                thesaurus
        );

        if (reponsesSelectionnees.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }

        int choix = (int) (Math.random() * reponsesSelectionnees.size());
        int idReponse = reponsesSelectionnees.get(choix);

        return reponses.get(idReponse);
    }

    // partie 2
    static private String repondreEnContexte(String question, String questionPrecedente) {
        // return "";
        if (questionPrecedente.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }

        ArrayList<Integer> reponsesCandidates = Utilitaire.constructionReponsesCandidates(questionPrecedente, indexThemes, motsOutils, thesaurus);
        if (reponsesCandidates.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }

        ArrayList<Integer> reponsesSelectionnes = Utilitaire.selectionReponsesCandidates(question, reponsesCandidates, indexFormes, reponses, formesReponses, motsOutils, thesaurus);
        if (reponsesSelectionnes.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }

        int choix = (int) (Math.random() * reponsesSelectionnes.size());
        int idReponse = reponsesSelectionnes.get(choix);

        return reponses.get(idReponse);
    }
}
