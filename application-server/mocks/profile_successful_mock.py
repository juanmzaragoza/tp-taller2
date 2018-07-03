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
	"expired_time" : "",
	"visibility": "public",
	"comments": [],
	"reactions": [],
	"user_name": "userName6",
	"last_name": "heanna",
	"picture": "",
	"name": "kaoru",
	"_id": "5"
}]

friends_successful_mock = [{
	"_id": "5b1af06549628695914f8222",
	"date": "01/05/2018",
	"last_name": "Fernandez",
	"name": "Carlos",
	"user_id": "5ae66a31d4ef925dac59a94e",
	"picture": ""
}]

rcv_requests_successful_mock = [{
	"_id": "5ae66a31d4ef925dac59a94a",
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

profile1 = {
	"metadata": {
		"version": "1.0"
	},
	"profile": {
		"_id": "1",
		"_rev": "",
		"birthday": "01/01/2000",
		"email": "pepe@email.com",
		"friends": [
			{
				"_id": "5ae66fc1d4ef925dar59a95b",
				"date": "06/06/2018 10:10:40",
				"last_name": "fb",
				"name": "Nico",
				"picture": "81fe061232fc4a47abdb29bfcf0d2621",
				"user_id": "10",
				"user_name": "userName1"
			}
		],
		"gender": "M",
		"last_name": "Pepe",
		"name": "Erik",
		"picture": "",
		"requests": {
			"rcv": [],
			"sent": []
		},
		"stories": [
			{
				"_id": "5ae66a31d4ef925dac69a95b",
				"_rev": "",
				"comments": [],
				"created_time": "06/07/2018 10:10:40",
				"description": "Hello World! This is my first Storie!",
				"expired_time": "",
				"last_name": "Pepe",
				"location": "(-34.4,-58.4)",
				"multimedia": "",
				"name": "Erik",
				"picture": "",
				"reactions": {
					"ENJOY": {
						"count": 0,
						"react": None
					},
					"GETBORED": {
						"count": 0,
						"react": None
					},
					"LIKE": {
						"count": 0,
						"react": None
					},
					"NOTLIKE": {
						"count": 0,
						"react": None
					}
				},
				"story_type": "normal",
				"title": "First Storie",
				"updated_time": "",
				"user_id": "1",
				"user_name": "userName4",
				"visibility": "public"
			}
		]
	}
}