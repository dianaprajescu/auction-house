# Proiect IDP
===========

## Tema1

Tema 1 urmareste constituirea unei imagini de ansamblu asupra modulelor aplicatiei si a interactiunilor intre acestea, precum si realizarea modulului grafic (GUI) al aplicatiei.

### Cerinte

1. Definiti functionalitatea celor 4 module ale aplicatiei (GUI, Network, Web Service Client, Mediator), sub forma unor interfete. Serviciul web propriu-zis nu constituie obiectul acestei teme. El va fi definit separat, in tema aferenta. Pentru scopul acestei teme, modulul de retea poate fi doar simulat, nu implementat.
  * Numarul de interfete ce descriu, din punct de vedere comportamental, un modul, este egal cu numarul de legaturi in care acesta este implicat. De exemplu, modulul GUI se va ascunde in spatele unei singure interfete, in timp ce Mediatorului i se vor asocia 3 interfete, corespunzatoare celor 3 acceptii posibile pe care le poate avea (modulul GUI percepe Mediatorul diferit fata de modulul Network etc.)
  * Pentru alegerea metodelor din componenta acestora, se poate dovedi utila imaginarea unor scenarii de intrebuintare a acestora. Acesta este un exemplu de elaborare inversa, in care definirea este dictata de utilizare.
2. Definiti clasele, care implementeaza, minimal, interfetele de la punctul anterior. Acestea vor stabili legaturile intre modulele aplicatiei, conform diagramei arhitecturale. Atentie! In cadrul claselor, referintele la celelalte module se vor tine in forma interfetelor si NU a claselor, pentru a realiza decuplarea dorita!
3. Implementati modului GUI al aplicatiei
4. Testati interfeta construita, utilizand un mock object ce va simula mediatorul, si va instiinta modulul grafic de producerea unor evenimente specifice: aparitia unui nou utilizator, pornirea unei licitatii, etc (evenimentele sunt, de fapt, simulate)

Modelarea arhitecturii sistemului are un rol orientativ, structurile putand suferi modificari pe parcursul dezvoltarii aplicatiei, in cadrul temelor ulterioare.

### Constructia interfetei
#### Logare
	
Utilizatorul se va autentifica in aplicatie folosind un username unic, un tip al utilizatorului(cumparator/furnizor) si o parola(optionala). Dupa autentificare, numele utilizatorului curent va fi reflectat in aplicatie sub forma unui JLabel.

#### Managementul utilizatorilor
Aplicatia va afisa pentru utilizatorul curent, lista serviciilor/produselor oferite/dorite.
Pentru fiecare dintre serviciile dorite, se vor mentine o serie de informatii.

##### Managementul cumparatorilor
Pentru un utilizator de tip cumparator(Buyer), aplicatia va afisa un tabel cu toate serviciilor dorite de acesta. Fiecare serviciu/produs, va avea o intrare in acest tabel si va afisa statusul aferent cu una dintre valorile de mai jos:

* Inactive → nu s-a lansat inca o cerere de oferta pentru acest serviciu
* Active → cererea de oferta a fost lansata in sistem

Daca serviciul este inactiv, celelalte campuri pentru serviciul respectiv vor ramane necompletate.
Daca serviciul este activ, cumparatorul va putea vedea in dreptul serviciului un camp continand lista de furnizori activi in sistem care ofera serviciului pe care il doresc. Statusul serviciului, acum „Active“, va putea lua una dintre valorile:

* No Offer → furnizorul nu a facut nici o ofera pentru serviciul respectiv
* Offer Made → furnizorul a facut o oferta pentru serviciul respectiv

In functie de ofertele primite pentru un anumit produs, sau intervalul de timp pe care cumparatorul il are alocat pentru achizitionarea un anumit serviciu, el va putea accepta sau refuza o oferta. Statusul licitatiei se va schimba primind una dintre valorile:

