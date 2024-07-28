Technologies Used:
Java 22
Spring Boot 3
Spring Data
Map Struct
Lombok
PostgreSQL 16
JUnit
Mockito
AssertJ

API Requests:

1) API to Get Campaigns for a Client Id:

Request:

curl --location 'http://localhost:8080/api/campaign?client_id=1'

Sample Response:

{
    "campaigns": [
        {
            "id": 7,
            "name": "Campaign2",
            "subject": "Subject1",
            "status": "SENT",
            "email_body": "Body1",
            "subscribers": [
                "a@a.com",
                "b@b.com"
            ],
            "client_id": 1,
            "client_email": "a@a.com"
        }
    ]
}

2) API to Create Campaign:

Request:

curl --location 'http://localhost:8080/api/campaign' \
--header 'Content-Type: application/json' \
--data-raw '{
    "client_id": 1,
    "name": "Campaign2",
    "subject": "Subject1",
    "email_body": "Body1",
    "subscribers": ["a@a.com", "b@b.com"]
}'

Sample Response:

{
    "id": 7,
    "name": "Campaign2",
    "subject": "Subject1",
    "status": "DRAFT",
    "email_body": "Body1",
    "subscribers": [
        "a@a.com",
        "b@b.com"
    ],
    "client_id": 1,
    "client_email": "a@a.com"
}

3) API to send campaign email:

Request:

curl --location 'http://localhost:8080/api/campaign/send_email' \
--header 'Content-Type: application/json' \
--data '{
    "id": 7
}'

//in the above request, id is campaign id

Sample Response:

{
    "b@b.com": "Email Sent",
    "a@a.com": "Email Sent"
}
