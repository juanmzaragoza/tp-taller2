comments_rules = [
# comments < 5
{ 
  "conditions": { "all": [{"name": "current_num_comments", "operator": "less_than","value": 5}]},
  "actions": [{"name": "inc_priority", "params": {"x": 1}}]
},
# comments >= 5 and comments < 10
{ 
  "conditions": { "all": [
							{"name": "current_num_comments", "operator": "greater_than_or_equal_to","value": 5},
							{"name": "current_num_comments", "operator": "less_than","value": 10}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 5}}]
},
# comments >= 10 and comments < 20
{ 
  "conditions": { "all": [
							{"name": "current_num_comments", "operator": "greater_than_or_equal_to","value": 10},
							{"name": "current_num_comments", "operator": "less_than","value": 20}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 10}}]
},
# comments >= 20 and comments < 50
{ 
  "conditions": { "all": [
							{"name": "current_num_comments", "operator": "greater_than_or_equal_to","value": 20},
							{"name": "current_num_comments", "operator": "less_than","value": 50}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 20}}]
},
# comments >= 50
{ 
  "conditions": { "all": [{"name": "current_num_comments", "operator": "greater_than_or_equal_to","value": 50}]},
  "actions": [{"name": "inc_priority", "params": {"x": 30}}]
}
]

reactions_rules = [
# reactions < 5
{ 
  "conditions": { "all": [{"name": "current_num_reactions", "operator": "less_than","value": 5}]},
  "actions": [{"name": "inc_priority", "params": {"x": 1}}]
},
# reactions >= 5 and reactions < 10
{ 
  "conditions": { "all": [
							{"name": "current_num_reactions", "operator": "greater_than_or_equal_to","value": 5},
							{"name": "current_num_reactions", "operator": "less_than","value": 10}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 5}}]
},
# reactions >= 10 and reactions < 20
{ 
  "conditions": { "all": [
							{"name": "current_num_reactions", "operator": "greater_than_or_equal_to","value": 10},
							{"name": "current_num_reactions", "operator": "less_than","value": 20}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 10}}]
},
# reactions >= 20 and reactions < 50
{ 
  "conditions": { "all": [
							{"name": "current_num_reactions", "operator": "greater_than_or_equal_to","value": 20},
							{"name": "current_num_reactions", "operator": "less_than","value": 50}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 20}}]
},
# reactions >= 50
{ 
  "conditions": { "all": [{"name": "current_num_reactions", "operator": "greater_than_or_equal_to","value": 50}]},
  "actions": [{"name": "inc_priority", "params": {"x": 30}}]
}
]

past_days_rules = [
# past_days < 1
{ 
  "conditions": { "all": [{"name": "current_num_past_days", "operator": "less_than","value": 1}]},
  "actions": [{"name": "inc_priority", "params": {"x": 100}}]
},
# past_days >= 1 and past_days < 2
{ 
  "conditions": { "all": [
							{"name": "current_num_past_days", "operator": "greater_than_or_equal_to","value": 1},
							{"name": "current_num_past_days", "operator": "less_than","value": 2}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 90}}]
},
# past_days >= 2 and past_days < 4
{ 
  "conditions": { "all": [
							{"name": "current_num_past_days", "operator": "greater_than_or_equal_to","value": 2},
							{"name": "current_num_past_days", "operator": "less_than","value": 4}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 70}}]
},
# past_days >= 4 and past_days < 8
{ 
  "conditions": { "all": [
							{"name": "current_num_past_days", "operator": "greater_than_or_equal_to","value": 4},
							{"name": "current_num_past_days", "operator": "less_than","value": 8}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 50}}]
},
# past_days >= 8
{ 
  "conditions": { "all": [{"name": "current_num_past_days", "operator": "greater_than_or_equal_to","value": 8}]},
  "actions": [{"name": "inc_priority", "params": {"x": 10}}]
}
]

friends_rules = [
# friends < 5
{ 
  "conditions": { "all": [{"name": "current_num_friends", "operator": "less_than","value": 5}]},
  "actions": [{"name": "inc_priority", "params": {"x": 1}}]
},
# friends >= 5 and friends < 10
{ 
  "conditions": { "all": [
							{"name": "current_num_friends", "operator": "greater_than_or_equal_to","value": 5},
							{"name": "current_num_friends", "operator": "less_than","value": 10}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 5}}]
},
# friends >= 10 and friends < 20
{ 
  "conditions": { "all": [
							{"name": "current_num_friends", "operator": "greater_than_or_equal_to","value": 10},
							{"name": "current_num_friends", "operator": "less_than","value": 20}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 10}}]
},
# friends >= 20 and friends < 50
{ 
  "conditions": { "all": [
							{"name": "current_num_friends", "operator": "greater_than_or_equal_to","value": 20},
							{"name": "current_num_friends", "operator": "less_than","value": 50}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 20}}]
},
# friends >= 50
{ 
  "conditions": { "all": [{"name": "current_num_friends", "operator": "greater_than_or_equal_to","value": 50}]},
  "actions": [{"name": "inc_priority", "params": {"x": 30}}]
}
]

stories_rules = [
# stories < 5
{ 
  "conditions": { "all": [{"name": "current_num_stories", "operator": "less_than","value": 5}]},
  "actions": [{"name": "inc_priority", "params": {"x": 1}}]
},
# stories >= 5 and stories < 10
{ 
  "conditions": { "all": [
							{"name": "current_num_stories", "operator": "greater_than_or_equal_to","value": 5},
							{"name": "current_num_stories", "operator": "less_than","value": 10}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 5}}]
},
# stories >= 10 and stories < 20
{ 
  "conditions": { "all": [
							{"name": "current_num_stories", "operator": "greater_than_or_equal_to","value": 10},
							{"name": "current_num_stories", "operator": "less_than","value": 20}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 10}}]
},
# stories >= 20 and stories < 50
{ 
  "conditions": { "all": [
							{"name": "current_num_stories", "operator": "greater_than_or_equal_to","value": 20},
							{"name": "current_num_stories", "operator": "less_than","value": 50}
						 ]
				},
  "actions": [{"name": "inc_priority", "params": {"x": 20}}]
},
# stories >= 50
{ 
  "conditions": { "all": [{"name": "current_num_stories", "operator": "greater_than_or_equal_to","value": 50}]},
  "actions": [{"name": "inc_priority", "params": {"x": 30}}]
}
]
