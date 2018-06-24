class StoriePriorityData():

	def __init__(self, storie_id, past, num_comments, num_reactions, num_friends, num_stories):
		self.storie_id = storie_id
		self.past_days = past
		self.num_comments = num_comments
		self.num_reactions = num_reactions
		self.num_friends = num_friends
		self.num_stories = num_stories
		self.priority = 0
	
	def get_storie_id(self):
		return self.storie_id
	
	def get_past_days(self):
		return self.past_days
	
	def get_num_comments(self):
		return self.num_comments
	
	def get_num_reactions(self):
		return self.num_reactions
	
	def get_num_friends(self):
		return self.num_friends
	
	def get_num_stories(self):
		return self.num_stories
	
	def get_priority(self):
		return self.priority
		
	def inc_priority(self, value):
		self.priority += value
