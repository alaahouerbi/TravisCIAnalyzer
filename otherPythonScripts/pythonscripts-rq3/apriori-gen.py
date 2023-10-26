import csv
import time
import pickle as pkl
from efficient_apriori import apriori

import pandas as pd

transactions=pkl.load(open('allTransactions.pkl', 'rb'))
#df = pd.read_csv('transactions/transactions_H2.csv', sep='";"', on_bad_lines='skip', engine='python')
#transactions = df.iloc[:, [0,1]].astype(str).values.tolist()

#transactions_4 = df.iloc[:, [1,3]].astype(str).values.tolist()

#print(transactions[1])
#print(transactions_2[1])
# print(transactions_3[1])
#print(transactions_4[1])
# exit(0)
# Get the current time in seconds before the task

itemsets, rules = apriori(transactions,min_confidence=0.5, min_support=0.000001, verbosity=1,max_length=2)

#itemsets_4, rules_4 = apriori(transactions_4,min_confidence=0.000001, min_support=0.000001, verbosity=1, max_length=2)


csv_out = open('rules_v2(low_support).csv','w+')
csvwriter = csv.writer(csv_out)
csvwriter.writerow(['rule_lhf', 'rule_rhs', 'conf', 'lift', 'supp', 'conv'])
rules.sort(key=lambda x: x.confidence, reverse=True)
for rule in rules:
    csvwriter.writerow([rule.lhs, rule.rhs, rule.confidence, rule.lift, rule.support, rule.conviction])
    
