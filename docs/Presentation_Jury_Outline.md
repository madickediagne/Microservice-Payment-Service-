# Support de Présentation & Démo Jury - Groupe 3 (Payment Service)

---

## 📽️ Structure de la Présentation PowerPoint (Diapositives)

### Slide 1 : Titre & Équipe
- **Titre :** Système Intelligent de Transport Public & Paiement Numérique
- **Sous-titre :** Groupe 3 – Microservice de Gestion des Paiements (Payment Service)
- **Membres du Groupe :** 4 étudiants Groupe 3
- **Contexte :** Hackathon Académique IPM 2026

### Slide 2 : Problématique & Objectifs du Groupe 3
- **Besoin :** Garantir un système de billettique rapide, sécurisé et sans contact pour les bus urbains.
- **Rôle du Payment Service :**
  1. Permettre aux usagers de recharger leur portefeuille via Wave Mobile Money.
  2. Traiter automatiquement les requêtes de débit envoyées par le *Transport Service* lors des validations NFC/QR Code.
  3. Bloquer immédiatement les tentatives de fraude ou les accès en cas de solde insuffisant.

### Slide 3 : Architecture Microservices & Interactions
- **Architecture :** Spring Boot 3 (Port 8083), Angular 17+ (Port 4200), H2/MySQL.
- **Interactions :**
  - Recevoir les requêtes de débit du **Groupe 2 (Transport Service)**.
  - Interroger/Mettre à jour le portefeuille relié aux utilisateurs du **Groupe 1 (User Service)**.

### Slide 4 : Démonstration des Fonctionnalités Clés
- **Simulation Wave :** Crédit instantané du compte usager.
- **Calculateur Automatique :** Tarification différenciée (Régulier, Étudiant, Senior).
- **Débit de Trajet Bus :** Déduction temps réel et gestion des exceptions (Solde insuffisant).

### Slide 5 : Fonctionnalités Bonus & Valeur Ajoutée
- 🚨 **Détection des Fraudes :** Détecteur comportemental (passages trop rapprochés, tentatives répétées sans solde).
- 📊 **Tableau de Bord Analytique :** Métriques financières globales et suivi du taux de succès.
- 💸 **Système de Remboursements :** Traitement des réclamations usagers.

---

## 🎬 Scénario de Démonstration Fonctionnelle (Pour le Jury)

1. **Étape 1 : Ouverture de l'interface Angular (Port 4200)**
   - Montrer la carte de transport virtuelle HAFILAT avec le solde initial.
2. **Étape 2 : Recharge via Wave**
   - Cliquer sur *Recharger via Wave Mobile Money*, saisir le numéro et valider un montant (ex: 2000 XOF).
   - Montrer le crédit instantané du solde et l'enregistrement de la transaction dans l'historique.
3. **Étape 3 : Simulation d'un Trajet Bus (Interaction Groupe 2)**
   - Aller sur l'onglet *Simulateur Bus (Validation)*.
   - Sélectionner une carte NFC, la ligne 01 et cliquer sur *Simuler Débit Passager*.
   - Constater le message de succès, le nouveau solde et la transaction de débit.
4. **Étape 4 : Démonstration du Solde Insuffisant & Alerte Anti-Fraude**
   - Simuler un débit sur un compte sans solde ou effectuer 4 validations en moins de 10 secondes.
   - Montrer le blocage HTTP 402 et la génération automatique d'une alerte dans l'onglet *Sécurité & Fraude*.
5. **Étape 5 : Présentation Swagger / OpenAPI (Port 8083)**
   - Ouvrir `http://localhost:8083/swagger-ui.html` et montrer la documentation interactive des endpoints REST.
