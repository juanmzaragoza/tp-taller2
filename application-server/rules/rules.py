from business_rules import run_all
from business_rules import engine
from rules.rules_definition import comments_rules, reactions_rules, past_days_rules, friends_rules, stories_rules 
from rules.storie_priority_data import StoriePriorityData
from rules.storie_priority_data_variables import StoriePriorityDataVariables
from rules.storie_priority_data_actions import StoriePriorityDataActions

class RulesMachine:
	
	@staticmethod
	def process_data(data):
		rules = []
		rules.extend(comments_rules)
		rules.extend(reactions_rules)
		rules.extend(past_days_rules)
		rules.extend(friends_rules)
		rules.extend(stories_rules)
		
		run_all(rule_list=rules,
				defined_variables=StoriePriorityDataVariables(data),
				defined_actions=StoriePriorityDataActions(data),
				stop_on_first_trigger=False
			   )
