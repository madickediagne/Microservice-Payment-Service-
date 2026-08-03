import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentApiService } from './services/payment.service';
import { 
  Wallet, 
  PaymentTransaction, 
  WaveRechargeRequest, 
  RidePaymentRequest, 
  FareCalculationRequest,
  FareCalculationResponse,
  FraudAlert,
  StatsResponse 
} from './models/payment.models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <nav class="navbar">
      <div class="brand">
        <div class="brand-icon">🇦🇪</div>
        <div>
          <div class="brand-title">Abu Dhabi Public Transport</div>
          <div style="font-size: 0.75rem; color: var(--text-muted);">Groupe 3 - Payment Microservice</div>
        </div>
      </div>
      <div style="display: flex; gap: 12px; align-items: center;">
        <span class="nav-badge">● API Online (Port 8083)</span>
        <a href="http://localhost:8083/swagger-ui.html" target="_blank" style="color: var(--accent-cyan); text-decoration: none; font-size: 0.85rem; font-weight: 600;">Swagger Docs ↗</a>
      </div>
    </nav>

    <div class="container">
      <!-- Top Stats Bar -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin-top: 16px;">
        <div class="card" style="padding: 16px;">
          <div class="balance-label">Total Rechargé (Plateforme Globale)</div>
          <div style="font-size: 1.4rem; font-weight: 700; color: var(--accent-green);">{{ stats?.totalRecharged || 0 }} XOF</div>
        </div>
        <div class="card" style="padding: 16px;">
          <div class="balance-label">Total Débité (Trajets Bus)</div>
          <div style="font-size: 1.4rem; font-weight: 700; color: var(--accent-cyan);">{{ stats?.totalDebited || 0 }} XOF</div>
        </div>
        <div class="card" style="padding: 16px;">
          <div class="balance-label">Total Transactions Réseau</div>
          <div style="font-size: 1.4rem; font-weight: 700; color: #fff;">{{ stats?.totalTransactionsCount || 0 }}</div>
        </div>
        <div class="card" style="padding: 16px;">
          <div class="balance-label">Alertes de Fraude (Actives)</div>
          <div style="font-size: 1.4rem; font-weight: 700; color: var(--accent-red);">{{ stats?.activeAlertsCount || 0 }}</div>
        </div>
      </div>

      <!-- Navigation Tabs -->
      <div style="display: flex; gap: 12px; margin-top: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 12px; overflow-x: auto;">
        <button (click)="activeTab = 'wallet'" [style.border-color]="activeTab === 'wallet' ? 'var(--accent-gold)' : 'transparent'" class="btn" style="width: auto; background: transparent; color: #fff; border-bottom: 2px solid;">💳 Portefeuille & Wave</button>
        <button (click)="activeTab = 'simulator'" [style.border-color]="activeTab === 'simulator' ? 'var(--accent-cyan)' : 'transparent'" class="btn" style="width: auto; background: transparent; color: #fff; border-bottom: 2px solid;">🚌 Simulateur Bus (Validation)</button>
        <button (click)="activeTab = 'fare'" [style.border-color]="activeTab === 'fare' ? 'var(--accent-green)' : 'transparent'" class="btn" style="width: auto; background: transparent; color: #fff; border-bottom: 2px solid;">📊 Calculateur Tarif</button>
        <button (click)="activeTab = 'history'" [style.border-color]="activeTab === 'history' ? '#a855f7' : 'transparent'" class="btn" style="width: auto; background: transparent; color: #fff; border-bottom: 2px solid;">📋 Historique</button>
        <button (click)="activeTab = 'fraud'" [style.border-color]="activeTab === 'fraud' ? 'var(--accent-red)' : 'transparent'" class="btn" style="width: auto; background: transparent; color: #fff; border-bottom: 2px solid;">🚨 Sécurité & Fraude</button>
      </div>

      <!-- Tab 1: Wallet & Wave -->
      <div *ngIf="activeTab === 'wallet'" class="dashboard-grid">
        <div class="card">
          <div class="card-title">
            <span>Carte de Transport Virtuelle</span>
            <span style="color: var(--accent-gold); font-weight: 600;">ACTIVE</span>
          </div>
          
          <div class="virtual-card">
            <div class="chip-logo">
              <div class="nfc-chip"></div>
              <div style="color: var(--accent-gold); font-weight: bold; font-size: 1.1rem;">HAFILAT BUS</div>
            </div>
            <div class="balance-label">SOLDE DISPONIBLE</div>
            <div class="balance-display">{{ wallet?.balance || 0 }} {{ wallet?.currency || 'XOF' }}</div>
            <div class="card-details">
              <div>USER: {{ wallet?.userId || 'USR_1001' }}</div>
              <div>ID CARTE: {{ wallet?.cardId || 'CARD_NFC_883921' }}</div>
            </div>
          </div>

          <button (click)="openWaveModal()" class="btn btn-wave" style="margin-top: 20px;">
            <span>🌊 Recharger via Wave Mobile Money</span>
          </button>
        </div>

        <div class="card">
          <div class="card-title">Guide d'utilisation Groupe 3</div>
          <p style="color: var(--text-muted); font-size: 0.9rem; line-height: 1.6;">
            Le <strong>Payment Service</strong> permet la recharge immédiate par simulation <strong>Wave</strong> et écoute les demandes de validation de passager transmises par le <strong>Transport Service (Groupe 2)</strong>.
          </p>
          <div style="margin-top: 16px; background: rgba(255,255,255,0.03); padding: 12px; border-radius: 8px; font-size: 0.85rem;">
            <div>✔ Vérification du solde avant débit</div>
            <div>✔ Débit automatique en temps réel</div>
            <div>✔ Détection de débits frauduleux ou rapprochés</div>
            <div>✔ Historisation complète des transactions</div>
          </div>
        </div>
      </div>

      <!-- Tab 2: Bus Validation Simulator (Group 2 Interaction) -->
      <div *ngIf="activeTab === 'simulator'" class="dashboard-grid">
        <div class="card">
          <div class="card-title">
            <span>Simulateur de Validation Bus (Transport Service)</span>
            <span style="font-size: 0.8rem; color: var(--accent-cyan);">Interaction Groupe 2 ➔ Groupe 3</span>
          </div>

          <div class="form-group">
            <label class="form-label">Identifiant Carte / QR Code</label>
            <input type="text" [(ngModel)]="simCardId" class="form-control" placeholder="CARD_NFC_883921">
          </div>

          <div class="form-group">
            <label class="form-label">Ligne de Bus</label>
            <select [(ngModel)]="simLineId" class="form-control">
              <option value="LINE_01">Ligne 01 - Express Centre Ville / Aéroport (500 XOF)</option>
              <option value="LINE_02">Ligne 02 - Corniche & Université (350 XOF)</option>
              <option value="LINE_03">Ligne 03 - Zone Industrielle (400 XOF)</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Type de Passager</label>
            <select [(ngModel)]="simPassengerType" class="form-control">
              <option value="REGULAR">Régulier (Tarif plein)</option>
              <option value="STUDENT">Étudiant (Tarif réduit)</option>
              <option value="SENIOR">Senior (Tarif préférentiel)</option>
            </select>
          </div>

          <button (click)="simulateRidePayment()" class="btn btn-primary" style="margin-top: 12px;">
            <span>🚌 Simuler Débit Passager dans le Bus</span>
          </button>

          <div *ngIf="simResult" [style.border-color]="simResult.status === 'SUCCESS' ? 'var(--accent-green)' : 'var(--accent-red)'" 
               style="margin-top: 16px; padding: 16px; border-radius: 8px; background: rgba(0,0,0,0.3); border: 1px solid;">
            <div [style.color]="simResult.status === 'SUCCESS' ? 'var(--accent-green)' : 'var(--accent-red)'" style="font-weight: bold; font-size: 1rem;">
              {{ simResult.status === 'SUCCESS' ? '✅ VALIDATION ET DÉBIT RÉUSSIS' : '❌ ÉCHEC PAIEMENT' }}
            </div>
            <div style="font-size: 0.9rem; margin-top: 6px;">{{ simResult.message }}</div>
            <div *ngIf="simResult.transactionRef" style="font-size: 0.8rem; color: var(--text-muted); margin-top: 4px;">Ref: {{ simResult.transactionRef }}</div>
          </div>
        </div>
      </div>

      <!-- Tab 3: Fare Calculator -->
      <div *ngIf="activeTab === 'fare'" class="dashboard-grid">
        <div class="card">
          <div class="card-title">Calculateur Automatique de Tarifs</div>
          
          <div class="form-group">
            <label class="form-label">Ligne</label>
            <select [(ngModel)]="fareLineId" (change)="onCalculateFare()" class="form-control">
              <option value="LINE_01">Ligne 01 - Centre Ville / Aéroport</option>
              <option value="LINE_02">Ligne 02 - Corniche & Université</option>
              <option value="LINE_03">Ligne 03 - Zone Industrielle</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Catégorie Passager</label>
            <select [(ngModel)]="farePassengerType" (change)="onCalculateFare()" class="form-control">
              <option value="REGULAR">Régulier</option>
              <option value="STUDENT">Étudiant</option>
              <option value="SENIOR">Senior</option>
            </select>
          </div>

          <div *ngIf="fareResult" style="margin-top: 20px; text-align: center; padding: 20px; background: rgba(56, 189, 248, 0.08); border-radius: 12px; border: 1px solid rgba(56, 189, 248, 0.2);">
            <div class="balance-label">TARIF CALCULÉ</div>
            <div style="font-size: 2.2rem; font-weight: bold; color: var(--accent-cyan);">{{ fareResult.calculatedFare }} {{ fareResult.currency }}</div>
            <div style="font-size: 0.85rem; color: var(--text-muted); margin-top: 4px;">{{ fareResult.lineName }} ({{ fareResult.passengerType }})</div>
          </div>
        </div>
      </div>

      <!-- Tab 4: History -->
      <div *ngIf="activeTab === 'history'" class="card" style="margin-top: 0;">
        <div class="card-title">
          <span>Historique des Transactions</span>
          <button (click)="loadHistory()" class="btn" style="width: auto; padding: 6px 12px; font-size: 0.8rem; background: rgba(255,255,255,0.1);">🔄 Rafraîchir</button>
        </div>

        <div class="table-container">
          <table class="table">
            <thead>
              <tr>
                <th>Référence</th>
                <th>Type</th>
                <th>Montant</th>
                <th>Solde Après</th>
                <th>Description</th>
                <th>Date</th>
                <th>Statut</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let tx of transactions">
                <td style="font-family: monospace; font-weight: 600;">{{ tx.transactionRef }}</td>
                <td>
                  <span [style.color]="tx.type === 'RECHARGE_WAVE' ? 'var(--accent-green)' : (tx.type === 'REFUND' ? 'var(--accent-gold)' : 'var(--accent-cyan)')">
                    {{ tx.type }}
                  </span>
                </td>
                <td style="font-weight: 600;">{{ tx.amount }} XOF</td>
                <td>{{ tx.balanceAfter }} XOF</td>
                <td>{{ tx.description }}</td>
                <td style="font-size: 0.8rem; color: var(--text-muted);">{{ tx.timestamp | date:'dd/MM/yyyy HH:mm:ss' }}</td>
                <td>
                  <span class="status-badge" [ngClass]="{'status-success': tx.status === 'SUCCESS', 'status-failed': tx.status === 'FAILED'}">
                    {{ tx.status }}
                  </span>
                </td>
              </tr>
              <tr *ngIf="transactions.length === 0">
                <td colspan="7" style="text-align: center; color: var(--text-muted); padding: 24px;">Aucune transaction enregistrée.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Tab 5: Fraud & Security -->
      <div *ngIf="activeTab === 'fraud'" class="card" style="margin-top: 0;">
        <div class="card-title">
          <span>Centre de Détection des Fraudes & Sécurité</span>
          <span style="color: var(--accent-red); font-weight: 600;">BONUS INCLUS</span>
        </div>

        <div class="table-container">
          <table class="table">
            <thead>
              <tr>
                <th>Utilisateur / Carte</th>
                <th>Type de Fraude</th>
                <th>Niveau de Risque</th>
                <th>Description</th>
                <th>Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let alert of fraudAlerts">
                <td style="font-family: monospace;">{{ alert.userId }} ({{ alert.cardId }})</td>
                <td><span style="color: var(--accent-red); font-weight: 600;">{{ alert.fraudType }}</span></td>
                <td>
                  <span class="status-badge" style="background: rgba(244,63,94,0.2); color: var(--accent-red);">
                    {{ alert.riskLevel }}
                  </span>
                </td>
                <td>{{ alert.description }}</td>
                <td style="font-size: 0.8rem; color: var(--text-muted);">{{ alert.timestamp | date:'dd/MM/yyyy HH:mm' }}</td>
                <td>
                  <button *ngIf="!alert.resolved" (click)="resolveAlert(alert.id)" class="btn" style="width: auto; padding: 4px 10px; font-size: 0.75rem; background: var(--accent-green); color: #000;">
                    Résoudre
                  </button>
                  <span *ngIf="alert.resolved" style="color: var(--accent-green); font-size: 0.8rem;">Résolu ✔</span>
                </td>
              </tr>
              <tr *ngIf="fraudAlerts.length === 0">
                <td colspan="6" style="text-align: center; color: var(--accent-green); padding: 24px;">Aucune alerte de fraude suspecte détectée.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </div>

    <!-- Wave Recharge Modal -->
    <div *ngIf="showWaveModal" class="modal-overlay">
      <div class="modal-content">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
          <h3 style="color: #fff;">🌊 Simulation Recharge Wave</h3>
          <button (click)="closeWaveModal()" style="background: transparent; border: none; color: var(--text-muted); font-size: 1.5rem; cursor: pointer;">✕</button>
        </div>

        <div class="form-group">
          <label class="form-label">Numéro Mobile Money Wave</label>
          <input type="text" [(ngModel)]="wavePhone" class="form-control" placeholder="+225 07 00 00 00 00">
        </div>

        <div class="form-group">
          <label class="form-label">Montant à Recharger (XOF)</label>
          <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; margin-bottom: 12px;">
            <button (click)="waveAmount = 500" class="btn" style="background: rgba(255,255,255,0.05); color: #fff;">500</button>
            <button (click)="waveAmount = 1000" class="btn" style="background: rgba(255,255,255,0.05); color: #fff;">1000</button>
            <button (click)="waveAmount = 2000" class="btn" style="background: rgba(255,255,255,0.05); color: #fff;">2000</button>
            <button (click)="waveAmount = 5000" class="btn" style="background: rgba(255,255,255,0.05); color: #fff;">5000</button>
          </div>
          <input type="number" [(ngModel)]="waveAmount" class="form-control" placeholder="Montant personnalisé">
        </div>

        <button (click)="submitWaveRecharge()" class="btn btn-wave" style="margin-top: 16px;">
          <span>Confirmer et Recharger {{ waveAmount }} XOF</span>
        </button>
      </div>
    </div>
  `
})
export class AppComponent implements OnInit {
  activeTab: string = 'wallet';

  wallet: Wallet | null = null;
  transactions: PaymentTransaction[] = [];
  fraudAlerts: FraudAlert[] = [];
  stats: StatsResponse | null = null;

  // Wave Modal State
  showWaveModal = false;
  wavePhone = '+221770000000';
  waveAmount = 2000;

  // Ride Simulator State
  simCardId = 'CARD_NFC_883921';
  simLineId = 'LINE_01';
  simPassengerType = 'REGULAR';
  simResult: any = null;

  // Fare State
  fareLineId = 'LINE_01';
  farePassengerType = 'REGULAR';
  fareResult: FareCalculationResponse | null = null;

  constructor(private paymentService: PaymentApiService) {}

  ngOnInit(): void {
    this.loadWallet();
    this.loadHistory();
    this.loadFraudAlerts();
    this.loadStats();
    this.onCalculateFare();
  }

  loadWallet() {
    this.paymentService.getWallet('USR_1001').subscribe(w => this.wallet = w);
  }

  loadHistory() {
    this.paymentService.getUserHistory('USR_1001').subscribe(t => this.transactions = t);
  }

  loadFraudAlerts() {
    this.paymentService.getFraudAlerts().subscribe(a => this.fraudAlerts = a);
  }

  loadStats() {
    this.paymentService.getDashboardStats().subscribe(s => this.stats = s);
  }

  openWaveModal() {
    this.showWaveModal = true;
  }

  closeWaveModal() {
    this.showWaveModal = false;
  }

  submitWaveRecharge() {
    const req: WaveRechargeRequest = {
      userId: 'USR_1001',
      cardId: this.wallet?.cardId || 'CARD_NFC_883921',
      phoneNumber: this.wavePhone,
      amount: this.waveAmount
    };

    this.paymentService.rechargeWave(req).subscribe({
      next: (res) => {
        alert(res.message);
        this.closeWaveModal();
        this.loadWallet();
        this.loadHistory();
        this.loadStats();
      },
      error: (err) => alert('Erreur lors de la recharge Wave: ' + (err.error?.message || err.message))
    });
  }

  simulateRidePayment() {
    const req: RidePaymentRequest = {
      userId: 'USR_1001',
      cardId: this.simCardId,
      rideId: 'RIDE_' + Math.floor(Math.random() * 10000),
      lineId: this.simLineId,
      passengerType: this.simPassengerType
    };

    this.paymentService.processRidePayment(req).subscribe({
      next: (res) => {
        this.simResult = res;
        this.loadWallet();
        this.loadHistory();
        this.loadStats();
        this.loadFraudAlerts();
      },
      error: (err) => {
        this.simResult = {
          status: 'FAILED',
          message: err.error?.message || 'Paiement refusé : Solde insuffisant'
        };
        this.loadStats();
        this.loadFraudAlerts();
      }
    });
  }

  onCalculateFare() {
    const req: FareCalculationRequest = {
      lineId: this.fareLineId,
      passengerType: this.farePassengerType
    };
    this.paymentService.calculateFare(req).subscribe(res => this.fareResult = res);
  }

  resolveAlert(alertId: number) {
    this.paymentService.resolveFraudAlert(alertId).subscribe(() => {
      this.loadFraudAlerts();
      this.loadStats();
    });
  }
}
