from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_login import LoginManager
from flask_mail import Mail
from config import Config


db = SQLAlchemy()
migrate = Migrate()
login = LoginManager()
mail = Mail()


def create_app(config_class=Config):
    app = Flask(__name__)
    app.config.from_object(config_class)

    db.init_app(app)
    migrate.init_app(app, db)
    login.init_app(app)
    mail.init_app(app)

    login.login_view = 'auth.login'
    login.login_message_category = 'danger'

    with app.app_context():
        from app.blueprints.auth import bp as auth
        app.register_blueprint(auth)

        from app.blueprints.blog import bp as blog
        app.register_blueprint(blog)

        from app.blueprints.main import bp as main
        app.register_blueprint(main)

    return app