# 🎤 Script & Guide de Présentation Orale devant le Jury
## Hackathon Académique IPM 2026 - Groupe 3 : Payment Service

Ce document contient votre **guide de parole mot-à-mot**, la **structure de votre présentation** et le **déroulé exact de votre démonstration** pour réussir votre passage devant le jury.

---

## 🕒 Chronométrage Recommandé (10 Minutes)
* **Partie 1 :** Introduction & Pitch (1 min 30 s)
* **Partie 2 :** Architecture & Rôle du Payment Service (2 min)
* **Partie 3 :** Démonstration Fonctionnelle en Direct (4 min)
* **Partie 4 :** Fonctionnalités Bonus & Avantages Techniques (1 min 30 s)
* **Partie 5 :** Conclusion & Réponses aux questions (1 min)

---

## 🗣️ PARTIE 1 : Introduction & Pitch (1 min 30 s)

> **À dire au jury :**
> 
> *"Bonjour Mesdames et Messieurs les membres du jury.*
> 
> *Je m'appelle [Votre Nom] et je représente le **Groupe 3** chargé du développement du **Payment Service**, le cœur financier de notre **Plateforme Intelligente de Transport Public**, inspirée des systèmes modernes de billettique tels que celui de la ville d'Abu Dhabi.*
> 
> *Dans un réseau de transport urbain moderne, le paiement doit être instantané, transparent et sécurisé. L'usager ne doit pas perdre de temps à acheter un ticket en papier. Notre objectif principal dans le Groupe 3 était de concevoir un microservice autonome capable de :*
> 1. *Gérer le portefeuille virtuel des usagers et permettre des recharges simples via **Wave Mobile Money**.*
> 2. *Traiter en temps réel les demandes de débits automatiques transmises par le **Transport Service (Groupe 2)** lorsqu'un passager monte dans le bus.*
> 3. *Calculer automatiquement les tarifs et détecter les tentatives de fraude pour protéger le réseau."*

---

## 🏗️ PARTIE 2 : Architecture & Interconnexion Microservices (2 min)

> **À dire au jury :**
> 
> *"Notre plateforme globale repose sur une architecture microservices modulaires découpée en 3 composants clés :*
> - *Le **User Service (Groupe 1)** qui gère la création des utilisateurs et l'attribution des cartes HAFILAT.*
> - *Le **Transport Service (Groupe 2)** qui gère la flotte de bus, les lignes et la validation des passagers par NFC ou QR Code.*
> - *Et notre microservice, le **Payment Service (Groupe 3)**.*
> 
> *Comment fonctionne notre Payment Service ?*
> - *D'une part, il s'appuie sur un backend **Java 21 avec Spring Boot 3**, sécurisé par **Spring Security et JWT**.*
> - *D'autre part, il propose une application web **Angular 17+** offrant une expérience utilisateur fluide et réactive.*
> - *En arrière-plan, nous maintenons la persistance des données avec **Spring Data JPA** sur une base relationnelle, documentée avec **Swagger OpenAPI 3.0**."*

---

## 🎬 PARTIE 3 : Démonstration Fonctionnelle en Direct (4 min)

*(Pendant cette phase, projetez votre écran sur `http://localhost:4200`)*

### 📍 Étape 1 : Présentation du Portefeuille Virtuel
> **Action :** Montrez la carte virtuelle HAFILAT sur l'écran d'accueil.  
> **À dire :** *"Voici l'interface utilisateur développée en Angular. L'usager y retrouve sa carte de transport virtuelle HAFILAT, le numéro unique de sa carte NFC et son solde en temps réel (ici 5 000 XOF)."*

### 📍 Étape 2 : Recharge via Wave Mobile Money (Simulation)
> **Action :** Cliquez sur le bouton **🌊 Recharger via Wave Mobile Money**, saisissez le numéro `+221770000000` et choisissez `5 000 XOF`. Cliquez sur valider.  
> **À dire :** *"Pour recharger sa carte, l'utilisateur sélectionne l'option Wave. Nous avons intégré une simulation fluide de l'API Wave adaptée aux numéros sénégalais (+221). En validant la recharge de 5 000 XOF, le backend crédite immédiatement le portefeuille. Le solde passe instantanément à 10 000 XOF et la transaction est archivée."*

