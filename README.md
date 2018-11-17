# spring-reactive-primes

Reactive Spring application serving first n prime numbers

## Usage

To build the project run

```shell
gradlew build
```

To start the application use

```shell
gradlew bootRun
```

## API

### /primes

#### Method

`GET`

#### Request Parameters

| Parameter | Type    | Description        |      Required?     | Possible Values         | Default Value |
|-----------|---------|--------------------|:------------------:|-------------------------|---------------|
| n         | Integer | Prime number count | :heavy_check_mark: | 1 to Integer.MAX_VALUE  | n/a           |
| format    | String  | Output format      |         :x:        | string, array, combined | string        |

#### Example

##### Request

```http
GET /primes?n=10 HTTP/1.1
Host: localhost:8080
Accept: */*
```

##### Response

```http
HTTP/1.1 200 OK
Content-Type: text/plain
Content-Length: 25

2 3 5 7 11 13 17 19 23 29
```
