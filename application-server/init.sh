#!/bin/sh
pip install -r requirements.txt
python rules/test.py &
gunicorn -w 4 -b 0.0.0.0:5858 app:app --log-file=- --reload
