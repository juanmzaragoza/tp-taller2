user_data_successful_mock = {
    "_id": "5ae66a31d4ef925dac59a90b", 
    "_rev": "", 
    "email": "mock@mock.com", 
    "last_name": "Gonzalez", 
    "name": "Esteban", 
    "picture": ""
}

stories_successful_mock = [{
	"_rev": "", 
	"created_time": "", 
	"description": "Hello World! This is my first Storie!", 
	"location": "", 
	"multimedia": "", 
	"story_type": "normal", 
	"title": "First Storie", 
	"updated_time": "", 
	"visibility": "public"
}]

friends_successful_mock = [{
	"date": "01/05/2018", 
	"last_name": "Fernandez", 
	"name": "Carlos", 
	"user_id": "5ae66a31d4ef925dac59a94e"
}]

rcv_requests_successful_mock = [{
	"date": "01/05/2018", 
	"last_name": "Fernandez", 
	"name": "Maria", 
	"user_id": "5ae66a31d4ef925dac59a94f"
}]

sent_requests_successful_mock = []

profile_successful_mock = {
	"metadata": {
		"version": "1.0"
	}, 
	"profile": {
		"_id": "5ae66a31d4ef925dac59a90b", 
		"_rev": "", 
		"email": "mock@mock.com", 
		"last_name": "Gonzalez", 
		"name": "Esteban", 
		"picture": "",
		"stories": stories_successful_mock,
		"friends": friends_successful_mock, 
		"requests": {
			"rcv": rcv_requests_successful_mock, 
			"sent": sent_requests_successful_mock
		}
	}
}

profile2 = {
	"metadata": {
		"version": "1.0"
	}, 
	"profile": {
		"_id": "5ae66a31d4ef925dac59a94b", 
		"_rev": "d2d5b6bccd234538a0618dd58f522178", 
		"email": "pepe@email.com", 
		"last_name": "Gomez", 
		"name": "Pepe", 
		"picture": ""
	}
}
