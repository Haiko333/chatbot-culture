import java.util.ArrayList;

public class Index {

    // un index est un vecteur d'EntreeIndex
    private class EntreeIndex {

        // une EntreeIndex associe un vecteur trié d'entiers (sorties) à une String (entree)
        private String entree;
        private ArrayList<Integer> sorties;

        public ArrayList<Integer> getSorties() {
            return sorties;
        }

        //constructeur
        public EntreeIndex(String entree) {
            this.entree = entree;
            sorties = new ArrayList<>();
        }

        public int rechercherSortie(Integer sortie) {
            // pré-requis : sorties.size() > 0, sortie > sorties[0]
            // {}=>{recherche dichotomique de sortie dans sorties (triée dans l'ordre croissant)
            // résultat = l'indice de sortie dans sorties si trouvé, - l'indice d'insertion si non trouvé }
            int debut = 0;
            int fin = sorties.size() - 1;
            int milieu,comp;
            boolean trouve = false;

            do {
                milieu = (debut + fin) / 2;
                comp = sorties.get(milieu).compareTo(sortie);

                // si milieu = sortie => pos trouvé = stop
                if (comp == 0) {
                    trouve=true;
                }
                // sinon si milieu < sortie => milieu + 1
                else if (comp < 0) {
                    debut = milieu + 1;
                // si milieu > sortie => milieu - 1
                } else {
                    fin = milieu - 1;
                }
            } while (debut <= fin && !trouve);

            // si trouve alors return milieu
            if (trouve) {
                return milieu;
            }
            // sinon return milieu négatif
            else {
                return -milieu;
            }
        }

        public void ajouterSortie(Integer sortie) {
            //{}=>{insère sortie à la bonne place dans sorties (triée dans l'ordre croissant)
            // remarque : utilise rechercherSortie de EntreeIndex }

            // si sorties non vide et sortie < que 1er element de sorties
            if (sorties.isEmpty() || sortie.compareTo(sorties.getFirst()) <= 0) {
                sorties.addFirst(sortie); // ajouter sortie a sorties a 1ere position
            }
            // sinon si sortie > que dernier element de sorties
            else if (sortie.compareTo(sorties.getLast()) >=0) {
                sorties.addLast(sortie); // ajouter sortie a sorties a derniere position
            }
            // sinon tt ce qui concerne pas autre condition
            else {
                int indice = rechercherSortie(sortie); // recherche place
                // si trouvé place alors ajouté
                if (indice < 0) {
                    // indice = -milieu donc -(-milieu) = +milieu
                    sorties.add(-indice, sortie);
                }
            }
        }


        @Override
        public String toString() {
            return entree + "=>" + sorties;
        }
    }

    //Un vecteur d'EntreeIndex trié sur l'attribut entree (String) des EntreeIndex
    private ArrayList<EntreeIndex> table;

    //constructeur
    public Index() {
        table = new ArrayList<>();
    }

    public int rechercherEntree(String entree) {
        // pré-requis : table.size() > 0, entree > table[0]
        // {}=>  {recherche dichotomique de entree dans table (triée dans l'ordre lexicographique des attributs entree des EntreeIndex) }
        // résultat =  l'indice de entree dans table si trouvé et -l'indice d'insertion sinon }
        int debut = 0;
        int fin = table.size() - 1;
        int milieu = 0,comp;
        boolean trouve = false;

        while (debut <= fin && !trouve) {
            milieu = (debut + fin) / 2;
            comp = table.get(milieu).entree.compareTo(entree);

            // si milieu = entree => pos trouvé = stop
            if (comp == 0) {
                trouve = true;
            }
            // sinon si milieu < entree => milieu + 1
            else if (table.get(milieu).entree.compareTo(entree) < 0) {
                debut = milieu + 1;
            }
            // sinon milieu - 1
            else {
                fin = milieu - 1;
            }
        };

        // si trouve alors return milieu
        if (trouve) {
            return milieu;
        }
        // sinon return milieu négatif
        else {
            return -debut - 1;
        }
    }

    public void ajouterSortieAEntree(String entree, Integer sortie) {
        // {}=>{ajoute l'entier sortie dans les sorties associées à l'entrée entree
        // si l'entrée entree n'existe pas elle est créée.
        // ne fait rien si sortie était déjà présente dans ses sorties.
        // remarque : utilise la fonction rechercherEntree de Index et la procedure ajouterSortie de EntreeIndex}
        int e = rechercherEntree(entree);
        EntreeIndex index;

        // si entree >= 0
        if (e >= 0) {
            index = table.get(e); // initier index a pos e dans table
        }
        else {
            int insertion = -e - 1;
            index = new EntreeIndex(entree); // initier index avec new entree
            table.add(insertion, index); // ajouter new index a table  a pos insertion
        }

        // ajouter sortie a index
        index.ajouterSortie(sortie);
    }

    public ArrayList<Integer> rechercherSorties(String entree) {
        // {}=>{résultat = les sorties associées à l'entrée entree
        // si l'entrée entree n'existe pas, une ArrayList vide est retournée.
        // remarque : utilise la fonction rechercherEntree de Index}
        int r = rechercherEntree(entree);

        // si recherche positif
        if (r >= 0) {
            return table.get(r).getSorties(); // return la liste des sorties a position r dans table
        }
        else {
            return new ArrayList<Integer>(); // sinon return liste vide
        }
    }

    public void afficher() {
        // {}=>{affiche la table de l'index}
        for (int i = 0; i < table.size(); i++) {
            System.out.println(this.table.get(i));
        }
    }
}