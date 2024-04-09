-- promotion-service
CREATE INDEX idx_orderslipid ON orderitem (orderslipid);
CREATE INDEX idx_transactionid ON orderslip (transactionid);
CREATE INDEX idx_customerid_orderstatus ON orderslip (customerid, orderstatus);
CREATE INDEX idx_transactionrevisionid ON orderslipforrevision (transactionrevisionid);
