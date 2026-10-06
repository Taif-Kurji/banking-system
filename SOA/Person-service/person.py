from fastapi import FastAPI, HTTPException
import uvicorn

app = FastAPI()

PERSONS = [
    {"key": "ahRtu7874Gdgd345Tlgd39TyurrG7", "name": "Jakob Pogulis"},
    {"key": "2657TyuhR57575GGhu7", "name": "Xena"},
    {"key": "3Wytrt5638HUTyPWert", "name": "Marcus Bendtsen"},
    {"key": "4786NmbVyt67d232hj", "name": "Zorro"},
    {"key": "5OOpluoRt5612Nhu", "name": "Q"},
    
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

@app.get("/person/find.name/{name}")
async def find_person(name: str):
    for person in PERSONS:
        if person["name"] == name:
            return person

    raise HTTPException(status_code=404, detail="Person not found")

@app.get("/person/find.key/{key}")
async def find_person_by_key(key: str):
    for person in PERSONS:
        if person["key"] == key:
            return person

    raise HTTPException(status_code=404, detail="Person not found")



if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8060)