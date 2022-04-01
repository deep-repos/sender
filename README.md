# sender
Below url is exposed in Sender service
url=>http://localhost:8080/v1/natwest/sender/transaction with POST method.
Payload is expected in Json format with below fields:

	accountNumber: must be of atmost 18 characters
	type:must be of atmost 6 characters 
	currency:must be of atmost 3 characters 
	amount:must be of atmost 7 characters 
	accountFrom: must be of atmost 18 characters

payload example is given below:
{  "accountNumber":"1234567891",
    "type":"Debit",
    "amount":"990000",
    "currency":"INR",
    "accountFrom":"987654321012"}

It returns below code on given condition
201=>if transaction is inserted sucessfully ,
500=>if exception occured ,
400=>if payload is null


Sender  converts Transaction object to Json String and then encrypts json String using AES Algorithm and secret key mentioned in application.properties file.
It then sends POST request to receiver service with encrypted json String as payload
and returns response from receiver to client

