import flask
import flask_restful

app = flask.Flask(__name__)
api = flask_restful.Api(app)


class HelloWorld(flask_restful.Resource):
    def get(self):
        return {'hello': 'world'}

api.add_resource(HelloWorld, '/')

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5858,debug=True)
