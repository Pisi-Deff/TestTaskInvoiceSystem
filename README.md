## Invoice System

This application was written as a test assignment for [Arvato](http://www.arvato.ee/).

It's a very simplified representation of a car parking invoice system.

### Running

Run via command line: `mvn clean install jetty:run`

Then visit [localhost:8080/invoice_system](http://localhost:8080/invoice_system)

Note: There are currently 2 hardcoded customers and 1 hardcoded parking house. There is currently no API to add additional ones of either.

### REST api

#### Add parking

Path: `/api/addParking`

Body:  
* `customerID` - number  
  Unique identifier for customer.
* `parkingHouseID` - number  
  Unique identifier for parking house.
* `start` - object  
  * `year` - number  
    The year when parking began.
  * `month` - number  
    The month when parking began. (1-12)
  * `day` - number  
    The day when parking began. (1-31)
  * `hour` - number  
    The hour when parking began. (0-23)
  * `minute` - number  
    The minute when parking began. (0-59)
  * `second` - number  
    The second when parking began. (0-59)
* `end` - object  
  * `year` - number  
    The year when parking ended.
  * `month` - number  
    The month when parking ended. (1-12)
  * `day` - number  
    The day when parking ended. (1-31)
  * `hour` - number  
    The hour when parking ended. (0-23)
  * `minute` - number  
    The minute when parking ended. (0-59)
  * `second` - number  
    The second when parking ended. (0-59)

Example:
```json
{
	"customerID": 0,
	"parkingHouseID": 0,
	"start": {
		"year": 2017,
		"month": 5,
		"day": 13,
		"hour": 14,
		"minute": 32,
		"second": 46
	},
	"end": {
		"year": 2017,
		"month": 5,
		"day": 13,
		"hour": 20,
		"minute": 58,
		"second": 13
	}
}
```
