-- payment-sevice
CREATE INDEX idx_transactionid ON billingslip (transactionid);
CREATE INDEX idx_transactionrevisionid ON billingslipforrevision (transactionrevisionid);
CREATE INDEX idx_billingslipid ON orderpayment (billingslipid);
