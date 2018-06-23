from durable.lang import *

REDIS_PASSWD = 'SaCQrlgw1UHvZliIHjW3CBh2pCFy2uNv'
REDIS_PORT = 14298
REDIS_HOST = 'redis-14298.c11.us-east-1-2.ec2.cloud.redislabs.com'

with ruleset('importance'):

    # antecedent
    @when_all(m.subject == 'renew')
    def renew(c):
        # consequent
        c.s.result = {}


    # antecedent
    @when_all(
			  (m.subject == 'comments') & 
			  (m.storie_data.num_comments < 5)
	)
    def comments_range_0_5(c):
        # consequent
        add_importance(c, 1)

    # antecedent
    @when_all(
			  (m.subject == 'comments') & 
			  (m.storie_data.num_comments >= 5) &
			  (m.storie_data.num_comments < 10)
	)
    def comments_range_5_10(c):
        # consequent
        add_importance(c, 10)
	
	# antecedent
    @when_all(
			  (m.subject == 'comments') & 
			  (m.storie_data.num_comments >= 10) &
			  (m.storie_data.num_comments < 20)
	)
    def comments_range_10_20(c):
        # consequent
        add_importance(c, 20)
    
    # antecedent
    @when_all(
			  (m.subject == 'comments') & 
			  (m.storie_data.num_comments >= 20)
	)
    def comments_ge_20(c):
        # consequent
        add_importance(c, 30)
    
    # antecedent
    @when_all(
			  (m.subject == 'reactions') & 
			  (m.storie_data.num_reactions < 5)
	)
    def reactions_range_0_5(c):
        # consequent
        add_importance(c, 1)

    # antecedent
    @when_all(
			  (m.subject == 'reactions') & 
			  (m.storie_data.num_reactions >= 5) &
			  (m.storie_data.num_reactions < 10)
	)
    def reactions_range_5_10(c):
        # consequent
        add_importance(c, 5)
	
	# antecedent
    @when_all(
			  (m.subject == 'reactions') & 
			  (m.storie_data.num_reactions >= 10) &
			  (m.storie_data.num_reactions < 20)
	)
    def reactions_range_10_20(c):
        # consequent
        add_importance(c, 10)
    
    # antecedent
    @when_all(
			  (m.subject == 'reactions') & 
			  (m.storie_data.num_reactions >= 20) &
			  (m.storie_data.num_reactions < 50)
	)
    def reactions_range_20_50(c):
        # consequent
        add_importance(c, 20)
    
    # antecedent
    @when_all(
			  (m.subject == 'reactions') & 
			  (m.storie_data.num_reactions >= 50)
	)
    def reactions_ge_50(c):
        # consequent
        add_importance(c, 30)
    
    # antecedent
    @when_all(
			  (m.subject == 'stories') &
			  (m.user_data.num_stories < 5)
	)
    
    def stories_range_0_5(c):
        # consequent
        add_importance(c, 1)

    # antecedent
    @when_all(
			  (m.subject == 'stories') & 
			  (m.user_data.num_stories >= 5) &
			  (m.user_data.num_stories < 10)
	)
    def stories_range_5_10(c):
        # consequent
        add_importance(c, 10)
	
	# antecedent
    @when_all(
			  (m.subject == 'stories') & 
			  (m.user_data.num_stories >= 10) &
			  (m.user_data.num_stories < 20)
	)
    def stories_range_10_20(c):
        # consequent
        add_importance(c, 15)
    
    # antecedent
    @when_all(
			  (m.subject == 'stories') & 
			  (m.user_data.num_stories >= 20) &
			  (m.user_data.num_stories < 50)
	)
    def stories_range_20_50(c):
        # consequent
        add_importance(c, 30)
    
    # antecedent
    @when_all(
			  (m.subject == 'stories') & 
			  (m.user_data.num_stories >= 50)
	)
    def stories_ge_50(c):
        # consequent
        add_importance(c, 50)
    
    # antecedent
    @when_all(
			  (m.subject == 'friends') &
			  (m.user_data.num_friends < 5)
	)
    
    def friends_range_0_5(c):
        # consequent
        add_importance(c, 0)

    # antecedent
    @when_all(
			  (m.subject == 'friends') & 
			  (m.user_data.num_friends >= 5) &
			  (m.user_data.num_friends < 10)
	)
    def friends_range_5_10(c):
        # consequent
        add_importance(c, 5)
	
	# antecedent
    @when_all(
			  (m.subject == 'friends') & 
			  (m.user_data.num_friends >= 10) &
			  (m.user_data.num_friends < 20)
	)
    def friends_range_10_20(c):
        # consequent
        add_importance(c, 15)
    
    # antecedent
    @when_all(
			  (m.subject == 'friends') & 
			  (m.user_data.num_friends >= 20) &
			  (m.user_data.num_friends < 50)
	)
    def friends_range_20_50(c):
        # consequent
        add_importance(c, 30)
    
    # antecedent
    @when_all(
			  (m.subject == 'friends') & 
			  (m.user_data.num_friends >= 50)
	)
    def friends_ge_50(c):
        # consequent
        add_importance(c, 50)
    
   # antecedent
    @when_all(
			  (m.subject == 'date') & 
			  (m.storie_data.past < 1)
	)
    def date_past_0_1(c):
        # consequent
        add_importance(c, 100)
    
    # antecedent
    @when_all(
			  (m.subject == 'date') & 
			  (m.storie_data.past >= 1) &
			  (m.storie_data.past < 2)
	)
    def date_past_1_2(c):
        # consequent
        add_importance(c, 90)
    
    # antecedent
    @when_all(
			  (m.subject == 'date') & 
			  (m.storie_data.past >= 2) &
			  (m.storie_data.past <= 3)
	)
    def date_past_2_3(c):
        # consequent
        add_importance(c, 80)
   
   # antecedent
    @when_all(
			  (m.subject == 'date') & 
			  (m.storie_data.past >= 4) &
			  (m.storie_data.past <= 5)
	)
    def date_past_4_5(c):
        # consequent
        add_importance(c, 60)
    
    # antecedent
    @when_all(
			  (m.subject == 'date') & 
			  (m.storie_data.past > 5) &
			  (m.storie_data.past < 10)
	)
    def date_past_5_10(c):
        # consequent
        add_importance(c, 30)
    
    # antecedent
    @when_all(
			  (m.subject == 'date') & 
			  (m.storie_data.past > 10)
	)
    def date_ge_10(c):
        # consequent
        add_importance(c, 0)
        
    def add_importance(c, importance):
        if c.m.storie_data.storie_id not in c.s.result:
            c.s.result[c.m.storie_data.storie_id] = 0
        
        c.s.result[c.m.storie_data.storie_id] += importance
	

run_all([{'host': REDIS_HOST, 'port': REDIS_PORT, 'password': REDIS_PASSWD}])
