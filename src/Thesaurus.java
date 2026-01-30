import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Thesaurus {

    private class EntreeSortie implements Comparable<EntreeSortie> {
        private String entree; // un mot
        private String sortie; // sa forme canonique

        public EntreeSortie(String entree, String sortie) {
            this.entree = entree;
            this.sortie = sortie;
        }

        public int compareTo(EntreeSortie o) {
            return this.entree.compareTo(o.entree);
        }
    }

    private ArrayList<EntreeSortie> table;

    public Thesaurus(String nomFichier) {
        //{}=>{ constructeur créant et initialisant l'attribut table à partir du contenu du fichier dont le nom est passé en paramètre, puis triant la table
        // en utilisant la méthode compareTo d'EntreeSortie
        // remarque 1 : utilise ajouterEntreeSortie et trierEntreesSorties
        // remarque 2 : pour la lecture du fichier, inspirez-vous de lireMotsOutils de Utilitaire
        // remarque 3 : pour les traitements de la chaîne lue, utilisez les méthodes indexOf,substring de String
        table = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int pos = ligne.indexOf(":"); // separation entre forme canonique et forme valide

                String entree = ligne.substring(0, pos);
                String sortie = ligne.substring(pos + 1);

                if (!entree.isEmpty() && !sortie.isEmpty()) {
                    ajouterEntreeSortie(entree, sortie);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        trierEntreesSorties(table);
    }

    public void ajouterEntreeSortie(String entree, String sortie) {
        //{}=>{ajoute à la fin de la table une nouvelle EntreeSortie avec les attributs entree et sortie}
        EntreeSortie e = new EntreeSortie(entree, sortie);

        table.add(e);
    }


    public String rechercherSortiePourEntree(String entree) {
        // {l'attribut table du thesaurus est trié sur l'attribut entree des Entree-Sortie}=>
        // {résultat = la forme canonique associée à entree dans le thésaurus si l'entrée entree existe,
        // entree elle-même si elle n'existe pas. La recherche doit être dichotomique.
        // remarque : utilise compareTo de EntreeSortie }
        int inf = 0;
        int sup = table.size() - 1;
        int m,comp;
        boolean trouve = false;

        do {
            m = (inf + sup) / 2;
            comp = entree.compareTo(table.get(m).entree);

            // si pos entre = pos milieu
            if (comp == 0) {
                trouve = true;
            }
            // sinon si entre > milieu
            else if (comp > 0) {
                inf = m + 1;
            }
            // sinon si entre < milieu
            else {
                sup = m - 1;
            }
        } while (inf <= sup && !trouve);

        // si trouve
        if (trouve) {
            return table.get(m).sortie; // return sortie dans table a pos m
        }
        // si pas trouve
        else {
            return entree; // sinon return entree
        }
    }

    static void trierEntreesSorties(ArrayList<EntreeSortie> v) {
        //{} => {trie v sur la base de la méthode compareTo de EntreeSortie}
        for (int i = 0; i < v.size() - 1; i++) {
            EntreeSortie tmp = v.get(i);
            int j = i - 1;

            while (j >= 0 && v.get(j).compareTo(tmp) > 0) {
                v.set(j + 1, v.get(j));

                j--;
            }
            v.set(j + 1, tmp);
        }
    }

}