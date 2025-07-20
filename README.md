# 🃏 Scopa — Gioco di carte in Java

Un’applicazione Java che simula una partita a Scopa, il famoso gioco di carte tradizionale italiano.

L’applicativo è scritto in Java SE, con interfaccia grafica realizzata in Swing.  
Consente di giocare contro un avversario virtuale dotato di un algoritmo di scelta intelligente (Minimax semplificato).

---

## 🚀 Come funziona

- Viene creato un mazzo di carte napoletane (40 carte, 4 semi: coppe, denari, bastoni, spade, valori da 1 a 10).
- Il giocatore e l’avversario ricevono le carte, e alcune carte vengono posizionate sul tavolo.
- Il giocatore può scegliere una carta dalla propria mano e tentare di prendere le carte sul tavolo.
- Dopo la mossa del giocatore, l’avversario risponde scegliendo una mossa calcolata per massimizzare il proprio punteggio.
- Si prosegue finché il mazzo e le mani dei giocatori sono vuote.
- Viene calcolato il punteggio finale: carte prese, scope, ecc.

---

## 🎨 Interfaccia

- **Mano del giocatore:** mostra le carte del giocatore in basso.
- **Tavolo:** mostra le carte attualmente disponibili al centro.
- **Mano avversario:** mostra il dorso delle carte in alto.
- **Log:** mostra lo storico delle mosse, punteggio e scope.

---

## 🧠 Algoritmo dell’avversario: Minimax semplificato

L’avversario virtuale utilizza un algoritmo **Minimax a profondità 2** per scegliere la mossa ottimale.

### 👇 Come funziona:
1️⃣ Analizza tutte le possibili carte nella propria mano.  
2️⃣ Per ogni carta, calcola tutte le possibili combinazioni valide sul tavolo.  
3️⃣ Per ogni combinazione, simula la presa e chiama ricorsivamente `valutaCombinazione` per considerare anche la possibile risposta del giocatore.
4️⃣ Alla fine sceglie la mossa che massimizza il proprio punteggio, assumendo che il giocatore risponda nel modo più dannoso possibile (strategia minimax: massimo del minimo).
5️⃣ Se non può prendere carte, scarta una carta sul tavolo.

### 📋 Valutazione:
- La funzione di valutazione conta principalmente:
  - Numero di carte prese.
  - Scope (se implementate nel punteggio).
- Non considera strategie avanzate (come il settebello o primiera) ma può essere estesa.

---

## 📂 Struttura del progetto

