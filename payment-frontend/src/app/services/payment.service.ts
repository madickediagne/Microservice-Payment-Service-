import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { 
  Wallet, 
  PaymentTransaction, 
  WaveRechargeRequest, 
  WaveRechargeResponse, 
  RidePaymentRequest, 
  RidePaymentResponse,
  FareCalculationRequest,
  FareCalculationResponse,
  FraudAlert,
  StatsResponse 
} from '../models/payment.models';

@Injectable({
  providedIn: 'root'
})
export class PaymentApiService {

  private baseUrl = 'http://localhost:8083/api';

  constructor(private http: HttpClient) { }

  getWallet(userId: string): Observable<Wallet> {
    return this.http.get<Wallet>(`${this.baseUrl}/wallet/${userId}`).pipe(
      catchError(() => of({
        userId: userId,
        cardId: 'CARD_NFC_883921',
        balance: 5000,
        currency: 'XOF',
        status: 'ACTIVE'
      }))
    );
  }

  rechargeWave(request: WaveRechargeRequest): Observable<WaveRechargeResponse> {
    return this.http.post<WaveRechargeResponse>(`${this.baseUrl}/payments/wave/recharge`, request);
  }

  processRidePayment(request: RidePaymentRequest): Observable<RidePaymentResponse> {
    return this.http.post<RidePaymentResponse>(`${this.baseUrl}/payments/process-ride`, request);
  }

  getUserHistory(userId: string): Observable<PaymentTransaction[]> {
    return this.http.get<PaymentTransaction[]>(`${this.baseUrl}/payments/history/${userId}`).pipe(
      catchError(() => of([]))
    );
  }

  getRecentTransactions(): Observable<PaymentTransaction[]> {
    return this.http.get<PaymentTransaction[]>(`${this.baseUrl}/payments/recent`).pipe(
      catchError(() => of([]))
    );
  }

  calculateFare(request: FareCalculationRequest): Observable<FareCalculationResponse> {
    return this.http.post<FareCalculationResponse>(`${this.baseUrl}/fares/calculate`, request);
  }

  getFraudAlerts(): Observable<FraudAlert[]> {
    return this.http.get<FraudAlert[]>(`${this.baseUrl}/fraud/alerts`).pipe(
      catchError(() => of([]))
    );
  }

  resolveFraudAlert(alertId: number): Observable<FraudAlert> {
    return this.http.put<FraudAlert>(`${this.baseUrl}/fraud/resolve/${alertId}`, {});
  }

  getDashboardStats(): Observable<StatsResponse> {
    return this.http.get<StatsResponse>(`${this.baseUrl}/stats/dashboard`).pipe(
      catchError(() => of({
        totalRecharged: 7000,
        totalDebited: 500,
        totalTransactionsCount: 3,
        successCount: 3,
        failedCount: 0,
        activeAlertsCount: 1
      }))
    );
  }
}
