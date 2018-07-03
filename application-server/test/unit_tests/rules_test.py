import unittest

from rules.rules import RulesMachine
from rules.storie_priority_data import StoriePriorityData
from rules.storie_priority_data_variables import StoriePriorityDataVariables


class TestRulesMachine(unittest.TestCase):

    def test_rules_priority_increment(self):
        storie_id = 1
        past_days = 1
        num_comments = 10
        num_reactions = 15
        num_friends = 10
        num_stories = 5

        storiePD = StoriePriorityData(
            storie_id,
            past_days,
            num_comments,
            num_reactions,
            num_friends,
            num_stories,
            )

        priority1 = storiePD.get_priority()
        storiePD.inc_priority(5)
        priority2 = storiePD.get_priority()

        self.assertTrue(priority1 == 0)
        self.assertTrue(priority2 == 5)

    def test_storie_variables_successfull_load(self):
        storie_id = 1
        past_days = 1
        num_comments = 10
        num_reactions = 15
        num_friends = 10
        num_stories = 5

        storiePD = StoriePriorityData(
            storie_id,
            past_days,
            num_comments,
            num_reactions,
            num_friends,
            num_stories,
            )

        storieDV = StoriePriorityDataVariables(storiePD)

        self.assertEqual(storieDV.current_num_comments(), num_comments)
        self.assertEqual(storieDV.current_num_reactions(),
                         num_reactions)
        self.assertEqual(storieDV.current_num_past_days(), past_days)
        self.assertEqual(storieDV.current_num_friends(), num_friends)
        self.assertEqual(storieDV.current_num_stories(), num_stories)

    def test_rules_successfull_process(self):
        storie_id = 1
        past_days = 1
        num_comments = 10
        num_reactions = 15
        num_friends = 10
        num_stories = 5

        storiePD = StoriePriorityData(
            storie_id,
            past_days,
            num_comments,
            num_reactions,
            num_friends,
            num_stories,
            )

        RulesMachine.process_data(storiePD)
        priority = storiePD.get_priority()

        self.assertTrue(priority > 0)

    def test_rules_successfull_process_priority_125(self):
        storie_id = 1
        past_days = 1
        num_comments = 10
        num_reactions = 15
        num_friends = 10
        num_stories = 5

        storiePD = StoriePriorityData(
            storie_id,
            past_days,
            num_comments,
            num_reactions,
            num_friends,
            num_stories,
            )
        RulesMachine.process_data(storiePD)
        priority = storiePD.get_priority()

        self.assertEqual(priority, 125)