* Offer Accepted
* Offer Refuzed

In orice moment pana la acceptarea unei oferte, cumparatorul isi poate retrage cererea de oferta facuta pentru un serviciu, ceea ce va aduce cu sine anularea tuturor licitatiilor aflate in desfasurare pentru serviciul respectiv. Acest lucru nu mai este posibil odata ce o oferta a fost acceptata pentru o cerere lansata. Anularea licitatiilor se va face prin refuzarea tuturor ofertelor primite.
La acceptarea unei oferte pentru un anumit produs, toate celelalte oferte facute de furnizori vor fi automat refuzate, iar transferul produsului poate incepe. Dupa acceptarea cu succes a unei oferte, statusul licitatiei va fi actualizat conform progresului transferului efectiv.

* Transfer Started
* Transfer In progress
* Transfer Complete
* Transfer Failed → unul dintre participanti s-a delogat si transferul nu s-a putut efectua

Transferul serviciului/produsului va fi ilustrat folosind o bara de progres. Pentru aceasta, se poate folosi o componenta de tip JProgressBar similar cu laboratorul 4.

##### Managementul furnizorilor

Pentru un utilizator de tip furnizor(Seller), aplicatia va afisa un tabel cu toate serviciilor oferite de acesta.
Fiecare serviciu/produs, va avea o intrare in acest tabel avand afisat statusul aferent cu una dintre valorile de mai jos:

* Inactive → nu s-a lansat inca o cerere de oferta pentru acest serviciu
* Active → exista o cerere de oferta in sistem pentru acest serviciu

Daca serviciul este inactiv, celelalte campuri pentru serviciul respectiv vor ramane necompletate.
Daca serviciul este activ, furnizorul va putea vedea in dreptul serviciului, un nou camp continand lista de cumparatori activi in sistem care doresc serviciului furnizat. Statusul serviciului, acum „Active“, va lua una dintre valorile de mai jos:

* No Offer → furnizorul nu a facut nici o ofera pentru serviciul respectiv
* Offer Made → furnizorul a facut o oferta pentru serviciul respectiv

In functie de ofertele facute pentru un anumit produs, statusul licitatiei se poate schimba primind una dintre valorile:

* Offer Exceed → oferta facuta de furnizor a fost depasita de o contra-oferta
* Offer Accepted → oferta a fost acceptata de cumparator
* Offer Refuzed → oferta a fost refuzata

Pana la acceptarea unei oferte de catre un cumparator, furnizorul isi poate retrage oferta facuta daca oferta sa a fost depasita, iar furnizorul va fi automat sters din licitatia respectiva.

Daca o oferta facuta de furnizor a fost refuzata, el poate face o noua oferta pentru acelasi serviciu, sau poate renunta complet la licitatia respectiva.

Atunci cand oferta unui furnizor a fost depasita de o alta oferta, acest lucru va fi vizibil pentru furnizor in asa fel incat el sa poata face o contra-oferta.

La acceptarea unei oferte pentru un anumit produs, trebuie realizat transferul produsului catre cumparator. Statusul licitatiei va fi acum actualizat conform progresului transferului:

* Transfer Started
* Transfer In progress
* Transfer Complete
* Transfer Failed

Transferul serviciului/produsului va fi ilustrat similar ca in cazul unui cumparator.

### Managementul listelor

Listele de servicii se vor popula la logarea utilizatorilor cu valorile citite din fiserele de configurare ale acestora.
Statusurile serviciilor din lista se vor actualiza dinamic in functie de evolutia sistemul.

In cazul cumparatorilor, la pornirea aplicatiei, toate serviciile vor fi inactive. La activarea unei cereri pentru un serviciu, statusul acestuia va fi automat actualizat ca fiind activ(respectiv: No Offer), iar in dreptul acestui serviciu, va fi automat populata o lista a furnizorilor care sunt logati in sistem, si pot oferi acest serviciu.

