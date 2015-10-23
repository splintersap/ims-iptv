## Pomysły na wysyłane wiadmości (treść i sposób przesyłu) przy ustanawianiu sesji. ##

1.	Wysyłanie informacji o filmach
a.	Wykorzystanie frameworku do tworzenia wiadomosci w protokole SDP – podawanie tego w postaci „Tytuł” 

&lt;-&gt;

 „James Bond”.
b.	Wykorzystanie pól protokołu SIP.
i.	MESSAGE – tam umieszczalibyśmy wiadomości w postaci SDP.
ii.	INVITE – to daje możliwość np zmian parametrów sesji – może tutaj przesyłać informacje np o zmianie jakości firmu na gorszą
iii.	UPDATE – np do wysylania oceny filmu wystawionej przez uzytkownika
iv.	INFO – podobnie jak np MESSAGE tutaj mozna wysylac informacje o filmach.

c.	Information to receive those media (addresses, ports, formats and so on) – to jest o SDP więc możemy wysyłać np ifnormacje o porcie który wybrał serwer do strumieniowania.
d.	http://tools.ietf.org/html/rfc3665#page-5 – to jest RFC i tutaj jest przykład związany z otrzymywaniem listy kontaktów – ogólnie jest tak, iż po wysłaniu wiadomosci REGISTER od UA, serwer wysyła wiadomosc OK umieszczając tam liste kontaków. Niestety w przykładzie tym nie znalazłem gdzie ta lista była dokładnie umieszczona.
e.	SIP presence functionality gives the opportunity to know who is online among a given contacts list before the session is established. – to tak w odniesieniu do listy filmów; można by zestawiać sesje dopiero wtedy gdy użytkownik zechce coś oglądać; a na poczatku przeslac mu liste filmow jako zwykla wiadomosc (tak jak to jest np w przykladzie HelloWorld) SDP moim zdaniem nie powinien zawierac listy filmow – on opisuje sesje wobec czego do niego mozna powrzucac info o chociazby przepustowosci.

2.	Przy ewentualnym czacie – wysylanie informacji o dostępności (slajd 42 z wykładu o SIP-ie).
3.	Ponieważ serwer w SDP wysyła informacje o sesji (typie transmitowanych mediow, kodek, itp) uzytkownik moze zadecydowac czy chce przystapic do transmisji – np moze byc tak ze uzytkownik chce wysoka jakos filmu ale ma za slabe lacze wiec wtedy np stwierdzi ze nie chce odbierac filmu w gorszej jakosci.
4.	Można zrobić tak, że oferować klientowi tylko te jakości filmów które będzie można przesłać przy danej przepustowości łącza klienta.
5.


Sposoby na determinacje tego jakiej jakości film będzie przesyłany.