from business_rules.variables import BaseVariables, numeric_rule_variable


class StoriePriorityDataVariables(BaseVariables):

    def __init__(self, storie_priority_data):
        self.storie_priority_data = storie_priority_data

    @numeric_rule_variable
    def current_num_comments(self):
        return self.storie_priority_data.get_num_comments()

    @numeric_rule_variable
    def current_num_reactions(self):
        return self.storie_priority_data.get_num_reactions()

    @numeric_rule_variable
    def current_num_past_days(self):
        return self.storie_priority_data.get_past_days()

    @numeric_rule_variable
    def current_num_friends(self):
        return self.storie_priority_data.get_num_friends()

    @numeric_rule_variable
    def current_num_stories(self):
        return self.storie_priority_data.get_num_stories()
