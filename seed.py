import requests
import random
from datetime import datetime, timedelta

API_URL = "http://localhost:8080/api/v1"
REGISTER_PATH = "/auth/register"
PLAYER_PATH = "/players"
HEADERS = {"Content-Type": "application/json"}

first_names = [
    "Michael", "Sarah", "David", "Emily", "John", "Alice", "Daniel", "Grace",
    "Kevin", "Olivia", "James", "Emma", "Ryan", "Sophia", "Matthew", "Chloe",
    "Brian", "Ava", "Eric", "Lily", "Justin", "Zoe", "Andrew", "Mia",
    "Jason", "Ella", "Chris", "Nora", "Tyler", "Aria"
]

last_names = [
    "Yi", "Kim", "Lee", "Johnson", "Smith", "Davis", "Brown", "Garcia",
    "Martinez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson",
    "White", "Harris", "Martin", "Thompson", "Lewis", "Clark", "Walker",
    "Robinson", "Hall", "Allen", "Young", "King", "Wright", "Scott", "Green"
]

positions = ["Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"]

bios = [
    "Basketball is my passion.",
    "Always grinding.",
    "Ready to hoop.",
    "Love the energy of the game.",
    "Born to ball.",
    "Defense wins games.",
    "Fast, focused, fearless.",
    "Let's run it back.",
    "Team player first.",
    "Catch me at the arc."
]

yoe = [
    "1", "0", "3", "2", "5", "1", "4", "2", "0", "6",
    "3", "1", "2", "4", "0", "5", "2", "3", "1", "6",
    "0", "2", "4", "3", "1", "5", "0", "2", "1", "3"
]

COURT_ONE_ID = "87ce20fc-9ff8-48ca-94de-183ef8608037"
COURT_TWO_ID = "7133604d-9d8a-4c95-9059-6d9762244fca"
COURT_THREE_ID = "57a09f96-3c3f-4efb-809c-5e872f8db81d"

def random_dob():
    start = datetime.strptime("1998-01-01", "%Y-%m-%d")
    end = datetime.strptime("2006-12-31", "%Y-%m-%d")
    random_date = start + timedelta(days=random.randint(0, (end - start).days))
    return random_date.strftime("%Y-%m-%d")

def random_height():
    feet = random.randint(5, 6)
    inches = random.randint(0, 11)
    return f"{feet}'{inches}\""

def random_weight():
    return f"{random.randint(120, 220)} lbs"

for i in range(30):
    first_name = first_names[i]
    last_name = last_names[i]
    email = f"{first_name.lower()}{last_name.lower()}@example.com"

    user_payload = {
        "firstName": first_name,
        "lastName": last_name,
        "email": email,
        "password": "password"
    }

    res = requests.post(API_URL + REGISTER_PATH, json=user_payload, headers=HEADERS)

    if res.status_code not in [200, 201, 204]:
        print(f"❌ Failed to register {email}: {res.status_code} - {res.text}")
        continue

    print(f"✅ Registered {first_name} {last_name} <{email}>")
    user_id = res.json().get("userId")

    if not user_id:
        print(f"⚠️  No user_id returned in response: {res.text}")
        continue

    player_payload = {
        "userId": user_id,
        "bio": random.choice(bios),
        "height": random_height(),
        "weight": random_weight(),
        "position": positions[i % len(positions)],
        "yearsOfExperience": yoe[i % len(yoe)],
        "dateOfBirth": random_dob()
    }

    res = requests.post(API_URL + PLAYER_PATH, json=player_payload, headers=HEADERS)
    if res.status_code not in [200, 201, 204]:
        print(f"❌ Failed to create player for {email}: {res.status_code} - {res.text}")
        continue
    print(f"🏀 Created player profile for {first_name} {last_name}")
    player_id = res.json().get("playerId")

    yoe_value = int(yoe[i % len(yoe)])
    if yoe_value <= 2:
        court_id = COURT_ONE_ID
    elif yoe_value <= 4:
        court_id = COURT_TWO_ID
    else:
        court_id = COURT_THREE_ID

    queue_payload = {
        "courtId": court_id,
        "playerId": player_id
    }

    res = requests.post(f"{API_URL}/courts/queue", json=queue_payload, headers=HEADERS)
    if res.status_code not in [200, 201, 204]:
        print(f"❌ Failed to queue player {email}: {res.status_code} - {res.text}")
        continue
    print(f"⏳ Player {first_name} {last_name} queued on court {court_id}")

requests.post(f"{API_URL}/courts/next-team",
              json= {"courtId": COURT_ONE_ID, "teamId": 1},
              headers=HEADERS)

requests.post(f"{API_URL}/courts/next-team",
              json= {"courtId": COURT_ONE_ID, "teamId": 2},
              headers=HEADERS)

requests.post(f"{API_URL}/courts/next-team",
              json= {"courtId": COURT_TWO_ID, "teamId": 1},
              headers=HEADERS)

requests.post(f"{API_URL}/courts/next-team",
              json= {"courtId": COURT_THREE_ID, "teamId": 1},
              headers=HEADERS)
