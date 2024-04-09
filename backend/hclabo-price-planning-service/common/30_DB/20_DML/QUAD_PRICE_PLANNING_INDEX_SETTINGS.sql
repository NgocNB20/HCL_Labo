-- price-planning-service
CREATE INDEX idx_transactionid ON salesslip (transactionid);
CREATE INDEX idx_salesslipid ON itempurchasepricesubtotal (salesslipid);
CREATE INDEX idx_transactionrevisionid ON salesslipforrevision (transactionrevisionid);
CREATE INDEX idx_salessliprevisionid ON itempurchasepricesubtotalforrevision (salessliprevisionid);
