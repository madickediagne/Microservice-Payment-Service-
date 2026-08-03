export interface Wallet {
  id?: number;
  userId: string;
  cardId: string;
  balance: number;
  currency: string;
  status: string;
  updatedAt?: string;
}

export interface PaymentTransaction {
  id?: number;
  transactionRef: string;
  userId: string;
  cardId: string;
  type: 'RECHARGE_WAVE' | 'RIDE_DEBIT' | 'REFUND';
  amount: number;
  balanceAfter: number;
  currency: string;
  status: 'SUCCESS' | 'FAILED' | 'PENDING';
  paymentMethod?: string;
  rideId?: string;
  lineId?: string;
  busId?: string;
  description: string;
  timestamp: string;
}

export interface WaveRechargeRequest {
  userId: string;
  cardId?: string;
  phoneNumber: string;
  amount: number;
  otpCode?: string;
}

export interface WaveRechargeResponse {
  transactionRef: string;
  userId: string;
  cardId: string;
  amount: number;
  newBalance: number;
  currency: string;
  status: string;
  message: string;
  wavePaymentUrl: string;
  timestamp: string;
}

export interface RidePaymentRequest {
  userId?: string;
  cardId: string;
  rideId: string;
  lineId: string;
  busId?: string;
  passengerType?: string;
  fixedFare?: number;
}

export interface RidePaymentResponse {
  transactionRef: string;
  userId: string;
  cardId: string;
  rideId: string;
  debitedAmount: number;
  remainingBalance: number;
  status: string;
  message: string;
  timestamp: string;
}

export interface FareCalculationRequest {
  lineId: string;
  passengerType: string;
}

export interface FareCalculationResponse {
  lineId: string;
  lineName: string;
  passengerType: string;
  calculatedFare: number;
  currency: string;
}

export interface FraudAlert {
  id: number;
  userId: string;
  cardId: string;
  fraudType: string;
  riskLevel: string;
  description: string;
  timestamp: string;
  resolved: boolean;
}

export interface StatsResponse {
  totalRecharged: number;
  totalDebited: number;
  totalTransactionsCount: number;
  successCount: number;
  failedCount: number;
  activeAlertsCount: number;
}
