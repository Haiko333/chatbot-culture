import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Utilitaire {

    private static final int NBMOTS_FORME = 10; // nombre maximal de mots-outils pris en compte pour les formes dans l'étape 2

    static public ArrayList<String> lireMotsOutils(String nomFichier) {
        //{}=>{résultat = le vecteur des mots outils construit à partir du fichier nomFichier}
        ArrayList<String> motsOutils = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                motsOutils.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return motsOutils;
    }

    static public ArrayList<String> lireReponses(String nomFichier) {
        //{}=>{résultat = le vecteur des réponses construit à partir du fichier nomFichier}
        ArrayList<String> reponses = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                reponses.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reponses;
    }

    static public ArrayList<String> lireQuestionsReponses(String nomFichier) {
        //{}=>{résultat = le vecteur des questions/réponses construit à partir du fichier nomFichier}
        ArrayList<String> questionsReponses = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                questionsReponses.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionsReponses;


    }

    public static void ecrireFichier(String nomFichier, String chaineAEcrire) {
        //{}=>{la chaîne  chaineAEcrire est écrite après saut de ligne à la suite du fichier nomFichier}
        // true = mode append ? écrit à la suite sans effacer ce qui existe
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.newLine();
            writer.write(chaineAEcrire);// ajoute un retour à la ligne
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static private ArrayList<String> decoupeEnMots(String contenu) {
        //{}=>{résultat = le vecteur des mots de la chaîne contenu après pré-traitements divers}
        String chaine = contenu.toLowerCase();
        chaine = chaine.replace('\n', ' ');
        chaine = chaine.replace('?', ' ');
        chaine = chaine.replace('-', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('.', ' ');
        chaine = chaine.replace(',', ' ');
        chaine = chaine.replace(':', ' ');
        chaine = chaine.replace(';', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('"', ' ');
        chaine = chaine.replace('', ' ');
        chaine = chaine.replace('’', ' ');
        chaine = chaine.replace("'", " ");
        chaine = chaine.replace('(', ' ');
        chaine = chaine.replace(')', ' ');
        chaine = chaine.replace('»', ' ');
        chaine = chaine.replace('«', ' ');
        chaine = chaine.replace('-', ' ');


        String[] tabchaine = chaine.split(" ");
        ArrayList<String> resultat = new ArrayList<>();

        for (int i = 0; i < tabchaine.length; ++i) {
            if (!tabchaine[i].equals("")) {
                resultat.add(tabchaine[i]);
            }
        }

        return resultat;
    }


    // cherche un mot dans une liste en regardant chaque élément l'un après l'autre
    static private boolean existeChaine(ArrayList<String> mots, String mot) {
        //{}=>  {recherche séquentielle de mot dans mots
        // résultat =  true si trouvé et false sinon }
        int i = 0;
        boolean trouve = false;

        // tant que pas tt parcouru et mot pas trouvé
        while (i < mots.size() && !mots.get(i).equals(mot)) {
            i++;
        }

        // si i < taille et mot correspond => trouve = true
        if (i < mots.size() && mots.get(i).equals(mot)){
            trouve = true;
        };

        return trouve;
    }

    // cherche un mot dans une liste triée
    static private boolean existeChaineDicho(ArrayList<String> lesChaines, String chaine) {
        //{lesChaines (triée dans l'ordre lexicographique)}=>  {recherche dichotomique de chaine dans lesChaines
        // résultat =  true si trouvé et false sinon }
        int debut = 0;
        int fin = lesChaines.size() - 1;
        int milieu, comp;
        boolean trouve = false;

        do {
            milieu = (debut + fin) / 2;
            comp = lesChaines.get(milieu).compareTo(chaine);

            // si pos milieu dans lesChaines = chaine => chaine trouvé = stop
            if (comp == 0) {
                trouve = true;
            }
            // sinon si pos milieu dans lesChaines < chaine => milieu + 1
            else if (comp < 0) {
                debut = milieu + 1;
            }
            // sinon pos milieu dans lesChaines > chaine
            else {
                fin = milieu - 1;
            }
        } while (debut <= fin && !trouve);

        return trouve;
    }

    // vérifie si tout les mots importants d'une question se retrouvent bien dans une liste 
    static public boolean entierementInclus(ArrayList<String> mots, String question, Thesaurus thesaurus) {
        //{mots est trié dans l'ordre lexicographique}=>
        // résultat = true si tous les mots de questions sont dans mots, false sinon
        // remarque : utilise decoupeEnMots et existeChaineDicho}
        ArrayList<String> motQuestion = decoupeEnMots(question);
        boolean result = true;

        // parcourt chaque mot brut de la question
        for (String motBrut : motQuestion) {
            // transforme motBrut en un mot canonique grace a thesaurus
            String mot = thesaurus.rechercherSortiePourEntree(motBrut);

            // si mot pas dans la liste mots
            if (!existeChaineDicho(mots, mot)) {
                result = false;
            }
        }

        return result;
    }

    // trouve la position ( indice ) d'un mot dans une liste ou renvoie -1 s'il n'y est pas
    static private int rechercherChaine(ArrayList<String> lesChaines, String chaine) {
        // {}=>{résultat = l'indice de chaine dans lesChaines si trouvé et -1 sinon }
        if (lesChaines.isEmpty()) {
            return -1;
        }

        int i = 0;
        int result;

        // tant que pas tt parcouru et mot pas trouvé
        while (i < lesChaines.size() && !lesChaines.get(i).equals(chaine)) {
            i++;
        }

        // si i < taille et mot correspond => result = pos i
        if (i < lesChaines.size() && lesChaines.get(i).equals(chaine)) {
            result = i;
        }
        else {
            result = -1;
        }

        return result;
    }

    // enregistre un nouveau question-réponse et met à jour l'index des formes
    static public void integrerNouvelleQuestionReponse(String question, String reponse, ArrayList<String> formes, Index indexFormes, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{la forme de reponse n'existe pas ou n'est pas associée à question dans indexFormes}=>{la forme de reponse est ajoutée à la fin de formes si elle n'y est pas déjà
        // et indexFormes est mis à jour pour tenir compte de cette nouvelle question-réponse
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
        // remarque 2 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        String formeRep = calculForme(reponse, motsOutils, thesaurus); // calcul forme de la reponse
        int idForme = rechercherChaine(formes, formeRep); // chercher id de la forme dans formes

        // si forme pas trouvé
        if (idForme == -1) {
            formes.add(formeRep); // ajout nouvelle forme
            idForme = formes.size() - 1; // update idForme => dernier pos
        }

        ArrayList<String> motsQuestion = decoupeEnMots(question);
        int nbMotsOutils = 0;

        // parcourt motsQuestion tant que nbMotsOutils < NBMOTS_FORME ( nombre max de mots-outils )
        for (int i = 0; i < motsQuestion.size() && nbMotsOutils < NBMOTS_FORME; i++) {
            String mot = motsQuestion.get(i);
            String motCanon;

            // si mot = chiffre => motCanon = "NUM"
            if (estUnNombre(mot)) {
                motCanon = "NUM";
            }
            // sinon on cherche mot normalise dans thesaurus
            else {
                motCanon = thesaurus.rechercherSortiePourEntree(mot);
            }

            // si mot est un mot outil
            if (existeChaineDicho(motsOutils, motCanon)) {
                indexFormes.ajouterSortieAEntree(motCanon + "_" + nbMotsOutils, idForme);

                nbMotsOutils++;
            }
        }
    }

    // ajoute une nouvelle réponse à la base
    static public void IntegrerNouvelleReponse(String reponse, ArrayList<String> reponses, Index indexContenu, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{reponse n'est pas présent dans reponses}=>{reponse est ajoutée à la fin de reponses et indexContenu est mis à jour pour tenir compte de cette nouvelle réponse
        // remarque : utilise decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree

        // si la reponse existe pas dans index
        if (!reponseExiste(reponse, indexContenu, reponses, motsOutils, thesaurus)) {
            reponses.add(reponse); // ajouter reponse a la liste
            int idReponse = reponses.size() - 1; // idReponse = derniere pos

            ArrayList<String> mots = decoupeEnMots(reponse);
            ArrayList<String> dejaTraite = new ArrayList<>();

            // parcourt chaque mot de la reponse
            for (String motBrut : mots) {
                String motCanon = thesaurus.rechercherSortiePourEntree(motBrut);

                // si c pas un mot outil et pas encore traiter
                if (!existeChaineDicho(motsOutils, motCanon) && !existeChaine(dejaTraite, motCanon)) {
                    indexContenu.ajouterSortieAEntree(motCanon, idReponse); // lie mot thematique a l'id
                    dejaTraite.add(motCanon); // mettre mot comme traité
                }
            }
        }
    }

    // crée l'index qui relie chaque mot thématique aux réponses qui le contiennent
    static public Index constructionIndexReponses(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les mots des réponses (reponses) absents de motsOutils.
        // et les sorties sont les indices (dans reponses) des réponses les contenant.
        // remarque : utilise existeChaineDicho, decoupeEnMots et ajouterSortieAEntree }
        Index index = new Index();

        // parcout tte les reponses
        for (int i = 0; i < reponses.size(); i++) {
            ArrayList<String> mots = decoupeEnMots(reponses.get(i));
            ArrayList<String> dejaVu = new ArrayList<>();

            // parcourt chaque mot de reponses
            for (String motBrut : mots) {
                String mot = thesaurus.rechercherSortiePourEntree(motBrut);

                // si c pas un mot outil
                if (!existeChaineDicho(motsOutils, mot) && !existeChaine(dejaVu, mot)) {
                    dejaVu.add(mot); // ajout mot a dejaVu => eviter doublons
                    index.ajouterSortieAEntree(mot, i); // lier mot a l'id reponse i dans index
                }
            }
        }

        return index;
    }

    // range une liste de mots par ordre lexicographique
    static void trierChaines(ArrayList<String> v) {
        //{}=>{v est trié dans l'ordre lexicographique }

        // parcour les mots dans v
        for (int i = 0; i < v.size() - 1; i++) {

            // boucle pour comparer mot i avec  les j suivants
            for (int j = i + 1; j < v.size(); j++) {

                // si mot i > mot j => permutation
                if (v.get(i).compareTo(v.get(j)) > 0) {
                    String tmp = v.get(i); // save i dans tmp

                    v.set(i, v.get(j)); // remplace i par j
                    v.set(j, tmp); // remplace j par tmp ( old i )
                }
            }
        }
    }

    // sélectionne les réponses qui contiennent le plus grand nombre de mots-clés communs avec la question
    static ArrayList<Integer> maxOccurences(ArrayList<Integer> v, int seuil) {
        //{v trié} => {résultat = vecteur des entiers dont le nombre d'occurences
        // est maximal et au moins égal au seuil. Si le nombre d'occurences maximal est inférieur au seuil , un vecteur vide est retourné.
        // Par exemple, si V est [3,4,5,5,5,6,6,8,8,8,12,16,16,20]
        // si seuil<=3 alors le résultat est [5,8].
        // si le seuil>3 alors le résultat est []}
        ArrayList<Integer> result = new ArrayList<Integer>();

        // si v est vide => return liste vide
        if (v.isEmpty()) {
            return new ArrayList<Integer>();
        }

        int count = 0;
        int current = v.getFirst();

        // parcourt chaque i dans liste v
        for (Integer i : v) {
            // si i = nombre actuel
            if (i == current) {
                count++;

                // si count atteint / depase seuil
                if (count >= seuil) {
                    result.add(i); // add i a result
                }
            }
            else {
                current = i;
                count = 1;
            }
        }

        return result;
    }

    // rassemble 2 listes d'ids triées en une seule liste triée
    static ArrayList<Integer> fusion(ArrayList<Integer> v1, ArrayList<Integer> v2) {
        //{v1 et v2 triés}=>{résultat = vecteur trié fusionnant v1 et v2 sans supprimer les répétitions
        // par exemple si v1 est [4,8,8,10,25] et v2 est [5,8,9,25]
        // le résultat est [4,5,8,8,8,9,10,25,25]}
        ArrayList<Integer> result = new ArrayList<Integer>();

        // parcourt v1 pour ajouter a result
        for (Integer integer : v1) {
            result.add(integer);
        }

        // parcourt v2 pour ajouter a result
        for (Integer integer : v2) {
            result.add(integer);
        }

        int j;
        boolean onAPermute = true;
        int i = 0;

        // debut tri a bulle
        while (onAPermute) {
            j = result.size() - 1;
            onAPermute = false;

            // remonte la liste et compare
            while (j > i) {
                // si element droite < element gauche => inversion
                if (result.get(j) < result.get(j-1)) {
                    int tmp = result.get(j); // save j dans tmp

                    result.set(j, result.get( - 1)); // decale gauche vers la droite
                    result.set(j-1, tmp); // met tmp ( j ) a gauche

                    onAPermute = true; // on a permuter
                }
                j = j - 1;
            }
            i = i + 1;
        }
        return result;
    }

    // extrait le "squelette" d'une phrase en ne gardant que ses 5 premiers mots-outils
    static String calculForme(String chaine, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = la concaténation des NBMOTS_FORME premiers mots-outils de chaine séparés par des blancs
        // remarque 1 : utilise decoupeMots et existeChaineDicho
        // remarque 2 : la limitation de la taille des formes permet d'accepter des réponses terminant par des précisions }
        String resultat = ""; // On initialise une chaîne vide
        ArrayList<String> mots = decoupeEnMots(chaine);
        int nbMotsTrouves = 0;

        // parcourt mots tant que pas fini et pas encore atteint limite NBMOTS_FORME
        for (int i = 0; i < mots.size() && nbMotsTrouves < NBMOTS_FORME; i++) {
            String mot = mots.get(i);
            String motCanon;

            // si mot = chiffre => motCanon = "NUM"
            if (estUnNombre(mot)) {
                motCanon = "NUM";
            }
            // sinon on cherche mot normalise dans thesaurus
            else {
                motCanon = thesaurus.rechercherSortiePourEntree(mot);
            }

            // si motCanon est dans liste motOutil
            if (existeChaineDicho(motsOutils, motCanon)) {
                resultat = resultat + motCanon + " ";

                nbMotsTrouves++;
            }
        }

        return resultat;
    }

    // génère la liste de tout les squelettes sans répétition
    static public ArrayList<String> constructionTableFormes(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = le vecteur de toutes les formes de réponses dans reponses.
        // remarque : utilise calculForme et existeChaine }
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < reponses.size(); i++) {
            // calcul la forme de la reponse i
            String forme = calculForme(reponses.get(i), motsOutils, thesaurus);

            // si forme est pas la la liste result
            if (!existeChaine(result, forme)) {
                result.add(forme); // on l'ajoute
            }
        }

        return result;
    }

    // crée l'index qui lie les mots outils positionnés (exemple: qui_0) aux structures de réponses correspondantes
    static public Index constructionIndexFormes(ArrayList<String> questionsReponses, ArrayList<String> formes, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les "mots-outils positionnés" des questions (par exemple l'entrée pour un "Qui" en première position sera "qui_0")
        // et les sorties sont les indices (dans formes) des formes de réponses répondant aux questions contenant le mot-outil à cette position.
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots, rechercherEntree, existeChaineDicho et ajouterSortieAEntree
        // remarque 2 : utilisez les méthodes indexOf et substring de String pour décomposer la question-réponse en question et réponse
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        Index index = new Index(); // index pour forme

        // parcour fichier question-reponse
        for (String qr : questionsReponses) {
            int pos = qr.indexOf('?'); // chercher pos "?"

            String quest = qr.substring(0, pos + 1); // partie question
            String rep = qr.substring(pos + 1); // partie reponse

            String forme = calculForme(rep, motsOutils, thesaurus); // calcul forme de la reponse
            int idForme = rechercherChaine(formes, forme); // chercher id de la forme dans formes

            ArrayList<String> motsQuestion = decoupeEnMots(quest);

            int nb = 0;
            // parcourt les mots de la question tant que limite NBMOTS_FORME pas atteint
            for (int j = 0; j < motsQuestion.size() && nb < NBMOTS_FORME; j++) {
                String mot = motsQuestion.get(j);

                // si c'est un chiffre => mot devient "NUM"
                if (estUnNombre(mot)) {
                    mot = "NUM";
                } else {
                    // sinon cherche version canonique dans thesaurus
                    mot = thesaurus.rechercherSortiePourEntree(mot);
                }

                // si mot outil
                if (existeChaineDicho(motsOutils, mot)) {
                    String entree = mot + "_" + nb; // cree "mot_position" (exemple: qui_0)
                    index.ajouterSortieAEntree(entree, idForme); // lie "mot_position" a idForme

                    nb++;
                }
            }
        }

        return index;
    }

    // filtre les réponses du bon thème pour ne garder que celles qui ont une structure grammaticale logique
    static public ArrayList<Integer> constructionReponsesCandidates(String question, Index IndexReponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = vecteur des identifiants de réponses contenant l'ensemble des mots non outils de la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion et maxOccurences
        // remarque 2 : maxOccurences est appelé en passant le nombre de mots non outils de la question comme valeur de seuil.
        // remarque 3 : on aurait pu calculer directement une intersection au lieu d'une fusion et se passer de maxOccurences mais on
        // souhaite pouvoir garder la possibilité d'assouplir par la suite la contrainte sur la présence de l'intégralité
        // des mots de la question dans la réponse }
        ArrayList<String> motsQuestion = decoupeEnMots(question);
        ArrayList<Integer> fusionnee = new ArrayList<>();
        int nbMotsNonOutils = 0;

        // parcourt chaque mot s de motsQuestion
        for (String s : motsQuestion) {
            String motCanon = thesaurus.rechercherSortiePourEntree(s);

            // si pas mot outil
            if (!existeChaineDicho(motsOutils, motCanon)) {
                ArrayList<Integer> sorties = IndexReponses.rechercherSorties(motCanon); // recupere ids des reponse contenant mot
                fusionnee = fusion(fusionnee, sorties); // fusionne ids
                nbMotsNonOutils++;
            }
        }

        return maxOccurences(fusionnee, nbMotsNonOutils);
    }

    // vérifie si un mot n'est composé que de chiffres (de 0 à 9)
    static public boolean estUnNombre(String s) {
        //{s est non vide}=>{résultat = true si s ne contient que des caractères représentant des chiffres (>='0'&<='9') et false sinon}
        if (s.isEmpty()) {
            return false;
        }
        boolean result = true;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // si c pas entre "0" et "9" = pas un chiffre
            if (c < '0' || c > '9') {
                result = false;
                break;
            }
        }
        return result;
    }

    // filtre les réponses du bon thème pour ne garder que celles qui ont une structure grammaticale logique
    static public ArrayList<Integer> selectionReponsesCandidates(String question,
                                                                 ArrayList<Integer> candidates,
                                                                 Index IndexFormes,
                                                                 ArrayList<String> reponses,
                                                                 ArrayList<String> formesReponses,
                                                                 ArrayList<String> motsOutils,
                                                                 Thesaurus thesaurus) {
        //{}=>{résultat = vecteur des identifiants de réponses (parmi les candidates) dont la forme est cohérente
        // avec la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : l'algorithme procède en 2 temps. D'abord il trouve les formes de réponses qui répondent à la question.
        // puis ajoute au résultat l'identifiant des réponses candidates qui respectent au moins une de ces formes.
        // remarque 3 : pour trouver les formes de réponses qui répondent à la question, on utilise l'index des formes, et on sélectionne
        // en appelant maxOccurences (avec seuil = nombre des mots-outils de la question) celles associées dans l'index à tous les mots-outils de la question.
        // remarque 4 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        ArrayList<String> mots = decoupeEnMots(question);
        ArrayList<Integer> fusionnee = new ArrayList<>();
        int nbMotsOutilsQuestion = 0;

        // parcourt chaque mot de la question tant que limite NBMOTS_FORME pas atteinte
        for (int i = 0; i < mots.size() && nbMotsOutilsQuestion < NBMOTS_FORME; i++) {
            String mot = mots.get(i);
            String motCanon;

            // si mot = chiffre => motCanon = "NUM"
            if (estUnNombre(mot)) {
                motCanon = "NUM";
            }
            // sinon on cherche mot normalise dans thesaurus
            else {
                motCanon = thesaurus.rechercherSortiePourEntree(mot);
            }

            // si mot outil
            if (existeChaineDicho(motsOutils, motCanon)) {
                String entree = motCanon + "_" + nbMotsOutilsQuestion; // cree "mot_position" (exemple: qui_0)
                ArrayList<Integer> sorties = IndexFormes.rechercherSorties(entree); //  recupere ids des formes possible pour ce mot a cette position
                fusionnee = fusion(fusionnee, sorties); // fusion ids

                nbMotsOutilsQuestion++;
            }
        }

        // chercher les formes qui sont = aux mots outils de la question
        ArrayList<Integer> formesCandidates = maxOccurences(fusionnee, nbMotsOutilsQuestion);
        ArrayList<Integer> selection = new ArrayList<>();

        // parcourt chaque reponse candidate trouve
        for (int idRep : candidates) {
            String formeRep = calculForme(reponses.get(idRep), motsOutils, thesaurus); // caclcul la forme de la reponse candidate
            int idForme = rechercherChaine(formesReponses, formeRep); // chercher id de forme formeRep

            int j = 0;
            // cherche si id de la forme est dans les formes candidates
            while (j < formesCandidates.size() && !formesCandidates.get(j).equals(idForme)) {
                j++;
            }

            // si trouve
            if (j < formesCandidates.size()) {
                selection.add(idRep); // ajouter reponse a la selection
            }
        }

        return selection;
    }

    // vérifie via l'index si une phrase de réponse est déjà enregistrée pour éviter les doublons
    static public boolean reponseExiste(String reponse,
                                        Index indexReponses,
                                        ArrayList<String> reponses,
                                        ArrayList<String> motsOutils,
                                        Thesaurus thesaurus) {
        //{}=>{résultat = true si la reponse est présente dans reponses et false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences
        // remarque 2 : Le vecteur reponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, on utilise indexReponses pour trouver les réponses contenant tous les mots non outils de la
        // reponse, puis on vérifie si l'une d'entre elle est identique à reponse.}
        ArrayList<String> motsRep = decoupeEnMots(reponse);

        int nbNonOutils = 0;
        ArrayList<Integer> fusion = new ArrayList<>();

        // parcourt chaque motBrut de reponse
        for (String motBrut : motsRep) {
            String mot;

            // si motBrut = chiffre => mot = "NUM"
            if (estUnNombre(motBrut)) {
                mot = "NUM";
            }
            // sinon on cherche motBrut normalise dans thesaurus
            else {
                mot = thesaurus.rechercherSortiePourEntree(motBrut);
            }

            // si pas mot outil
            if (!existeChaineDicho(motsOutils, mot)) {
                ArrayList<Integer> sorties = indexReponses.rechercherSorties(mot); // recupere ids des phrases contenant mot
                fusion = fusion(fusion, sorties); // fusion des ids

                nbNonOutils++;
            }
        }

        // si aucun mot important trouvé
        if (nbNonOutils == 0) {
            // parcourt reponse
            for (String r : reponses) {
                // si correspond => return true
                if (r.equals(reponse)) {
                    return true;
                }
            }

            return false;
        }

        // cherche les ids qui contiennent les mots thematiqque
        ArrayList<Integer> candidats = maxOccurences(fusion, nbNonOutils);

        // parcourt candidats trouve
        for (Integer id : candidats) {
            // si id est valide et que phrase correspond => return true
            if (id >= 0 && id < reponses.size() && reponses.get(id).equals(reponse)) {
                return true;
            }
        }

        return false;
    }

    // vérifie si le lien entre une structure de question et une structure de réponse est déjà connu
    static public boolean formeQuestionReponseExiste(String question,
                                                     String reponse,
                                                     Index indexFormes,
                                                     ArrayList<String> formesReponses,
                                                     ArrayList<String> motsOutils,
                                                     Thesaurus thesaurus) {
        //{}=>{résultat = * true si la forme de reponse est présente dans formesReponses
        // et qu'elle est accessible à partir des mots de la question en utilisant indexFormes.
        //                * false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : Le vecteur formesReponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, et afin de vérifier l'accessibilité à partir des mots de la question en utilisant indexFormes,
        // on utilise indexFormes pour trouver les formes indexées par les mots-outils de la
        // question, puis on vérifie si l'une de ces formes est identique à la forme de reponse.
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de question sont pris en compte}
        String formeRep = calculForme(reponse, motsOutils, thesaurus);
        int idFormeRep = rechercherChaine(formesReponses, formeRep);

        // si forme existe pas => return false
        if (idFormeRep == -1) {
            return false;
        }

        ArrayList<String> motsQuestion = decoupeEnMots(question);
        ArrayList<Integer> fusionnee = new ArrayList<>();
        int nbMotsOutils = 0;

        // parcourt chaque mot de motsQuestion tant que limite NBMOTS_FORME pas atteinte
        for (int i = 0; i < motsQuestion.size() && nbMotsOutils < NBMOTS_FORME; i++) {
            String mot = motsQuestion.get(i);
            String motCanon;

            // si mot = chiffre => motCanon = "NUM"
            if (estUnNombre(mot)) {
                motCanon = "NUM";
            }
            // sinon on cherche mot normalise dans thesaurus
            else {
                motCanon = thesaurus.rechercherSortiePourEntree(mot);
            }

            // si mot outil
            if (existeChaineDicho(motsOutils, motCanon)) {
                // recupere id des formes lies a "mot_position"
                ArrayList<Integer> sorties = indexFormes.rechercherSorties(motCanon + "_" + nbMotsOutils);
                fusionnee = fusion(fusionnee, sorties);

                nbMotsOutils++;
            }
        }

        // trouve formes qui correspondent aux  mots outils de question
        ArrayList<Integer> formesCandidates = maxOccurences(fusionnee, nbMotsOutils);

        // parcourt chaque forme candidate trouve
        for (Integer id : formesCandidates) {
            // si id correspond => return true
            if (id.equals(idFormeRep)) {
                return true;
            }
        }

        return false;
    }
}
