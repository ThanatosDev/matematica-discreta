import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {
  static final String[] NOMS = {};







  /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
   */
  static class Tema1 {
    /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
     */
    static final char CONJ = '∧';
    static final char DISJ = '∨';
    static final char IMPL = '→';
    static final char NAND = '.';

    static int exercici1(char[] ops, int[] vars)
    {
      int numVars = Arrays.stream(vars).max().getAsInt() + 1;

      int numCombinacions = 1 << numVars;

      boolean esTautologia = true;
      boolean esContradiccio = true;

      for (int j = 0; j < numCombinacions; j++)
      {
        boolean[] valors = new boolean[numVars];

        for (int i = 0; i < numVars; i++)
        {
          valors[i] = ((j >> i) & 1) == 1;
        }

        boolean resultat = valors[vars[0]];

        for (int i = 0; i < ops.length; i++)
        {
          boolean next = valors[vars[i + 1]];
          resultat = aplicarOp(resultat, next, ops[i]);
        }

        if (resultat)
        {
          esContradiccio = false;
        }
        else
        {
          esTautologia = false;
        }

      }

      if (esTautologia) return 1;
      if (esContradiccio) return 0;
      return -1;
    }

    public static boolean aplicarOp(boolean a, boolean b, char op)
    {
      switch (op)
      {
        case CONJ: return a && b;
        case DISJ: return a || b;
        case IMPL: return !a || b;
        case NAND: return !(a && b);
        default:   throw new IllegalArgumentException("Operador desconegut: "+op);
      }
    }

    /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
     */
    static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q)
    {
      boolean totP = true;

      for (int x : universe)
      {
        if (!p.test(x))
        {
          totP = false;
          break;
        }
      }

      int contadorQ = 0;
      for (int x : universe)
      {
        if (q.test(x))
        {
          contadorQ++;
        }
      }

      boolean existeExactamenteUnaQ = contadorQ == 1;
      return totP == existeExactamenteUnaQ;
    }

    static void tests() {
      // Exercici 1
      // Taules de veritat

      // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
      test(1, 1, 1, () -> exercici1(new char[] { IMPL, DISJ, DISJ }, new int[] { 0, 2, 1, 0 }) == 1);

      // Contradicció: (p0 . p0) ∧ p0
      test(1, 1, 2, () -> exercici1(new char[] { NAND, CONJ }, new int[] { 0, 0, 0 }) == 0);

      // Exercici 2
      // Equivalència

      test(1, 2, 1, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x == 0, (x) -> x == 0);
      });

      test(1, 2, 2, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x >= 1, (x) -> x % 2 == 0);
      });
    }
  }
















  /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
   */
  static class Tema2 {
    /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
     */
    static int exercici1(int[] a)
    {
      int n = a.length;

      // crear tabla para los números de Stirling de segundo tipo
      int [][] stirling = new int[n+1][n+1];

      // caso base
      stirling[0][0] = 1;

      // ampliar la tabla con recurrencia
      for (int i = 1; i <= n; i++)
      {
        for (int j = 1; j <= i; j++)
        {
          stirling[i][j] = j*stirling[i-1][j] + stirling[i-1][j-1];
        }
      }

      // calcular número de Bell B(n)
      int bell = 0;
      for (int k = 0; k <= n; k++)
      {
        bell += stirling[n][k];
      }

      return bell;
    }

    /*
     * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
     * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
     * antisimètrica.
     *
     * Si no existeix, retornau -1.
     */
    static int exercici2(int[] a, int[][] rel)
    {
      int n = a.length;
      boolean[][] mat = new boolean[n][n];

      // Afegim parelles inicials
      for (int[] pair : rel)
      {
        int x = Arrays.binarySearch(a, pair[0]);
        int y = Arrays.binarySearch(a, pair[1]);

        if (x >= 0 && y >= 0)
        {
          mat[x][y] = true;
        }
      }

      // Reflexivitat
      for (int i = 0; i < n; i++)
      {
        mat[i][i] = true;
      }

      // Clausura transitiva (Floyd-Warshall)
      for (int k = 0; k < n; k++)
      {
        for (int i = 0; i < n; i++)
        {
          for (int j = 0; j < n; j++)
          {
            if (mat[i][k] && mat[k][j])
            {
              mat[i][j] = true;
            }
          }
        }
      }

      // Comprovació d'antisimetria
      for (int i = 0; i < n; i++)
      {
        for (int j = 0; j < n; j++)
        {
          if (i != j && mat[i][j] && mat[j][i])
          {
            return -1;
          }
        }
      }

      // Comptar cardinal de la relació final
      int contador = 0;
      for (int i = 0; i < n; i++)
      {
        for (int j = 0; j < n; j++)
        {
          if (mat[i][j]) contador++;
        }
      }

      return contador;
    }

    /*
     * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
     * retornau:
     * - L'ínfim de `x` si existeix i `op` és false
     * - El suprem de `x` si existeix i `op` és true
     * - null en qualsevol altre cas
     */
    static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op)
    {
      int n = a.length;
      boolean[][] mat = new boolean[n][n];

      // Construir matriu de relació
      for (int[] pair : rel)
      {
        int i = Arrays.binarySearch(a, pair[0]);
        int j = Arrays.binarySearch(a, pair[1]);

        if (i >= 0 && j >= 0)
        {
          mat[i][j] = true;
        }
      }

      // Clausura transitiva + reflexiva
      for (int i = 0; i < n; i++) mat[i][i] = true;

      for (int k = 0; k < n; k++)
      {
        for (int i = 0; i < n; i++)
        {
          for (int j = 0; j < n; j++)
          {
            if (mat[i][k] && mat[k][j]) mat[i][j] = true;
          }
        }
      }

      // Marcar índexs dels elements de x
      List<Integer> xIdx = new ArrayList<>();

      for (int xi : x)
      {
        int idx = Arrays.binarySearch(a, xi);
        if (idx >= 0) xIdx.add(idx);
      }

      List<Integer> candidats = new ArrayList<>();

      for (int i = 0; i < n; i++)
      {
        boolean valid = true;
        for (int xi : xIdx)
        {
          if (op) // SUPREM: a[xi] <= a[i]
          {
            if (!mat[xi][i])
            {
              valid = false;
              break;
            }

          }
          else // ÍNFIM: a[i] <= a[xi]
          {
            if (!mat[i][xi])
            {
              valid = false;
              break;
            }
          }
        }

        if (valid) candidats.add(i);
      }

      if (candidats.isEmpty()) return null;

      // Trobar el mínim (suprem) o màxim (ínfim) entre els candidats
      int millor = candidats.get(0);

      for (int i : candidats)
      {
        if (op)
        { // Suprem → volem el més petit
          if (mat[i][millor] && i != millor) millor = i;
        }
        else
        { // Ínfim → volem el més gran
          if (mat[millor][i] && i != millor) millor = i;
        }
      }

      return a[millor];
    }

    /*
     * Donada una funció `f` de `a` a `b`, retornau:
     *  - El graf de la seva inversa (si existeix)
     *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
     *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
     *  - Sinó, null.
     */
    static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f)
    {
      int n = a.length;
      int[][] graf = new int[n][2];  // Per guardar parells (f(x), x)
      int[] imatges = new int[n];    // Per guardar f(x)
      int imatgesCount = 0;
      boolean injectiva = true;

      // Construim graf i comprovam injectivitat
      for (int i = 0; i < n; i++)
      {
        int x = a[i];
        int y = f.apply(x);

        // Comprovar si y ja s'ha vist abans → no injectiva
        for (int j = 0; j < imatgesCount; j++)
        {
          if (imatges[j] == y)
          {
            injectiva = false;
            break;
          }
        }

        imatges[imatgesCount++] = y;
        graf[i][0] = y;
        graf[i][1] = x;
      }

      // Comprovar surjectivitat: cada b[i] ha d'aparèixer a imatges[]
      boolean surjectiva = true;

      for (int i = 0; i < b.length; i++)
      {
        boolean trobat = false;

        for (int j = 0; j < imatgesCount; j++)
        {
          if (b[i] == imatges[j])
          {
            trobat = true;
            break;
          }
        }


        if (!trobat)
        {
          surjectiva = false;
          break;
        }
      }

      if (injectiva && surjectiva)
      {
        // Inversa completa → retornem graf ordenat
        ordenarGraf(graf);
        return graf;
      }
      else if (injectiva)
      {
        ordenarGraf(graf);
        return graf;
      }
      else if (surjectiva)
      {
        // Inversa per la dreta: cercam un x tal que f(x) = y per cada y ∈ b
        int[][] resultat = new int[b.length][2];
        int count = 0;

        for (int i = 0; i < b.length; i++)
        {
          int y = b[i];

          for (int j = 0; j < a.length; j++)
          {
            int x = a[j];

            if (f.apply(x) == y)
            {
              resultat[count][0] = y;
              resultat[count][1] = x;
              count++;
              break;
            }
          }
        }

        ordenarGraf(resultat);
        return resultat;
      }

      // Cap inversa possible
      return null;
    }

    static void ordenarGraf(int[][] graf)
    {
      for (int i = 0; i < graf.length - 1; i++)
      {
        for (int j = i + 1; j < graf.length; j++)
        {
          if  (graf[i][0] > graf[j][0] ||
                  (graf[i][0] == graf[j][0] && graf[i][1] > graf[j][1]))
          {
            int tmp0 = graf[i][0], tmp1 = graf[i][1];
            graf[i][0] = graf[j][0];
            graf[i][1] = graf[j][1];
            graf[j][0] = tmp0;
            graf[j][1] = tmp1;
          }
        }
      }
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // Nombre de particions

      test(2, 1, 1, () -> exercici1(new int[] { 1 }) == 1);
      test(2, 1, 2, () -> exercici1(new int[] { 1, 2, 3 }) == 5);

      // Exercici 2
      // Clausura d'ordre parcial

      final int[] INT02 = { 0, 1, 2 };

      test(2, 2, 1, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 2} }) == 6);
      test(2, 2, 2, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 0}, {1, 2} }) == -1);

      // Exercici 3
      // Ínfims i suprems

      final int[] INT15 = { 1, 2, 3, 4, 5 };
      final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
      final Integer ONE = 1;

      test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[] { 2, 3 }, false)));
      test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[] { 2, 3 }, true) == null);

      // Exercici 4
      // Inverses

      final int[] INT05 = { 0, 1, 2, 3, 4, 5 };

      test(2, 4, 1, () -> {
        var inv = exercici4(INT05, INT02, (x) -> x/2);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT02.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1]/2 != i)
            return false;
        }

        return true;
      });

      test(2, 4, 2, () -> {
        var inv = exercici4(INT02, INT05, (x) -> x);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT05.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1] != i)
            return false;
        }

        return true;
      });
    }

    /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
     */
    static int[][] lexSorted(int[][] arr) {
      if (arr == null)
        return null;

      var arr2 = Arrays.copyOf(arr, arr.length);
      Arrays.sort(arr2, Arrays::compare);
      return arr2;
    }

    /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
     */
    static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
      var rel = new ArrayList<int[]>();

      for (int a : as) {
        for (int b : bs) {
          if (pred.test(a, b)) {
            rel.add(new int[] { a, b });
          }
        }
      }

      return rel.toArray(new int[][] {});
    }

    // Especialització de generateRel per as = bs
    static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
      return generateRel(as, as, pred);
    }
  }




















  /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
   */
  static class Tema3 {
    /*
     * Determinau si el graf `g` (no dirigit) té cicles.
     */
    static boolean exercici1(int[][] g)
    {
        boolean[] visitat=new boolean[g.length];

        for(int v=0; v<g.length; v++)
        {
          if(!visitat[v])
          {
            if(téCicle(g,v,-1,visitat))
            {
              return true;
            }
          }
        }
        return false;
    }
    static boolean téCicle(int[][] g, int v, int pare, boolean[] visitat)
    {
      visitat[v]=true;
      for(int veí:g[v])
      {
        if(!visitat[veí])
        {
          if(téCicle(g,veí,v,visitat)) return true;
        }
        else if(veí!=pare)
        {
          //si el valor ja ha estat visitat i no és el pare, tenim un cicle
          return true;
        }
      }
      return false;
    }

    /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
     */
    static boolean exercici2(int[][] g1, int[][] g2)
    {
      int n=g1.length;
      if(g2.length!=n) return false;

      int[] perm=new int[n];
      for(int i=0;i<n;i++) perm[i]=i;

      do
      {
        if(isPermutacióIsoforma(g1,g2,perm)) return true;
      }
      while(nextPermutation(perm));

      return false;
    }

    static boolean isPermutacióIsoforma(int[][] g1, int[][] g2, int[] p)
    {
      int n = g1.length;

      for (int i = 0; i < n; i++)
      {
        int[] veinsG1 = new int[g1[p[i]].length];
        for (int j = 0; j < g1[p[i]].length; j++)
        {
          veinsG1[j] = pInverse(p, g1[p[i]][j]);
        }

        int[] veinsG2 = Arrays.copyOf(g2[i], g2[i].length);

        Arrays.sort(veinsG1);
        Arrays.sort(veinsG2);

        if (!Arrays.equals(veinsG1, veinsG2)) return false;
      }

      return true;
    }

    static int pInverse(int[] p, int v)
    {
      for(int i=0;i<p.length;i++)
      {
        if(p[i]==v) return i;
      }
      throw new IllegalArgumentException("Valor no trobat en la permutació");
    }

    // Generador de permutacions lexicográfiqies
    static boolean nextPermutation(int[] a)
    {
      int i=a.length-2;
      while(i>=0 && a[i] >= a[i+1]) i--;
      if(i<0) return false;

      int j=a.length-1;
      while(a[j] <= a[i]) j--;
      int tmp=a[i]; a[i]=a[j]; a[j]=tmp;

      for (int l = i + 1, r = a.length - 1; l < r; l++, r--)
      {
        tmp = a[l]; a[l] = a[r]; a[r] = tmp;
      }
      return true;
    }



    /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
     */
    static int[] exercici3(int[][] g, int r)
    {
      int n = g.length;
      boolean [] visitat=new boolean[n];
      if(téCicle(g,r,-1,visitat)) return null;

      // comprobar si es conexo
      for (boolean v : visitat) if (!v) return null;

      // si es arbol hacer postorden
      List<Integer> postordre=new ArrayList<>();
      Arrays.fill(visitat,false);
      dfsPostordre(g,r,-1,visitat,postordre);

      // convertir a un array
      int[] res=new int[postordre.size()];
      for (int i = 0; i < res.length; i++) res[i] = postordre.get(i);
      return res;
    }
    static void dfsPostordre(int[][] g, int v, int pare, boolean[] visitat, List<Integer> llista)
    {
      visitat[v] = true;
      for (int veí : g[v])
      {
        if (veí != pare)
        {
          dfsPostordre(g, veí, v, visitat, llista);
        }
      }
      llista.add(v);
    }

    /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
     */
    static int exercici4(char[][] mapa)
    {
      int files=mapa.length;
      int columnes=mapa[0].length;
      boolean[][] visitat=new boolean[files][columnes];

      int origenX=-1,origenY=-1;

      // buscar 'O'
      for (int i = 0; i < files; i++)
      {
        for (int j = 0; j < columnes; j++)
        {
          if (mapa[i][j] == 'O')
          {
            origenX=i;
            origenY=j;
            break;
          }
        }
      }
      if (origenX==-1 || origenY==-1) return -1; // No hay origen

      // BFS
      List<int[]> cua = new ArrayList<>();
      cua.add(new int[]{origenX, origenY, 0});
      visitat[origenX][origenY] = true;

      int[] dx = {-1, 1, 0, 0}; // arriba, abajo, izquierda, derecha
      int[] dy = {0, 0, -1, 1};

      int index = 0;
      while (index < cua.size())
      {
        int[] actual = cua.get(index++);
        int x = actual[0], y = actual[1], passos = actual[2];

        if (mapa[x][y] == 'D') return passos;

        for (int d = 0; d < 4; d++)
        {
          int nx = x + dx[d], ny = y + dy[d];

          if (nx >= 0 && ny >= 0 && nx < files && ny < columnes &&
                  !visitat[nx][ny] && mapa[nx][ny] != '#')
          {
            visitat[nx][ny] = true;
            cua.add(new int[]{nx, ny, passos + 1});
          }
        }
      }
      return -1;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {

      final int[][] D2 = { {}, {} };
      final int[][] C3 = { {1, 2}, {0, 2}, {0, 1} };

      final int[][] T1 = { {1, 2}, {0}, {0} };
      final int[][] T2 = { {1}, {0, 2}, {1} };

      // Exercici 1
      // G té cicles?

      test(3, 1, 1, () -> !exercici1(D2));
      test(3, 1, 2, () -> exercici1(C3));

      // Exercici 2
      // Isomorfisme de grafs

      test(3, 2, 1, () -> exercici2(T1, T2));
      test(3, 2, 2, () -> !exercici2(T1, C3));

      // Exercici 3
      // Postordre

      test(3, 3, 1, () -> exercici3(C3, 1) == null);
      test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[] { 1, 2, 0 }));

      // Exercici 4
      // Laberint

      test(3, 4, 1, () -> {
        return -1 == exercici4(new char[][] {
            " #O".toCharArray(),
            "D# ".toCharArray(),
            " # ".toCharArray(),
        });
      });

      test(3, 4, 2, () -> {
        return 8 == exercici4(new char[][] {
            "###D".toCharArray(),
            "O # ".toCharArray(),
            " ## ".toCharArray(),
            "    ".toCharArray(),
        });
      });
    }
  }



















  /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
   */
  static class Tema4 {
    /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
     */
    static int[] exercici1(String msg, int n, int e)
    {
      byte[] bytes = msg.getBytes();
      int[] result = new int[bytes.length / 2];

      for (int i = 0; i < bytes.length; i += 2)
      {
        int a = bytes[i] & 0xFF;
        int b = bytes[i + 1] & 0xFF;
        int m = a * 256 + b;
        result[i / 2] = modPow(m, e, n);
      }

      return result;
    }
    // Exponenciación rápida por cuadrados
    static int modPow(int base, int exp, int mod)
    {
      int res = 1;
      base = base % mod;

      while (exp > 0)
      {
        if ((exp & 1) == 1)
        {
          res = modMult(res, base, mod);
        }
        base = modMult(base, base, mod);
        exp >>= 1;
      }

      return res;
    }
    static int modMult(int a, int b, int mod)
    {
      int res = 0;
      while (b > 0)
      {
        if ((b & 1) == 1)
        {
          res += a;
          if (res >= mod) res -= mod;
        }
        a += a;
        if (a >= mod) a -= mod;
        b >>= 1;
      }
      return res;
    }

    /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     */
    static String exercici2(int[] m, int n, int e)
    {
      // 1. Factorizar n
      int p=0, q=0;
      for (int i = 2; i*i <= n; i++)
      {
        if (n % i == 0)
        {
          p = i;
          q = n/i;
          break;
        }
      }

      int phi = (p-1) * (q-1);
      int d = modInverse(e,phi);

      byte[] bytes=new byte[m.length*2];

      for(int i=0; i<m.length; i++)
      {
        int mi=modPow(m[i],d,n);

        bytes[i*2] = (byte) (mi/256);
        bytes[i*2+1] = (byte) (mi % 256);
      }

      return new String(bytes);
    }
    // algoritme d'Euclides extés per trobar invers modular
    static int modInverse(int a, int m)
    {
      int m0=m,t,q;
      int x0=0, x1=1;

      while(a>1)
      {
        q=a/m;
        t=m;
        m=a%m;
        a=t;

        t=x0;
        x0=x1-q*x0;
        x1=t;
      }

      if(x1<0)
      {
        x1+=m0;
      }

      return x1;
    }

    static void tests() {
      // Exercici 1
      // Codificar i encriptar
      test(4, 1, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = exercici1("Patata", n, e);
        return Arrays.equals(encr, new int[] { 4907, 4785, 4785 });
      });

      // Exercici 2
      // Desencriptar i decodificar
      test(4, 2, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = new int[] { 4907, 4785, 4785 };
        var decr = exercici2(encr, n, e);
        return "Patata".equals(decr);
      });
    }
  }


















  /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
   */
  public static void main(String[] args) {
    System.out.println("---- Tema 1 ----");
    Tema1.tests();
    System.out.println("---- Tema 2 ----");
    Tema2.tests();
    System.out.println("---- Tema 3 ----");
    Tema3.tests();
    System.out.println("---- Tema 4 ----");
    Tema4.tests();
  }

  // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
  static void test(int tema, int exercici, int test, BooleanSupplier p) {
    try {
      if (p.getAsBoolean())
        System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
      else
        System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
        System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
      } else {
        System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
        e.printStackTrace();
      }
    }
  }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :
