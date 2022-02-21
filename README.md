# Project APOD Astronomy Picture of the Day
One of the most popular websites at NASA is the Astronomy Picture of the Day. 
APOD was published by NASA for public consumption. 
API - Documentation is available at https://github.com/nasa/apod-api/blob/master/README.md

Fields:

`date:` date is used to retreve Astronomy Picture of the provided date

- Verify valid date and valid date format
- Verify default date to be Today (now)
- Verify date query param is no earlier than 1995-06-16
- Verify date query param is no later than today local time
- Verify error Message for in case data is not avaliable
- Verify Invalid Date Format

`start_date:` it is the date used to start retrieving until today
- Verify date of APOD is after provided start date
- Verify invalid start date response with error message
- Verify that Query params, date and start_date should return status code 200

`end_date:` it is the date used to specify the range end date
- Verify that start_date and end_date are within correct range
- Verify that Query params, date and end_date should return status code 200
- Verify end date query parameter returns 400 status code when used without start date

`thumbs:` It is used to retrieve thumbnail Urls if present
- Verify when thumb query param is set to false, it should not be present in response body
- Verify when thumb query param is set to true, it should  be present in response body

`count:` it is count of randomly chosen Astronomy Picture of the days
- Verify count query params should return status code 400 when used with start date
- Verify max count is 100
- 
`api_key:` It is a unique identifier which is used to authenticate a user
- Verify API key with valid values
- Verify API key without api key query parameter
- Verify invalid api Key

    Testing Approach

Testing approach was designed using API documentation 

HTTP Request
GET https://api.nasa.gov/planetary/apod

concept_tags are now disabled in this service. Also, an optional return parameter copyright is returned if the image is not public domain.

Query Parameters

| Parameter    | Type    | Default    | Description    |
|-----|-----|-----|-----|
| date    | YYYY-MM-DD    | today    |  The date of the APOD image to retrieve   |
| start_date    | YYYY-MM-DD    | none    | The start of a date range, when requesting date for a range of dates. Cannot be used with date.    |
|  end_date   | YYYY-MM-DD    |  today   |  The end of the date range, when used with start_date.    |
|  count   |  int    | none    |   If this is specified then count randomly chosen images will be returned. Cannot be used with date or start_date and end_date.   |
|  thumbs	    | bool    | False	    |   Return the URL of video thumbnail. If an APOD is not a video, this parameter is ignored.   |
|  api_key   |   string   | DEMO_KEY    | 	api.nasa.gov key for expanded usage     |





	     	     	         
	      	  	    
	  	  	    
	  	  	   
	        	      	   
        	         
	       	      







# ProjectNasa