In cazul furnizorilor, la pornirea aplicatiei, toate serviciile vor fi inactive(nu exista nici o cerere in sistem). La activarea unei cereri pentru un serviciu aflat in lista furnizorului de catre un cumparator, statusul acestuia va fi automat actualizat ca fiind activ, iar in dreptul acestui serviciu, va fi automat adaugati cumparatorii care au lansat o cerere de oferta pentru serviciul respectiv.

La modificarea unei oferte facute pentru un serviciu, fiecare dintre furnizorii implicati in licitatie, vor fi notificati doar daca noua oferta primita a depasit(eg.: pretul oferit de un contra-ofertant este mai mic) oferta facuta.
La finalizarea unei licitatii pentru un anumit serviciu, toti furnizorii implicati in licitatie, vor primi actualizari ale licitatiei cu unul din statusurile mentionate mai devreme (Offer Refused, Offer Accepted).

Modelarea ofertarii, criteriile de depasire a unei oferte facute, precum si criteriile de finalizare ale unei oferte(pret prag, timp de cumparare depasit) raman la alegea voastra.

Operatiile asupra componentelor grafice trebuie facut in mod obligatoriu folosind patternul Model View Controller folosind modelele specifice fiecarei componente puse la dispozitie de Swing.

Pentru implementarea listelor de servicii si furnizori/clienti puteti folosi o componenta de tip JTable.
Puteti consulta acest tutorial. Aceasta componenta este asemanatoare unui JList, in sensul intrebuintarii unui model ce contine datele (implementarea implicita a modelului este DefaultTableModel). Diferenta este ca modelul este bidimensional, retinand pentru fiecare intrare mai multe campuri.
In cadrul aplicatiei noastre, pentru fiecare serviciu/produs se vor reprezenta:

* numele serviciului
* lista de utilizatori:
  1. furnizori care ofera serviciul dorit sau
  2. utilizatori care au lansat o cerere pentru serviciul dorit
* statusul:
  1. Inactiv
  2. Activ
  <ul>
    <li>No Offer</li>
    <li>Offer Made</li>
    <li>Offer Exceeded (doar pentru furnizori)</li>
    <li>Offer Accepted</li>
    <li>Offer Refused</li>
    <li>Transfer started</li>
    <li>Transfer in progress</li>
    <li>Transfer completed</li>
    <li>Transfer failed</li>
* progresul: componenta grafica ce indica stadiul de finalizare a transferului (doar intr-una din starile de transfer)(JProgressBar)

Dupa cum se poate observa, fiecare serviciu/produs, va avea intrare in tabel, impreuna cu o componenta care va mentine o intrare pentru fiecare utilizator relevant(furnizor/cumparator) cu care se poate initia o licitatie, impreuna cu statusului acesteia si progresul transferului de servicii, daca este cazul. Implementarea acestei componente ramane la alegerea voastra.
Cateva hinturi:
* In mod implicit, pentru reprezentarea unei celule de tabel simple(serviciu, status), se afiseaza un JLabel, al carui continut este textul intors de metoda toString a obiectului din celula respectiva. Pentru componenta de progres din coloana omonima a tabelului veti observa ca afisarea implicita este sub forma de sir. Pentru a determina afisarea componentei insesi, trebuie sa definiti pentru coloana de progres un TableCellRenderer in care sa supradefiniti metoda getTableCellRendererComponent, astfel incat sa intoarca chiar componenta din celula (parametrul value al metodei). Mai multe informatii aici.
* De asemenea, implicit, celulele unui tabel se pot edita la executarea unui dublu click. Pentru a le face read-only (nu vrem sa modificam datele unui transfer direct), trebuie sa extindeti modelul de tabel si sa supradefiniti metoda isCellEditable, incat sa intoarca false.

