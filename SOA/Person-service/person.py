from fastapi import FastAPI, HTTPException, Request
from kafka import KafkaProducer
import uvicorn

app = FastAPI()
producer = None


def get_producer():
    global producer
    if producer is None:
        try:
            producer = KafkaProducer(bootstrap_servers="localhost:9092", max_block_ms=1000)
        except Exception:
            producer = None
    return producer


@app.middleware("http")
async def log_request(request: Request, call_next):
    kafka_producer = get_producer()
    if kafka_producer is not None:
        try:
            message = f"person: {request.method} {request.url.path}?{request.url.query}"
            kafka_producer.send("rest-requests", message.encode())
        except Exception:
            pass
    return await call_next(request)

PERSONS = [
    {"key": "ahRtu7874Gdgd345Tlgd39TyurrG7", "name": "Jakob Pogulis"},  # gitleaks:allow (course mock data, not a secret)
    {"key": "2657TyuhR57575GGhu7", "name": "Xena"},  # gitleaks:allow (course mock data, not a secret)
    {"key": "3Wytrt5638HUTyPWert", "name": "Marcus Bendtsen"},  # gitleaks:allow (course mock data, not a secret)
    {"key": "4786NmbVyt67d232hj", "name": "Zorro"},  # gitleaks:allow (course mock data, not a secret)
    {"key": "5OOpluoRt5612Nhu", "name": "Q"},  # gitleaks:allow (course mock data, not a secret)
    
    {"key": "1", "name": "Jakob Pogulis"},
    {"key": "2", "name": "Xena"},
    {"key": "3", "name": "Marcus Bendtsen"},
    {"key": "4", "name": "Zorro"},
    {"key": "5", "name": "Q"},
]


@app.get("/")
async def root():
    return {"message": "Person service"}


@app.get("/person/list")
async def list_persons():
    return PERSONS

@app.get("/person/find.name")
async def find_person(name: str):
    for person in PERSONS:
        if person["name"] == name:
            return person

    raise HTTPException(status_code=404, detail="Person not found")

@app.get("/person/find.key")
async def find_person_by_key(key: str):
    for person in PERSONS:
        if person["key"] == key:
            return person

    raise HTTPException(status_code=404, detail="Person not found")



if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8060)