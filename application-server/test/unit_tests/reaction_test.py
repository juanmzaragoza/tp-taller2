import unittest
from unittest import mock
from unittest.mock import patch

from models.reaction import ReactionModel


class TestReactionModel(unittest.TestCase):

    @patch('models.storie.MongoController')
    def test_create_reaction(self, mock_db):
        mock_db_instance = mock.MagicMock()
        mock_db.return_value.get_mongodb_instance.return_value = mock_db_instance

        storie_id = '5ae66a31d4ef925dac59a95b'
        user_id = "10"
        result = ReactionModel.create_reaction({"storie_id": storie_id, "user_id": user_id, "reaction": "LIKE"})
        self.assertEqual(user_id, result["user_id"])
        self.assertEqual(storie_id, result["storie_id"])
        self.assertEqual("LIKE", result["reaction"])
