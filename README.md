# RepoViewer API

This API allows users to retrieve a list of GitHub repositories associated with a specific username while filtering out forked repositories.

# API Endpoints

## Get list of repositories for an existing user

### Request

`GET /api/repository-management/{username}`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/repository-management/user

### Response

    [
      {
        "name": "repository_name1",
        "owner_login": "name",
        "branches": [
            {
                "name": "branch1",
                "last_commit_sha": "sha1"
            },
            {
                "name": "branch2",
                "last_commit_sha": "sha2"
            }
        ]
      },
      {
        "name": "repository_name2",
        "owner_login": "name",
        "branches": [
            {
                "name": "branch1",
                "last_commit_sha": "sha1"
            },
            {
                "name": "branch2",
                "last_commit_sha": "sha2"
            }
        ]
      }
    ]

## Get list of repositories for an existing user with no repositories

### Request

`GET /api/repository-management/{username}`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/repository-management/userNoRepos

### Response

    []

## Get list of repositories for an non-existing user

### Request

`GET /api/repository-management/{username}`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/repository-management/non-existing

### Response

    {
      "statusCode": 404,
      "message": "User not found."
    }

## Get list of repositories with application/xml accept header

### Request

`GET /api/repository-management/{username}`

    curl -i -H 'Accept: application/xml' http://localhost:8080/api/repository-management/user

### Response

    {
      "statusCode": 406,
      "message": "application/xml not acceptable."
    }


