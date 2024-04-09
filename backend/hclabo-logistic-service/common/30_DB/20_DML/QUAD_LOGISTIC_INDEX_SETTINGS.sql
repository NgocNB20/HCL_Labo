-- logistic-service
CREATE INDEX idx_transactionid ON shippingslip (transactionid);
CREATE INDEX idx_shippingslipid ON securedshippingitem (shippingslipid);
CREATE INDEX idx_transactionrevisionid ON shippingslipforrevision (transactionrevisionid);
CREATE INDEX idx_shippingsliprevisionid ON securedshippingitemforrevision (shippingsliprevisionid);
CREATE INDEX idx_customerid ON addressbook (customerid);
