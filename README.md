# Projekt Interoperabilnost Informacijskih Sustava

Web aplikacija za upravljanje studentskim podacima s mogućnostima obrade XML-a, SOAP web uslugama, sigurnom autentikacijom i integracijom podataka o vremenu putem XML-RPC-a.

## Struktura projekta

Ovaj repozitorij sastoji se od dva glavna dijela:

### Backend
- **iisProject**: Glavna backend aplikacija koja pruža osnovnu funkcionalnost i REST/SOAP API-je
- **iisProjectDhmzFinal**: XML-RPC poslužitelj koji upravlja integracijom s DHMZ podacima

### Frontend
- React aplikacija koja komunicira s backend servisima

## Opis projekta

Ovaj projekt demonstrira različite protokole interoperabilnosti i tehnike, s fokusom na backend funkcionalnosti, obradu XML podataka, validaciju i integraciju s različitim servisima. Aplikacija kombinira napredni Spring Boot backend s modernim React frontendnom za potpuno korisničko iskustvo interoperabilnosti informacijskih sustava. Aplikacija omogućuje korisnicima:

- Učitavanje, validaciju i obradu studentskih podataka u XML formatu
- Upite nad studentskim podacima korištenjem XPath izraza
- Autentikaciju putem JWT tokena
- Pristup podacima o vremenu kroz XML-RPC
- Komunikaciju putem SOAP web servisa

Backend je izrađen pomoću Spring Boot-a i pruža REST API za klijentske aplikacije, kao i SOAP servise za integraciju između sustava.

## Vizualni prikaz

Screenshotovi aplikacije dostupni su u repozitoriju u mapi `/screenshots`. Ovi screenshotovi prikazuju različite dijelove aplikacije i njene funkcionalnosti.

## Funkcionalnosti

### Obrada XML-a
- Validacija XML-a prema XSD i RNG shemama
- Filtriranje XML podataka pomoću XPath-a
- Pretvaranje CSV-a u XML format
- JAXB mapiranje za konverziju objekt-XML

### Autentikacija i sigurnost
- JWT sustav autentikacije
- Mehanizmi obnavljanja tokena
- Kontrola pristupa temeljena na ulogama
- Enkripcija lozinki

### Upravljanje podacima
- CRUD operacije za studentske podatke
- Pohrana u bazu podataka s JPA/Hibernate
- Pretraživanje i filtriranje podataka

### Interoperabilnost
- SOAP web servisi za pristup podacima
- XML-RPC integracija za meteorološke podatke
- REST API endpointi za klijentske aplikacije

### XML validacija
- Validacija prema XSD shemi
- Validacija prema RELAX NG shemi
- Prikaz detaljnih poruka greške kod validacije

## Tehnologije

### Backend
- Java 17
- Spring Boot 3.4.0
- Spring Security za autentikaciju i autorizaciju
- JWT (JJWT 0.11.5) za token-baziranu autentikaciju
- Spring Data JPA i Hibernate za ORM
- MySQL baza podataka (MySQL Connector 8.0.33)
- JAXB za XML mapiranje
- Jing za RELAX NG validaciju
- Apache XML-RPC za XML-RPC komunikaciju

- Spring WS za SOAP web servise
- Lombok za smanjenje boilerplate koda

## Arhitektura

Aplikacija koristi višeslojnu arhitekturu:

### Backend
- **Controller sloj**: REST i SOAP kontroleri koji izlažu API endpointe
- **Service sloj**: Poslovna logika aplikacije
- **Repository sloj**: Pristup bazi podataka putem Spring Data JPA
- **DTO objekti**: Za prijenos podataka između slojeva
- **Security**: Implementacija JWT autentikacije i autorizacije
- **Utils**: Pomoćne klase za XML procesiranje i validaciju

### Frontend
- **Komponente**: React komponente organizirane po funkcionalnostima:
  - **auth**: Autentikacija (LoginForm, ProtectedRoute)
  - **student**: Komponente za upravljanje studentskim podacima (StudentSearch, XPathSearch)
  - **validation**: Komponente za validaciju XML-a (XmlUpload, ValidationResult)
  - **weather**: Komponente za prikaz podataka o vremenu (WeatherSearch, WeatherWidget)
  - **shared**: Ponovno iskoristive komponente (Button, FileInput, NavBar)
