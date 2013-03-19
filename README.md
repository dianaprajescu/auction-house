# Proiect IDP
===========

## Specificatie

Se urmareste construirea unei aplicatii in Java care sa implementeze un sistem de licitatii inverse intre cumparatori si furnizori. 

Licitatiile se vor desfasoara la Casa de licitatii(Auction House). Aici se vor inscrie atat furnizorii de servicii cat si cumparatorii care sunt interesati sa vanda/cumpere un anumit tip de serviciu sau produs.
Sistemul de licitatii va fi unul de licitatie inversa. Spre deosebire de sistemele traditionale de licitatii, unde preturile serviciilor cresc datorita competitiei intre cumparatorilor, in sistemele de licitatie inversa, competitia se muta intre furnizori, iar preturile vor scadea, pentru a face mai atractive serviciile pentru clientii interesati.

Fiecare furnizor, la inregistrare la Casa de licitatii, va expune o lista de servicii/produse pe care le poate oferi, si va putea:

* vedea lista serviciilor/produselor proprii aflate in oferta
* vedea lista tuturor cumparatorilor care doresc un anumit serviciul detinut de ei
* interactiona doar cu cei care sunt interesati sa cumpere produse oferite de ei
* face o oferta pentru cumparatorii interesati de produsele oferite
* vedea cea mai buna contra-oferta facute pentru aceleasi servicii/produse de alti furnizori
* modifica oferta facuta unui cumparator pentru a castiga licitatia
* anula o oferta facuta daca oferta sa a fost depasita
* observa evolutia ofertelor facute (oferta lansata/depasita/acceptata/refuzata)

Fiecare cumparator se va inregistra la Casa de lictiatii si va expune o lista de servicii/produse de care este interesat, si va putea:

* vedea lista serviciilor/produselor pe care doreste sa le achizitioneze
* vedea lista furnizorilor inregistrati care au in oferta serviciile dorite de ei
* interactiona doar cu furnizorii care ofera unul sau mai multe din produsele dorite de ei
* plasa o cerere de licitatie pentru un serviciu/produs
* anula o cerere de licitatie facuta pentru un serviciu/produs(daca nu a fost acceptata nici o oferta)
* inchide licitatia la atingerea unui criteriu predefinit(pret prag acceptabil, limita de timp)
* refuza/accepta o oferta de la furnizori
* observa evolutia cererii (cerere lansata/ofertata/incheiata)

### Configurare

Aplicatia va functiona similar pentru furnizori si cumparatori, numiti in continuare generic, utilizatori.
La pornire, aplicatia va citi in faza initiala dintr-un fisier de configurare, numele utilizatorului curent si tipul acestuia: furnizor, cumparator.

### Lista de utilizatori

Aplicatia(Action House) va fi implementata folosind un serviciu web si o baza de date, de unde va obtine lista utilizatorilor si furnizorilor conectati in momentul respectiv.

### Lista de produse/servicii

Aplicatia va pastra si lista de servicii/produse a fiecarui utilizator. Astfel la conectare, utlizatorul(furnizor/cumparator) va instiinta serverul de lista lui de servicii/produse (oferite/dorite).
La conectare un utilizator va face o cerere la server pentru a i se comunica lista de utilizatori si lista de servicii pentru fiecare utilizator din cercul sau de interes.
In cazul furnizorilor, serviciul ii va oferi lista cumparatorilor interesati de produsele oferite de el, iar in cazul cumparatorilor ii va oferi lista furnizorilor care ofera unul sau mai multe din serviciile/produsele dorite de el.

### Transferul serviciilor/produselor

La incheierea unei licitatii intre un furnizor si un cumparator, furnizorul va trebui sa ii transfere serviciul/produsul cumparatorului. Acest lucru se va simula prin transferul unor fisiere de dimensiuni/tipuri diferite, folosind modulul de retea.

## Implementare

Figura de mai jos ilustreaza arhitectura aplicatiei:

![ScreenShot](http://elf.cs.pub.ro/idp/_media/teme/t0/arch.jpg?cache=)

Modulele sunt urmatoarele:

* GUI: interfata grafica a aplicatiei
* Network: modulul de retea, folosit pentru comunicarea cu ceilalti utilizatori
* Web Service Client: modulul de comunicare cu serviciul web
* Web Service: serviciul de utilizatori
* Mediatorul: componenta centrala, impersonata de Casa de licitatii, care coordoneaza interactiunile din cadrul aplicatiei. Se prefera aceasta varianta, in care o singura componenta le cunoaste pe toate celelalte, acestea fiind in legatura directa doar cu componenta centrala. In acest mod, se evita necesitatea ca fiecare componenta sa posede referinte la toate celelalte, caz in care s-ar obtine o organizare asemanatoare unui graf complet. De asemenea, la adaugarea unei componente noi, ea trebuie integrata doar cu mediatorul.

Repartitia pe teme este urmatoarea:

* tema 1: GUI
* tema 2: Network
* tema 3: Web Service + Web Service Client

Mediatorul se va dezvolta in paralel, pe masura ce noi module sunt integrate.
