# Person service

## Setup and run

From this directory, create or repair the virtual environment and install the
service dependencies:

```bash
python3 -m venv --upgrade venv
./venv/bin/python -m pip install -r requirements.txt
```

Run the service with the virtual environment's interpreter:

```bash
./venv/bin/python person.py
```

The service listens on <http://127.0.0.1:8060>. If you activate the virtual
environment, use `python3 person.py`; do not use a system interpreter outside
the environment.