### 📍 Étape 3 : Débit Automatique de Bus (Interaction Groupe 2 ➔ Groupe 3)
> **Action :** Allez sur l'onglet **🚌 Simulateur Bus (Validation)**. Sélectionnez la *Ligne 01* et cliquez sur **Simuler Débit Passager dans le Bus**.  
> **À dire :** *"Lorsque le passager monte dans le bus et présente sa carte devant le lecteur NFC du Groupe 2, le Transport Service envoie une requête HTTP POST `/api/payments/process-ride` à notre microservice. Le Payment Service vérifie le solde, applique le tarif de 500 XOF et valide le passage en moins de 50 millisecondes."*

### 📍 Étape 4 : Gestion du Solde Insuffisant & Détection de Fraude (Bonus)
> **Action :** Tentez d'effectuer un débit sur un compte sans solde ou cliquez plusieurs fois d'affilée.  
> **À dire :** *"Si un usager tente de monter dans le bus sans solde suffisant, le Payment Service rejette la transaction avec un code d'erreur HTTP 402 Payment Required. De plus, notre moteur de détection de fraude identifie les tentatives suspectes et génère une alerte de sécurité dans notre tableau de bord."*

---

## 🌟 PARTIE 5 : Points Forts & Fonctionnalités Bonus (1 min 30 s)

> **À dire au jury :**
> 
> *"Pour aller au-delà des exigences de base, notre groupe a intégré plusieurs **fonctionnalités bonus** :*
> 1. *🚨 **Moteur de Détection des Fraudes :** Analyse comportementale qui signale les validations rapprochées et les tentatives de solde insuffisant.*
> 2. *📊 **Tableau de Bord Analytique :** Synthèse en temps réel des recettes, du nombre de débits et du taux de succès des transactions.*
> 3. *💸 **API de Remboursement :** Permet de traiter les réclamations et rembourser un trajet annulé.*
> 4. *📚 **Documentation & Qualité :** Une documentation Swagger OpenAPI 3.0 interactive, des scripts SQL d'initialisation (`schema.sql`, `data.sql`) et une suite de tests unitaires d'intégration validée à 100% avec JUnit 5."*

---

## ❓ PARTIE 6 : Conclusion & Réponses aux Questions du Jury

> **À dire au jury pour conclure :**
> 
> *"En conclusion, le **Payment Service du Groupe 3** fournit une solution financière robuste, scalable et prête à être intégrée au réseau global de transport. Je vous remercie pour votre attention et je suis à votre disposition pour répondre à toutes vos questions."*

---

## 💡 Réponses aux Questions Pièges Posées par le Jury

### Q1 : *"Comment gérez-vous la concurrence si deux débits surviennent au même moment ?"*
👉 **Réponse :** *"Nous utilisons la gestion des transactions Spring `@Transactional` avec des verrous au niveau de la base de données JPA, ce qui garantit l'atomicité et l'isolation des opérations financières selon les principes ACID."*

### Q2 : *"Comment le Transport Service (Groupe 2) communique-t-il avec votre microservice ?"*
👉 **Réponse :** *"Par des appels REST sécurisés en HTTP POST sur l'endpoint `/api/payments/process-ride`, transmettant l'identifiant de la carte (`cardId`), la ligne (`lineId`) et le type de passager. Le paiement renvoie un statut de succès ou de solde insuffisant."*

### Q3 : *"Comment simulez-vous l'API Wave ?"*
👉 **Réponse :** *"Notre service `WavePaymentService` simule le processus de paiement en générant une référence unique `WAVE_XXXXX`, en associant le numéro de téléphone au format sénégalais (`+221`) et en créditant directement le portefeuille via une transaction enregistrée."*
