# Aplikacja Pogodowa na Androida

## Część Teoretyczna
### Źródła
- [Dokumentacja Android Developer](https://developer.android.com/docs)
### Prezentacja
- [Link do prezentacji](https://github.com/Luckownia/WeatherForecastApp/blob/master/Aplikacja%20pogodowa%20na%20Androida.pptx)
## Część Praktyczna

## Zadanie 1: 

**Cel:** Nauka podstawowych elementów Android Stuido

### Kroki:
1. **Stwórz nowy projekt:**
   - Kliknij New Project
   - wybierz Empty Views Activity, jako język wybierz Java.
    
2. **Dodaj tekst:**
  - Otwórz plik `activity_main.xml`.
   - Dodaj dowolny tekst za pomocą kodu podanego poniżej, albo za pomocą edytora designu dostępnego w prawym górnym rogu lub po użyciu kombinacji klawiszy Alt+Shift+Left
   ```xml
   <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="176dp"
        android:layout_marginTop="184dp"
        android:text="Zadanie"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
   ```
   - Jeżeli używasz edytora designu, pamiętaj aby w Attributes -> Layout -> Constraint Widget ustawić Constraints, inaczej będzie się źle wyświetlało w aplikacji.
   - Zmień kolor tekstu na zielony, użyj do tego dowolnego sposobu.
3. **Sprawdź czy działa:**
  - Uruchom aplikacje w emulatorze i zobacz czy twój tekst poprawnie się wyświetla.

## Zadanie 2: 

**Cel:** Nauka podstaw obsługi zdarzeń i interakcji z użytkownikiem

### Kroki:
1. **Dodaj przycisk:**
   - Otwórz plik `activity_main.xml`.
   - Dodaj przycisk za pomocą edytora designu, lub użyj kodu poniżej.
   ```xml
   <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="45dp"
        android:layout_marginEnd="163dp"
        android:text="Button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView" />
   ```
    
2. **Spraw by kliknięcie guzika zmieniało treść napisu:**
   - Otwórz plik `MainActivity.java`.
   - Spraw aby po kliknięciu przycisku zmieniał się tekst dodany w poprzednim zadaniu na dowolny inny. Zastosuj metodę pokazaną na prezentacji, lub zadeklaruj funkcję w `MainActivity.java` i przypisz ją do guzika w `activity_main.xml` używając Attributes -> onClick
   - Pamiętaj o zaimportowaniu odpowiednich bibliotek, wykorzystaj findViewById. Metoda guzika odpowiadająca za zmienianie tekstu musi znajdować się w metodzie onCreate().
3. **Sprawdź czy działa:**
  - Uruchom aplikacje w emulatorze i zobacz czy twój przycisk działa poprawnie.

## Zadanie 3: 

**Cel:** Nauka podstaw obsługi danych z Api

### Kroki:
1. **Dodaj potrzebny kod do ściągania danych z Api:**
   - Otwórz plik `MainActivity.java`.
   - Dodaj poniższe importy bibliotek.
   ```xml
   biblioteki jakies zeby to api dzialalo
   ```
   - Dodaj poniższy kod #w jakieś tam miejsce to tu trzeba będzie napisać
   ```xml
   tutaj tak żeby ściągało to api dane
   ```  
2. **Spraw by kliknięcie guzika zmieniało treść napisu na opis aktualnej pogody w Krakowie:**
   - Otwórz plik `MainActivity.java`.
   - jakieś tam kroki
   - tutaj jakieś tam dalsze kroki
3. **Sprawdź czy działa:**
  - Uruchom aplikacje w emulatorze i zobacz czy wszystko wyświetla się poprawnie.