- **Services**: JavaScript servisi za komunikaciju s backendom
- **Pages**: Glavne stranice aplikacije

## API Dokumentacija

Swagger dokumentacija je dostupna kao dio repozitorija. Swagger file je uključen u repozitorij i mora se ručno pokrenuti za pregled API endpointa i njihove funkcionalnosti.

## API Endpointi

### Autentikacija
- POST `/rest/login` - Prijava korisnika (bez registracije, samo preddefinirani korisnici)
- POST `/rest/refreshToken` - Obnova JWT tokena

### XML i Studentski podaci
- POST `/api/xml/validateAndSave` - Validacija i spremanje XML-a

### Podaci o vremenu
- GET `/api/weather/temperature` - Dohvat temperature za grad
- GET `/api/weather/cities` - Dohvat gradova prema upitu

### SOAP Servisi
- WSDL dostupan na `/ws/students.wsdl`
- Operacije:
  - `searchByGpa` - Pretraga studenata prema prosjeku
  - `filterByXPath` - Filtriranje podataka pomoću XPath-a
  - `validateXml` - Validacija XML podataka

## Konfiguracija

Aplikacija zahtijeva postavljanje sljedećih konfiguracija:

1. U `application.properties` trebaju biti konfigurirani:
   - Podaci za MySQL bazu podataka
   - JWT konfiguracijske postavke
   - Port aplikacije (trenutno 8087)
   
Primjer konfiguracije:
```properties
# Baza podataka
spring.datasource.url=jdbc:mysql://localhost:3306/Test1
spring.datasource.username=root
spring.datasource.password=Lozinka1$
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Postavke aplikacije
server.port=8087

# JPA postavke
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Instalacija i pokretanje

### Preduvjeti
- Java 17 ili novija
- MySQL server
- Maven

### Backend
1. Klonirajte repozitorij
2. Kreirajte bazu podataka:
   ```sql
   CREATE DATABASE Test1;
   ```
3. Pokrenite backend aplikaciju:
   ```bash
   mvn spring-boot:run
   ```

Backend će biti dostupan na: `http://localhost:8087`

### Frontend
1. Navigirajte u direktorij `IIS_REACT_APP`
2. Instalirajte ovisnosti:
   ```bash
   npm install
   ```
3. Pokrenite frontend aplikaciju:
   ```bash
   npm run dev
   ```

Frontend će biti dostupan na: `http://localhost:5173`

### SOAP servisi
SOAP servisi će biti dostupni na: `http://localhost:8087/ws`
WSDL dokument: `http://localhost:8087/ws/students.wsdl`

## Napomene i obrazloženje

- Aplikacija ima implementiran login sustav ali ne i registraciju korisnika - umjesto toga koriste se preddefinirani korisnički računi u bazi podataka
- Projekt je razvijen kao demonstracija specifičnih tehnologija interoperabilnosti (XML, SOAP, XML-RPC) za potrebe akademskog okruženja i kao dodatak većem, kompletnijem projektu s punom funkcionalnošću
- Sučelje je razvijeno u Reactu s fokusom na funkcionalnost umjesto na estetiku
- Aplikacija trenutno nema deployment, koristi se lokalno
- Za pregled API dokumentacije potrebno je ručno pokrenuti Swagger file koji je uključen u repozitorij
- XML-RPC servis za podatke o vremenu mora biti pokrenut na portu 8085
- `.properties` datoteke s konfiguracijskim postavkama dodane su u `.gitignore` radi sigurnosti

## Primjena u portfoliju

Ovaj projekt je odličan primjer za demonstraciju:
- Razumijevanja fullstack arhitekture (Spring Boot + React)
- Implementacije naprednih XML tehnologija i protokola interoperabilnosti
- Iskustva sa Spring Boot i React ekosustavima
- Razumijevanja sigurnosnih principa i JWT autentikacije
- Sposobnosti integracije različitih sustava (SOAP, REST, XML-RPC)
- Razvoja modularnih React komponenti organiziranih po funkcionalnosti
- Implementacije zaštićenih ruta i autentikacijskog tijeka u frontend aplikaciji

Projekt jasno demonstrira sposobnost rada s punim stackom tehnologija, što je ključna vještina za junior fullstack pozicije.