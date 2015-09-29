ZC-AccountBalanceCache
======================

In-memory account balance cache.

History
=======

2015-03-12: v1.0.7.2
--------------------
- Minor enhancement in algorithm to solve duplicated trans.


2015-03-10: v1.0.7.1
--------------------
- Implement: "Refresh cache period to subtracting" trying to fix the negative balance.


2015-02-01: v1.0.6
------------------
- Rolled-back the fix on 2015-01-12 as it does not solve the "transactions not in order" issue.
- Increase cache size (better solution to fix the issue of 2015-01-15).


2015-01-15: v1.0.5
------------------
- More loggings.
- Try to prevent a rare case when balance-cache's method is called multiple times by PaymentZkBackend


2015-01-14: v1.0.0
------------------
First release:

- Viết lại module AccountBalanceCache cũ.
