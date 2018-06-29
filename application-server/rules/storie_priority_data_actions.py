from business_rules.actions import BaseActions, rule_action
from business_rules.fields import FIELD_NUMERIC

class StoriePriorityDataActions(BaseActions):

    def __init__(self, storie_priority_data):
        self.storie_priority_data = storie_priority_data

    @rule_action(params={"x": FIELD_NUMERIC})
    def inc_priority(self, x):
        self.storie_priority_data.inc_priority(x)
