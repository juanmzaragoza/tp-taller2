import flask
import flask_restful
from controllers.login_controller import LoginController


app = flask.Flask(__name__)
api = flask_restful.Api(app)


class HelloWorld(flask_restful.Resource):
    def get(self):
        return {'hello': 'world'}

api.add_resource(HelloWorld, '/')
api.add_resource(LoginController, '/token')

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5858,debug=True)