Operatiile posibile pe lista de servicii/utilizatori vor fi oferite sub forma unui meniu contextual construit cu ajutorul unei componente JPopupMenu. Meniul se va declansa la “click” dreapta pe celula din tabel.
Pentru cumparatori, meniul pentru servicii, va include urmatoarele intrari:
1. Launch Offer request - va lansa cererea de oferta pentru serviciul respectiv in sistem, si va activa serviciul(valabil doar pentru serviciile inactive)
2. Drop Offer request - va anula cererea de oferta pentru serviciul respectiv, refuzand toate ofertele active(valabil doar pentru serviciile active pentru care nu a fost acceptata nici o oferta)

Meniul contextual pentru lista de furnizori:
1. Accept offer - ofera respectiva va fi acceptata(intrare activa doar in cazul in care nu exista o alta oferta acceptata) iar toate celelalte oferte vor fi automat refuzate
2. Refuse offer - oferta este refuzata(celelalte oferte nu sunt afectate)

Pentru furnizor, meniul pentru servicii, nu este necesar, furnizorii pastrandu-si intacta lista de servicii/produse oferita pe tot parcursul sesiunii.
Meniul contextual pentru lista de cumparatori va contine urmatoarele intrari:
1. Make offer - furnizorul va face o oferta pentru serviciul dorit de cumparator
2. Drop auction - furnizorul va renunta la licitatie (valabil doar daca oferta sa a fost depasita de o alta oferta)

Implementarea cuantificarii ofertelor si criteriul de comparatie al acestora ramane la alegerea voastra.

### Parasirea aplicatiei

Utilizatorul paraseste aplicatia folosind un buton cu numele Log Out.
La delogarea unui cumparator, el va parasi toate licitatiile active, refuzand toate ofertele facute de furnizori. In cazul in care nu exista nici o oferta facuta pentru serviciile/produsele dorite pentru care s-a lansat o cerere de oferta, cererea este anulata. Daca delogarea se face in timpul transferului de servicii, acesta va esua, si va fi marcat corespunzator.
Furnizorii se vor putea deloga doar daca ofertele facute in licitatiile in care sunt implicati au fost deja depasite de contra-oferte, sau daca nu se afla implicati in nici o licitiatie.

### Detalii de arhitectura

In vederea implementarii temei, se vor folosi urmatoarele design pattern-uri: MVC, Mediator, State, Command, ( optional Decorator).

Design-urile mentionate anterior urmaresc o decuplare a componentelor folosite, facilitand extensibilitatea arhitecturii in vederea implementarii temelor urmatoare.

## Simularea interfetei

Aceasta sectiune urmareste testarea interfetei construite. Definiti o clasa care sa mimeze comportamentul mediatorului, simuland aparitia diferitelor evenimente, precum:

1. un utilizator face log in / log out,
2. se lanseaza o cerere de oferta,
3. se anuleaza o cerere de oferta,
4. se face o oferta,
5. oferta este acceptata/depasita/refuzata,
6. transferul de servicii/produse.

Puteti utiliza o bucla, in cadrul careia generati evenimente, la anumite intervale.
Folositi clasa SwingWorker pentru a rula o astfel de bucla in fundal si perechea de metode publish/process pentru a apela metodele interfetei grafice.
Pentru simulare puteti genera aleator nume de utilizatori, servicii, produse.

## Bonus (maxim 10p/100p)

1. (5p) Cumparatorii/Furnizorii isi pot retrage cererea de oferta/oferta in orice etapa a licitatiei
2. (5p) Cumparatorii/Furnizorii isi pot modifica lista de servicii/produse in mod dinamic in functie de cererile existente in sistem

## Modalitate de rulare

Tema va contine un fisier XML pentru rularea folosind Ant precum si Readme cu alte informatiile necesare rularii.
Temele care nu ruleaza conform indicatiilor oferite in Readme si/sau Ant folosind una dintre comenzile :

```xml
ant 
<!-- sau --> 
ant run
```

nu vor fi notate.
