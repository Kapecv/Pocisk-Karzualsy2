# Kapiec VISUALS (Fabric 1.21.4)

Funkcje: stretch ekranu, viewmodel (main hand / off hand), serca nad wrogami, dzwiek przy krytyku.
Klawisze: K = stretch ON/OFF, Right Shift = menu.

## Budowanie
Wymagane: JDK 21.

Opcja A (lokalnie): zainstaluj Gradle 8.12+ i uruchom w tym folderze:
    gradle build
Gotowy plik: build/libs/kapiec-visuals-1.0.0.jar

Opcja B (bez instalowania): wrzuc folder na GitHub - workflow .github/workflows/build.yml
zbuduje jar (zakladka Actions -> artifact "kapiec-visuals").

Do folderu mods wrzuc jar + Fabric API 0.119.2+1.21.4.

Wlasny dzwiek: dodaj mono plik crit.ogg do src/main/resources/assets/kapiec_visuals/sounds/
